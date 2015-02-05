package com.anypresence.masterpass_android_library;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anypresence.masterpass_android_library.activities.MPLightBox;
import com.anypresence.masterpass_android_library.dto.Details;
import com.anypresence.masterpass_android_library.dto.LightBoxParams;
import com.anypresence.masterpass_android_library.dto.Order;
import com.anypresence.masterpass_android_library.dto.PreCheckoutResponse;
import com.anypresence.masterpass_android_library.dto.StatusWithError;
import com.anypresence.masterpass_android_library.dto.WebViewOptions;
import com.anypresence.masterpass_android_library.exception.ManualCheckoutException;
import com.anypresence.masterpass_android_library.exception.NotPairedException;
import com.anypresence.masterpass_android_library.exception.PairCheckoutException;
import com.anypresence.masterpass_android_library.interfaces.FutureCallback;
import com.anypresence.masterpass_android_library.interfaces.ILightBox;
import com.anypresence.masterpass_android_library.interfaces.IManager;
import com.anypresence.masterpass_android_library.interfaces.OnCompleteCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;
import com.anypresence.masterpass_android_library.util.ConnectionUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPManager implements ILightBox {
    private static final String LOG_TAG = MPManager.class.getSimpleName();

    public static String DATA_TYPE_CARD = "CARD";
    public static String DATA_TYPE_ADDRESS = "ADDRESS";
    public static String DATA_TYPE_PROFILE = "PROFILE";

    public static String CARD_TYPE_AMEX = "amex";
    public static String CARD_TYPE_MASTERCARD = "master";
    public static String CARD_TYPE_DISCOVER = "discover";
    public static String CARD_TYPE_VISA = "visa";
    public static String CARD_TYPE_MAESTRO = "maestro";

    public static String MP_VERSION = "v6";
    private static MPManager instance;

    private IManager delegate;

    private String prefixURL;
    private String pairURL;
    private String preCheckoutURL;
    private String checkoutURL;
    private String pairAndCheckoutURL;
    private String completePairCheckout;
    private String manualCheckoutURL;

    //Init

    /**
     * Initializes (for returns the shared instance of) the manager to interact with
     * the MasterPass services
     *
     * @return MPManager instance
     */
    public static MPManager getInstance() {
        if (instance == null)
            instance = new MPManager();
        return instance;
    }

    public void setDelegate(IManager delegate) {
        this.delegate = delegate;
        initializeURL();
    }

    private void initializeURL() {
        prefixURL = delegate.getServerAddress() + "/masterpass/";
        pairURL = prefixURL + "pair";
        preCheckoutURL = prefixURL + "precheckout";
        checkoutURL = prefixURL + "return_checkout";
        pairAndCheckoutURL = prefixURL + "pair_and_checkout";
        completePairCheckout = prefixURL + "complete_checkout";
        manualCheckoutURL = prefixURL + "non_masterpass_checkout";
    }
    //Pairing

    /**
     * Opens the pairing modal over a viewController.
     *
     * @param viewController The viewController to show the pairing modal over
     */
    public void pair(final ViewController viewController) {
        FutureCallback<Details> futureCallback = new FutureCallback<Details>() {
            @Override
            public void onSuccess(Details details) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(delegate.getSupportedDataTypes());
                options.setDetails(details);
                options.setRequestPairing(1);
                options.setVersion(MP_VERSION);
                showLightBoxWindowOfType(MPLightBox.MPLightBoxType.MPLightBoxTypeConnect, options, viewController);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(MPManager.class.getSimpleName(), "Error Requesting Pairing: " + throwable.getLocalizedMessage());
                delegate.pairingDidComplete(false, throwable);
            }
        };
        this.requestPairing(viewController, futureCallback);
    }

    protected void requestPairing(ViewController viewController, final FutureCallback<Details> callback) {
        FutureCallback<JSONObject> listener = new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                String responseString = response.toString();
                Log.d("Approved Pairing Request: ", responseString);
                callback.onSuccess(new Gson().fromJson(responseString, Details.class));
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e("Error Pairing Request: ", error.toString());
                callback.onFailure(error);
            }
        };
        ConnectionUtil.call(getPairURL(), viewController.getXSessionId(), null, listener);
    }

    private void showLightBoxWindowOfType(final MPLightBox.MPLightBoxType type, final LightBoxParams options, ViewController viewController) {
        final MPLightBox mpLightBox = new MPLightBox();
        mpLightBox.setDelegate(this);
        viewController.presentViewController(mpLightBox, true, new WebViewOptions(type, options));
    }

    /**
     * The current pairing status as defined by the delegate.
     * This is just a convenience method for [self.delegate isAppPaired]
     *
     * @return the current pairing status
     */
    private Boolean isAppPaired() {
        return delegate.isAppPaired();
    }

    //Return Checkout

    /**
     * Retrieves the preCheckout data from the MasterPass service
     */
    public void preCheckout(final ViewController viewController, final FutureCallback<PreCheckoutResponse> callback) {
        checkoutPaired();
        viewController.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FutureCallback callbackCall = new FutureCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        String responseString = response.toString();
                        Log.d("Received PreCheckout Data: ", responseString);
                        PreCheckoutResponse preCheckoutResponse = new Gson().fromJson(responseString, PreCheckoutResponse.class);
                        //TODO:Check with David
                        //if (preCheckoutResponse.hasError()) {
                        //if (preCheckoutResponse.isNotPaired()) {
                                // User is not paired. They may have disconnected
                                // via the MasterPass console.
                                // We will optionally reset that pairing status here
                        //delegate.resetUserPairing();
                        //callback.onFailure(new BadRequestException(preCheckoutResponse.errors));
                        //}
                        //} else {
                            callback.onSuccess(preCheckoutResponse);
                        //}
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e(LOG_TAG, throwable.toString());
                    }
                };

                ConnectionUtil.call(getPreCheckoutURL(), viewController.getXSessionId(), getPreCheckoutParams(), callbackCall);
            }
        });
    }

    public JSONObject getPreCheckoutParams() {
        JSONObject params = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            List<String> supportedDataTypes = delegate.getSupportedDataTypes();
            for (String supportedDataType : supportedDataTypes) {
                jsonArray.put(supportedDataType);
            }
            params.put("requested_data_types", jsonArray);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return params;
    }

    private void checkoutPaired() {
        if (!isAppPaired())
            throw new NotPairedException();
    }

    private void requestReturnCheckoutForOrder(Order order, ViewController viewController, final FutureCallback callback) {
        checkoutPaired();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Approved Return Checkout Request: ", responseString);
                Details details = new Gson().fromJson(responseString, Details.class);
                //TODO:Check with David
                //if (details.hasError()) {
                //callback.onFailure(new CheckoutException(details.errors));
                //} else {
                callback.onSuccess(details);
                //}
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Return Checkout Request: ", error.toString());
                callback.onFailure(error);
            }
        };
        getRequestQueue(viewController).add(new JsonObjectRequest(getCheckoutURL(), order.getParams(), listener, errorListener));
    }

    private void returnCheckoutForOrder(final Order order, final ViewController viewController) {
        FutureCallback<Details> callback = new FutureCallback<Details>() {
            @Override
            public void onSuccess(Details details) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(null);
                options.setDetails(details);
                options.setRequestPairing(null);
                options.setVersion(MP_VERSION);
                options.setOrder(order);
                showLightBoxWindowOfType(MPLightBox.MPLightBoxType.MPLightBoxTypeConnect, options, viewController);
            }

            @Override
            public void onFailure(Throwable error) {
                delegate.pairingDidComplete(false, error);
            }
        };
        requestReturnCheckoutForOrder(order, viewController, callback);
    }

    //Pair Checkout

    public void pairCheckoutForOrder(Order order, final ViewController viewController) {
        FutureCallback<Details> callback = new FutureCallback<Details>() {
            @Override
            public void onSuccess(Details details) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(delegate.getSupportedDataTypes());
                options.setDetails(details);
                options.setAllowedCardType(delegate.getSupportedCardTypes());
                options.setRequestPairing(1);
                options.setVersion(MP_VERSION);
                showLightBoxWindowOfType(MPLightBox.MPLightBoxType.MPLightBoxTypeConnect, options, viewController);
            }

            @Override
            public void onFailure(Throwable error) {
                delegate.pairingDidComplete(false, error);
            }
        };
        requestPairCheckoutForOrder(order, viewController, callback);
    }

    private void requestPairCheckoutForOrder(Order order, ViewController viewController, final FutureCallback callback) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Approved Pair Checkout Request: ", responseString);
                Details details = new Gson().fromJson(responseString, Details.class);
                //TODO:Check with David
                //if (details.hasError()) {
                //  callback.onFailure(new CheckoutException(details.errors));
                //} else {
                callback.onSuccess(details);
                //}
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Return Checkout Request: ", error.toString());
                callback.onFailure(error);
            }
        };
        getRequestQueue(viewController).add(new JsonObjectRequest(getPairAndCheckoutURL(), order.getParams(), listener, errorListener));
    }

    private void completePairCheckoutForOrder(Order order, ViewController viewController) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Completed Checkout Successfully: ", responseString);
                StatusWithError status = new Gson().fromJson(responseString, StatusWithError.class);
                if (status.hasError()) {
                    delegate.pairCheckoutDidComplete(false, new PairCheckoutException(status.errors));
                } else {
                    delegate.pairCheckoutDidComplete(true, null);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error in Completed Checkout Successfully: ", error.toString());
            }
        };
        getRequestQueue(viewController).add(new JsonObjectRequest(getCompletePairCheckout(), order.getCompleteParams(), listener, errorListener));
    }

    //Manual Checkout

    public void completeManualCheckout(Order order, ViewController viewController) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Completed Manual Checkout Successfully: ", responseString);
                StatusWithError status = new Gson().fromJson(responseString, StatusWithError.class);
                if (status.hasError()) {
                    delegate.manualCheckoutDidComplete(false, new ManualCheckoutException(status.errors));
                } else {
                    delegate.manualCheckoutDidComplete(true, null);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error in Completed Manual Checkout Successfully: ", error.toString());
            }
        };

        getRequestQueue(viewController).add(new JsonObjectRequest(getManualCheckoutURL(), order.getParams(), listener, errorListener));
    }

    //ILightBox
    @Override
    public void pairingViewDidCompletePairing(ViewController pairingViewController, final Boolean success, final Throwable error) {
        pairingViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.pairingDidComplete(success, error);
            }
        });
    }

    @Override
    public void lightBoxDidCompletePreCheckout(ViewController lightBoxViewController, final Boolean success, final Map<Object, Object> data, final Throwable error) {
        lightBoxViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.preCheckoutDidComplete(success, data, error);
            }
        });
    }

    @Override
    public void lightBoxDidCompleteCheckout(ViewController pairingViewController, final Boolean success, final Throwable error) {
        pairingViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.checkoutDidComplete(success, error);
            }
        });
    }

    public String getPreCheckoutURL() {
        return preCheckoutURL;
    }

    public String getPairURL() {
        return pairURL;
    }

    public String getCheckoutURL() {
        return checkoutURL;
    }

    public String getPairAndCheckoutURL() {
        return pairAndCheckoutURL;
    }

    public String getCompletePairCheckout() {
        return completePairCheckout;
    }

    public String getManualCheckoutURL() {
        return manualCheckoutURL;
    }

    public RequestQueue getRequestQueue(ViewController viewController) {
        return Volley.newRequestQueue(viewController.getContext());
    }
}

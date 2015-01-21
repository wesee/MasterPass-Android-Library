package com.anypresence.masterpass_android_library;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anypresence.masterpass_android_library.dto.CheckoutDetails;
import com.anypresence.masterpass_android_library.dto.JsonStatus;
import com.anypresence.masterpass_android_library.dto.LightBoxParams;
import com.anypresence.masterpass_android_library.dto.Order;
import com.anypresence.masterpass_android_library.dto.PairingDetails;
import com.anypresence.masterpass_android_library.dto.PreCheckoutData;
import com.anypresence.masterpass_android_library.exception.BadRequestException;
import com.anypresence.masterpass_android_library.exception.CheckoutException;
import com.anypresence.masterpass_android_library.exception.ManualCheckoutException;
import com.anypresence.masterpass_android_library.exception.NotPairedException;
import com.anypresence.masterpass_android_library.exception.PairCheckoutException;
import com.anypresence.masterpass_android_library.interfaces.FutureCallback;
import com.anypresence.masterpass_android_library.interfaces.OnCompleteCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;
import com.anypresence.masterpass_android_library.volleyRequest.CompleteOrderRequest;
import com.anypresence.masterpass_android_library.volleyRequest.OrderRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPManager implements ILightBox {
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
    }
    //Pairing

    /**
     * Opens the pairing modal over a viewController.
     *
     * @param viewController The viewController to show the pairing modal over
     */
    private void pairInViewController(final ViewController viewController) {
        FutureCallback<PairingDetails> futureCallback = new FutureCallback<PairingDetails>() {
            @Override
            public void onSuccess(PairingDetails pairingDetails) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(delegate.getSupportedDataTypes());
                options.setPairingDetails(pairingDetails);
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
        this.requestPairing(futureCallback);
    }

    private void requestPairing(final FutureCallback<PairingDetails> callback) {
        String url = delegate.getServerAddress() + "/masterpass/pair";
        JsonObjectRequest response = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        Log.d("Approved Pairing Request: ", responseString);
                        callback.onSuccess(new Gson().fromJson(responseString, PairingDetails.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Pairing Request: ", error.toString());
                        callback.onFailure(error);
                    }
                }
        );
        MPLibraryApplication.getInstance().getRequestQueue().add(response);
    }

    private void showLightBoxWindowOfType(final MPLightBox.MPLightBoxType type, final LightBoxParams options, ViewController viewController) {
        final MPLightBox mpLightBox = new MPLightBox();
        mpLightBox.delegate = this;
        viewController.presentViewController(mpLightBox, true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                mpLightBox.initiateLightBoxOfTypeWithOptions(type, options);
            }
        });
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
    private void preCheckoutData(final FutureCallback callback) {
        checkoutPaired();
        String url = delegate.getServerAddress() + "/masterpass/precheckout";
        JsonObjectRequest response = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        Log.d("Received PreCheckout Data: ", responseString);
                        PreCheckoutData preCheckoutData = new Gson().fromJson(responseString, PreCheckoutData.class);
                        if (preCheckoutData.hasError()) {
                            if (preCheckoutData.isNotPaired()) {
                                // User is not paired. They may have disconnected
                                // via the MasterPass console.
                                // We will optionally reset that pairing status here
                                delegate.resetUserPairing();
                                callback.onFailure(new BadRequestException(preCheckoutData.errors));
                            }
                        } else {
                            callback.onSuccess(preCheckoutData);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error PreCheckout Data: ", error.toString());
                        callback.onFailure(error);
                    }
                }
        );
        MPLibraryApplication.getInstance().getRequestQueue().add(response);
    }

    private void checkoutPaired() {
        if (!isAppPaired())
            throw new NotPairedException();
    }

    private void requestReturnCheckoutForOrder(Order order, final FutureCallback callback) {
        checkoutPaired();
        String url = delegate.getServerAddress() + "/masterpass/return_checkout";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Approved Return Checkout Request: ", responseString);
                CheckoutDetails checkoutDetails = new Gson().fromJson(responseString, CheckoutDetails.class);
                if (checkoutDetails.hasError()) {
                    callback.onFailure(new CheckoutException(checkoutDetails.errors));
                } else {
                    callback.onSuccess(checkoutDetails);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Return Checkout Request: ", error.toString());
                callback.onFailure(error);
            }
        };
        MPLibraryApplication.getInstance().getRequestQueue().add(new OrderRequest(url, order, listener, errorListener));
    }

    private void returnCheckoutForOrder(final Order order, final ViewController viewController) {
        FutureCallback<CheckoutDetails> callback = new FutureCallback<CheckoutDetails>() {
            @Override
            public void onSuccess(CheckoutDetails checkoutDetails) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(null);
                options.setPairingDetails(null);
                options.setRequestPairing(null);
                options.setCheckoutDetails(checkoutDetails);
                options.setVersion(MP_VERSION);
                showLightBoxWindowOfType(MPLightBox.MPLightBoxType.MPLightBoxTypeConnect, options, viewController);
            }

            @Override
            public void onFailure(Throwable error) {
                delegate.pairingDidComplete(false, error);
            }
        };
        requestReturnCheckoutForOrder(order, callback);
    }

    //Pair Checkout

    private void pairCheckoutForOrder(Order order, final ViewController viewController) {
        FutureCallback<PairingDetails> callback = new FutureCallback<PairingDetails>() {
            @Override
            public void onSuccess(PairingDetails pairingDetails) {
                LightBoxParams options = new LightBoxParams();
                options.setRequestedDataTypes(delegate.getSupportedDataTypes());
                options.setPairingDetails(pairingDetails);
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
        requestPairCheckoutForOrder(order, callback);
    }

    private void requestPairCheckoutForOrder(Order order, final FutureCallback callback) {
        String url = delegate.getServerAddress() + "/masterpass/pair_and_checkout";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Approved Pair Checkout Request: ", responseString);
                PairingDetails pairingDetails = new Gson().fromJson(responseString, PairingDetails.class);
                if (pairingDetails.hasError()) {
                    callback.onFailure(new CheckoutException(pairingDetails.errors));
                } else {
                    callback.onSuccess(pairingDetails);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Return Checkout Request: ", error.toString());
                callback.onFailure(error);
            }
        };
        MPLibraryApplication.getInstance().getRequestQueue().add(new OrderRequest(url, order, listener, errorListener));
    }

    private void completePairCheckoutForOrder(Order order) {
        String url = delegate.getServerAddress() + "/masterpass/complete_checkout";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Completed Checkout Successfully: ", responseString);
                JsonStatus jsonStatus = new Gson().fromJson(responseString, JsonStatus.class);
                if (jsonStatus.hasError()) {
                    delegate.pairCheckoutDidComplete(false, new PairCheckoutException(jsonStatus.errors));
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
        MPLibraryApplication.getInstance().getRequestQueue().add(new CompleteOrderRequest(url, order, listener, errorListener));
    }

    //Manual Checkout

    private void completeManualCheckout(Order order) {
        String url = delegate.getServerAddress() + "/masterpass/non_masterpass_checkout";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                Log.d("Completed Manual Checkout Successfully: ", responseString);
                JsonStatus jsonStatus = new Gson().fromJson(responseString, JsonStatus.class);
                if (jsonStatus.hasError()) {
                    delegate.manualCheckoutDidComplete(false, new ManualCheckoutException(jsonStatus.errors));
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
        MPLibraryApplication.getInstance().getRequestQueue().add(new OrderRequest(url, order, listener, errorListener));
    }

    //ILightBox
    @Override
    public void pairingViewDidCompletePairing(MPLightBox pairingViewController, final Boolean success, final Throwable error) {
        pairingViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.pairingDidComplete(success, error);
            }
        });
    }

    @Override
    public void lightBoxDidCompletePreCheckout(MPLightBox lightBoxViewController, final Boolean success, final Map<Object, Object> data, final Throwable error) {
        lightBoxViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.preCheckoutDidComplete(success, data, error);
            }
        });
    }

    @Override
    public void lightBoxDidCompleteCheckout(MPLightBox pairingViewController, final Boolean success, final Throwable error) {
        pairingViewController.dismissViewControllerAnimated(true, new OnCompleteCallback() {
            @Override
            public void onComplete() {
                delegate.checkoutDidComplete(success, error);
            }
        });
    }
}

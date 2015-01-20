package com.anypresence.masterpass_android_library;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anypresence.masterpass_android_library.dto.LightBoxParams;
import com.anypresence.masterpass_android_library.dto.Order;
import com.anypresence.masterpass_android_library.dto.PairingDetails;
import com.anypresence.masterpass_android_library.dto.PreCheckoutData;
import com.anypresence.masterpass_android_library.exception.BadRequestException;
import com.anypresence.masterpass_android_library.exception.NotPairedException;
import com.anypresence.masterpass_android_library.interfaces.FutureCallback;
import com.anypresence.masterpass_android_library.interfaces.OnCompleteCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;
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

    //Pairing

    /**
     * Opens the pairing modal over a viewController.
     *
     * @param viewController The viewController to show the pairing modal over
     */
    void pairInViewController(final ViewController viewController) {
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

    void showLightBoxWindowOfType(final MPLightBox.MPLightBoxType type, final LightBoxParams options, ViewController viewController) {
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
    Boolean isAppPaired() {
        return delegate.isAppPaired();
    }

    //Return Checkout

    /**
     * Retrieves the preCheckout data from the MasterPass service
     */
    void preCheckoutDataCallback(final FutureCallback callback) {
        //PreCheckoutData data
        if (!isAppPaired())
            throw new NotPairedException();

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
                        Log.e("Error Pairing Request: ", error.toString());
                        callback.onFailure(error);
                    }
                }
        );
        MPLibraryApplication.getInstance().getRequestQueue().add(response);
    }

    void returnCheckoutForOrder(Order order, ViewController viewController) {
    }

    //Pair Checkout

    void pairCheckoutForOrder(String orderNumber, ViewController viewController) {
    }

    void completePairCheckoutForOrder(String orderNumber, String transactionId, String preCheckoutTransactionId) {
    }

    //Manual Checkout

    void completeManualCheckoutForOrder(String orderNumber) {

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
    public void lightBoxDidCompletePreCheckout(MPLightBox lightBoxViewController, Boolean success, Map<Object, Object> data, Throwable error) {

    }

    @Override
    public void lightBoxDidCompleteCheckout(MPLightBox pairingViewController, Boolean success, Throwable error) {

    }
}

package com.anypresence.masterpass_android_library.dto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class LightBoxParams implements Serializable {
    private static final String LOG_TAG = LightBoxParams.class.getSimpleName();
    private Details details;
    private String version;
    private List<String> requestedDataTypes;
    private Boolean requestPairing;
    private List<String> allowedCardType;
    private Order order;
    private MPLightBoxParamsType type;

    public MPLightBoxParamsType getType() {
        return type;
    }

    public void setType(MPLightBoxParamsType type) {
        this.type = type;
    }

    public void setRequestedDataTypes(List<String> requestedDataTypes) {
        this.requestedDataTypes = requestedDataTypes;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public void setRequestPairing(Boolean requestPairing) {
        this.requestPairing = requestPairing;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAllowedCardType(List<String> allowedCardType) {
        this.allowedCardType = allowedCardType;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            if (this.type == MPLightBoxParamsType.Checkout) {
                getCheckoutParams(json);
            } else {
                getParams(json);
            }
        } catch (JSONException e) {
            Log.e(LightBoxParams.class.getSimpleName(), e.getMessage());
        }
        return json.toString();
    }

    private void getParams(JSONObject json) throws JSONException {
        json.put("requestedDataTypes", getRequestDataTypes());
        if (details.checkoutRequestToken != null)
            json.put("requestToken", details.checkoutRequestToken);
        if (details.merchantCheckoutId != null)
            json.put("merchantCheckoutId", details.merchantCheckoutId);
        if (details.callbackUrl != null)
            json.put("callbackUrl", getCallbackURL());
        if (version != null)
            json.put("version", version);
        if (order != null) {
            if (order.card != null)
                json.put("cardId", order.card.cardId);
            if (order.shippingAddress != null)
                json.put("shippingId", order.shippingAddress.addressId);
            if (order.walletInfo != null) {
                json.put("precheckoutTransactionId", order.walletInfo.preCheckoutTransactionId);
                json.put("walletName", order.walletInfo.walletName);
                json.put("consumerWalletId", order.walletInfo.consumerWalletId);
            }
        }
    }

    private void getCheckoutParams(JSONObject json) throws JSONException {
        if (details.checkoutRequestToken != null)
            json.put("requestToken", details.checkoutRequestToken);
        if (details.pairingRequestToken != null)
            json.put("pairingRequestToken", details.pairingRequestToken);
        json.put("requestedDataTypes", getRequestDataTypes());
        if (details.merchantCheckoutId != null)
            json.put("merchantCheckoutId", details.merchantCheckoutId);
        json.put("allowedCardTypes", getAllowedCardTypes());
        if (details.callbackUrl != null)
            json.put("callbackUrl", getCallbackURL());
        if (requestPairing != null)
            json.put("requestPairing", requestPairing);
        if (version != null)
            json.put("version", version);
    }

    public JSONArray getRequestDataTypes() {
        JSONArray jsonArray = new JSONArray();
        for (String supportedDataType : requestedDataTypes) {
            jsonArray.put(supportedDataType);
        }
        return jsonArray;
    }

    public JSONArray getAllowedCardTypes() {
        JSONArray jsonArray = new JSONArray();
        for (String supportedDataType : allowedCardType) {
            jsonArray.put(supportedDataType);
        }
        return jsonArray;
    }

    public String getCallbackURL() {
        return details.callbackUrl.replace("\\", "");
    }

    public enum MPLightBoxParamsType {
        Checkout(0),
        MPLightBoxTypeCheckout(1),;

        public int value;

        MPLightBoxParamsType(int value) {
            this.value = value;
        }
    }
}

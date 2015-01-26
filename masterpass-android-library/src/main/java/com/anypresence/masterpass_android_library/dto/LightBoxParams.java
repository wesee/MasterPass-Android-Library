package com.anypresence.masterpass_android_library.dto;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class LightBoxParams implements Serializable {
    private Details details;
    private String version;
    private List<String> requestedDataTypes;
    private Integer requestPairing;
    private List<String> allowedCardType;
    private Order order;

    public void setRequestedDataTypes(List<String> requestedDataTypes) {
        this.requestedDataTypes = requestedDataTypes;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public void setRequestPairing(Integer requestPairing) {
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
            if (details.checkoutRequestToken != null)
                json.put("requestToken", details.checkoutRequestToken);
            if (details.merchantCheckoutId != null)
                json.put("merchantCheckoutId", details.merchantCheckoutId);
            if (details.callbackUrl != null)
                json.put("callbackUrl", details.callbackUrl);
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
        } catch (JSONException e) {
            Log.e(LightBoxParams.class.getSimpleName(), e.getMessage());
        }
        return json.toString();
    }

    public String getCallbackURL() {
        return details.callbackUrl.replace("\\", "");
    }
}

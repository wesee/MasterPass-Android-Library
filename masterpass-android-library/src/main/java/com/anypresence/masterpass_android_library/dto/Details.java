package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class Details extends StatusWithError {
    @SerializedName("checkout_request_token")
    public String checkoutRequestToken;
    @SerializedName("pairing_request_token")
    public String pairingRequestToken;
    @SerializedName("cart_token")
    public String cartToken;
    @SerializedName("merchant_checkout_id")
    public String merchantCheckoutId;
    @SerializedName("callback_url")
    public String callbackUrl;
}

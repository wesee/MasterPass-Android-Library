package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego.rotondale on 1/19/2015.
 */
public class PairingDetails {
    @SerializedName("pairing_request_token")
    public String pairingRequestToken;
    @SerializedName("merchant_checkout_id")
    public String merchantCheckoutId;
    @SerializedName("callback_url")
    public String callbackUrl;
}

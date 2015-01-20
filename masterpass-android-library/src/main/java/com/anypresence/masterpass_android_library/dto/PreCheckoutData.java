package com.anypresence.masterpass_android_library.dto;

import com.anypresence.masterpass_android_library.model.MPAddress;
import com.anypresence.masterpass_android_library.model.MPCreditCard;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class PreCheckoutData {
    private static String MP_ERROR_NOT_PAIRED = "No long access token found associated with user (user not paired with Masterpass)";
    public String status;
    public String errors;
    public List<MPCreditCard> cards;
    @SerializedName("shipping_addresses")
    public List<MPAddress> addresses;
    @SerializedName("contact")
    public Map<Object, Object> contactInfo;
    @SerializedName("wallet_info")
    public Map<Object, Object> walletInfo;


    public boolean hasError() {
        return status.equals("error");
    }

    public boolean isNotPaired() {
        return errors.equals(MP_ERROR_NOT_PAIRED);
    }
}

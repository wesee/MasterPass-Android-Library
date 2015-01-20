package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class CheckoutDetails extends Details {
    @SerializedName("checkout_request_token")
    public String requestToken;
}

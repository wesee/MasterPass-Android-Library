package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego.rotondale on 1/21/2015.
 */
public class Checkout {
    public static final String TRANSACTION_ID_KEY = "transaction_id";

    public CreditCard card;
    public Address contact;
    @SerializedName(TRANSACTION_ID_KEY)
    public String transactionId;
}

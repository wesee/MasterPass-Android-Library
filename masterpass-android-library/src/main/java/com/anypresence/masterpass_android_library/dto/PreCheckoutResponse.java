package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class PreCheckoutResponse extends StatusWithError {
    public static final String SHIPPING_ADDRESSES_KEY = "shipping_addresses";
    public static final String WALLET_INFO_KEY = "wallet_info";
    public static final String CONTACT_KEY = "contact";
    public static final String TRANSACTION_ID_KEY = "transaction_id";

    public List<CreditCard> cards;
    @SerializedName(SHIPPING_ADDRESSES_KEY)
    public List<Address> addresses;
    @SerializedName(WALLET_INFO_KEY)
    public Wallet walletInfo;
    @SerializedName(CONTACT_KEY)
    public Contact contact;
    @SerializedName(TRANSACTION_ID_KEY)
    public String transactionId;
}

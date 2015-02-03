package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by diego.rotondale on 1/16/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class CreditCard implements Serializable {

    public static final String BRAND_ID_KEY = "brand_id";
    public static final String BRAND_NAME_KEY = "brand_name";
    public static final String CARD_ALIAS_KEY = "card_alias";
    public static final String BILLING_ADDRESS_KEY = "billing_address";
    public static final String CARD_HOLDER_NAME_KEY = "card_holder_name";
    public static final String CARD_ID_KEY = "card_id";
    public static final String EXPIRY_MONTH_KEY = "expiry_month";
    public static final String EXPIRY_YEAR_KEY = "expiry_year";
    public static final String LAST_FOUR_KEY = "last_four";
    public static final String SELECTED_AS_DEFAULT_KEY = "selected_as_default";

    @SerializedName(BRAND_ID_KEY)
    String brandId;
    @SerializedName(BRAND_NAME_KEY)
    String brandName;
    @SerializedName(BILLING_ADDRESS_KEY)
    Address billingAddress;
    @SerializedName(CARD_ALIAS_KEY)
    String cardAlias;
    @SerializedName(CARD_HOLDER_NAME_KEY)
    String cardHolderName;
    @SerializedName(CARD_ID_KEY)
    Integer cardId;
    @SerializedName(EXPIRY_MONTH_KEY)
    String expiryMonth;
    @SerializedName(EXPIRY_YEAR_KEY)
    String expiryYear;
    @SerializedName(LAST_FOUR_KEY)
    String lastFour;
    @SerializedName(SELECTED_AS_DEFAULT_KEY)
    Boolean selectedAsDefault;
}

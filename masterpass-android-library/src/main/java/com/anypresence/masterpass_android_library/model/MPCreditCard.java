package com.anypresence.masterpass_android_library.model;

import com.anypresence.masterpass_android_library.util.ConvertUtil;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/16/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPCreditCard {

    public static String BRAND_ID_KEY = "brand_id";
    public static String BRAND_NAME_KEY = "brand_name";
    public static String CARD_ALIAS_KEY = "card_alias";
    public static String CARD_HOLDER_NAME_KEY = "card_holder_name";
    public static String CARD_ID_KEY = "card_id";
    public static String EXPIRY_MONTH_KEY = "expiry_month";
    public static String EXPIRY_YEAR_KEY = "expiry_year";
    public static String LAST_FOUR_KEY = "last_four";
    public static String SELECTED_AS_DEFAULT_KEY = "selected_as_default";

    String brandId;
    String brandName;
    String cardAlias;
    String cardHolderName;
    Integer cardId;
    String expiryMonth;
    String expiryYear;
    String lastFour;
    Integer selectedAsDefault;

    public static MPCreditCard getMPAddress(Map<String, Object> dictionary) {
        MPCreditCard mpCreditCard = new MPCreditCard();
        if (dictionary == null)
            return mpCreditCard;
        mpCreditCard.cardId = ConvertUtil.getInteger(dictionary, CARD_ID_KEY);
        mpCreditCard.selectedAsDefault = ConvertUtil.getInteger(dictionary, SELECTED_AS_DEFAULT_KEY);

        mpCreditCard.brandId = ConvertUtil.getString(dictionary, BRAND_ID_KEY);
        mpCreditCard.brandName = ConvertUtil.getString(dictionary, BRAND_NAME_KEY);
        mpCreditCard.cardAlias = ConvertUtil.getString(dictionary, CARD_ALIAS_KEY);
        mpCreditCard.cardHolderName = ConvertUtil.getString(dictionary, CARD_HOLDER_NAME_KEY);
        mpCreditCard.expiryMonth = ConvertUtil.getString(dictionary, EXPIRY_MONTH_KEY);
        mpCreditCard.expiryYear = ConvertUtil.getString(dictionary, EXPIRY_YEAR_KEY);
        mpCreditCard.lastFour = ConvertUtil.getString(dictionary, LAST_FOUR_KEY);
        return mpCreditCard;
    }
}

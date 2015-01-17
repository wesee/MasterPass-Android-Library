package com.anypresence.masterpass_android_library.model;

import com.anypresence.masterpass_android_library.util.ConvertUtil;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/16/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPAddress {
    public static String ADDRESS_ID_KEY = "address_id";
    public static String CITY_KEY = "city";
    public static String COUNTRY_KEY = "country";
    public static String COUNTRY_SUBDIVISION_KEY = "country_subdivision";
    public static String LINE_1_KEY = "line1";
    public static String LINE_2_KEY = "line2";
    public static String LINE_3_KEY = "line3";
    public static String POSTAL_CODE_KEY = "postal_code";
    public static String RECIPIENT_NAME_KEY = "recipient_name";
    public static String RECIPIENT_PHONE_NUMBER_KEY = "recipient_phone_number";
    public static String SELECTED_AS_DEFAULT_KEY = "selected_as_default";
    public static String SHIPPING_ALIAS_KEY = "shipping_alias";

    public Integer addressId;
    public String city;
    public String country;
    public String countrySubdivision;
    public String lineOne;
    public String lineTwo;
    public String lineThree;
    public String postalCode;
    public String recipientName;
    public String recipientPhoneNumber;
    public Integer selectedAsDefault;
    public String shippingAlias;

    public static MPAddress getMPAddress(Map<String, Object> dictionary) {
        MPAddress mpAddress = new MPAddress();
        if (dictionary == null)
            return mpAddress;
        mpAddress.addressId = ConvertUtil.getInteger(dictionary, ADDRESS_ID_KEY);
        mpAddress.selectedAsDefault = ConvertUtil.getInteger(dictionary, SELECTED_AS_DEFAULT_KEY);

        mpAddress.city = ConvertUtil.getString(dictionary, CITY_KEY);
        mpAddress.country = ConvertUtil.getString(dictionary, COUNTRY_KEY);
        mpAddress.countrySubdivision = ConvertUtil.getString(dictionary, COUNTRY_SUBDIVISION_KEY);
        mpAddress.lineOne = ConvertUtil.getString(dictionary, LINE_1_KEY);
        mpAddress.lineTwo = ConvertUtil.getString(dictionary, LINE_2_KEY);
        mpAddress.lineThree = ConvertUtil.getString(dictionary, LINE_3_KEY);
        mpAddress.postalCode = ConvertUtil.getString(dictionary, POSTAL_CODE_KEY);
        mpAddress.recipientName = ConvertUtil.getString(dictionary, RECIPIENT_NAME_KEY);
        mpAddress.recipientPhoneNumber = ConvertUtil.getString(dictionary, RECIPIENT_PHONE_NUMBER_KEY);
        mpAddress.shippingAlias = ConvertUtil.getString(dictionary, SHIPPING_ALIAS_KEY);
        return mpAddress;
    }
}

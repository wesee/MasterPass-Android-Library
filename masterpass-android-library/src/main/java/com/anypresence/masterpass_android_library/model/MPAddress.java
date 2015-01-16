package com.anypresence.masterpass_android_library.model;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/16/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPAddress {
    private static String ADDRESS_ID_KEY = "address_id";
    private static String CITY_ID_KEY = "city";
    private static String COUNTRY_ID_KEY = "country";
    private static String COUNTRY_SUBDIVISION_KEY = "country_subdivision";
    private static String LINE_1_KEY = "line1";
    private static String LINE_2_KEY = "line2";
    private static String LINE_3_KEY = "line3";
    private static String POSTAL_CODE_KEY = "postal_code";
    private static String RECIPIENT_NAME_KEY = "recipient_name";
    private static String RECIPIENT_PHONE_NUMBER_KEY = "recipient_phone_number";
    private static String SELECTED_AS_DEFAULT_KEY = "selected_as_default";
    private static String SHIPPING_ALIAS_KEY = "shipping_alias";

    Integer addressId;
    String city;
    String country;
    String countrySubdivision;
    String lineOne;
    String lineTwo;
    String lineThree;
    String postalCode;
    String recipientName;
    String recipientPhoneNumber;
    Integer selectedAsDefault;
    String shippingAlias;

    public static MPAddress getMPAddress(Map<String, Object> dictionary) {
        MPAddress mpAddress = new MPAddress();
        setInteger(dictionary, ADDRESS_ID_KEY, mpAddress.addressId);
        setInteger(dictionary, SELECTED_AS_DEFAULT_KEY, mpAddress.selectedAsDefault);

        setString(dictionary, CITY_ID_KEY, mpAddress.city);
        setString(dictionary, COUNTRY_ID_KEY, mpAddress.country);
        setString(dictionary, COUNTRY_SUBDIVISION_KEY, mpAddress.countrySubdivision);
        setString(dictionary, LINE_1_KEY, mpAddress.lineOne);
        setString(dictionary, LINE_2_KEY, mpAddress.lineTwo);
        setString(dictionary, LINE_3_KEY, mpAddress.lineThree);
        setString(dictionary, POSTAL_CODE_KEY, mpAddress.postalCode);
        setString(dictionary, RECIPIENT_NAME_KEY, mpAddress.recipientName);
        setString(dictionary, RECIPIENT_PHONE_NUMBER_KEY, mpAddress.recipientPhoneNumber);
        setString(dictionary, SHIPPING_ALIAS_KEY, mpAddress.shippingAlias);
        return mpAddress;
    }

    private static void setInteger(Map<String, Object> dictionary, String key, Integer attribute) {
        if (dictionary.containsKey(key)) {
            attribute = (Integer) dictionary.get(key);
        }
    }

    private static void setString(Map<String, Object> dictionary, String key, String attribute) {
        if (dictionary.containsKey(key)) {
            attribute = (String) dictionary.get(key);
        }
    }
}

package com.anypresence.masterpass_android_library;

import android.test.InstrumentationTestCase;

import com.anypresence.masterpass_android_library.model.MPAddress;

import junit.framework.Assert;

import java.util.HashMap;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class ConvertUtilTest extends InstrumentationTestCase {

    public void testLoadCity() throws Exception {
        HashMap<String, Object> dictionary = new HashMap<>();
        String ny = "NY";
        dictionary.put(MPAddress.CITY_KEY, ny);
        MPAddress mpAddress = MPAddress.getMPAddress(dictionary);
        Assert.assertEquals(mpAddress.city, ny);
    }

    public void testEmptyWithNullDictionary() throws Exception {
        MPAddress mpAddress = MPAddress.getMPAddress(new HashMap<String, Object>());
        isEmptyMPAddress(mpAddress);
    }

    public void testEmptyWithSpaceDictionary() throws Exception {
        HashMap<String, Object> dictionary = new HashMap<>();
        dictionary.put(MPAddress.CITY_KEY, "");
        MPAddress mpAddress = MPAddress.getMPAddress(dictionary);
        Assert.assertEquals(mpAddress.city, null);
    }

    public void testNullDictionary() throws Exception {
        MPAddress mpAddress = MPAddress.getMPAddress(null);
        isEmptyMPAddress(mpAddress);
    }


    private void isEmptyMPAddress(MPAddress mpAddress) {
        Assert.assertEquals(mpAddress.addressId, null);
        Assert.assertEquals(mpAddress.city, null);
        Assert.assertEquals(mpAddress.country, null);
        Assert.assertEquals(mpAddress.countrySubdivision, null);
        Assert.assertEquals(mpAddress.lineOne, null);
        Assert.assertEquals(mpAddress.lineTwo, null);
        Assert.assertEquals(mpAddress.lineThree, null);
        Assert.assertEquals(mpAddress.postalCode, null);
        Assert.assertEquals(mpAddress.recipientName, null);
        Assert.assertEquals(mpAddress.recipientPhoneNumber, null);
        Assert.assertEquals(mpAddress.selectedAsDefault, null);
        Assert.assertEquals(mpAddress.shippingAlias, null);
    }
}

package com.anypresence.masterpass_android_library.model;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPManager {
    private static String DATA_TYPE_CARD = "CARD";
    private static String DATA_TYPE_ADDRESS = "ADDRESS";
    private static String DATA_TYPE_PROFILE = "PROFILE";

    private static String CARD_TYPE_AMEX = "amex";
    private static String CARD_TYPE_MASTERCARD = "master";
    private static String CARD_TYPE_DISCOVER = "discover";
    private static String CARD_TYPE_VISA = "visa";
    private static String CARD_TYPE_MAESTRO = "maestro";

    private static String MP_VERSION = "v6";

    private static String MP_ERROR_DOMAIN = "MasterPassErrorDomain";
    private static String MP_ERROR_NOT_PAIRED = "No long access token found associated with user (user not paired with Masterpass)";
    private static Integer MPErrorCodeBadRequest = 400;

}

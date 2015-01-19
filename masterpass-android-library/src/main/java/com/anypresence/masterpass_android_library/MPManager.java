package com.anypresence.masterpass_android_library;

import com.anypresence.masterpass_android_library.interfaces.FutureCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;
import com.anypresence.masterpass_android_library.model.MPOrder;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPManager {
    public static String DATA_TYPE_CARD = "CARD";
    public static String DATA_TYPE_ADDRESS = "ADDRESS";
    public static String DATA_TYPE_PROFILE = "PROFILE";
    public static String CARD_TYPE_AMEX = "amex";
    public static String CARD_TYPE_MASTERCARD = "master";
    public static String CARD_TYPE_DISCOVER = "discover";
    public static String CARD_TYPE_VISA = "visa";
    public static String CARD_TYPE_MAESTRO = "maestro";
    public static String MP_VERSION = "v6";
    public static String MP_ERROR_DOMAIN = "MasterPassErrorDomain";
    public static String MP_ERROR_NOT_PAIRED = "No long access token found associated with user (user not paired with Masterpass)";
    public static Integer MPErrorCodeBadRequest = 400;
    private static MPManager instance;
    private MPManagerAbstract delegate;

//Init

    /**
     * Initializes (for returns the shared instance of) the manager to interact with
     * the MasterPass services
     *
     * @return MPManager instance
     */
    public static MPManager getInstance() {
        if (instance == null)
            instance = new MPManager();
        return instance;
    }

//Pairing

    /**
     * Opens the pairing modal over a viewcontroller.
     *
     * @param viewController The viewcontroller to show the pairing modal over
     */
    void pairInViewController(ViewController viewController) {
    }

    /**
     * The current pairing status as defined by the delegate.
     * This is just a convenience method for [self.delegate isAppPaired]
     *
     * @return the current pairing status
     */
    Boolean isAppPaired() {
        return null;
    }

//Return Checkout

    /**
     * Retrieves the precheckout data from the MasterPass service
     */
    void preCheckoutDataCallback(FutureCallback callback) {
        //PreCheckoutData data
    }

    void returnCheckoutForOrder(MPOrder order, ViewController viewController) {
    }

//Pair Checkout

    void pairCheckoutForOrder(String orderNumber, ViewController viewController) {
    }

    void completePairCheckoutForOrder(String orderNumber, String transactionId, String preCheckoutTransactionId) {
    }

//Manual Checkout

    void completeManualCheckoutForOrder(String orderNumber) {

    }
}

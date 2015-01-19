package com.anypresence.masterpass_android_library;

import java.util.List;
import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public interface IManager {
    /**
     * Provides the server address (url) at which the MasterPass services reside
     *
     * @return the server address at which the MasterPass service resides
     */
    abstract String getServerAddress();

    /**
     * Provides the logic from the host app to determine if the current
     * user is paired. Usually this incorporates some logic to determine
     * if a user has a long access token, which is stored server-side.
     *
     * @return the current pairing status
     */
    abstract Boolean isAppPaired();

    /**
     * Returns an array of supported data types (card, address, profile, etc)
     * for this app
     *
     * @return the supported data types
     */
    abstract List<Object> getSupportedDataTypes();

    /**
     * Returns an array of supported card types (MasterCard, Visa, Discover, etc)
     * for this app
     *
     * @return the supported card types
     */
    abstract List<Object> getSupportedCardTypes();

    //OPTIONAL

    /**
     * Method that executes when pairing completes
     *
     * @param success the status of the pairing
     * @param error   any errors that occurred during pairing
     */
    void pairingDidComplete(Boolean success, Throwable error);

    /**
     * Method that executes when checkout completes
     *
     * @param success the status of the checkout
     * @param error   any errors that occurred during checkout
     */
    void checkoutDidComplete(Boolean success, Throwable error);

    /**
     * Method that executes when preCheckout completes
     *
     * @param success the status of the checkout
     * @param data    the preCheckout data
     * @param error   any errors that occurred during checkout
     */
    void preCheckoutDidComplete(Boolean success, Map<Object, Object> data, Throwable error);

    /**
     * Method that executes when pair & checkout completes
     *
     * @param success the status of the checkout
     * @param error   any errors that occurred during checkout
     */
    void pairCheckoutDidComplete(Boolean success, Throwable error);

    /**
     * Method that executes when manual checkout completes
     *
     * @param success the status of the checkout
     * @param error   any errors that occurred during checkout
     */
    void manualCheckoutDidComplete(Boolean success, Throwable error);

    /**
     * Method to force the reset of a user token
     * This is usually achieved by forcibly removing the
     * long access token from the user object
     */
    void resetUserPairing();

}

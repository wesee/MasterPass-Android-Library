package com.anypresence.masterpass_android_library.model;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPOrder {
    String orderNumber;
    Map<Object, Object> walletInfo;
    MPCreditCard card;
    MPAddress shippingAddress;
}

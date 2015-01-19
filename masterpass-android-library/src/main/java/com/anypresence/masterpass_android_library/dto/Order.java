package com.anypresence.masterpass_android_library.dto;

import com.anypresence.masterpass_android_library.model.MPAddress;
import com.anypresence.masterpass_android_library.model.MPCreditCard;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class Order {
    String orderNumber;
    Map<Object, Object> walletInfo;
    MPCreditCard card;
    MPAddress shippingAddress;
}

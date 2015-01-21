package com.anypresence.masterpass_android_library.dto;

import com.anypresence.masterpass_android_library.model.MPAddress;
import com.anypresence.masterpass_android_library.model.MPCreditCard;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class Order {
    public String orderNumber;
    public Wallet walletInfo;
    public MPCreditCard card;
    public MPAddress shippingAddress;
    public String transactionId;
    public String preCheckoutTransactionId;
}

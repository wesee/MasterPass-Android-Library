package com.anypresence.masterpass_android_library.dto;

import java.io.Serializable;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class Order implements Serializable {
    public String orderNumber;
    public Wallet walletInfo;
    public CreditCard card;
    public Address shippingAddress;
    public String transactionId;
    public String preCheckoutTransactionId;
}

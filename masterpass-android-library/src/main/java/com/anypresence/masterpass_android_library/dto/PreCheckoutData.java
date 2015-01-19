package com.anypresence.masterpass_android_library.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class PreCheckoutData {
    List<Object> cards;
    List<Object> addresses;
    Map<Object, Object> contactInfo;
    Map<Object, Object> walletInfo;
    Throwable error;
}

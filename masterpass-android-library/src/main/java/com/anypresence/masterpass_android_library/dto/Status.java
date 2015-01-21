package com.anypresence.masterpass_android_library.dto;

/**
 * Created by diego.rotondale on 1/20/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class Status {
    public String status;

    public boolean hasError() {
        return status.equals("error");
    }
}

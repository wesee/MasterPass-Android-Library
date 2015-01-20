package com.anypresence.masterpass_android_library.dto;

/**
 * Created by diego.rotondale on 1/20/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class JsonStatus {
    private static String MP_ERROR_NOT_PAIRED = "No long access token found associated with user (user not paired with Masterpass)";
    public String status;
    public String errors;


    public boolean hasError() {
        return status.equals("error");
    }

    public boolean isNotPaired() {
        return errors.equals(MP_ERROR_NOT_PAIRED);
    }
}

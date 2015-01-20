package com.anypresence.masterpass_android_library.exception;

/**
 * Created by diego.rotondale on 1/20/2015.
 */
public class BadRequestException extends Throwable {
    //public static String MP_ERROR_DOMAIN = "MasterPassErrorDomain";
    public static Integer MPErrorCodeBadRequest = 400;

    public BadRequestException(String detailMessage) {
        super(detailMessage);
    }
}

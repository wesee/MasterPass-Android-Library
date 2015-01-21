package com.anypresence.masterpass_android_library.dto;

import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class LightBoxParams {
    private List<String> requestedDataTypes;
    private Details details;
    private Integer requestPairing;
    private String version;
    private List<Object> allowedCardType;

    public List<String> getRequestedDataTypes() {
        return requestedDataTypes;
    }

    public void setRequestedDataTypes(List<String> requestedDataTypes) {
        this.requestedDataTypes = requestedDataTypes;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public Integer getRequestPairing() {
        return requestPairing;
    }

    public void setRequestPairing(Integer requestPairing) {
        this.requestPairing = requestPairing;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Object> getAllowedCardType() {
        return allowedCardType;
    }

    public void setAllowedCardType(List<Object> allowedCardType) {
        this.allowedCardType = allowedCardType;
    }
}

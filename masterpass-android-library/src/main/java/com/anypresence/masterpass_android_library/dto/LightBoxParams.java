package com.anypresence.masterpass_android_library.dto;

import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class LightBoxParams {
    private List<Object> requestedDataTypes;
    private PairingDetails pairingDetails;
    private Integer requestPairing;
    private String version;
    private CheckoutDetails checkoutDetails;
    private Order order;

    public List<Object> getRequestedDataTypes() {
        return requestedDataTypes;
    }

    public void setRequestedDataTypes(List<Object> requestedDataTypes) {
        this.requestedDataTypes = requestedDataTypes;
    }

    public PairingDetails getPairingDetails() {
        return pairingDetails;
    }

    public void setPairingDetails(PairingDetails pairingDetails) {
        this.pairingDetails = pairingDetails;
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

    public CheckoutDetails getCheckoutDetails() {
        return checkoutDetails;
    }

    public void setCheckoutDetails(CheckoutDetails checkoutDetails) {
        this.checkoutDetails = checkoutDetails;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

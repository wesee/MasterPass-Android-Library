package com.anypresence.masterpass_android_library.dto;

import java.util.List;

/**
 * Created by diego.rotondale on 1/19/2015.
 */
public class LightBoxParams {
    private List<Object> requestedDataTypes;
    private PairingDetails pairingDetails;
    private int requestPairing;
    private String version;

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

    public int getRequestPairing() {
        return requestPairing;
    }

    public void setRequestPairing(int requestPairing) {
        this.requestPairing = requestPairing;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

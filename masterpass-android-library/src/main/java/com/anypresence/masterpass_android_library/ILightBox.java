package com.anypresence.masterpass_android_library;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 */
public interface ILightBox {

    void pairingViewDidCompletePairing(MPLightBox pairingViewController, Boolean success, Throwable error);

    void lightBoxDidCompletePreCheckout(MPLightBox lightBoxViewController, Boolean success, Map<Object, Object> data, Throwable error);

    void lightBoxDidCompleteCheckout(MPLightBox pairingViewController, Boolean success, Throwable error);
}

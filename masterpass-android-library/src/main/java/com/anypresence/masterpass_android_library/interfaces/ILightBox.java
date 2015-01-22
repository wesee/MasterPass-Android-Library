package com.anypresence.masterpass_android_library.interfaces;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public interface ILightBox {

    void pairingViewDidCompletePairing(ViewController pairingViewController, Boolean success, Throwable error);

    void lightBoxDidCompletePreCheckout(ViewController lightBoxViewController, Boolean success, Map<Object, Object> data, Throwable error);

    void lightBoxDidCompleteCheckout(ViewController pairingViewController, Boolean success, Throwable error);
}

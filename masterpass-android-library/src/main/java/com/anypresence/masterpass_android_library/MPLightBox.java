package com.anypresence.masterpass_android_library;

import com.anypresence.masterpass_android_library.dto.LightBoxParams;
import com.anypresence.masterpass_android_library.interfaces.OnCompleteCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;

/**
 * Created by diego.rotondale on 1/19/2015.
 */
public class MPLightBox implements ViewController {

    public ILightBox delegate;

    public void initiateLightBoxOfTypeWithOptions(MPLightBoxType type, LightBoxParams options) {

    }

    @Override
    public void presentViewController(ViewController presentViewController, Boolean animated, OnCompleteCallback callback) {

    }

    public enum MPLightBoxType {
        MPLightBoxTypeConnect,
        MPLightBoxTypeCheckout,
        MPLightBoxTypePreCheckout
    }


}

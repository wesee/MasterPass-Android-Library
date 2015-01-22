package com.anypresence.masterpass_android_library.interfaces;

import android.app.Activity;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public interface ViewController {
    void presentViewController(Activity activity, Boolean animated, OnCompleteCallback callback);

    void dismissViewControllerAnimated(boolean animated, OnCompleteCallback callback);
}

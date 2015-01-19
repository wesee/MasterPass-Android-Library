package com.anypresence.masterpass_android_library.interfaces;

/**
 * Created by diego.rotondale on 1/19/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public interface ViewController {
    void presentViewController(ViewController presentViewController, Boolean animated, OnCompleteCallback callback);

    void dismissViewControllerAnimated(boolean animated, OnCompleteCallback callback);
}

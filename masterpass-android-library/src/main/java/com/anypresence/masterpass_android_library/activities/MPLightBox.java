package com.anypresence.masterpass_android_library.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anypresence.masterpass_android_library.Constants;
import com.anypresence.masterpass_android_library.MPManager;
import com.anypresence.masterpass_android_library.R;
import com.anypresence.masterpass_android_library.dto.LightBoxParams;
import com.anypresence.masterpass_android_library.dto.Status;
import com.anypresence.masterpass_android_library.dto.WebViewOptions;
import com.anypresence.masterpass_android_library.interfaces.OnCompleteCallback;
import com.anypresence.masterpass_android_library.interfaces.ViewController;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by diego.rotondale on 16/09/2014.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class MPLightBox extends Activity implements ViewController {

    private static final String URL = "file:///android_asset/mp_lightbox_base.html";
    private MPLightBoxType type;
    private LightBoxParams options;
    private WebView web;
    private ProgressDialog progressDialog;
    private MPManager delegate;
    private boolean wasLoad = false;

    public void setDelegate(MPManager delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        web = (WebView) findViewById(R.id.webview_content);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            WebViewOptions options = (WebViewOptions) extras.getSerializable(Constants.OPTIONS_PARAMETER);
            initiateLightBoxOfTypeWithOptions(options.type, options.options);
        }
    }

    public void initiateLightBoxOfTypeWithOptions(MPLightBoxType type, LightBoxParams options) {
        this.type = type;
        this.options = options;
        web.loadUrl(URL);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new MPLibraryWebViewClient());
    }

    @Override
    public void presentViewController(Activity activity, Boolean animated, WebViewOptions options) {
    }

    @Override
    public void dismissViewControllerAnimated(boolean animate, OnCompleteCallback onCompleteCallback) {
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void checkIfLoadDone(WebView webView) {
        if (progressDialog != null)
            progressDialog.dismiss();
        web.loadUrl(getJs());
    }

    private String getJs() {
        String value = null;
        if (!wasLoad && type != null && options != null) {
            value = "javascript:initiateLightbox(" + getType() + ", " + options.toJson() + ");";
            wasLoad = true;
        }
        return value;
    }

    private String getType() {
        if (type != null) {
            int JSType = type.value;
            if (type == MPLightBoxType.MPLightBoxTypePreCheckout)
                JSType = 1;
            return String.valueOf(JSType);
        }
        return null;
    }

    private RequestQueue getRequestQueue() {
        return Volley.newRequestQueue(this);
    }

    public enum MPLightBoxType {
        MPLightBoxTypeConnect(0),
        MPLightBoxTypeCheckout(1),
        MPLightBoxTypePreCheckout(2);

        int value;

        MPLightBoxType(int value) {
            this.value = value;
        }
    }

    private class MPLibraryWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                URI uri = new URI(url);
                String urlConverted = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
                if (urlConverted.equals(options.getCallbackURL())) {
                    JsonObjectRequest response = new JsonObjectRequest(Request.Method.GET, urlConverted, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String responseString = response.toString();
                                    Status status = new Gson().fromJson(responseString, Status.class);
                                    boolean success = !status.hasError();
                                    switch (type) {
                                        case MPLightBoxTypeConnect:
                                            delegate.pairingViewDidCompletePairing(MPLightBox.this, success, null);
                                            break;
                                        case MPLightBoxTypeCheckout:
                                            delegate.lightBoxDidCompleteCheckout(MPLightBox.this, success, null);
                                            break;
                                        case MPLightBoxTypePreCheckout:
                                            delegate.lightBoxDidCompletePreCheckout(MPLightBox.this, success, null, null);
                                            break;
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(MPLightBox.class.getSimpleName(), error.getMessage());
                                }
                            }
                    );
                    getRequestQueue().add(response);
                }
            } catch (URISyntaxException e) {
                Log.e(MPLightBox.class.getSimpleName(), e.getMessage());
            } finally {
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            if (url.equals(URL))
                checkIfLoadDone(webView);
        }
    }
}

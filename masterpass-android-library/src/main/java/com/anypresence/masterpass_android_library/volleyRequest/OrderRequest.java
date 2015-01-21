package com.anypresence.masterpass_android_library.volleyRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anypresence.masterpass_android_library.dto.Order;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diego.rotondale on 1/20/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class OrderRequest extends JsonObjectRequest {
    private Order order;

    public OrderRequest(String url, Order order, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null, listener, errorListener);
        this.order = order;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("order_header_id", order.orderNumber);
        return params;
    }
}

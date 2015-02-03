package com.anypresence.masterpass_android_library.util;

import android.os.Looper;

import com.anypresence.masterpass_android_library.interfaces.FutureCallback;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by diego.rotondale on 02/02/2015.
 */
public class ConnectionUtil {
    private static final String LOG_TAG = ConnectionUtil.class.getSimpleName();
    private static final Integer TIMEOUT = 10000;

    public static void call(final String url, final String xSessionId, final JSONObject json, final FutureCallback<JSONObject> listener) {
        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
                HttpResponse response;
                try {
                    HttpPost post = new HttpPost(url);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    post.setHeader("X-Session-Id", xSessionId);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType("application/json");
                    post.setEntity(se);

                    response = client.execute(post);
                    if (response != null) {
                        StatusLine statusLine = response.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        if (statusCode == 200) {
                            InputStream in = response.getEntity().getContent();
                            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr;
                            while ((inputStr = streamReader.readLine()) != null)
                                responseStrBuilder.append(inputStr);
                            listener.onSuccess(new JSONObject(responseStrBuilder.toString()));
                        } else {
                            listener.onFailure(new Throwable());
                        }
                    }
                } catch (Exception e) {
                    listener.onFailure(e);
                }
                Looper.loop();
            }
        };
        t.start();
    }
}

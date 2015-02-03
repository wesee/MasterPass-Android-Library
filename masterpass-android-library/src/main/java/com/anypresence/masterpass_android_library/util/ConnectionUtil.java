package com.anypresence.masterpass_android_library.util;

import android.os.Looper;

import com.anypresence.masterpass_android_library.connection.MPSSLSocketFactory;
import com.anypresence.masterpass_android_library.interfaces.FutureCallback;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by diego.rotondale on 02/02/2015.
 */
public class ConnectionUtil {
    private static final String LOG_TAG = ConnectionUtil.class.getSimpleName();
    private static final Integer TIMEOUT = 10000;

    public static void call(final String url, final JSONObject json, final FutureCallback<JSONObject> listener) {
        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                HttpClient client = getHttpsClient(new DefaultHttpClient());
                HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
                HttpResponse response;
                try {
                    HttpPost post = new HttpPost(url);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");

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
                            listener.onSuccess(new JSONObject(inputStr));
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

    public static HttpClient getHttpsClient(HttpClient client) {
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new MPSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
}

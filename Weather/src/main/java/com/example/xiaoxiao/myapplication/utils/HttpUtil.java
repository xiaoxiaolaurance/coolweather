package com.example.xiaoxiao.myapplication.utils;


import android.net.Uri;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class HttpUtil {

    static OkHttpClient okHttpClient;
    public static void sendRequestWithOkhttp(String address, Callback callback){
        if(okHttpClient ==null){
            okHttpClient =new OkHttpClient();
        }

        try {
            setSSL(okHttpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request =new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    private static void setSSL(OkHttpClient okHttpClient) throws Exception{
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {

            }
        }}, new SecureRandom());
        okHttpClient.newBuilder().sslSocketFactory(sc.getSocketFactory());
        okHttpClient.newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
}

package com.triskelapps.etherchusky.api;


import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ApiClient {

    // Tutorial Retrofit 2.0
    // http://inthecheesefactory.com/blog/retrofit-2.0/en

    //    public static final String BASE_URL = "http://192.168.0.17:5000";
    public static final String BASE_URL = "http://90.173.123.147:5000/";

    private static Retrofit sharedInstance;

    public static Retrofit getInstance(Context context) {
        if (sharedInstance == null) {

            sharedInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }

        return sharedInstance;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private static OkHttpClient getSSLClient(Context context) {
//
//
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme("http", PlainSocketFactory
//                .getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
//                443));
//
//        HttpParams params = new BasicHttpParams();
//        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
//        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
//                new ConnPerRouteBean(30));
//        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
//        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//
//        ClientConnectionManager cm = new SingleClientConnManager(params,
//                schemeRegistry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
//
//        OkHttpClient client = new OkHttpClient();
//
//        return null;
//
//    }
//
//
//    private static OkHttpClient getSSLClientTrusted(Context context) {
//
//        try {
//            OkHttpClient client = new OkHttpClient();
//            KeyStore keyStore = readKeyStore(context); //your method to obtain KeyStore
//
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
//            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//            client.setSslSocketFactory(sslContext.getSocketFactory());
//
//            return client;
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        throw new RuntimeException("Unable to configure SSL client");
//
//    }
//
//    static KeyStore readKeyStore(Context context) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//        // get user password and file input stream
//        char[] password = getPassword();
//
//        FileInputStream fis = null;
//        try {
//            fis = context.getResources().openRawResource(R.raw.your_keystore_filename);
//            ks.load(fis, password);
//        } finally {
//            if (fis != null) {
//                fis.close();
//            }
//        }
//        return ks;
//    }
}

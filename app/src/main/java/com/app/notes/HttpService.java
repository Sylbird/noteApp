package com.app.notes;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {
    public interface OnResponseListener {
        void onResponse(String response);
    }

    public static void get(String urlService, OnResponseListener listener) {
        // Make an HTTP GET request
        new Thread(() -> {
            try {
                URL url = new URL(urlService);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                listener.onResponse(response.toString());

            }catch (Exception e){
                e.printStackTrace();
                Log.e("HttpService-GET",e.toString());
            }
        }).start();
    }
}

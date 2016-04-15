package com.iot.shome;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class WebRequest extends AsyncTask {

    @Override
    protected Object doInBackground(Object [] params) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(params[0].toString()).build();
        try {

            Response response = client.newCall(request).execute();
            System.out.println("RESPONSE : "+response);
            return response.body().string();

        } catch (IOException io){
            io.printStackTrace();
        }

        return null;
    }
}

package com.iot.shome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Kareem Diab on 11/17/2015.
 */
public class WebViewFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        WebView browser = (WebView) rootView.findViewById(R.id.webView);
        browser.setWebViewClient(new WebViewClient());
        WebSettings webSettings = browser.getSettings();
        //Toast.makeText(getContext(), "whats going on", Toast.LENGTH_SHORT).show();
        webSettings.setJavaScriptEnabled(true);
        browser.loadUrl("http://104.131.53.165/Digital_Home");

        return rootView;
    }
}

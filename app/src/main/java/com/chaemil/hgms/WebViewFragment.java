package com.chaemil.hgms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

public class WebViewFragment extends Fragment {

    private WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reportbug, container, false);

        Bundle bundle = this.getArguments();
        String link = "";
        if (bundle != null) {
            link = bundle.getString(Basic.BUNDLE_LINK);
        }

        Utils.log("WebViewFragmentLink", link);

        webview = (WebView) rootView.findViewById(R.id.webview);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(link);

        return rootView;
    }
}

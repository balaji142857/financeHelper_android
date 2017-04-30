package com.krishan.balaji.fh.activities.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.util.Util;

public class AssetUsageFragment extends Fragment{

    WebView webView;
    public AssetUsageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);;

        String customHtml = Util.htmlHeader+Util.getAssetUsageInfo(getActivity())+Util.htmlClosure;
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadDataWithBaseURL("file:///android_asset/", customHtml, "text/html", "utf-8", "");
        return view;
    }

}
package com.krishan.balaji.fh.activities.dashboard;


import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.krishan.balaji.fh.R;
import com.krishan.balaji.fh.data.AssetEntry;
import com.krishan.balaji.fh.util.Util;

public class AssetFragment extends Fragment{

    WebView webView;
    public AssetFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);;

        String customHtml = Util.htmlHeader+Util.getAssetInfo(getActivity())+Util.htmlClosure;
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadDataWithBaseURL("file:///android_asset/", customHtml, "text/html", "utf-8", "");
        return view;
    }

}
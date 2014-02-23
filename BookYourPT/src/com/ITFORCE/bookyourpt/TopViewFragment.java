package com.ITFORCE.bookyourpt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TopViewFragment extends Fragment {
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_topview, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		localInit();
		functionInit();
	}

	private void localInit() {
		webView = (WebView) getActivity().findViewById(R.id.frag_webview);
	}

	private void functionInit() {
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://google.com");
	}
}

package runeaudio.com.runeaudio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class RuneAudioUiFragment extends BaseFragment {

    // TODO: need to pass in URL or read from shared prefs.

    private WebView mWebView;

    @Override
    public int getLayout() {
        return R.layout.fragment_runeaudio_ui;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mWebView = (WebView) view.findViewById(R.id.runeUi);
        // TOOD: remove hard-coded URL.
        mWebView.loadUrl("http://172.16.0.10");


        return view;
    }
}
package runeaudio.com.runeaudio;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public final class RuneAudioApplication extends Application {

    private SharedPreferences mSharedPrefs;


    private static final String HOST = "host";
    private static final String PORT = "port";

    @Override
    public void onCreate() {
        super.onCreate();

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }


    // TODO: move preferences into separate class if too many values are needed.
    public void setPort(int port) {
        mSharedPrefs
                .edit()
                .putInt(PORT, port)
                .apply();
    }

    public int getPort() {
        return mSharedPrefs.getInt(PORT, -1);
    }

    public void setHost(String host) {
        mSharedPrefs
                .edit()
                .putString(HOST, host)
                .apply();
    }

    public String getHost() {
        return mSharedPrefs.getString(HOST, null);
    }

}

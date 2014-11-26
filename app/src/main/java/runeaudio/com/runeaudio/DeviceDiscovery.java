package runeaudio.com.runeaudio;


import android.content.AsyncTaskLoader;
import android.content.Context;

public class DeviceDiscovery extends AsyncTaskLoader<Object> {

    public DeviceDiscovery(Context wtf) {
        super(wtf);
    }

    @Override
        protected void onStartLoading() {
                forceLoad();
        }


    @Override
    public Object loadInBackground() {
        return null;
    }
}

package runeaudio.com.runeaudio;


import android.nsdchat.NsdHelper;
import android.util.Log;

public class DiscoveryFragment extends BaseFragment implements NsdHelper.NsdHelperListener {

    private NsdHelper mNsdHelper;

    private static final String TAG = "DiscoveryFragment";

    @Override
    public int getLayout() {
        return R.layout.fragment_discovery;
    }

    @Override
    public void onStart() {
        mNsdHelper = new NsdHelper(getActivity(), this);
        mNsdHelper.initializeNsd();

        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Pausing.");
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        Log.d(TAG, "Resuming.");
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }

    // For KitKat and earlier releases, it is necessary to remove the
    // service registration when the application is stopped. There's
    // no guarantee that the onDestroy() method will be called (we're
    // killable after onStop() returns) and the NSD service won't remove
    // the registration for us if we're killed.
    // In L and later, NsdService will automatically unregister us when
    // our connection goes away when we're killed, so this step is
    // optional (but recommended).
    @Override
    public void onStop() {
        Log.d(TAG, "Being stopped.");
        mNsdHelper = null;
        super.onStop();
    }

    @Override
    public void OnServiceAdded() {
        Log.d(TAG, "service added: " + mNsdHelper.getChosenServiceInfo());
    }



}

package runeaudio.com.runeaudio;


import android.net.nsd.NsdServiceInfo;
import android.nsdchat.NsdHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DiscoveryFragment extends BaseFragment implements NsdHelper.NsdHelperListener, AdapterView.OnItemClickListener {

    // TODO: need to handle some timeout. show popup, toast, etc.

    private NsdHelper mNsdHelper;

    private static final String TAG = "DiscoveryFragment";

    private View mProgressLayout;
    private ListView mServiceListView;

    @Override
    public int getLayout() {
        return R.layout.fragment_discovery;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mProgressLayout = rootView.findViewById(R.id.progressLayout);
        mServiceListView = (ListView) rootView.findViewById(R.id.serviceList);

        mServiceListView.setOnItemClickListener(this);

        return rootView;
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

    /**
     * Save Service info in shared prefs.
     * @param serviceInfo
     */
    private void saveSelectedService(NsdServiceInfo serviceInfo) {
        MainActivity activity = (MainActivity) getActivity();

        // save service, host & port into shared prefs
        RuneAudioApplication application = (RuneAudioApplication) activity.getApplication();

        application.setServiceName(serviceInfo.getServiceName());
        application.setHost(serviceInfo.getHost().getHostAddress());
        application.setPort(serviceInfo.getPort());
    }


    private void showServicesList() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mServiceListView.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void hideProgressLayout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void OnServicesAdded() {

        ArrayList<NsdServiceInfo> resolvedServiceList = mNsdHelper.getResolvedServiceList();

        switch (resolvedServiceList.size()) {
            case 0:
                // TODO: give a toast or something to say nothing found.
                break;


            case 1:
                // single host found: use it!
                saveSelectedService(resolvedServiceList.get(0));

                // switch to ui fragment.
                // TODO: title in action bar needs to be updated.
                MainActivity activity = (MainActivity) getActivity();
                activity.onNavigationDrawerItemSelected(BaseFragment.DEVICE_FRAGMENT);
                break;

            default:

                // switch to listview for user to choose.
                hideProgressLayout();

                // TODO: put this above & update via setter?
                final DiscoveryServicesAdapter adapter = new DiscoveryServicesAdapter(getActivity(), mNsdHelper.getResolvedServiceList());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mServiceListView.setAdapter(adapter);
                    }
                });

                showServicesList();
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<NsdServiceInfo> resolvedServiceList = mNsdHelper.getResolvedServiceList();

        saveSelectedService(resolvedServiceList.get(position));

        // switch to ui fragment.
        // TODO: title in action bar needs to be updated.
        MainActivity activity = (MainActivity) getActivity();
        activity.onNavigationDrawerItemSelected(BaseFragment.DEVICE_FRAGMENT);
    }
}

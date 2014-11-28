package runeaudio.com.runeaudio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected  static final String ARG_SECTION_NUMBER = "section_number";

    public static final int DISCOVERY_FRAGMENT = 0;
    public static final int DEVICE_FRAGMENT = 1;

    /**
     * Returns a new instance of this fragment for the given section
     * number. Basically this is a factory method.
     */
    public static BaseFragment newInstance(int sectionNumber) {

        BaseFragment fragment;

        switch (sectionNumber) {
            case DISCOVERY_FRAGMENT:
                fragment = new DiscoveryFragment();
                break;

            case DEVICE_FRAGMENT:
                fragment = new RuneAudioUiFragment();
                break;

            default:
                return null;
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BaseFragment() {
    }

    public abstract int getLayout();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);

        // TODO: start here. need to get access to device info to show progress bar or show "device found".
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

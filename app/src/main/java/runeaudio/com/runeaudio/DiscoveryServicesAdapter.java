package runeaudio.com.runeaudio;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class DiscoveryServicesAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<NsdServiceInfo> mServicesList;

    public DiscoveryServicesAdapter(Context context, ArrayList<NsdServiceInfo> servicesList) {
        mContext = context;
        mServicesList = servicesList;
    }


    @Override
    public int getCount() {
        return mServicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: update with view holder & possibly other service info like host & port.
        convertView = LayoutInflater.from(mContext).inflate(R.layout.service_info, parent, false);

        TextView serviceName = (TextView) convertView.findViewById(R.id.serviceName);
        serviceName.setText(mServicesList.get(position).getServiceName());

        return convertView;
    }
}

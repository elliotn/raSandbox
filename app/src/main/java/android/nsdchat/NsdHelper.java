/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.nsdchat;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class NsdHelper {

    public interface NsdHelperListener {
        public void OnServicesAdded();
    }


    private Context mContext;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private NsdHelperListener mListener;

    // TODO: is this the service we want? it returns port 9.
    public static final String SERVICE_TYPE = "_workstation._tcp";
    private static final int HARD_CODED_PORT = 80;

    public static final String TAG = "NsdHelper";
    public static final String SERVICE_NAME = "runeaudio";

    public static final int DISCOVERY_TIMEOUT_SECONDS = 5000;
    public Handler mDiscoveryTimeoutHandler;


    private NsdServiceInfo mService;
    private LinkedList<NsdServiceInfo> mServiceList = new LinkedList<NsdServiceInfo>();
    private Iterator<NsdServiceInfo> mServiceListIterator  = null;

    private ArrayList<NsdServiceInfo> mResolvedServiceList = new ArrayList<>();

    public NsdHelper(Context context, NsdHelperListener listener) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        mListener = listener;
    }

    public void initializeNsd() {
        startDiscoveryTimer();
        initializeDiscoveryListener();

        //mNsdManager.init(mContext.getMainLooper(), this);
    }

    private void allServicesResolved() {
        mServiceList.clear();

        if (mListener != null) {
            mListener.OnServicesAdded();
        }
    }


    public void resolveNextService() {
        if (mServiceListIterator == null) {
            return;
        }

        // either resolve the next service or we are done.
        if (mServiceListIterator.hasNext()) {
            NsdServiceInfo service = (NsdServiceInfo) mServiceListIterator.next();
            mNsdManager.resolveService(service, new ResolveListener());
        } else {
            allServicesResolved();
        }
    }


    /**
     * Discovery still be stopped after DISCOVERY_TIMEOUT_SECONDS.
     */
    public void startDiscoveryTimer() {
        mDiscoveryTimeoutHandler = new Handler();

        mDiscoveryTimeoutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopDiscovery();
            }
        }, DISCOVERY_TIMEOUT_SECONDS);
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success " + service);

                // only add service that we are looking for.
                if (service.getServiceName().contains(SERVICE_NAME)) {
                    mServiceList.add(service);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // TODO: think about this.
                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
            }


            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);

                // get iterator and start resolving services one at a time.
                mServiceListIterator = mServiceList.iterator();

                resolveNextService();
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };

    }




    private class ResolveListener implements NsdManager.ResolveListener {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Resolve failed " + errorCode + " name == " + serviceInfo.getServiceName());

            resolveNextService();
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

            // TODO: change this? since we are not searching for http service, we will hard code port.
            serviceInfo.setPort(HARD_CODED_PORT);

            mResolvedServiceList.add(serviceInfo);

            resolveNextService();
        }


    }




    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery() {
        if (mDiscoveryListener != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            mDiscoveryListener = null;
        }
    }

    public ArrayList<NsdServiceInfo> getResolvedServiceList() {
        return mResolvedServiceList;
    }


}

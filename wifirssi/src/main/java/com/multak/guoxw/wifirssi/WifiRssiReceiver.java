package com.multak.guoxw.wifirssi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiRssiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){

            int strength=getStrengthLevels(context);
            System.out.println("当前信号 "+strength);
        }
    }

    public int getStrengthLevels(Context context)
    {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            int x=info.getRssi();
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);

            Log.d("wifi", "getStrength: ---"+x+"____"+strength);
            return strength;

        }
        return 0;
    }
}

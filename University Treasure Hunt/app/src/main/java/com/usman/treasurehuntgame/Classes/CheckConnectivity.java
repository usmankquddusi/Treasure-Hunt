package com.usman.treasurehuntgame.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sibghat on 4/22/2018.
 */

public class CheckConnectivity extends BroadcastReceiver {

    private static String TAG = "CheckConnectivity";
    @Override
    public void onReceive(Context context, Intent arg1) {

        boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(isConnected){
            Log.d(TAG, "onReceive: Internet Connection Lost");
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
            StaticVariables.isInternetConnected = false;
        }
        else{
            Log.d(TAG, "onReceive: Internet Connected");
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
            StaticVariables.isInternetConnected = true;
        }
    }
}
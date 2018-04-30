package com.usman.treasurehuntgame.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;

import com.usman.treasurehuntgame.Adapters.ScoreboardAdapter;
import com.usman.treasurehuntgame.Firebase.FirebaseHandler;
import com.usman.treasurehuntgame.R;

public class ScoreBoardActivity extends BaseActivity {

    private static final String TAG ="ScoreBoardActivity" ;
    ListView listView;
    BroadcastReceiver broadCastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        setids();
        registerLocalBroadcastReceiver();
        if(FirebaseHandler.scoreList != null)
        {
            setListviewAdapter();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocalBroadcastReceiver();
    }

    void setids(){
        listView = (ListView) findViewById(R.id.score_listview);
    }
    void setListviewAdapter(){
// Create the adapter to convert the array to views
        ScoreboardAdapter adapter = new ScoreboardAdapter(this, FirebaseHandler.scoreList);
// Attach the adapter to a ListView
        listView.setAdapter(adapter);
    }

    private void registerLocalBroadcastReceiver(){
        Log.d(TAG, "registerLocalBroadcastReceiver: ");
        broadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                setListviewAdapter();
            }};
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, new IntentFilter(FirebaseHandler.SCORE_BROADCAST));
    }
    private void unregisterLocalBroadcastReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        Log.d(TAG, "unregisterLocalBroadcastReceiver: ");
    }
}

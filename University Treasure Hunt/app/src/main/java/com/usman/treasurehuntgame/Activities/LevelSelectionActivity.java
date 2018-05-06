package com.usman.treasurehuntgame.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.usman.treasurehuntgame.Adapters.StageAdapter;
import com.usman.treasurehuntgame.Classes.JsonFileReader;
import com.usman.treasurehuntgame.R;

import java.util.ArrayList;

public class LevelSelectionActivity extends BaseActivity {

    private static String TAG = "LevelSelectionActivity";
    JsonFileReader jsonFileReader = new JsonFileReader(this);
    ListView list;  // To show contacts in List

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_level_selection);
        setIds();
        setListeners();
        askForPermission();
        setListView();
    }


    void setIds(){
        list = (ListView) findViewById(R.id.stages_list);

    }

    void setListeners(){

        // ListView Item Listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    void setListView(){
        // Registering for context menu which opens when a contact is long clicked in listview
        registerForContextMenu(list);
// Setting custom list adapter
        ArrayList<String> stageNameList = new ArrayList<>();
        for (String stageName: jsonFileReader.getStagesNames()){
            stageNameList.add(stageName.substring(0,stageName.length()-5));
        }
        StageAdapter adapter = null;
        adapter = new StageAdapter(this, R.layout.stages_item,stageNameList);

        list.setAdapter(adapter);

    }

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        checkPlayerDataPresent();
                    } else {
                        Snackbar.make(this.getCurrentFocus(),"Application needs to have Write Storage permission to work properly!",Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();
                    }
                }

            }
        }
    }
}
package com.usman.treasurehuntgame.Classes;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.usman.treasurehuntgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * Created by sibghat on 4/1/2018.
 */

public class JsonFileWriter extends JSONObject {
    private static String TAG = "JsonFileWriter";
    public JsonFileWriter(){

    }

    public boolean writePlayerObjectFile (Context context,String name) {

        JSONObject obj = new JSONObject() ;
        try {
            obj.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Writer output = null;
//            String path = context.getFilesDir().getAbsolutePath();
            String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
            File folder = new File(Environment.getExternalStorageDirectory()+ "/", context.getString(R.string.app_name));
            if(!folder.exists()) {
                folder.mkdirs();
                boolean result = folder.mkdirs();
                Log.d("MyActivity", "mkdirs: " + result);
            }

            String fileName = context.getString(R.string.player_data);
            File file = new File(path +"/",fileName + ".json");

            output = new BufferedWriter(new FileWriter(file));
            output.write(obj.toString());
            output.close();
            Log.d(TAG, "writePlayerObjectFile: path="+path);
            Log.d(TAG, "writePlayerObjectFile: Player file saved successfully");
            return true;

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }
}

package com.usman.treasurehuntgame.Classes;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.usman.treasurehuntgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sibghat on 4/1/2018.
 */

public class JsonFileReader {

    private static String TAG = "JsonFileReader";
    Context context;
    public JsonFileReader(Context context){
        this.context = context;
    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ fileName + ".json";
        File file = new File(path);
        return file.exists();
    }

    public boolean isStagesPresent(String folderName){
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ folderName;
        File file = new File(path);
        File[] contents = file.listFiles();

// the directory file is not really a directory..
        if (contents == null) {
            return false;
        }
// Folder is empty
        else if (contents.length == 0) {
            Log.d(TAG, "isStagesPresent: No Stages Present in Folder");
            return false;
        }
// Folder contains files
        else {
            return true;
        }
    }

    public void readStages() throws JSONException {

        File folder = new File(Environment.getExternalStorageDirectory()+ "/", context.getString(R.string.app_name)+"/stages");
        if(!folder.exists()) {
            folder.mkdirs();
            boolean result = folder.mkdirs();
            Log.d("MyActivity", "mkdirs: " + result);
        }

        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/stages";
        File file = new File(path);
        File[] contents = file.listFiles();

        for(File stage: contents){
            Log.d(TAG, "readStages: stage:"+stage);
            readStageDataFromFile(stage);
        }
    }

    private JSONObject readStageDataFromFile(File stage) throws JSONException {
        String data = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(stage));
            try {
                data = br.readLine();
                br.close();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONObject playerJson = new JSONObject(data);
        Log.d(TAG, "readFromFile: data read from stage file:"+data);
        return playerJson;
    }

    public JSONObject readPlayerDataFromFile() throws JSONException {
        String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
        File file = new File(path, "player.json");
        String data = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                data = br.readLine();
                br.close();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
//            try {
////                file.createNewFile();
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//            }
            e.printStackTrace();
        }
        JSONObject playerJson = new JSONObject(data);
        Log.d(TAG, "readFromFile: data read from player file:"+data);
        return playerJson;
    }

}

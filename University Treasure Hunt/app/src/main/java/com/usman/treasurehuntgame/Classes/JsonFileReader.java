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
import java.util.ArrayList;

/**
 * Created by sibghat on 4/1/2018.
 */

public class JsonFileReader {

    private static String TAG = "JsonFileReader";
    Context context;
    public JsonFileReader(Context context){
        this.context = context;
    }

    // generic function to check if a file is present at a location
    public boolean isFilePresent(Context context, String fileName) {
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ fileName + ".json";
        File file = new File(path);
        return file.exists();
    }

    //Check if stages are present in the stages folder
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

    // Reading all stages files from stages folder containing jason data
    public ArrayList<String> getStagesNames(){

        File folder = new File(Environment.getExternalStorageDirectory()+ "/", context.getString(R.string.app_name)+"/stages");
        if(!folder.exists()) {
            folder.mkdirs();
            boolean result = folder.mkdirs();
            Log.d("MyActivity", "mkdirs: " + result);
        }

        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/stages";
        File file = new File(path);
        ArrayList<String> stageFileNames = new ArrayList<>();

        File[] stageFiles = file.listFiles();
        if(stageFiles != null) {
            for (File stage : stageFiles) {
                Log.d(TAG, "readStages: stage:" + stage + "|" + "name:" + stage.getName());
                stageFileNames.add(stage.getName());
//            readStageDataFromFile(stage);
            }
        }
        return stageFileNames;

    }


    // Reading all stages files from stages folder containing jason data
    public ArrayList<File> getStagesFiles() throws JSONException {

        File folder = new File(Environment.getExternalStorageDirectory()+ "/", context.getString(R.string.app_name)+"/stages");
        if(!folder.exists()) {
            folder.mkdirs();
            boolean result = folder.mkdirs();
            Log.d("MyActivity", "mkdirs: " + result);
        }

        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/stages";
        File file = new File(path);
        ArrayList<File> stageFilesList = new ArrayList<>();

        File[] stageFiles = file.listFiles();

        for(File stage: stageFiles){
            Log.d(TAG, "readStages: stage:"+stage+"|"+"name:"+stage.getName());
            stageFilesList.add(stage);
//            readStageDataFromFile(stage);
        }
        return stageFilesList;
    }

    // Read stage files data which is in json format
    public JSONObject readStageDataFromFile(String stageName) throws JSONException {
        String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name )+"/stages";
        File file = new File(path, stageName);
//        String data = "";
        StringBuilder data = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {

                String line;
                while ((line = br.readLine()) != null) {
                    data.append(line);
                    data.append('\n');
                }
                br.close();
//                data = br.readLine();
//                Log.d(TAG, "readStageDataFromFile: br.readLine():"+br.readLine());
//                br.close();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readStageDataFromFile: data read from stage file:"+data);
        return new JSONObject(data.toString());


    }

    // Reading player's data from file
    public JSONObject readPlayerDataFromFile() throws JSONException {
        String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
        File file = new File(path, "player.json");
        StringBuilder data = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    data.append(line);
                    data.append('\n');
                }
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
        JSONObject playerJson = new JSONObject(data.toString());
        Log.d(TAG, "readFromFile: data read from player file:"+data);
        return playerJson;
    }

}

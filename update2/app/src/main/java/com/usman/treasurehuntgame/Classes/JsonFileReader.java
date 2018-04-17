package com.usman.treasurehuntgame.Classes;

import android.content.Context;
import android.os.Environment;

import com.usman.treasurehuntgame.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by sibghat on 4/1/2018.
 */

public class JsonFileReader {
    public JsonFileReader(){

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ fileName + ".json";
        File file = new File(path);
        return file.exists();
    }

    public boolean isStagesPresent(Context context, String folderName){
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ folderName;
        File file = new File(path);
        File[] contents = file.listFiles();

// the directory file is not really a directory..
        if (contents == null) {
            return false;
        }
// Folder is empty
        else if (contents.length == 0) {
            return false;
        }
// Folder contains files
        else {
            return true;
        }
    }

    public void readStages(Context context, String folderName){
        String path = Environment.getExternalStorageDirectory()+"/"+context.getString(R.string.app_name)+"/"+ folderName;
        File file = new File(path);
        File[] contents = file.listFiles();

        for(File stages: contents){
        }
    }
    public static Object objectFromFile(String path) throws IOException, ClassNotFoundException {
        Object object = null;
        File data = new File(path);
        if(data.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
            object = objectInputStream.readObject();
            objectInputStream.close();
        }
        return object;
    }

}

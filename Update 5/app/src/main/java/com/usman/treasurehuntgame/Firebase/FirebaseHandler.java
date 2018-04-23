package com.usman.treasurehuntgame.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usman.treasurehuntgame.Classes.JsonFileReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sibghat on 4/22/2018.
 */

public class FirebaseHandler {

    Context context;
    DatabaseReference dbRef;
    JsonFileReader jsonFileReader;

    public FirebaseHandler(Context context) {
        this.context = context;
        dbRef = FirebaseDatabase.getInstance().getReference();
        jsonFileReader =  new JsonFileReader(context);
    }

    public void savePlayerData(){
        String name= null, score=null, age=null;
        JSONObject playerJson=null;
        try {
            playerJson = jsonFileReader.readPlayerDataFromFile();
            name =  playerJson.getString("name");
            age =  playerJson.getString("age");
            score = playerJson.getString("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("name",name);
        hashMap.put("age", age);
        hashMap.put("score", score);
        dbRef.child("users").child(name).setValue(hashMap);

    }


}

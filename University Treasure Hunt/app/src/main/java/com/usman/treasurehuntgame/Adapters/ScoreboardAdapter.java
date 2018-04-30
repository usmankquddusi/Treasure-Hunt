package com.usman.treasurehuntgame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usman.treasurehuntgame.Firebase.FirebaseHandler;
import com.usman.treasurehuntgame.Models.ScoreObj;
import com.usman.treasurehuntgame.R;

import java.util.ArrayList;

/**
 * Created by sibghat on 4/29/2018.
 */

public class ScoreboardAdapter extends ArrayAdapter<ScoreObj> {
    public ScoreboardAdapter(Context context, ArrayList<ScoreObj> scoreObj) {
        super(context, 0, scoreObj);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.name_tv_score);
        TextView tvScore = (TextView) convertView.findViewById(R.id.score_tv);
        // Populate the data into the template view using the data object
        tvName.setText(FirebaseHandler.scoreList.get(position).getName());
        tvScore.setText(FirebaseHandler.scoreList.get(position).getScore() + "");
        // Return the completed view to render on screen
        return convertView;
    }
}

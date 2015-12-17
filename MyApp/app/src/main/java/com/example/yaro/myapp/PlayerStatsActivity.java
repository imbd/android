package com.example.yaro.myapp;

import android.os.Bundle;
import android.widget.TextView;


public class PlayerStatsActivity extends Parent {
    String teamName = "";
    String playerName = "";
    int champNumber;
    int[] stats = new int[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);
        teamName = getIntent().getExtras().getString("teamName");
        playerName = getIntent().getExtras().getString("playerName");
        champNumber = getIntent().getExtras().getInt("champNumber");
        for (int i = 0; i < 5; i++) {
            stats[i] = getIntent().getExtras().getInt(String.valueOf(i));
        }
        setTitle(playerName+'('+teamName+')');
        TextView tv = (TextView) findViewById(R.id.textView);
        String all_text = "";
        if (stats[4] == 0) {
            for (int i = 0; i < 4; i++) {
                all_text += (PRINTSTATS[i] + ": " + stats[i] + '\n');
            }
            if (stats[2] != 0) {
                double res = (double)stats[1] / (double)stats[2];
                all_text += "Минут на гол: " + Math.round(res * 100.0) / 100.0 + '\n';

            }
            if (stats[3] != 0) {
                double res = (double)stats[1] / (double)stats[3];
                all_text += "Минут на голевую передачу: " + Math.round(res * 100.0) / 100.0 + '\n';

            }
        } else {

            for (int i = 0; i < 5; i++) {
                if (i == 2 || i == 3) {
                    continue;
                }
                all_text += (PRINTSTATS[i] + ": " + stats[i] + '\n');
            }
            if (stats[0] != 0) {
                double res = (double)stats[4] / (double)stats[0];
                all_text += "В среднем пропущено: " + Math.round(res * 100.0) / 100.0 + '\n';
            }
        }
        if (stats[0] == 0) {
            all_text = (PRINTSTATS[0] + ": " + stats[0] + '\n');
        }
        tv.setText(all_text);
    }
}

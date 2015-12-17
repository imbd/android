package com.example.yaro.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class TeamActivity extends Parent {
    String teamRef = "";
    String teamName = "";
    int champNumber;
    int teamNumber = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        teamName = getIntent().getExtras().getString("teamName");
        teamRef = getIntent().getExtras().getString("teamRef");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        champNumber = getIntent().getExtras().getInt("champnumber");
        setTitle(teamNames[champNumber][teamNumber]);
        TextView tv = (TextView) findViewById(R.id.textView3);
        String text = "";
        for (int j = 0; j < 8; j++) {
            text += TEAMSTATS[j] + ": " + teamStats[champNumber][teamNumber][j] + '\n';
        }
        tv.setText(text);

    }
    public void showPlayersPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            popupMenu.getMenu().add(Menu.NONE,i,Menu.NONE,playerNames[champNumber][teamNumber][i]);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int num = item.getItemId();
                Intent intent = new Intent(TeamActivity.this, PlayerStatsActivity.class);
                intent.putExtra("teamRef", teamRef);
                intent.putExtra("teamName", teamName);
                intent.putExtra("playerName", playerNames[champNumber][teamNumber][num]);
                intent.putExtra("champnumber", champNumber);
                for (int j = 0; j < 5; j++) {
                    intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][num][j]);
                }

                startActivity(intent);
                return true;
            }
        });
        popupMenu.show();
    }
    public void showScorersPopupMenu(View v) {

        final Score[] sortPlayers = new Score[numberOfPlayers[champNumber][teamNumber]];
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            Score newPlayer = new Score(playerNames[champNumber][teamNumber][i], playerStats[champNumber][teamNumber][i][2], i);
            sortPlayers[i] = newPlayer;
        }
        Arrays.sort(sortPlayers, new Comparator<Score>() {
            @Override
            public int compare(Score n1, Score n2) {
                return n1.goals > n2.goals ? -1 : n1.goals == n2.goals ? 0 : 1;
            }
        });

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            popupMenu.getMenu().add(Menu.NONE,i,Menu.NONE, sortPlayers[i].name + "(" + sortPlayers[i].goals + ")");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int num = item.getItemId();
                num = sortPlayers[num].number;
                Intent intent = new Intent(TeamActivity.this, PlayerStatsActivity.class);
                intent.putExtra("teamRef", teamRef);
                intent.putExtra("teamName", teamName);
                intent.putExtra("playerName", playerNames[champNumber][teamNumber][num]);
                intent.putExtra("champnumber", champNumber);
                for (int j = 0; j < 5; j++) {
                    intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][num][j]);
                }

                startActivity(intent);
                return true;
            }
        });

        popupMenu.show();
    }
    public void showSchedule(View v) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                oldData = false;
                makeRefs(champNumber, teamNames[champNumber][teamNumber].replaceAll(" ", ""), teamRefs[champNumber][teamNumber], "teamschedule", teamNumber);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (wasInterrupted[champNumber] || wasInter || oldData) {
            showAlert2();
            return;
        }
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        String event;
        for (int i = 0; i < matchNumber[teamNumber]; i++) {
            if (matchType[i]) {
                event = matches[i] + "(дома)";
            }
            else {
                event = matches[i] + "(в гостях)";
            }
            popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, event);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
        }
        popupMenu.show();
    }
}

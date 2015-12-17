package com.example.yaro.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import java.util.Arrays;
import java.util.Comparator;

public class ChampStatsActivity extends Parent {

    int champNumber;
    String champ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_stats);

        champNumber = getIntent().getExtras().getInt("champNumber");
        champ = getIntent().getExtras().getString("champName");
        setTitle(champ);
    }

    public void showScorers(View v) {
        Button button = (Button) findViewById(v.getId());
        int type = 0;
        switch (button.getId()) {
            case R.id.button6:
                type = 2;
                break;
            case R.id.button7:
                type = 3;
                break;
            case R.id.button8:
                type = 23;
                break;
            case R.id.button9:
                type = 4;
                break;
            default:
                break;
        }

        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlert2();
            return;
        }
        final PlayerInfo[] sortScorers = new Parent.PlayerInfo[numberOfPlayersInChamp[champNumber]];
        int num = 0;
        for (int i = 0; i < numberOfTeams[champNumber]; i++) {
            for (int j = 0; j < numberOfPlayers[champNumber][i]; j++) {
                PlayerInfo np = new PlayerInfo(i,j,0);
                if (type == 2 | type == 3) {
                    np = new PlayerInfo(i, j, (double) playerStats[champNumber][i][j][type]);
                }
                if (type == 23) {
                    if (playerStats[champNumber][i][j][0] != 0) {
                        np = new PlayerInfo(i, j, (double)(playerStats[champNumber][i][j][2] + playerStats[champNumber][i][j][3])/(double)playerStats[champNumber][i][j][0]);
                    }
                    else {
                        np = new PlayerInfo(i, j, 0);
                    }
                }
                if (type == 4) {
                    if (playerStats[champNumber][i][j][4] != 0) {
                        double res = ((double)playerStats[champNumber][i][j][4]/(double)playerStats[champNumber][i][j][0]);
                        np = new PlayerInfo(i, j, res);
                    }
                    else {
                        np = new PlayerInfo(i,j,Double.MAX_VALUE);
                    }
                }
                sortScorers[num] = np;
                num++;
            }
        }

        final int TYPE = type;
        Arrays.sort(sortScorers, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo n1, PlayerInfo n2) {
                if (TYPE != 4) {
                    return n1.stat > n2.stat ? -1 : n1.stat == n2.stat ? 0 : 1;
                }
                else {
                    return n1.stat > n2.stat ? 1 : n1.stat == n2.stat ? 0 : -1;
                }
            }
        });
        for (int i = 0; i < 30; i++) {
            int tn = sortScorers[i].teamNumber;
            int pn = sortScorers[i].playerNumber;
            int res = (int) sortScorers[i].stat;
        }

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        for (int i = 0; i < 30; i++) {
            int tn = sortScorers[i].teamNumber;
            int pn = sortScorers[i].playerNumber;
            int res;
            double res2;
            if (type == 2 || type == 3) {
                res = (int) sortScorers[i].stat;
                popupMenu.getMenu().add(Menu.NONE,i,Menu.NONE, playerNames[champNumber][tn][pn] + "(" + res + ")");
            }
            else {
                res2 = Math.round(sortScorers[i].stat * 100.0) / 100.0;
                popupMenu.getMenu().add(Menu.NONE,i,Menu.NONE, playerNames[champNumber][tn][pn] + "(" + res2 + ")");
            }

        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int num = item.getItemId();
                int teamNumber = sortScorers[num].teamNumber;
                int playerNumber = sortScorers[num].playerNumber;
                String playerName = playerNames[champNumber][teamNumber][playerNumber];
                Intent intent = new Intent(ChampStatsActivity.this, PlayerStatsActivity.class);
                intent.putExtra("teamName", teamNames[champNumber][teamNumber]);
                intent.putExtra("playerName", playerName);
                intent.putExtra("champNumber", champNumber);
                for (int j = 0; j < 5; j++) {
                    intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][playerNumber][j]);
                }

                startActivity(intent);
                return true;
            }
        });
        popupMenu.show();
    }
}

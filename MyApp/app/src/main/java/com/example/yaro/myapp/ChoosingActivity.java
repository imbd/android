package com.example.yaro.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class ChoosingActivity extends Parent {
    String champ;
    String champRef;
    int champNumber;
    boolean wasDone;
    boolean afterHelp;



    @Override
    public void onBackPressed() {
        ChoosingActivity.super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        champ = getIntent().getExtras().getString("champname");
        champRef = getIntent().getExtras().getString("champref");
        champNumber = getIntent().getExtras().getInt("champnumber");
        super.onCreate(savedInstanceState);

        Thread t = new  Thread(new Runnable() {
            @Override
            public void run() {

                setTitle(champ);

                setContentView(R.layout.activity_choosing);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                switch (champ) {
                    case "Англия":
                        imageView.setImageResource(R.drawable.apl);
                        break;
                    case "Испания":
                        imageView.setImageResource(R.drawable.laliga2);
                        break;
                    case "Германия":
                        imageView.setImageResource(R.drawable.bundesliga);
                        break;
                    case "Россия":
                        imageView.setImageResource(R.drawable.rfpl2);
                        break;
                    case "Франция":
                        imageView.setImageResource(R.drawable.francel1);
                        break;
                    case "Италия":
                        imageView.setImageResource(R.drawable.logoseriea);
                        break;
                    case "Португалия":
                        imageView.setImageResource(R.drawable.portugal);
                        break;
                    case "Голландия":
                        imageView.setImageResource(R.drawable.eredivisie);
                        break;
                    case "Украина":
                        imageView.setImageResource(R.drawable.ukraine);
                        break;
                    case "Турция":
                        imageView.setImageResource(R.drawable.superliga);
                        break;
                    default:
                        break;
                }
            }
        });
        try {
            t.start();
            t.join();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void help() {

        makeRefs(champNumber, champ, champRef, "loadteams", -1);
        makeRefs(champNumber, champ, champRef, "table", -1);
        if (wasInterrupted[champNumber]) {
            noInternet = true;
            return;
        }
        for (int i = 0; i < numberOfTeams[champNumber]; i++) {
            makeRefs(champNumber, teamNames[champNumber][i].replaceAll(" ", ""), teamRefs[champNumber][i], "teamstats", i);
            if (wasInterrupted[champNumber]) {
                return;
            }
            if (progress.isShowing()) {
                progress.setProgress((100 * (i + 1)) / numberOfTeams[champNumber]);
            }
        }

    }

    public void showTeamsPopupMenu(final View v) {

        Button button = (Button) findViewById(v.getId());
        if (!wasShowed) {
            internetCheck();
        }
        boolean wasShowedNow = false;
        if (!wasShowed && noInternet) {
            showAlert();
            wasShowed = true;
        } else
        if (wasInterrupted[champNumber] && !(button.getText().equals("Статистика чемпионата") && menu[champNumber])) {
            wasShowedNow = true;
            showAlert2();
        }

        progress = new ProgressDialog(v.getContext());
        progress.setCancelable(false);
        progress.setMessage("Downloading data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(0);
        progress.setMax(100);
        if (numberOfTeams[champNumber] == 0) {
            progress.show();
        }
        wasDone = true;
        afterHelp = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (numberOfTeams[champNumber] == 0) {
                    oldData = false;
                    help();
                    if (!wasInterrupted[champNumber]) {
                        progress.setProgress(100);
                        wasDone = true;
                        afterHelp = true;
                    }
                    else {
                        progress.setProgress(0);
                        wasDone = false;
                        afterHelp = false;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    menu[champNumber] = true;
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }

                }
            }
        }).start();
        if (button.getText().equals("Статистика чемпионата") && menu[champNumber]) {
            Intent intent = new Intent(ChoosingActivity.this, ChampStatsActivity.class);
            intent.putExtra("champNumber", champNumber);
            intent.putExtra("champName", champ);
            startActivity(intent);
            return;

        } else
        if ((wasInterrupted[champNumber] || oldData) && !wasShowedNow) {
            showAlert2();
        }

        if (numberOfTeams[champNumber] > 0) {

            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.inflate(R.menu.popupmenu);
            for (int i = 0; i < numberOfTeams[champNumber]; i++) {
                popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, teamNames[champNumber][i]);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int num = item.getItemId();
                    Intent intent = new Intent(ChoosingActivity.this, TeamActivity.class);
                    intent.putExtra("teamRef", teamRefs[champNumber][num]);
                    intent.putExtra("teamName", teamNames[champNumber][num]);
                    intent.putExtra("teamNumber", num);
                    intent.putExtra("champnumber", champNumber);
                    startActivity(intent);
                    return true;
                }
            });


            popupMenu.show();
        }

    }



}

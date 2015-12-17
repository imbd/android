package com.example.yaro.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Parent
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("FootballStats");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.textView2);
        String text = "В этом приложении вы сможете найти:\n-информацию о лучших чемпионатах Старого Света\n";
        text += "-самую свежую статистику команд АПЛ, РФПЛ и других лиг\n";
        text += "-данные о выступлениях игроков";
        tv.setText(text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        internetCheck();
        if (noInternet) {
            showAlert();
            wasShowed = true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, ChoosingActivity.class);
        intent.putExtra("champname", item.getTitle());
        String champRef = "";
        int champNumber = 0;
        switch (id) {
            case R.id.england:
                champRef = "http://www.championat.com/football/_england/1323/";
                break;
            case R.id.spain:
                champNumber = 1;
                champRef = "http://www.championat.com/football/_spain/1331/";
                break;
            case R.id.germany:
                champNumber = 2;
                champRef = "http://www.championat.com/football/_germany/1327/";
                break;
            case R.id.france:
                champNumber = 3;
                champRef = "http://www.championat.com/football/_france/1333/";
                break;
            case R.id.italy:
                champNumber = 4;
                champRef = "http://www.championat.com/football/_italy/1329/";
                break;
            case R.id.russia:
                champNumber = 5;
                champRef = "http://www.championat.com/football/_russiapl/1299/";
                break;
            case R.id.portugal:
                champNumber = 6;
                champRef = "http://www.championat.com/football/_other/1378/";
                break;
            case R.id.holland:
                champNumber = 7;
                champRef = "http://www.championat.com/football/_other/1388/";
                break;

            case R.id.ukraine:
                champNumber = 8;
                champRef = "http://www.championat.com/football/_ukraine/1354/";
                break;

            case R.id.turkey:
                champNumber = 9;
                champRef = "http://www.championat.com/football/_other/1402/";
                break;

            default:
                break;
        }
        intent.putExtra("champref", champRef);
        intent.putExtra("champnumber", champNumber);
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}

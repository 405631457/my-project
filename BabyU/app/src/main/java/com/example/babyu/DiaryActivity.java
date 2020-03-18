package com.example.babyu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyu.Diaper.DiaperInsert;
import com.example.babyu.Diaper.DiaperPage;
import com.example.babyu.Diary.DiaryInsert;
import com.example.babyu.Diary.DiaryPage;
import com.example.babyu.Feed.FeedInsert;
import com.example.babyu.Feed.FeedPage;
import com.example.babyu.Growth.GrowthInsert;
import com.example.babyu.Growth.GrowthPage;
import com.example.babyu.Login.LoginActivity;
import com.example.babyu.Login.ProfileData;
import com.example.babyu.Sleep.SleepPage;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    CardView diaryCard,feedCard,growthCard,diaperCard;

    private RecyclerView mRecyclerView;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton feedFloat, growthFloat, diaperFloat,diaryFloat;
    TextView textView_signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        diaryCard=findViewById(R.id.diary_card);
        feedCard=findViewById(R.id.feed_card);
        growthCard=findViewById(R.id.growth_card);
        diaperCard=findViewById(R.id.diaper_card);

        diaryCard.setOnClickListener(this);
        feedCard.setOnClickListener(this);
        growthCard.setOnClickListener(this);
        diaperCard.setOnClickListener(this);

        floatingActionMenu=(FloatingActionMenu)findViewById(R.id.floatingActionMenu);
        feedFloat=(FloatingActionButton)findViewById(R.id.f1);
        growthFloat=(FloatingActionButton)findViewById(R.id.f3);
        diaperFloat=(FloatingActionButton)findViewById(R.id.f4);
        diaryFloat=(FloatingActionButton)findViewById(R.id.f5);

        feedFloat.setOnClickListener(this);
        diaperFloat.setOnClickListener(this);
        growthFloat.setOnClickListener(this);
        diaryFloat.setOnClickListener(this);

        mRecyclerView=findViewById(R.id.profile_Recycler);
        new FirebaseDatabaseHelper().readProfile(new FirebaseDatabaseHelper.DataStatus_profile() {
            @Override
            public void DataIsLoaded(List<ProfileData> Profile, List<String> keys) {
                new Recycler_config().setConfig_profile(mRecyclerView, DiaryActivity.this,Profile,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.feed_card: i=new Intent(this, FeedPage.class); startActivity(i); break;
            case R.id.growth_card: i=new Intent(this, GrowthPage.class); startActivity(i); break;
            case R.id.diaper_card: i=new Intent(this, DiaperPage.class); startActivity(i); break;
            case R.id.diary_card: i=new Intent(this, DiaryPage.class); startActivity(i);break;
            case R.id.f1: i=new Intent(this, FeedInsert.class); startActivity(i); break;
            case R.id.f3: i=new Intent(this, GrowthInsert.class); startActivity(i); break;
            case R.id.f4:i=new Intent(this, DiaperInsert.class); startActivity(i); break;
            case R.id.f5:i=new Intent(this, DiaryInsert.class); startActivity(i); break;
            default:break;
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent h =new Intent(DiaryActivity.this, MainActivity1.class);
                startActivity(h);
                break;
            case R.id.nav_calender:
                Intent c =new Intent(DiaryActivity.this,calender1.class);
                startActivity(c);
                break;
            case R.id.nav_diary:
                Intent d =new Intent(DiaryActivity.this,DiaryActivity.class);
                startActivity(d);
                break;
            case R.id.nav_visibility:
                Intent v =new Intent(DiaryActivity.this,Video.class);
                startActivity(v);
                break;
            case R.id.nav_LogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

package com.example.babyu.Diaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import com.example.babyu.DiaryActivity;
import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.R;
import com.example.babyu.Recycler_config;

import java.util.List;

public class DiaperPage extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView=findViewById(R.id.diaper_Recycler);
        new FirebaseDatabaseHelper().readDiaper(new FirebaseDatabaseHelper.DataStatus_diaper() {
            @Override
            public void DataIsLoaded(List<DiaperData> Diaper, List<String> keys) {
                new Recycler_config().setConfig_1(mRecyclerView, DiaperPage.this,Diaper,keys);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent=new Intent(this, DiaryActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.babyu.Growth;

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

public class GrowthPage extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView=findViewById(R.id.growth_Recycler);
        new FirebaseDatabaseHelper().readGrowth(new FirebaseDatabaseHelper.DataStatus_growth() {
            @Override
            public void DataIsLoaded(List<GrowthData> Growth, List<String> keys) {
                new Recycler_config().setConfig_2(mRecyclerView, GrowthPage.this,Growth,keys);
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

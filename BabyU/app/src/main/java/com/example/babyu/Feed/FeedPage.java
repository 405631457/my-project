package com.example.babyu.Feed;

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

public class FeedPage extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView=findViewById(R.id.feed_Recycler);
        new FirebaseDatabaseHelper().readFeed(new FirebaseDatabaseHelper.DataStatus_feed() {
            @Override
            public void DataIsLoaded(List<FeedData> Feed, List<String> keys) {
                new Recycler_config().setConfig(mRecyclerView, FeedPage.this,Feed,keys);
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

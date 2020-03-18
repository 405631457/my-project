package com.example.babyu.Growth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.R;

import java.util.List;

public class GrowthPage_detail extends AppCompatActivity {

    private TextClock textClock;
    private EditText editText_cm;
    private EditText editText_kg;
    private EditText editText_hcm;
    private Button button_delete;

    private String key;
    private String date;
    private String height;
    private String weight;
    private String head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_page_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        key=getIntent().getStringExtra("key");
        date=getIntent().getStringExtra("date");
        height=getIntent().getStringExtra("height");
        weight=getIntent().getStringExtra("weight");
        head=getIntent().getStringExtra("head");

        textClock=findViewById(R.id.textClock);
        editText_cm=findViewById(R.id.editText_cm);
        editText_cm.setText(height);
        editText_kg=findViewById(R.id.editText_kg);
        editText_kg.setText(weight);
        editText_hcm=findViewById(R.id.editText_hcm);
        editText_hcm.setText(head);
        button_delete=findViewById(R.id.button_delete);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteGrowthData(key, new FirebaseDatabaseHelper.DataStatus_growth() {
                    @Override
                    public void DataIsLoaded(List<GrowthData> Growth, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        finish(); return;
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_revise,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.revise:
                GrowthData Growth=new GrowthData();
                Growth.setHeight(editText_cm.getText().toString());
                Growth.setDate(textClock.getText().toString());
                Growth.setWeight(editText_kg.getText().toString());
                Growth.setHead(editText_hcm.getText().toString());
                new FirebaseDatabaseHelper().updateGrowthData(key, Growth, new FirebaseDatabaseHelper.DataStatus_growth() {
                    @Override
                    public void DataIsLoaded(List<GrowthData> Growth, List<String> keys) {

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
                Intent i=new Intent(this, GrowthPage.class); startActivity(i);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

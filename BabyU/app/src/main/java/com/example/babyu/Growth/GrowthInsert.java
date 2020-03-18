package com.example.babyu.Growth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.R;

import java.util.List;

public class GrowthInsert extends AppCompatActivity {

    private TextClock textClock;
    private EditText editText_cm;
    private EditText editText_kg;
    private EditText editText_hcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_insert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textClock=findViewById(R.id.textClock);
        editText_cm=findViewById(R.id.editText_cm);
        editText_kg=findViewById(R.id.editText_kg);
        editText_hcm=findViewById(R.id.editText_hcm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_check,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.check:
                GrowthData Growth=new GrowthData();
                Growth.setDate(textClock.getText().toString());
                Growth.setHeight(editText_cm.getText().toString());
                Growth.setWeight(editText_kg.getText().toString());
                Growth.setHead(editText_hcm.getText().toString());
                new FirebaseDatabaseHelper().addGrowthData(Growth, new FirebaseDatabaseHelper.DataStatus_growth() {
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

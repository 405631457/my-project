package com.example.babyu.Feed;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.R;

import java.util.Calendar;
import java.util.List;

public class FeedInsert extends AppCompatActivity {

    private TextClock textClock;
    private ImageView buttonTime;
    private TextView textTime;
    private ImageView buttonDate;
    private TextView textDate;
    private DatePickerDialog datePickerDialog;
    private EditText editText_drink;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_insert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textTime = (TextView) findViewById(R.id.textTime);
        buttonTime = (ImageView) findViewById(R.id.buttonTime);
        textDate = (TextView) findViewById(R.id.textDate);
        buttonDate = (ImageView) findViewById(R.id.buttonDate);
        editText_drink = (EditText) findViewById(R.id.editText_drink);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day0fMonth = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(FeedInsert.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textDate.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                },year,month,day0fMonth);
                datePickerDialog.show();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                new TimePickerDialog(FeedInsert.this,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_check, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check:
                FeedData Feed = new FeedData();
                Feed.setDate(textDate.getText().toString());
                Feed.setTime(textTime.getText().toString());
                Feed.setMl(editText_drink.getText().toString());
                new FirebaseDatabaseHelper().addFeedData(Feed, new FirebaseDatabaseHelper.DataStatus_feed() {
                    @Override
                    public void DataIsLoaded(List<FeedData> Feed, List<String> keys) {

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
                Intent i = new Intent(this, FeedPage.class);
                startActivity(i);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

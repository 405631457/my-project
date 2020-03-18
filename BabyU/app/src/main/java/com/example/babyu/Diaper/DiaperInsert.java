package com.example.babyu.Diaper;

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
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.R;
import java.util.Calendar;
import java.util.List;

public class DiaperInsert extends AppCompatActivity {

    private TextView textTime;
    private ImageView buttonTime;
    private ImageView buttonDate;
    private TextView textDate;
    private DatePickerDialog datePickerDialog;
    private TextClock textClock;
    private Spinner spinner;
    private EditText editText_remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_insert);

        textClock = findViewById(R.id.textClock);
        textTime=findViewById(R.id.textTime);
        buttonTime = findViewById(R.id.buttonTime);
        spinner = findViewById(R.id.Spinner);
        textDate = (TextView) findViewById(R.id.textDate);
        buttonDate = (ImageView) findViewById(R.id.buttonDate);
        editText_remarks=findViewById(R.id.editText_remarks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day0fMonth = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(DiaperInsert.this, new DatePickerDialog.OnDateSetListener() {
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


                new TimePickerDialog(DiaperInsert.this, new TimePickerDialog.OnTimeSetListener() {
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
                DiaperData Diaper = new DiaperData();
                Diaper.setTime(textTime.getText().toString());
                Diaper.setDate(textDate.getText().toString());
                Diaper.setStatus(spinner.getSelectedItem().toString());
                Diaper.setRemarks(editText_remarks.getText().toString());
                new FirebaseDatabaseHelper().addDiaperData(Diaper, new FirebaseDatabaseHelper.DataStatus_diaper() {
                    @Override
                    public void DataIsLoaded(List<DiaperData> Diaper, List<String> keys) {

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
                Intent i = new Intent(this, DiaperPage.class);
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

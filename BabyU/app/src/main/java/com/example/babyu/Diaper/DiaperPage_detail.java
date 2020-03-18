package com.example.babyu.Diaper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class DiaperPage_detail extends AppCompatActivity {

    private TextView textTime;
    private ImageView buttonDate;
    private TextClock textClock;
    private Spinner spinner;
    private EditText editText_remarks;
    private TextView textDate;
    private Button button_delete;
    private DatePickerDialog datePickerDialog;

    private String key;
    private String status;
    private String remarks;
    private String time;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_page_detail);

        key=getIntent().getStringExtra("key");
        time=getIntent().getStringExtra("time");
        status=getIntent().getStringExtra("status");
        date=getIntent().getStringExtra("date");
        remarks=getIntent().getStringExtra("remarks");

        textClock=findViewById(R.id.textClock);
        textTime=findViewById(R.id.textTime);
        textTime.setText(time);
        textDate = (TextView) findViewById(R.id.textDate);
        textDate.setText(date);
        buttonDate= findViewById(R.id.buttonDate);
        spinner=findViewById(R.id.Spinner);
        spinner.setSelection(getIndex_SpinnerItem(spinner,status));
        editText_remarks=findViewById(R.id.editText_remarks);
        editText_remarks.setText(remarks);
        button_delete=findViewById(R.id.button_delete);

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

                datePickerDialog=new DatePickerDialog(DiaperPage_detail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textDate.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                },year,month,day0fMonth);
                datePickerDialog.show();
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);


                new TimePickerDialog(DiaperPage_detail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textTime.setText(hourOfDay+":"+minute);
                    }
                },hour ,minute ,true).show();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteDiaperData(key, new FirebaseDatabaseHelper.DataStatus_diaper() {
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
                        finish(); return;
                    }
                });
            }
        });
    }

    private int getIndex_SpinnerItem(Spinner spinner,String item){
        int index = 0;
        for(int i =0;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index=i;
                break;
            }

        }
        return index;
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
                DiaperData Diaper=new DiaperData();
                Diaper.setDate(textDate.getText().toString());
                Diaper.setTime(textTime.getText().toString());
                Diaper.setStatus(spinner.getSelectedItem().toString());
                Diaper.setRemarks(editText_remarks.getText().toString());
                new FirebaseDatabaseHelper().updateDiaperData(key, Diaper, new FirebaseDatabaseHelper.DataStatus_diaper() {
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
                Intent i=new Intent(this, DiaperPage.class); startActivity(i);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

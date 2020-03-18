package com.example.babyu;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.babyu.Login.LoginActivity;
import com.example.babyu.Login.SignUpActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.content.Context;

import java.util.Date;
import java.util.Map;


public class calender1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG="calender1";
    private ArrayList<String> mNote = new ArrayList<>();
    static CompactCalendarView compactCalendar;
    private Button backbtn;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("<    yyyy年MM月    >", Locale.getDefault());
    TextView title;
    TextView myDate;
    String JSON_STRING;
    static List<Event> events;
    int color = Color.argb(255, 160,223,228);

    private FloatingActionButton fab_add;
    private EditText edit_topic,edit_remarks;
    private TextView textdate,text_close;
    private ImageView buttonDate;
    private Button btn_submit,btn;
    Dialog myDialog;
    private DatePickerDialog datePickerDialog;
    int m ;
    int d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        myDialog = new Dialog(this);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        title=(TextView)findViewById(R.id.title);
        myDate=(TextView)findViewById(R.id.myDate);
        backbtn= (Button) findViewById(R.id.back);

        Date date = new Date();
        title.setText(dateFormatMonth.format(date));

        fab_add=findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopPopup();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender1.this.finish();
            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                String day=String.format("%tF", dateClicked);
                myDate.setText(day);
                myDate.setTextColor(Color.BLACK);
                mNote.clear();
                if (compactCalendar.getEvents(dateClicked.getTime()).size() != 0) {
                    for (int i = 0; i < compactCalendar.getEvents(dateClicked.getTime()).size(); i++) {
                        String a = (compactCalendar.getEvents(dateClicked.getTime()).get(i) + "").replaceFirst("Event.*data", "")
                                .replaceAll("=","").replaceAll("\\}","");
                        mNote.add(a);
                    }
                }
                initRecyclerView();

            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
               title.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }

        });
        new BackgroundTask().execute();
        //
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startAlarm(c);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }
public void getJSON(){
 new BackgroundTask().execute();
}
     class BackgroundTask extends AsyncTask<Void,Void,String>{
         String json_url;
         protected void onPreExecute(){
             json_url ="http://192.168.43.25/Android/v2/json_get_php.php";
         }
         @Override
         protected String doInBackground(Void... voids) {
             try {
                 URL url = new URL(json_url);
                 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                 InputStream inputStream =httpURLConnection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                 StringBuilder stringBuilder =new StringBuilder();
                 while ((JSON_STRING = bufferedReader.readLine())!=null){
                     stringBuilder.append(JSON_STRING+'\n');
                 }
                 bufferedReader.close();
                 inputStream.close();
                 httpURLConnection.disconnect();
                 return stringBuilder.toString().trim();
             }catch (MalformedURLException e){
                 e.printStackTrace();
             }catch (IOException e){
                 e.printStackTrace();
             }
             return null;
         }
         protected void onProgressUpdate(Void... values){
             super.onProgressUpdate(values);
         }
         protected void onPostExecute(String result){
             JSONArray jsonArray = null;
             try {
                 jsonArray = new JSONArray(result);
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             try {
                 events = new ArrayList<>(jsonArray.length());

                 for (int i = 0; i < jsonArray.length(); i++) {
                     long epoctTime = Long.parseLong((String) jsonArray.getJSONObject(i).get("dates"));
                     String description = (String) jsonArray.getJSONObject(i).get("note");

                     Event ev1 = new Event(color,epoctTime,description);
                     events.add(ev1);

                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             compactCalendar.addEvents(events);
         }
}
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent h = new Intent(calender1.this, MainActivity1.class);
                startActivity(h);
                break;
            case R.id.nav_calender:
                Intent c = new Intent(calender1.this, calender1.class);
                startActivity(c);
                break;
            case R.id.nav_diary:
                Intent d =new Intent(calender1.this,DiaryActivity.class);
                startActivity(d);
                break;
            case R.id.nav_visibility:
                Intent v =new Intent(calender1.this,Video.class);
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
    private void initRecyclerView(){
        RecyclerView recyclerView =findViewById(R.id.calender_recycler_view);
        CalenderAdpter adpter= new CalenderAdpter(this,mNote);
        recyclerView.setAdapter(adpter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public static class NotificationHelper extends ContextWrapper {
        public static final String channelID = "channelID";
        public static final String channelName = "Channel Name";

        private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        private NotificationManager mManager;
        String title;

        public NotificationHelper(Context base) {
            super(base);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel();
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private void createChannel() {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

            getManager().createNotificationChannel(channel);
        }

        public NotificationManager getManager() {
            if (mManager == null) {
                mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }

            return mManager;
        }

        public NotificationCompat.Builder getChannelNotification() {
            Date date=new Date();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            if (compactCalendar.getEvents(date.getTime()).size() != 0) {
                for (int i = 0; i < compactCalendar.getEvents(date.getTime()).size(); i++) {
                    String a = (compactCalendar.getEvents(date.getTime()).get(i) + "").replaceFirst("Event.*data", "")
                            .replaceAll("=","").replaceAll("\\}","");
                    if(i==0)title =a;
                    else title=title+"跟"+a;
                }
            }
            if(title==null){
                return new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle("祝您有個美好的一天")
                        .setContentText(dateFormatMonth.format(date))
                        .setSmallIcon(R.drawable.icon)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setCategory(NotificationCompat.CATEGORY_EVENT);
            }else{
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("您有新行程 : "+title)
                    .setContentText(dateFormatMonth.format(date))
                    .setSmallIcon(R.drawable.icon)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setCategory(NotificationCompat.CATEGORY_EVENT);
        }
        }
    }
    public void ShopPopup(){

        myDialog.setContentView(R.layout.addeventpopup);
        textdate=(TextView) myDialog.findViewById(R.id.textdate);
        btn_submit = (Button) myDialog.findViewById(R.id.btn_submit);
        edit_topic = (EditText)myDialog.findViewById(R.id.edit_text);
        buttonDate=(ImageView)myDialog.findViewById(R.id.buttonDate);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                final int day0fMonth = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(calender1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textdate.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                        m=month+1;
                        d=dayOfMonth;
                    }
                },year,month,day0fMonth);
                datePickerDialog.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day=m+"月"+d;
                putDay("null",day,edit_topic.getText().toString());

                Intent intent=new Intent(calender1.this, calender1.class);
                startActivity(intent);
                finish();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void putDay(final String receive1, final String receive2, final String note){
        RequestQueue requestQueue = Volley.newRequestQueue(this); StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("receive1",receive1);
                params.put("receive2",receive2);
                params.put("note",note);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
  }



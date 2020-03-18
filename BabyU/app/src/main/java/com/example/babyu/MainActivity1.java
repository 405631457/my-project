package com.example.babyu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.babyu.Diary.DiaryPage;
import com.example.babyu.Diary.Upload;
import com.example.babyu.Login.LoginActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH;
import static com.example.babyu.calender1.compactCalendar;
import static com.example.babyu.calender1.events;

public class MainActivity1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private ImageView send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private WordSeg tt;
    private String[] days={"一","二","三","四","五","六","日","天"};
    private android.app.ProgressDialog progressDialog;
    String text;
    String final1;
    String find;
    String findDate;
    String JSON_STRING;
    private TextToSpeech mTTS;
    Boolean WeekTrue;
    Boolean WeekTrue_w;
    String fireDay;
    String finallNote;
    long  timeMill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        progressDialog = new ProgressDialog(this);

        initMsgs();
        inputText = (EditText) findViewById(R.id.input_text);
        send = (ImageView) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        send.setEnabled(true);
                    }

                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        tt = new WordSeg();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                    WeekTrue=false;
                    WeekTrue_w=false;
                    try {
                        tt.txt=content.replaceAll("號","").replaceAll("要","")
                                .replaceAll("我","").replaceAll("幫","").replaceAll("看","")
                                .replaceAll("查","").replaceAll("想知道","").replaceAll("告訴我","");
                        String segment=tt.segWords(tt.txt, " ");
                        String[] word=segment.split(" "); //1 2 3 4 5....號用
                        //段字未改
                        if(content.matches("(.*)刪除(.*)")){
                            String[] words=segment.split(" ");
                            if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(content.matches("(.*)"+days[i]+"(.*)")){
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete(words[1],words[2],words[3]);
                                        speak();
                                        WeekTrue=true;
                                        break;
                                    }
                                }if(WeekTrue==false){
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)下個月(.*)")){
                                try {
                                    int i = Integer.parseInt(words[2]);
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete(words[1],words[2],words[3]);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(word[2].matches("月")){
                                try {
                                    int i = Integer.parseInt(word[3]);
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete("null",word[1]+word[2]+word[3],word[4]);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)明天(.*)")||content.matches("(.*)後天(.*)")){
                                final1="好的，已幫您刪除。";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                putDay_delete("null",word[1],word[2]);
                                speak();
                            }
                        }else if(content.matches("(.*)不用(.*)")){
                            String[] words=segment.split(" ");
                            if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(content.matches("(.*)"+days[i]+"(.*)")){
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete(words[0],words[1],words[3]);
                                        speak();
                                        WeekTrue=true;
                                        break;
                                    }
                                }if(WeekTrue==false){
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)下個月(.*)")){
                                try {
                                    int i = Integer.parseInt(words[1]);
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete(words[0],words[1],words[3]);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(words[1].matches("月")){
                                try {
                                    int i = Integer.parseInt(words[2]);
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete("null",words[0]+words[1]+words[2],words[4]);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)明天(.*)")||content.matches("(.*)後天(.*)")){
                                final1="好的，已幫您刪除。";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                putDay_delete("null",words[0],words[2]);
                                speak();
                            }
                        }else if(content.matches("(.*)幹嘛")||content.matches("(.*)有空(.*)")||content.matches("(.*)什麼安排(.*)")||content.matches("(.*)做什麼(.*)")
                                ||content.matches("(.*)什麼事(.*)")){
                            String[] words=segment.split(" ");
                            find=null;
                            long time = 0;
                            Calendar date =Calendar.getInstance();
                            if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(content.matches("(.*)"+days[i]+"(.*)")){
                                        if(words[0].matches("星期")||words[0].matches("禮拜")){
                                        long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                        if(words[1].matches("一")){
                                            time=(firstday+86400000);
                                        }else if(words[1].matches("二")){
                                            time=(firstday+86400*2000);
                                        }else if(words[1].matches("三")){
                                            time=(firstday+86400*3000);
                                        }else if(words[1].matches("四")){
                                            time=(firstday+86400*4000);
                                        }else if(words[1].matches("五")){
                                            time=(firstday+86400*5000);
                                        }else if(words[1].matches("六")){
                                            time=(firstday+86400*6000);
                                        }else if(words[1].matches("日")||words[1].matches("天")){
                                            time=firstday;
                                        }}else if(words[0].matches("下星期")||words[0].matches("下禮拜")){
                                            long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                            long weekseconds=86400000*7;
                                            if(words[1].matches("一")){
                                                time=(firstday+86400000)+weekseconds;
                                            }else if(words[1].matches("二")){
                                                time=(firstday+86400*2000)+weekseconds;
                                            }else if(words[1].matches("三")){
                                                time=(firstday+86400*3000)+weekseconds;
                                            }else if(words[1].matches("四")){
                                                time=(firstday+86400*4000)+weekseconds;
                                            }else if(words[1].matches("五")){
                                                time=(firstday+86400*5000)+weekseconds;
                                            }else if(words[1].matches("六")){
                                                time=(firstday+86400*6000)+weekseconds;
                                            }else if(words[1].matches("日")||words[1].matches("天")){
                                                time=firstday+weekseconds;
                                            }}
                                        timeMill=time;
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        final1="您"+words[0]+words[1]+"要"+find;
                                        WeekTrue=true;
                                        break;
                                    }
                                }if(WeekTrue==false){
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)下個月(.*)")){
                                try {
                                    int i = Integer.parseInt(words[1]);
                                    date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)+1,i);
                                    timeMill=date.getTimeInMillis();
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    final1="您"+words[0]+words[1]+"號要"+find;
                                    WeekTrue=true;
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(words[1].matches("月")){
                                try {
                                    int m=Integer.parseInt(words[0]);
                                    int i = Integer.parseInt(words[2]);
                                    date.set(date.get(Calendar.YEAR),m-1,i);
                                    timeMill=date.getTimeInMillis();
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    final1="您"+words[0]+words[1]+word[2]+"號要"+find;
                                    WeekTrue=true;
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)明天(.*)")||content.matches("(.*)後天(.*)")){
                                if(content.matches("(.*)明天(.*)")){
                                   timeMill=date.getTimeInMillis()+86400000;
                                }else if(content.matches("(.*)後天(.*)")){
                                    timeMill=date.getTimeInMillis()+86400000*2;
                                }
                                Date d_t=new Date(timeMill);
                                if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                    for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                        String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                .replaceAll("=","").replaceAll("\\}","");
                                        if(k==0)find =a;
                                        else find=find+"跟"+a;
                                    }
                                }
                                final1="您"+words[0]+"要"+find;
                                WeekTrue=true;
                            }
                            if(WeekTrue){
                            if(final1.matches("(.*)null(.*)")){
                                Msg msg1 = new Msg("您當天沒有行程喔",Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text="您當天沒有行程喔";
                            }else {
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                            }
                            speak();
                            }
                        }else if(content.matches("(.*)什麼時候(.*)")){
                            String[] words=segment.split(" ");
                            int e=0;
                            for (int k=0; k<events.size();k++){
                                String A= (String) events.get(k).getData();
                                if(words[1].matches("(.*)"+A+"(.*)")){
                                    e++;
                                    Date d_c= new Date(events.get(k).getTimeInMillis());
                                    int mon =  d_c.getMonth()+1;
                                    int d = d_c.getDate();
                                    if(e==1)findDate=mon+"月"+d+"號";
                                    else findDate=findDate+"跟"+mon+"月"+d+"號";
                                }
                            }
                            final1="您"+findDate+"要"+words[1];
                            Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                            msgList.add(msg1);
                            adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                            msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                            text=final1;
                            speak();
                        }else if(content.matches("(.*)要(.*)嗎")){
                            String[] words=segment.split(" ");
                            find=null;
                            long time = 0;
                            Calendar date =Calendar.getInstance();
                            if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(content.matches("(.*)"+days[i]+"(.*)")){
                                        if(words[0].matches("星期")||words[0].matches("禮拜")){
                                            long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                            if(words[1].matches("一")){
                                                time=(firstday+86400000);
                                            }else if(words[1].matches("二")){
                                                time=(firstday+86400*2000);
                                            }else if(words[1].matches("三")){
                                                time=(firstday+86400*3000);
                                            }else if(words[1].matches("四")){
                                                time=(firstday+86400*4000);
                                            }else if(words[1].matches("五")){
                                                time=(firstday+86400*5000);
                                            }else if(words[1].matches("六")){
                                                time=(firstday+86400*6000);
                                            }else if(words[1].matches("日")||words[1].matches("天")){
                                                time=firstday;
                                            }}else if(words[0].matches("下星期")||words[0].matches("下禮拜")){
                                            long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                            long weekseconds=86400000*7;
                                            if(words[1].matches("一")){
                                                time=(firstday+86400000)+weekseconds;
                                            }else if(words[1].matches("二")){
                                                time=(firstday+86400*2000)+weekseconds;
                                            }else if(words[1].matches("三")){
                                                time=(firstday+86400*3000)+weekseconds;
                                            }else if(words[1].matches("四")){
                                                time=(firstday+86400*4000)+weekseconds;
                                            }else if(words[1].matches("五")){
                                                time=(firstday+86400*5000)+weekseconds;
                                            }else if(words[1].matches("六")){
                                                time=(firstday+86400*6000)+weekseconds;
                                            }else if(words[1].matches("日")||words[1].matches("天")){
                                                time=firstday+weekseconds;
                                            }}
                                        timeMill=time;
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        if(words[2].matches("(.*)"+find+"(.*)")){
                                            final1="是的，沒錯。您"+words[0]+words[1]+"要"+words[2];
                                            WeekTrue=true;
                                        }
                                        WeekTrue_w=true;
                                        break;
                                    }
                                }if(WeekTrue_w==false){
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)下個月(.*)")){
                                try {
                                    int i = Integer.parseInt(words[1]);
                                    date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)+1,i);
                                    timeMill=date.getTimeInMillis();
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    if(words[2].matches("(.*)"+find+"(.*)")) {
                                        final1="是的，沒錯。您"+words[0]+words[1]+"號要"+words[2];
                                        WeekTrue=true;
                                    }
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(words[1].matches("月")){
                                try {
                                    int m=Integer.parseInt(words[0]);
                                    int i = Integer.parseInt(words[2]);
                                    date.set(date.get(Calendar.YEAR),m-1,i);
                                    timeMill=date.getTimeInMillis();
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    if(words[3].matches("(.*)"+find+"(.*)")) {
                                        final1="是的，沒錯。您"+words[0]+words[1]+word[2]+"號要"+words[3];
                                        WeekTrue=true;
                                    }
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)明天(.*)")||content.matches("(.*)後天(.*)")){
                                if(content.matches("(.*)明天(.*)")){
                                    timeMill=date.getTimeInMillis()+86400000;
                                }else if(content.matches("(.*)後天(.*)")){
                                    timeMill=date.getTimeInMillis()+86400000*2;
                                }
                                Date d_t=new Date(timeMill);
                                if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                    for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                        String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                .replaceAll("=","").replaceAll("\\}","");
                                        if(k==0)find =a;
                                        else find=find+"跟"+a;
                                    }
                                }
                                if(words[1].matches("(.*)"+find+"(.*)")) {
                                    final1 = "是的，沒錯。您" + words[0] + "要" + words[1];
                                    WeekTrue = true;
                                }
                            }
                            if(WeekTrue){
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }else {
                                final1="不需要喔";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }
                        }else if(content.matches("(.*)日記(.*)")){
                            String[] words=segment.split(" ");
                            find=null;
                            long time = 0;
                            Calendar date =Calendar.getInstance();
                            if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(content.matches("(.*)"+days[i]+"(.*)")){
                                        if(words[0].matches("星期")||words[0].matches("禮拜")){
                                            long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                            if(words[1].matches("一")){
                                                time=(firstday+86400000);
                                            }else if(words[1].matches("二")){
                                                time=(firstday+86400*2000);
                                            }else if(words[1].matches("三")){
                                                time=(firstday+86400*3000);
                                            }else if(words[1].matches("四")){
                                                time=(firstday+86400*4000);
                                            }else if(words[1].matches("五")){
                                                time=(firstday+86400*5000);
                                            }else if(words[1].matches("六")){
                                                time=(firstday+86400*6000);
                                            }else if(words[1].matches("日")||words[1].matches("天")){
                                                time=firstday;
                                            }}else if(words[0].matches("上星期")||words[0].matches("上禮拜")){
                                            long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                            long weekseconds=86400000*7;
                                            if(words[1].matches("一")){
                                                time=(firstday+86400000)-weekseconds;
                                            }else if(words[1].matches("二")){
                                                time=(firstday+86400*2000)-weekseconds;
                                            }else if(words[1].matches("三")){
                                                time=(firstday+86400*3000)-weekseconds;
                                            }else if(words[1].matches("四")){
                                                time=(firstday+86400*4000)-weekseconds;
                                            }else if(words[1].matches("五")){
                                                time=(firstday+86400*5000)-weekseconds;
                                            }else if(words[1].matches("六")){
                                                time=(firstday+86400*6000)-weekseconds;
                                            }else if(words[1].matches("日")||words[1].matches("天")){
                                                time=firstday+weekseconds;
                                            }}
                                            timeMill=time;
                                            Date d_t=new Date(timeMill);
                                            int mon =d_t.getMonth()+1;
                                            int d =d_t.getDate();
                                            fireDay = "2019年" + mon+"月"+d + "日";//2019年9月3日
                                            WeekTrue=true;
                                            break;
                                    }
                                }if(WeekTrue==false){
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)上個月(.*)")){
                                try {
                                    int i = Integer.parseInt(words[1]);
                                    date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)-1,i);
                                    timeMill=date.getTimeInMillis();
                                    Date d_t=new Date(timeMill);
                                    int mon =d_t.getMonth()+1;
                                    int d =d_t.getDate();
                                    fireDay = "2019年" + mon+"月"+d + "日";
                                    WeekTrue=true;

                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(words[1].matches("月")){
                                try {
                                    int m=Integer.parseInt(words[0]);
                                    int i = Integer.parseInt(words[2]);
                                    fireDay = "2019年" + m+"月"+i + "日";
                                    WeekTrue=true;
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(content.matches("(.*)昨天(.*)")||content.matches("(.*)前天(.*)")){
                                if(content.matches("(.*)昨天(.*)")){
                                    timeMill=date.getTimeInMillis()-86400000;
                                }else if(content.matches("(.*)前天(.*)")){
                                    timeMill=date.getTimeInMillis()-86400000*2;
                                }
                                Date d_t=new Date(timeMill);
                                int mon =d_t.getMonth()+1;
                                int d =d_t.getDate();
                                fireDay = "2019年" + mon+"月"+d + "日";
                                WeekTrue=true;
                            }
                            if(WeekTrue){
                                firebase(fireDay);
                                text="為您呈現日記";
                                speak();
                            }
                        }
                        else if(content.matches("(.*)星期(.*)")||content.matches("(.*)禮拜(.*)")){
                            for(int i=0;i<days.length;i++){
                                if(content.matches("(.*)"+days[i]+"(.*)")){
                                    String[] words=segment.split(" ");
                                    final1="已添加至行事曆。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    finallNote=content.replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                    .replaceAll(words[0],"").replaceAll(words[1],"");
                                    putDay(words[0],words[1],finallNote);
                                    speak();
                                    WeekTrue=true;
                                    break;
                                }
                            }if(WeekTrue==false){
                                final1="不好意思,請再輸入一次準確的日期及事情~";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }
                        }else if(content.matches("(.*)下個月(.*)")){
                            String[] words=segment.split(" ");
                            try {
                                int i = Integer.parseInt(words[1]);
                                final1="已添加至行事曆。";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                finallNote=content.replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                        .replaceAll(words[0],"").replaceAll(words[1],"");
                                putDay(words[0],words[1],finallNote);
                                speak();
                            } catch(Exception e) {
                                final1="不好意思,請再輸入一次準確的日期及事情~";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }
                        }else if(word[1].matches("月")){
                            try {
                                int i = Integer.parseInt(word[2]);
                                final1="已添加至行事曆。";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                finallNote=content.replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                        .replaceAll(word[0],"").replaceAll(word[1],"").replaceAll(word[2],"");
                                putDay("null",word[0]+word[1]+word[2],finallNote);
                                speak();
                            } catch(Exception e) {
                                final1="不好意思,請再輸入一次準確的日期及事情~";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }
                        }else if(content.matches("(.*)明天(.*)")||content.matches("(.*)後天(.*)")){
                            String[] words=segment.split(" ");
                            final1="已添加至行事曆。";
                            Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                            msgList.add(msg1);
                            adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                            msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                            text=final1;
                            finallNote=content.replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                    .replaceAll(words[0],"");
                            putDay("null",words[0],finallNote);
                            speak();
                        }
                    }catch (Exception e){
                    }
                }
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public void putDay_delete(final String receive1, final String receive2, final String note){
        RequestQueue requestQueue = Volley.newRequestQueue(this); StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER2,
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
                Intent h =new Intent(MainActivity1.this, MainActivity1.class);
                startActivity(h);
                break;
            case R.id.nav_calender:
                Intent c =new Intent(MainActivity1.this,calender1.class);
                startActivity(c);
                break;
            case R.id.nav_diary:
                Intent d =new Intent(MainActivity1.this,DiaryActivity.class);
                startActivity(d);
                break;
            case R.id.nav_visibility:
                Intent v =new Intent(MainActivity1.this,Video.class);
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
    private void initMsgs() {
        Msg msg1 = new Msg("你好。",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg3 = new Msg("我是照護你寶寶的好幫手，請多多指教",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
    public void getSpeechInput(View view) {

        Intent intent = new Intent(ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //段字未改 無刪除查詢
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (!"".equals(result.get(0))) {
                        Msg msg = new Msg(result.get(0),Msg.TYPE_SENT);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        WeekTrue=false;
                        try {
                            tt.txt=result.get(0).replaceAll("號","").replaceAll("要","")
                                    .replaceAll("我","").replaceAll("幫","").replaceAll("看","").replaceAll("查","")
                                    .replaceAll("想知道","").replaceAll("告訴我","");
                            String segment=tt.segWords(tt.txt, " ");
                            String[] word=segment.split(" "); //1 2 3 4 5....號用
                            //段字未改
                            if(result.get(0).matches("(.*)刪除(.*)")){
                                String[] words=segment.split(" ");
                                if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                    for(int i=0;i<days.length;i++){
                                        if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                            final1="好的，已幫您刪除。";
                                            Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                            msgList.add(msg1);
                                            adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                            msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                            text=final1;
                                            putDay_delete(words[1],words[2],words[3]);
                                            speak();
                                            WeekTrue=true;
                                            break;
                                        }
                                    }if(WeekTrue==false){
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)下個月(.*)")){
                                    try {
                                        int i = Integer.parseInt(words[2]);
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete(words[1],words[2],words[3]);
                                        speak();
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(word[2].matches("月")){
                                    try {
                                        int i = Integer.parseInt(word[3]);
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete("null",word[1]+word[2]+word[3],word[4]);
                                        speak();
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)明天(.*)")||result.get(0).matches("(.*)後天(.*)")){
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete("null",word[1],word[2]);
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)不用(.*)")){
                                String[] words=segment.split(" ");
                                if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                    for(int i=0;i<days.length;i++){
                                        if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                            final1="好的，已幫您刪除。";
                                            Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                            msgList.add(msg1);
                                            adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                            msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                            text=final1;
                                            putDay_delete(words[0],words[1],words[3]);
                                            speak();
                                            WeekTrue=true;
                                            break;
                                        }
                                    }if(WeekTrue==false){
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)下個月(.*)")){
                                    try {
                                        int i = Integer.parseInt(words[1]);
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete(words[0],words[1],words[3]);
                                        speak();
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(words[1].matches("月")){
                                    try {
                                        int i = Integer.parseInt(words[2]);
                                        final1="好的，已幫您刪除。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        putDay_delete("null",words[0]+words[1]+words[2],words[4]);
                                        speak();
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期及事情~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)明天(.*)")||result.get(0).matches("(.*)後天(.*)")){
                                    final1="好的，已幫您刪除。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    putDay_delete("null",words[0],words[2]);
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)幹嘛")||result.get(0).matches("(.*)有空(.*)")||result.get(0).matches("(.*)什麼安排(.*)")||result.get(0).matches("(.*)做什麼(.*)")
                                    ||result.get(0).matches("(.*)什麼事(.*)")){
                                String[] words=segment.split(" ");
                                find=null;
                                long time = 0;
                                Calendar date =Calendar.getInstance();
                                if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                    for(int i=0;i<days.length;i++){
                                        if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                            if(words[0].matches("星期")||words[0].matches("禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000);
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000);
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000);
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000);
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000);
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000);
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday;
                                                }}else if(words[0].matches("下星期")||words[0].matches("下禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                long weekseconds=86400000*7;
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000)+weekseconds;
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000)+weekseconds;
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000)+weekseconds;
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000)+weekseconds;
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000)+weekseconds;
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000)+weekseconds;
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday+weekseconds;
                                                }}
                                            timeMill=time;
                                            Date d_t=new Date(timeMill);
                                            if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                                for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                    String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                            .replaceAll("=","").replaceAll("\\}","");
                                                    if(k==0)find =a;
                                                    else find=find+"跟"+a;
                                                }
                                            }
                                            final1="您"+words[0]+words[1]+"要"+find;
                                            WeekTrue=true;
                                            break;
                                        }
                                    }if(WeekTrue==false){
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)下個月(.*)")){
                                    try {
                                        int i = Integer.parseInt(words[1]);
                                        date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)+1,i);
                                        timeMill=date.getTimeInMillis();
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        final1="您"+words[0]+words[1]+"號要"+find;
                                        WeekTrue=true;
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(words[1].matches("月")){
                                    try {
                                        int m=Integer.parseInt(words[0]);
                                        int i = Integer.parseInt(words[2]);
                                        date.set(date.get(Calendar.YEAR),m-1,i);
                                        timeMill=date.getTimeInMillis();
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        final1="您"+words[0]+words[1]+word[2]+"號要"+find;
                                        WeekTrue=true;
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)明天(.*)")||result.get(0).matches("(.*)後天(.*)")){
                                    if(result.get(0).matches("(.*)明天(.*)")){
                                        timeMill=date.getTimeInMillis()+86400000;
                                    }else if(result.get(0).matches("(.*)後天(.*)")){
                                        timeMill=date.getTimeInMillis()+86400000*2;
                                    }
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    final1="您"+words[0]+"要"+find;
                                    WeekTrue=true;
                                }
                                if(WeekTrue){
                                    if(final1.matches("(.*)null(.*)")){
                                        Msg msg1 = new Msg("您當天沒有行程喔",Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text="您當天沒有行程喔";
                                    }else {
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                    }
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)什麼時候(.*)")){
                                String[] words=segment.split(" ");
                                int e=0;
                                for (int k=0; k<events.size();k++){
                                    String A= (String) events.get(k).getData();
                                    if(words[1].matches("(.*)"+A+"(.*)")){
                                        e++;
                                        Date d_c= new Date(events.get(k).getTimeInMillis());
                                        int mon =  d_c.getMonth()+1;
                                        int d = d_c.getDate();
                                        if(e==1)findDate=mon+"月"+d+"號";
                                        else findDate=findDate+"跟"+mon+"月"+d+"號";
                                    }
                                }
                                final1="您"+findDate+"要"+words[1];
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                speak();
                            }else if(result.get(0).matches("(.*)要(.*)嗎")){
                                String[] words=segment.split(" ");
                                find=null;
                                long time = 0;
                                Calendar date =Calendar.getInstance();
                                if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                    for(int i=0;i<days.length;i++){
                                        if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                            if(words[0].matches("星期")||words[0].matches("禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000);
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000);
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000);
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000);
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000);
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000);
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday;
                                                }}else if(words[0].matches("下星期")||words[0].matches("下禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                long weekseconds=86400000*7;
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000)+weekseconds;
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000)+weekseconds;
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000)+weekseconds;
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000)+weekseconds;
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000)+weekseconds;
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000)+weekseconds;
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday+weekseconds;
                                                }}
                                            timeMill=time;
                                            Date d_t=new Date(timeMill);
                                            if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                                for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                    String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                            .replaceAll("=","").replaceAll("\\}","");
                                                    if(k==0)find =a;
                                                    else find=find+"跟"+a;
                                                }
                                            }
                                            if(words[2].matches("(.*)"+find+"(.*)")){
                                                final1="是的，沒錯。您"+words[0]+words[1]+"要"+words[2];
                                                WeekTrue=true;
                                            }
                                            WeekTrue_w=true;
                                            break;
                                        }
                                    }if(WeekTrue_w==false){
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)下個月(.*)")){
                                    try {
                                        int i = Integer.parseInt(words[1]);
                                        date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)+1,i);
                                        timeMill=date.getTimeInMillis();
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        if(words[2].matches("(.*)"+find+"(.*)")) {
                                            final1="是的，沒錯。您"+words[0]+words[1]+"號要"+words[2];
                                            WeekTrue=true;
                                        }
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(words[1].matches("月")){
                                    try {
                                        int m=Integer.parseInt(words[0]);
                                        int i = Integer.parseInt(words[2]);
                                        date.set(date.get(Calendar.YEAR),m-1,i);
                                        timeMill=date.getTimeInMillis();
                                        Date d_t=new Date(timeMill);
                                        if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                            for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                                String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                        .replaceAll("=","").replaceAll("\\}","");
                                                if(k==0)find =a;
                                                else find=find+"跟"+a;
                                            }
                                        }
                                        if(words[3].matches("(.*)"+find+"(.*)")) {
                                            final1="是的，沒錯。您"+words[0]+words[1]+word[2]+"號要"+words[3];
                                            WeekTrue=true;
                                        }
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)明天(.*)")||result.get(0).matches("(.*)後天(.*)")){
                                    if(result.get(0).matches("(.*)明天(.*)")){
                                        timeMill=date.getTimeInMillis()+86400000;
                                    }else if(result.get(0).matches("(.*)後天(.*)")){
                                        timeMill=date.getTimeInMillis()+86400000*2;
                                    }
                                    Date d_t=new Date(timeMill);
                                    if (compactCalendar.getEvents(d_t.getTime()).size() != 0) {
                                        for (int k= 0; k < compactCalendar.getEvents(d_t.getTime()).size(); k++) {
                                            String a = (compactCalendar.getEvents(d_t.getTime()).get(k) + "").replaceFirst("Event.*data", "")
                                                    .replaceAll("=","").replaceAll("\\}","");
                                            if(k==0)find =a;
                                            else find=find+"跟"+a;
                                        }
                                    }
                                    if(words[1].matches("(.*)"+find+"(.*)")) {
                                        final1 = "是的，沒錯。您" + words[0] + "要" + words[1];
                                        WeekTrue = true;
                                    }
                                }
                                if(WeekTrue){
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }else {
                                    final1="不需要喔";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)的日記(.*)")){
                                String[] words=segment.split(" ");
                                find=null;
                                long time = 0;
                                Calendar date =Calendar.getInstance();
                                if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                    for(int i=0;i<days.length;i++){
                                        if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                            if(words[0].matches("星期")||words[0].matches("禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000);
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000);
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000);
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000);
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000);
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000);
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday;
                                                }}else if(words[0].matches("上星期")||words[0].matches("上禮拜")){
                                                long firstday=date.getTimeInMillis()-86400000*(date.get(Calendar.DAY_OF_WEEK)-1);
                                                long weekseconds=86400000*7;
                                                if(words[1].matches("一")){
                                                    time=(firstday+86400000)-weekseconds;
                                                }else if(words[1].matches("二")){
                                                    time=(firstday+86400*2000)-weekseconds;
                                                }else if(words[1].matches("三")){
                                                    time=(firstday+86400*3000)-weekseconds;
                                                }else if(words[1].matches("四")){
                                                    time=(firstday+86400*4000)-weekseconds;
                                                }else if(words[1].matches("五")){
                                                    time=(firstday+86400*5000)-weekseconds;
                                                }else if(words[1].matches("六")){
                                                    time=(firstday+86400*6000)-weekseconds;
                                                }else if(words[1].matches("日")||words[1].matches("天")){
                                                    time=firstday+weekseconds;
                                                }}
                                            timeMill=time;
                                            Date d_t=new Date(timeMill);
                                            int mon =d_t.getMonth()+1;
                                            int d =d_t.getDate();
                                            fireDay = "2019年" + mon+"月"+d + "日";//2019年9月3日
                                            WeekTrue=true;
                                            break;
                                        }
                                    }if(WeekTrue==false){
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)上個月(.*)")){
                                    try {
                                        int i = Integer.parseInt(words[1]);
                                        date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH)-1,i);
                                        timeMill=date.getTimeInMillis();
                                        Date d_t=new Date(timeMill);
                                        int mon =d_t.getMonth()+1;
                                        int d =d_t.getDate();
                                        fireDay = "2019年" + mon+"月"+d + "日";
                                        WeekTrue=true;

                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(words[1].matches("月")){
                                    try {
                                        int m=Integer.parseInt(words[0]);
                                        int i = Integer.parseInt(words[2]);
                                        fireDay = "2019年" + m+"月"+i + "日";
                                        WeekTrue=true;
                                    } catch(Exception e) {
                                        final1="不好意思,請再輸入一次準確的日期~";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新显示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        speak();
                                    }
                                }else if(result.get(0).matches("(.*)昨天(.*)")||result.get(0).matches("(.*)前天(.*)")){
                                    if(result.get(0).matches("(.*)昨天(.*)")){
                                        timeMill=date.getTimeInMillis()-86400000;
                                    }else if(result.get(0).matches("(.*)前天(.*)")){
                                        timeMill=date.getTimeInMillis()-86400000*2;
                                    }
                                    Date d_t=new Date(timeMill);
                                    int mon =d_t.getMonth()+1;
                                    int d =d_t.getDate();
                                    fireDay = "2019年" + mon+"月"+d + "日";
                                    WeekTrue=true;
                                }
                                if(WeekTrue){
                                    firebase(fireDay);
                                    text="為您呈現日記";
                                    speak();
                                }
                            }
                            else if(result.get(0).matches("(.*)星期(.*)")||result.get(0).matches("(.*)禮拜(.*)")){
                                for(int i=0;i<days.length;i++){
                                    if(result.get(0).matches("(.*)"+days[i]+"(.*)")){
                                        String[] words=segment.split(" ");
                                        final1="已添加至行事曆。";
                                        Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                        msgList.add(msg1);
                                        adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                        msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                        text=final1;
                                        finallNote=result.get(0).replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                                .replaceAll(words[0],"").replaceAll(words[1],"");
                                        putDay(words[0],words[1],finallNote);
                                        speak();
                                        WeekTrue=true;
                                        break;
                                    }
                                }if(WeekTrue==false){
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)下個月(.*)")){
                                String[] words=segment.split(" ");
                                try {
                                    int i = Integer.parseInt(words[1]);
                                    final1="已添加至行事曆。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    finallNote=result.get(0).replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                            .replaceAll(words[0],"").replaceAll(words[1],"");
                                    putDay(words[0],words[1],finallNote);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(word[1].matches("月")){
                                try {
                                    int i = Integer.parseInt(word[2]);
                                    final1="已添加至行事曆。";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    finallNote=result.get(0).replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                            .replaceAll(word[0],"").replaceAll(word[1],"").replaceAll(word[2],"");
                                    putDay("null",word[0]+word[1]+word[2],finallNote);
                                    speak();
                                } catch(Exception e) {
                                    final1="不好意思,請再輸入一次準確的日期及事情~";
                                    Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                    msgList.add(msg1);
                                    adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                    text=final1;
                                    speak();
                                }
                            }else if(result.get(0).matches("(.*)明天(.*)")||result.get(0).matches("(.*)後天(.*)")){
                                String[] words=segment.split(" ");
                                final1="已添加至行事曆。";
                                Msg msg1 = new Msg(final1,Msg.TYPE_RECEIVED);
                                msgList.add(msg1);
                                adapter.notifyItemInserted(msgList.size()-1);  //刷新?示
                                msgRecyclerView.scrollToPosition(msgList.size()-1);  //定位到最后一行
                                text=final1;
                                finallNote=result.get(0).replaceAll("號","").replaceAll("要","").replaceAll("我","")
                                        .replaceAll(word[0],"");
                                putDay("null",words[0],finallNote);
                                speak();
                            }
                        }catch (Exception e){
                        }
                    }
                }
                break;
        }
    }
    protected void firebase(String day){
        Intent intent = new Intent();
        intent.setClass(MainActivity1.this,DiaryPage.class);
        Bundle bundle = new Bundle();
        bundle.putString("day",day);
        intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
        startActivity(intent);
    }

    private void speak() {
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

}

package com.example.babyu;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;
import android.os.Vibrator;


public class Video extends AppCompatActivity implements View.OnClickListener{
    private Thread Thread1 = null;
    private static final String TAG = "Video";
    private String path= "rtsp://";
    private VideoView mVideoView;
    private Button urlbtn;
    private Button warningbtn;
    private Button backbtn;
    private EditText urltext;
    SharedPreferences pref;
    private String s;
    private String SERVER_IP;
    int SERVER_PORT =1080;
    private boolean RunThread=false;
    private Toast toast = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activiy_video);

        path = getSharedPreferences("text", MODE_PRIVATE)
                .getString("URL", "rtsp://");

        SERVER_IP = "192.168.43.25";
        warningbtn = (Button) findViewById(R.id.warnbutton);
        warningbtn.setVisibility(View.VISIBLE);
        urlbtn = (Button) findViewById(R.id.urlbutton);
        urlbtn.setOnClickListener(Video.this);
        urlbtn.setVisibility(View.VISIBLE);
        urltext=(EditText) findViewById(R.id.editText2);
        urltext.setText(path);
        urltext.setVisibility(View.INVISIBLE);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoPath(path);
        mVideoView.setBufferSize(1000);
        mVideoView.requestFocus();
        backbtn= (Button) findViewById(R.id.back);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Video.this.finish();
            }
        });
        warningbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RunThread==false){
                    RunThread=true;
                    toast = null;
                    warningbtn.setText("Warning off");
                    Thread1 = new Thread(new Thread1());
                    Thread1.start();
                }else{
                    RunThread=false;
                    warningbtn.setText("Warning on");
                }
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });


        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//当前状态
                    case MotionEvent.ACTION_DOWN:
                        if (urlbtn.getVisibility() == View.VISIBLE) {
                            urlbtn.setVisibility(View.INVISIBLE);
                            urltext.setVisibility(View.INVISIBLE);
                            warningbtn.setVisibility(View.INVISIBLE);
                        } else {
                            urlbtn.setVisibility(View.VISIBLE);
                            warningbtn.setVisibility(View.VISIBLE);
                        }
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (urltext.getVisibility() == View.INVISIBLE) {
            path = urltext.getText().toString().trim();
            urltext.setVisibility(View.VISIBLE);
        } else {
            s=urltext.getText().toString().trim();
            if (s != path) {
                path = s;
                mVideoView.setVideoPath(path);
                toast.makeText(this, "讀取中請稍等", Toast.LENGTH_SHORT).show();
                pref = getSharedPreferences("text", MODE_PRIVATE);
                pref.edit()
                        .putString("URL", path)
                        .commit();
            }
            urltext.setVisibility(View.INVISIBLE);
        }
    }

    private BufferedReader input;
    class Thread1 implements Runnable {
        public void run() {
            if(RunThread==true){
                Socket socket;
                try {
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
                    new Thread(new Thread2()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread2 implements Runnable {

        @Override
        public void run() {
            while (RunThread==true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    toast.makeText(Video.this, "請確認寶寶狀況", Toast.LENGTH_SHORT).show();
                                    setVibrate(1000);
                            }
                        });
                    }else{
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

}

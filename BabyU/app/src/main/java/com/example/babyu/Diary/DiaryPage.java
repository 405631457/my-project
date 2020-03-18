package com.example.babyu.Diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyu.DiaryActivity;
import com.example.babyu.ImageAdapter;
import com.example.babyu.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DiaryPage extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private Query query;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    String day;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView=findViewById(R.id.diary_Recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mUploads=new ArrayList<>();

        mAdapter=new ImageAdapter(DiaryPage.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(DiaryPage.this);

        if(getIntent().getExtras()!=null) {
            bundle = getIntent().getExtras();
            day = bundle.getString("day");
        }

        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        mStorage= FirebaseStorage.getInstance();
        if(day!=null){
//            day="2019年10月1日";//暫時
            query= FirebaseDatabase.getInstance().getReference("BabyDiary").child(currentUserId).child("Diary")
            .orderByChild("date")
            .equalTo(day);
            mDBListener=query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mUploads.clear();
           for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
           Upload upload=postSnapshot.getValue(Upload.class);
           upload.setKey(postSnapshot.getKey());
           mUploads.add(upload);
        }

           mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(DiaryPage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
        }
            });
        }else {
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("BabyDiary").child(currentUserId).child("Diary");
        mDBListener= mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Upload upload=postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DiaryPage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        }

    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectItem=mUploads.get(position);
        final String selectkey=selectItem.getKey();

        StorageReference imageRef=mStorage.getReferenceFromUrl(selectItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectkey).removeValue();
                Toast.makeText(DiaryPage.this,"刪除成功",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(query!=null){
            query.removeEventListener(mDBListener);
            day=null;
        }
        if(mDatabaseRef!=null){
            mDatabaseRef.removeEventListener(mDBListener);
            day=null;
        }
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
    //        if(day!="1"){
//    Query query=FirebaseDatabase.getInstance().getReference("BabyDiary").child(currentUserId).child("Diary")
//            .orderByChild("date")
//            .equalTo("2019年9月1日");
//            mDBListener=query.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        mUploads.clear();
//        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
//        Upload upload=postSnapshot.getValue(Upload.class);
//        upload.setKey(postSnapshot.getKey());
//        mUploads.add(upload);
//        }
//
//        mAdapter.notifyDataSetChanged();
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//        Toast.makeText(DiaryPage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//        }
//        });
//        }else {
//        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("BabyDiary").child(currentUserId).child("Diary");
//        mDBListener= mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                mUploads.clear();
//
//                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
//                    Upload upload=postSnapshot.getValue(Upload.class);
//                    upload.setKey(postSnapshot.getKey());
//                    mUploads.add(upload);
//                }
//
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(DiaryPage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        }
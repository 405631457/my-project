package com.example.babyu;


import androidx.annotation.NonNull;

import com.example.babyu.Diaper.DiaperData;
import com.example.babyu.Feed.FeedData;
import com.example.babyu.Growth.GrowthData;
import com.example.babyu.Login.ProfileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseAuth mAuth;
    private String currentUserId;
    private FirebaseDatabase mDatabaseFeed,mDatabaseDiaper,mDatabaseGrowth;
    private DatabaseReference mReferenceFeed,mReferenceDiaper,mReferenceGrowth,mReferenceUser;
    private List<FeedData> Feed=new ArrayList<>();
    private List<DiaperData> Diaper=new ArrayList<>();
    private List<GrowthData> Growth=new ArrayList<>();
    private List<ProfileData> Profile=new ArrayList<>();


    public interface DataStatus_feed{
        void DataIsLoaded(List<FeedData> Feed, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public interface DataStatus_diaper{
        void DataIsLoaded(List<DiaperData> Diaper, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public interface DataStatus_growth{
        void DataIsLoaded(List<GrowthData> Growth, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatus_profile{
        void DataIsLoaded(List<ProfileData> Profile, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseDatabaseHelper() {
        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        mReferenceFeed= FirebaseDatabase.getInstance().getReference().child("BabyDiary").child(currentUserId).child("Feed");
        mReferenceDiaper= FirebaseDatabase.getInstance().getReference().child("BabyDiary").child(currentUserId).child("Diaper");
        mReferenceGrowth= FirebaseDatabase.getInstance().getReference().child("BabyDiary").child(currentUserId).child("Growth");
        mReferenceUser= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
    }


    public void readFeed(final DataStatus_feed dataStatus){
        mReferenceFeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Feed.clear();
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    FeedData feeds=keyNode.getValue(FeedData.class);
                    Feed.add(feeds);
                }
                dataStatus.DataIsLoaded(Feed,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readDiaper(final DataStatus_diaper dataStatus_1){
        mReferenceDiaper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Diaper.clear();
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    DiaperData diapers=keyNode.getValue(DiaperData.class);
                    Diaper.add(diapers);
                }
                dataStatus_1.DataIsLoaded(Diaper,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readGrowth(final DataStatus_growth dataStatus){
        mReferenceGrowth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Growth.clear();
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    GrowthData growths=keyNode.getValue(GrowthData.class);
                    Growth.add(growths);
                }
                dataStatus.DataIsLoaded(Growth,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readProfile(final DataStatus_profile dataStatus){
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile.clear();
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    ProfileData profiles=keyNode.getValue(ProfileData.class);
                    Profile.add(profiles);
                }
                dataStatus.DataIsLoaded(Profile,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addFeedData(FeedData Feed,final DataStatus_feed dataStatus){
        String key=mReferenceFeed.push().getKey();
        mReferenceFeed.child(key).setValue(Feed).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void addDiaperData(DiaperData Diaper,final DataStatus_diaper dataStatus){
        String key=mReferenceDiaper.push().getKey();
        mReferenceDiaper.child(key).setValue(Diaper).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void addGrowthData(GrowthData Growth,final DataStatus_growth dataStatus){
        String key=mReferenceGrowth.push().getKey();
        mReferenceGrowth.child(key).setValue(Growth).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void addProfileData(ProfileData Profile,final DataStatus_profile dataStatus){
        String key=mReferenceUser.push().getKey();
        mReferenceUser.child(key).setValue(Profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateFeedData(String key,FeedData Feed,final DataStatus_feed dataStatus){
        mReferenceFeed.child(key).setValue(Feed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }



    public void updateDiaperData(String key,DiaperData Diaper,final DataStatus_diaper dataStatus){
        mReferenceDiaper.child(key).setValue(Diaper)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void updateGrowthData(String key,GrowthData Growth,final DataStatus_growth dataStatus){
        mReferenceGrowth.child(key).setValue(Growth)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteFeedData(String key,final DataStatus_feed dataStatus){
        mReferenceFeed.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void deleteDiaperData(String key,final DataStatus_diaper dataStatus){
        mReferenceDiaper.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void deleteGrowthData(String key,final DataStatus_growth dataStatus){
        mReferenceGrowth.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }
}

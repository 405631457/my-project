package com.example.babyu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyu.Diaper.DiaperData;
import com.example.babyu.Diaper.DiaperPage_detail;
import com.example.babyu.Feed.FeedData;
import com.example.babyu.Feed.FeedPage_detail;
import com.example.babyu.Growth.GrowthData;
import com.example.babyu.Growth.GrowthPage_detail;
import com.example.babyu.Login.ProfileData;

import java.util.List;

public class Recycler_config {

    private Context mContext,mContext_1,mContext_2,mContext_profile;
    private FeedAdapter mFeedAdapter;
    private DiaperAdapter mDiaperAdapter;
    private GrowthAdapter mGrowthAdapter;
    private ProfileAdapter mProfileAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<FeedData> Feed, List<String> key){
        mContext=context;
        mFeedAdapter=new FeedAdapter(Feed,key);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mFeedAdapter);
    }
    public void setConfig_1(RecyclerView recyclerView, Context context, List<DiaperData> Diaper, List<String> key){
        mContext_1=context;
        mDiaperAdapter=new DiaperAdapter(Diaper,key);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mDiaperAdapter);
    }
    public void setConfig_2(RecyclerView recyclerView, Context context, List<GrowthData> Growth, List<String> key){
        mContext_2=context;
        mGrowthAdapter=new GrowthAdapter(Growth,key);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mGrowthAdapter);
    }
    public void setConfig_profile(RecyclerView recyclerView, Context context, List<ProfileData> Profile, List<String> key){
        mContext_profile=context;
        mProfileAdapter=new ProfileAdapter(Profile,key);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mProfileAdapter);
    }


    class FeedItemView extends RecyclerView.ViewHolder{

        private TextView mTime;
        private TextView mMl;
        private TextView mDate;

        private String key;

        public FeedItemView(final ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_feed,parent,false));

            mTime=itemView.findViewById(R.id.textTitle);
            mMl=itemView.findViewById(R.id.textDescription);
            mDate=itemView.findViewById(R.id.text_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, FeedPage_detail.class);
                    intent.putExtra("key",key);
                    intent.putExtra("time",mTime.getText().toString());
                    intent.putExtra("date",mDate.getText().toString());
                    intent.putExtra("ml",mMl.getText().toString());

                    mContext.startActivity(intent);

                }
            });
        }

        public void bind(FeedData feed,String key){
            mTime.setText(feed.getTime());
            mMl.setText(feed.getMl());
            mDate.setText(feed.getDate());
            this.key=key;
        }

    }

    class FeedAdapter extends RecyclerView.Adapter<FeedItemView>{
        private List<FeedData> mFeedList;
        private List<String> mkeys;

        public FeedAdapter(List<FeedData> mFeedList, List<String> mkeys) {
            this.mFeedList = mFeedList;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public FeedItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final FeedItemView holder, final int position) {

            holder.bind(mFeedList.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mFeedList.size();
        }
    }

    class DiaperItemView extends RecyclerView.ViewHolder{

        private TextView mTime;
        private TextView mStatus;
        private TextView mDate;
        private TextView mRemarks;

        private String key;

        public DiaperItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext_1).
                    inflate(R.layout.item_diaper,parent,false));

            mTime=itemView.findViewById(R.id.textTitle);
            mStatus=itemView.findViewById(R.id.textDescription);
            mDate=itemView.findViewById(R.id.text_date);
            mRemarks=itemView.findViewById(R.id.textView_remarks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext_1, DiaperPage_detail.class);
                    intent.putExtra("key",key);
                    intent.putExtra("time",mTime.getText().toString());
                    intent.putExtra("date",mDate.getText().toString());
                    intent.putExtra("status",mStatus.getText().toString());
                    intent.putExtra("remarks",mRemarks.getText().toString());

                    mContext_1.startActivity(intent);
                }
            });


        }

        public void bind(DiaperData diaper, String key){
            mTime.setText(diaper.getTime());
            mStatus.setText(diaper.getStatus());
            mDate.setText(diaper.getDate());
            mRemarks.setText(diaper.getRemarks());
            this.key=key;

        }
    }

    class DiaperAdapter extends RecyclerView.Adapter<DiaperItemView>{
        private List<DiaperData> mDiaperList;
        private List<String> mkeys;

        public DiaperAdapter(List<DiaperData> mDiaperList, List<String> mkeys) {
            this.mDiaperList = mDiaperList;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public DiaperItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DiaperItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DiaperItemView holder, int position) {

            holder.bind(mDiaperList.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() { return mDiaperList.size(); }
    }

    class GrowthItemView extends RecyclerView.ViewHolder{

        private TextView mHeitht;
        private TextView mWeight;
        private TextView mHead;
        private TextView mDate;
        private TextView mtextOptionDigit;

        private String key;

        public GrowthItemView(final ViewGroup parent){
            super(LayoutInflater.from(mContext_2).
                    inflate(R.layout.item_growth,parent,false));

            mHeitht=itemView.findViewById(R.id.textView_height);
            mWeight=itemView.findViewById(R.id.textView_weight);
            mHead=itemView.findViewById(R.id.textView_head);
            mDate=itemView.findViewById(R.id.text_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext_2, GrowthPage_detail.class);
                    intent.putExtra("key",key);
                    intent.putExtra("date",mDate.getText().toString());
                    intent.putExtra("height",mHeitht.getText().toString());
                    intent.putExtra("weight",mWeight.getText().toString());
                    intent.putExtra("head",mHead.getText().toString());

                    mContext_2.startActivity(intent);
                }
            });

        }

        public void bind(GrowthData growth,String key){
            mHeitht.setText(growth.getHeight());
            mWeight.setText(growth.getWeight());
            mHead.setText(growth.getHead());
            mDate.setText(growth.getDate());
            this.key=key;
        }

    }

    class GrowthAdapter extends RecyclerView.Adapter<GrowthItemView>{
        private List<GrowthData> mGrowthList;
        private List<String> mkeys;

        GrowthAdapter(List<GrowthData> mGrowthList, List<String> mkeys) {
            this.mGrowthList = mGrowthList;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public GrowthItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GrowthItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GrowthItemView holder, int position) {
            holder.bind(mGrowthList.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() { return mGrowthList.size(); }
    }

    class ProfileItemView extends RecyclerView.ViewHolder{

        private TextView mName;
        private TextView mNickname;
        private TextView mDate;

        private String key;

        public ProfileItemView(final ViewGroup parent){
            super(LayoutInflater.from(mContext_profile).
                    inflate(R.layout.item_profile,parent,false));

//            mName=itemView.findViewById(R.id.text_name);
            mNickname=itemView.findViewById(R.id.text_nickname);
            mDate=itemView.findViewById(R.id.text_birthday);

        }

        public void bind(ProfileData profile, String key){
//            mName.setText(profile.getName());
            mNickname.setText(profile.getNickname());
            mDate.setText(profile.getDate());
            this.key=key;
        }

    }

    class ProfileAdapter extends RecyclerView.Adapter<ProfileItemView>{
        private List<ProfileData> mProfileList;
        private List<String> mkeys;

        public ProfileAdapter(List<ProfileData> mProfileList, List<String> mkeys) {
            this.mProfileList = mProfileList;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public ProfileItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProfileItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final ProfileItemView holder, final int position) {

            holder.bind(mProfileList.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mProfileList.size();
        }
    }
    
}

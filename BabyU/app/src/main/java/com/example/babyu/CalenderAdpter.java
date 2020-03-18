package com.example.babyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalenderAdpter extends RecyclerView.Adapter<CalenderAdpter.ViewHolder>{
    private static final String TAG="CalenderAdpter";
    private ArrayList<String> mNote = new ArrayList<>();
    private Context mContext;

    public CalenderAdpter(Context context,ArrayList<String> note){
        mNote=note;
        mContext=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_item,parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.note.setText(mNote.get(position));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView note;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.calender_note);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}

package com.example.babyu;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/* 适配器继承自RecyclerView.Adapter，将泛型指定为MsgAdapter.ViewHolder */
/* ViewHolder是在MsgAdapter中定义的一个内部类 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> mMsgList;                                    //储存消息

    /*定义 LinearLayout 和 TextView 通过view.findViewById将他们跟实例绑定起来 */
    /*继承自RecyclerView.ViewHolder*/
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }
    /*构造函数传入数据*/
    public MsgAdapter(List<Msg>msgList){
        mMsgList = msgList;
    }


    /*重写onCreateViewHolder()， onBindViewHolder(),getItemCount()方法*/
    @Override
    /*创建ViewHoldle实例，在这个方法中，将布局加载进来，并传入到构造函数中，实例返回*/
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    /*对RecyclerView子项的数据进行赋值，在子项滚动到屏幕内执行，通过position得到当前项的msg实例，将数据传入到TextView和LinearLayout中*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        if(msg.getType()==Msg.TYPE_RECEIVED){
            /*如果收到消息，显示左边的消息布局，隐藏右边的消息布局*/
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType()==Msg.TYPE_SENT){
            /*如果发送消息，显示右边的消息布局，隐藏左边的消息布局*/
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    /*返回有多少子项（数据源的长度）*/
    @Override
    public int getItemCount() {
        return  mMsgList.size();
    }
}

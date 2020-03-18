package com.example.babyu;

public class Msg {
    public static final int TYPE_RECEIVED = 0;  //表示这是一条收到的消息
    public static final int TYPE_SENT = 1;      //表示这是一条发送的消息
    private String content;                     //消息内容
    private int type;                           //消息类型
    public Msg(String content,int type){        //构造函数，初始化数据
        this.content = content;
        this.type = type;
    }
    public String getContent() {                 //返回消息内容
        return content;
    }
    public int getType(){                        //返回消息类型
        return type;
    }
}

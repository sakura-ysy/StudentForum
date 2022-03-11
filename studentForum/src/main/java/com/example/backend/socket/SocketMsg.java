package com.example.backend.socket;

/**
 * @Auther: zj
 * @Date: 2018/8/16 23:29
 * @Description:这里我们就不能使用简单的文本消息进行消息的发送了，我们使用json进行消息的发送。
 * 所以需要先创建一个消息对象，里面包含了消息发送者，消息接受者，消息类型（单聊还是群聊），还是就是消息，如下：
 */
public class SocketMsg {
    private int type;   //聊天类型0：群聊，1：单聊.
    private String fromUser;//发送者.
    private String toUser;//接受者.
    private String msg;//消息
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getFromUser() {
        return fromUser;
    }
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    public String getToUser() {
        return toUser;
    }
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}


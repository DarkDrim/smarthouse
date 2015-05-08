package com.isosystem.smarthouse.DataStructures;

import java.io.Serializable;

import com.isosystem.smarthouse.Notifications.Notifications.MessageType;

public class AlarmMessage implements Serializable {
	
	private static final long serialVersionUID = -7081775343933798513L;
	public String msgText ="";
	public MessageType msgType = null;
	public long msgTime=0;
	
	public AlarmMessage (String message_text, MessageType mType) {
		this.msgText = message_text;
		this.msgType = mType;
		this.msgTime = System.currentTimeMillis();
	}
}
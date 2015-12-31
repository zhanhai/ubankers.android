package cn.com.ubankers.www.authentication.controller.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver{
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static MessageListener mMessageListener;
	public SMSBroadcastReceiver(){
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		if(intent.getAction().equals(SMS_RECEIVED_ACTION)){
			Object[] pdus = (Object[])intent.getExtras().get("pdus");
			for(Object pdu:pdus){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String sender = smsMessage.getDisplayOriginatingAddress();
				//短信内容
				String content = smsMessage.getDisplayMessageBody();
				
				System.out.println(content+"");
				long date = smsMessage.getTimestampMillis();
				Date tiemDate = new Date(date);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = simpleDateFormat.format(tiemDate);
				
				//过滤不需要读取的短信的发送号码
				if("10690575".equals(sender)){
					mMessageListener.onReceived(content);
					abortBroadcast();
				}else if("1065502003081104311".equals(sender)){
					mMessageListener.onReceived(content);
					abortBroadcast();
				}else if("10690175715519".equals(sender)){
					mMessageListener.onReceived(content);
					abortBroadcast();
				}
			}
		}
	}
	
	public static String getDYnamicPassword(String content){
		String check ="(?<!\\d)\\d{6}(?!\\d)";
		Pattern continuousNumberPattern = Pattern.compile(check);
		Matcher m = continuousNumberPattern.matcher(content);
		String dynamicPassword = "";
		while(m.find()){
			if(m.group().length() == 6){
				System.out.print(m.group());
				dynamicPassword = m.group();
			}
		}
		return dynamicPassword;
	}
	
	public interface MessageListener{
		public void onReceived(String message);
	}
	
	public void setOnReceivedMessageLiatener(MessageListener messageListener){
		this.mMessageListener = messageListener;
	}

}

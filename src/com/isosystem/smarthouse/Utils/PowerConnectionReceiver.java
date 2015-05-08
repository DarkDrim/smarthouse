package com.isosystem.smarthouse.Utils;

import com.isosystem.smarthouse.MyApplication;
import com.isosystem.smarthouse.Notifications.Notifications.MessageType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class PowerConnectionReceiver extends BroadcastReceiver {

	MyApplication mApplication;

	@Override
	public void onReceive(Context context, Intent intent) {

		mApplication = (MyApplication) context.getApplicationContext();

		String msg = "";

		if (isSupplyEnabled()) {
			msg = "���������� ���������� � �����������";
		} else {
			msg = "���������� ��������� �� �����������";
		}

		mApplication.mAlarmMessages.addAlarmMessage(mApplication, msg,
				MessageType.PowerSupplyMessage);

		Intent i = new Intent();
		i.setAction("SMARTHOUSE.POWER_SUPPLY_CHANGED");
		context.sendBroadcast(i);
	}

	private Boolean isSupplyEnabled() {
		Intent intent = mApplication.registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		boolean result = (plugged != 0 && plugged!=-1);
		return result;
	}
}
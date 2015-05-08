/*
 * ��������� ���������� ��� ������� "����� ���"
 * 
 * author: ����������� �������
 * email: nick.godov@gmail.com
 * last edit: 11.09.2014
 */

package com.isosystem.smarthouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.isosystem.smarthouse.DataStructures.AlarmMessages;
import com.isosystem.smarthouse.DataStructures.FormattedScreens;
import com.isosystem.smarthouse.DataStructures.MenuTree;
import com.isosystem.smarthouse.Logging.Logging;
import com.isosystem.smarthouse.Notifications.SoundMessages;
import com.isosystem.smarthouse.XMLProcessor.XMLProcessor;

/**
 * ���� ����� �������� ������ ������ ���� {@link MenuTree}, � �����
 * ������-���������� ������ ���� {@link XMLProcessor}, ������� ���������
 * ��������� � ���������� ������ ���� � ���� �� ��������.
 * 
 * @author ����������� ������� (nick.godov@gmail.com)
 * @see MenuTree
 * @see XMLProcessor
 */
public class MyApplication extends Application {
	/** ������ ���� */
	public MenuTree mTree;

	/** ������ ��������� ��������� */
	public AlarmMessages mAlarmMessages;

	int pid;

	public Boolean isUsbConnected = false;

	/** ������ ���� ���������������� ������ */
	public FormattedScreens mFormattedScreens;

	/** ���������� ������ ���� (��������\����������) */
	public XMLProcessor mProcessor;

	public SoundMessages soundMessages;

	/**
	 * ��� ������ ����������:<br>
	 * 1) ��������� ������� ������� �����; <br>
	 * 2) �������� ������-����������; <br>
	 * 3) ��������� ������ ���� � ������� �������-�����������.
	 * 
	 * @author ����������� ������� (nick.godov@gmail.com)
	 */
	@Override
	public void onCreate() {

		PreferenceManager.setDefaultValues(this, R.xml.application_preferences,
				false);

		this.pid = android.os.Process.myPid(); // Save for later use.
		soundMessages = new SoundMessages(getApplicationContext());
		mProcessor = new XMLProcessor(getApplicationContext());

		if (!mProcessor.loadMessagesFromInternalStorage()) {
			Logging.v("�������� ��������� �� ����������� ��������� ������ � �������");
		}
	}

	public void killApp() {
		android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
	}
}
package com.isosystem.smarthouse.XMLProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.isosystem.smarthouse.Globals;
import com.isosystem.smarthouse.MyApplication;
import com.isosystem.smarthouse.DataStructures.AlarmMessages;
import com.isosystem.smarthouse.DataStructures.FormattedScreens;
import com.isosystem.smarthouse.DataStructures.MenuTree;
import com.isosystem.smarthouse.Logging.Logging;

public class old_XMLProcessor {

	MyApplication mApplication;

	public old_XMLProcessor(Context c) {
		mApplication = (MyApplication) c.getApplicationContext();
	}

	String menuDirectoryPath = Environment.getExternalStorageDirectory()
			+ File.separator + Globals.EXTERNAL_ROOT_DIRECTORY + File.separator
			+ Globals.EXTERNAL_MENUTREE_DIRECTORY;

	String menuFilePath = menuDirectoryPath + File.separator
			+ Globals.MENU_TREE_FILENAME;

	String messagesDirectoryPath = Environment.getExternalStorageDirectory()
			+ File.separator + Globals.EXTERNAL_ROOT_DIRECTORY + File.separator
			+ Globals.EXTERNAL_ALARM_MESSAGES_DIRECTORY;

	String messagesFilePath = messagesDirectoryPath + File.separator
			+ Globals.ALARM_MESSAGES_FILENAME;

	String screensDirectoryPath = Environment.getExternalStorageDirectory()
			+ File.separator + Globals.EXTERNAL_ROOT_DIRECTORY + File.separator
			+ Globals.EXTERNAL_FORMATTED_SCREENS_DIRECTORY;

	String screensFilePath = screensDirectoryPath + File.separator
			+ Globals.FORMATTED_SCREENS_FILENAME;

	String preferencesDirectoryPath = Environment.getExternalStorageDirectory()
			+ File.separator + Globals.EXTERNAL_ROOT_DIRECTORY + File.separator
			+ Globals.EXTERNAL_PREFERENCES_DIRECTORY;

	String preferencesFilePath = preferencesDirectoryPath + File.separator
			+ Globals.PREFERENCES_FILENAME;

	public Boolean loadMenuTree() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {

			File menuTreeDirectory = new File(menuDirectoryPath);
			menuTreeDirectory.mkdirs();

			// ���� ����� ��� - ������� ����� ���� � ������� ����� ����
			File menuTreeFile = new File(menuFilePath);
			if (menuTreeFile.exists()) {
				try {
					mApplication.mTree = loadMenuTreeFromSDCard();
				} catch (Exception e) {
					Logging.v(">>(Exception)<<. ���������� ��� ������� ��������� ���������");
					e.printStackTrace();
				}
			} else {
				try {
					menuTreeFile.createNewFile();
					writeMenuTreeToSDCard(new MenuTree());
				} catch (IOException e) {
					Logging.v(">>(Exception)<<. ���������� ��� ������� ��������� ���������");
					e.printStackTrace();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public Boolean saveMenuTree() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File menuTreeDirectory = new File(menuDirectoryPath);
			menuTreeDirectory.mkdirs();
			
			File menuTreeFile = new File(menuFilePath);
			if (menuTreeFile.exists()) {
				try {
					writeMenuTreeToSDCard(mApplication.mTree);
				} catch (Exception e) {
					Logging.v(">>(Exception)<<. ���������� ��� ������� ��������� ���������");
					e.printStackTrace();
				}
			} else {
				try {
					menuTreeFile.createNewFile();
					writeMenuTreeToSDCard(mApplication.mTree);
				} catch (IOException e) {
					Logging.v(">>(Exception)<<. ���������� ��� ������� ��������� ���������");
					e.printStackTrace();
				}
			}
			
			return true;
		}
		return false;
	}
	
	// ��������� ���� �� �����
	private MenuTree loadMenuTreeFromSDCard() throws StreamCorruptedException,
			IOException, ClassNotFoundException {
		FileInputStream inputStream = new FileInputStream(menuFilePath);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);

		MenuTree mTree = (MenuTree) objectStream.readObject();

		inputStream.close();
		inputStream = null;

		objectStream.close();
		objectStream = null;

		return mTree;
	}
	
	private void writeMenuTreeToSDCard(MenuTree mTree) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(menuFilePath);

		ObjectOutputStream objectStream = new ObjectOutputStream(
				fileOutputStream);
		objectStream.writeObject(mTree);

		fileOutputStream.flush();
		fileOutputStream.close();
		fileOutputStream = null;

		objectStream.flush();
		objectStream.close();
		objectStream = null;
	}
	
	// ���������� ������ � ����, ����� ��������� �� �����
	public Boolean reloadMenuTree() throws IOException,
			ClassNotFoundException {
		if (saveMenuTree()) return loadMenuTree();			
		else return false;
	}

	// >>>>>>>>>>>>>>>>>>>>>>> ��������� <<<<<<<<<<<<<<<<<<<<<<<<

	// ��������� ���������

	public AlarmMessages loadAlarmMessages() throws OptionalDataException,
			ClassNotFoundException, IOException {

		Logging.v("�������� ��������� ���������");
		Logging.v("��������� ���������� � �����������");

		// ��������� ���������� � ������
		File messagesTreeDirectory = new File(messagesDirectoryPath);

		if (messagesTreeDirectory.isDirectory()) {
			// ���� ���������� ����, �������� ��������� ����

			Logging.v("���������� � ����������� �������");
			Logging.v("���� ���� � �����������");

			File messagesTreeFile = new File(messagesFilePath);

			// ��������� ������� �����
			if (messagesTreeFile.exists()) {
				Logging.v("���� � ����������� ������");
			} else {
				Logging.v("����� � ����������� ���, ������� �����");
				messagesTreeFile.createNewFile();
				serializeAlarmMessages(new AlarmMessages());
			}
		} else {
			Logging.v("���������� � ���� ���, ������� ���������� � ���� � �����������");
			// ����� ����� ���, ������� �����. ��������, ��� ����� ���
			messagesTreeDirectory.mkdirs();
			// ������� ���� � ����� ������ �������
			File messagesTreeFile = new File(messagesFilePath);
			messagesTreeFile.createNewFile();
			serializeAlarmMessages(new AlarmMessages());
		}

		// ���� ����, �������� ���������������
		return deserializeAlarmMessages();
	}

	public void saveAlarmMessages() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("��������� ��������� ���������");

		Logging.v("��������� ���������� � �����������");
		File messagesTreeDirectory = new File(messagesDirectoryPath);
		if (messagesTreeDirectory.isDirectory()) {
			Logging.v("���������� � ����������� �������");

			Logging.v("���� ���� � �����������");
			// ���� ���������� ����, �������� �������� ����
			File messagesTreeFile = new File(messagesFilePath);
			// ��������� ������� �����
			if (messagesTreeFile.exists()) {
				Logging.v("���� � ����������� ������, ����� � ����");
				serializeAlarmMessages(mApplication.mAlarmMessages);
			} else {
				Logging.v("����� � ����������� ���, ������� �����");

				File newMessagesTreeFile = new File(messagesFilePath);
				newMessagesTreeFile.createNewFile();
				serializeAlarmMessages(mApplication.mAlarmMessages);
			}
		} else {
			Logging.v("���������� � ����������� ���, ������� ���������� � ���� � �����������");
			messagesTreeDirectory.mkdirs();

			File newMessagesTreeFile = new File(messagesFilePath);
			newMessagesTreeFile.createNewFile();
			serializeAlarmMessages(mApplication.mAlarmMessages);
		}
	}

	// ���������� ���� � ����
	private void serializeAlarmMessages(AlarmMessages messages)
			throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(
				messagesFilePath);

		ObjectOutputStream objectStream = new ObjectOutputStream(
				fileOutputStream);
		objectStream.writeObject(messages);

		fileOutputStream.flush();
		fileOutputStream.close();
		fileOutputStream = null;

		objectStream.flush();
		objectStream.close();
		objectStream = null;
	}

	// ��������� ���� �� �����
	private AlarmMessages deserializeAlarmMessages()
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		FileInputStream inputStream = new FileInputStream(messagesFilePath);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);

		AlarmMessages messages = (AlarmMessages) objectStream.readObject();

		inputStream.close();
		inputStream = null;

		objectStream.close();
		objectStream = null;

		return messages;
	}

	public void reloadAlarmMessages() throws IOException,
			ClassNotFoundException {
		saveAlarmMessages();
		mApplication.mAlarmMessages = loadAlarmMessages();
	}

	// >>>>>>>>>>>>>>>>>>>>>>> ���� ������ <<<<<<<<<<<<<<<<<<<<<<<<

	// ���� ���������������� ������

	public FormattedScreens loadFormattedScreens()
			throws OptionalDataException, ClassNotFoundException, IOException {

		Logging.v("�������� ���� ���������������� ������");
		Logging.v("��������� ���������� � ������");

		// ��������� ���������� � ������
		File screensDirectory = new File(screensDirectoryPath);

		if (screensDirectory.isDirectory()) {
			// ���� ���������� ����, �������� ��������� ����

			Logging.v("���������� � ������ �������");
			Logging.v("���� ���� � ������");

			File screensFile = new File(screensFilePath);

			// ��������� ������� �����
			if (screensFile.exists()) {
				Logging.v("���� � ������ ������ ������");
			} else {
				Logging.v("����� � ������ ���, ������� �����");
				screensFile.createNewFile();
				serializeFormattedScreens(new FormattedScreens());
			}
		} else {
			Logging.v("���������� � ������ ���, ������� ���������� � ���� � ������");
			// ����� ����� ���, ������� �����. ��������, ��� ����� ���
			screensDirectory.mkdirs();
			// ������� ���� � ����� ������ �������
			File screensFile = new File(screensFilePath);
			screensFile.createNewFile();
			serializeFormattedScreens(new FormattedScreens());
		}

		// ���� ����, �������� ���������������
		return deserializeFormattedScreens();
	}

	public void saveForamttedScreens() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("��������� ���� ���������������� ������");

		Logging.v("��������� ���������� � ������");
		File screensDirectory = new File(screensDirectoryPath);
		if (screensDirectory.isDirectory()) {
			Logging.v("���������� � ������ �������");

			Logging.v("���� ���� � ������");
			// ���� ���������� ����, �������� �������� ����
			File screensFile = new File(screensFilePath);
			// ��������� ������� �����
			if (screensFile.exists()) {
				Logging.v("���� � ������ ������, ����� � ����");
				serializeFormattedScreens(mApplication.mFormattedScreens);
			} else {
				Logging.v("����� � ������ ���, ������� �����");

				File newScreensFile = new File(screensFilePath);
				newScreensFile.createNewFile();
				serializeFormattedScreens(mApplication.mFormattedScreens);
			}
		} else {
			Logging.v("���������� � ������ ���, ������� ���������� � ���� � ������");
			screensDirectory.mkdirs();

			File newScreensFile = new File(screensFilePath);
			newScreensFile.createNewFile();
			serializeFormattedScreens(mApplication.mFormattedScreens);
		}
	}

	// ���������� ���� � ����
	private void serializeFormattedScreens(FormattedScreens screens)
			throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(
				screensFilePath);

		ObjectOutputStream objectStream = new ObjectOutputStream(
				fileOutputStream);
		objectStream.writeObject(screens);

		fileOutputStream.flush();
		fileOutputStream.close();
		fileOutputStream = null;

		objectStream.flush();
		objectStream.close();
		objectStream = null;
	}

	// ��������� ���� �� �����
	private FormattedScreens deserializeFormattedScreens()
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		FileInputStream inputStream = new FileInputStream(screensFilePath);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);

		FormattedScreens screens = (FormattedScreens) objectStream.readObject();

		inputStream.close();
		inputStream = null;

		objectStream.close();
		objectStream = null;

		return screens;
	}

	public void reloadFormattedScreens() throws IOException,
			ClassNotFoundException {
		saveForamttedScreens();
		mApplication.mFormattedScreens = loadFormattedScreens();
	}

	// >>>>>>>>>>>>>>>>>>>>>>> ��������� <<<<<<<<<<<<<<<<<<<<<<<<

	// ���� ���������������� ������

	public void loadPreferences() throws OptionalDataException,
			ClassNotFoundException, IOException {

		Logging.v("�������� ���������");
		Logging.v("��������� ���������� � �����������");

		// ��������� ���������� � ������
		File preferencesDirectory = new File(preferencesDirectoryPath);

		if (preferencesDirectory.isDirectory()) {
			// ���� ���������� ����, �������� ��������� ����

			Logging.v("���������� � ����������� �������");
			Logging.v("���� ���� � �����������");

			File preferencesFile = new File(preferencesFilePath);

			// ��������� ������� �����
			if (preferencesFile.exists()) {
				Logging.v("���� � ����������� ������");
			} else {
				Logging.v("����� � ����������� ���, ������� �����");
				preferencesFile.createNewFile();
				serializePreferences();
			}
		} else {
			Logging.v("���������� � ����������� ���, ������� ���������� � ���� � ������");
			// ����� ����� ���, ������� �����. ��������, ��� ����� ���
			preferencesDirectory.mkdirs();
			// ������� ���� � �����������
			File preferencesFile = new File(preferencesFilePath);
			preferencesFile.createNewFile();
			serializePreferences();
		}

		// ���� ����, �������� ���������������
		deserializePreferences();
	}

	public void savePreferences() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("��������� ���������");

		Logging.v("��������� ���������� � �����������");
		File preferencesDirectory = new File(preferencesDirectoryPath);
		if (preferencesDirectory.isDirectory()) {
			Logging.v("���������� � ����������� �������");

			Logging.v("���� ���� � �����������");
			// ���� ���������� ����, �������� �������� ����
			File preferencesFile = new File(preferencesFilePath);
			// ��������� ������� �����
			if (preferencesFile.exists()) {
				Logging.v("���� � ����������� ������, ����� � ����");
				serializePreferences();
			} else {
				Logging.v("����� � ����������� ���, ������� �����");

				File newPreferencesFile = new File(preferencesFilePath);
				newPreferencesFile.createNewFile();
				serializePreferences();
			}
		} else {
			Logging.v("���������� � ������ ���, ������� ���������� � ���� � ������");
			preferencesDirectory.mkdirs();

			File newPreferencesFile = new File(preferencesFilePath);
			newPreferencesFile.createNewFile();
			serializePreferences();
			;
		}
	}

	// ������ �������� �� ������� ����
	private void serializePreferences() throws IOException {

		String package_name = mApplication.getPackageName();
		String internal_path = File.separator + "data" + File.separator
				+ "data" + File.separator + package_name + File.separator
				+ "shared_prefs" + File.separator + package_name
				+ "_preferences.xml";

		FileInputStream fin = new FileInputStream(internal_path);
		FileOutputStream fout = new FileOutputStream(preferencesFilePath);

		int i = 0;
		while ((i = fin.read()) != -1) {
			fout.write((byte) i);
		}

		fin.close();
		fout.close();
	}

	// ���������� �������� �� �������� �����
	private void deserializePreferences() throws StreamCorruptedException,
			IOException, ClassNotFoundException {

		String package_name = mApplication.getPackageName();
		String internal_path = File.separator + "data" + File.separator
				+ "data" + File.separator + package_name + File.separator
				+ "shared_prefs" + File.separator + package_name
				+ "_preferences.xml";

		String ip_dir = File.separator + "data" + File.separator + "data"
				+ File.separator + package_name + File.separator
				+ "shared_prefs";

		File file_dir = new File(ip_dir);
		file_dir.mkdirs();
		file_dir = new File(internal_path);
		file_dir.createNewFile();
		file_dir = null;

		FileInputStream fin = new FileInputStream(preferencesFilePath);
		FileOutputStream fout = new FileOutputStream(internal_path, false);

		int i = 0;
		while ((i = fin.read()) != -1) {
			fout.write((byte) i);
		}

		fin.close();
		fout.close();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mApplication);

		int ss = 9;

	}

	public void reloadPreferences() throws IOException, ClassNotFoundException {
		loadPreferences();
		savePreferences();
	}
}
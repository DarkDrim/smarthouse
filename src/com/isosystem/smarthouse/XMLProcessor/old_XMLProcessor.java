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

			// Если файла нет - создать новый файл и создать новое меню
			File menuTreeFile = new File(menuFilePath);
			if (menuTreeFile.exists()) {
				try {
					mApplication.mTree = loadMenuTreeFromSDCard();
				} catch (Exception e) {
					Logging.v(">>(Exception)<<. Исключение при попытке загрузить настройки");
					e.printStackTrace();
				}
			} else {
				try {
					menuTreeFile.createNewFile();
					writeMenuTreeToSDCard(new MenuTree());
				} catch (IOException e) {
					Logging.v(">>(Exception)<<. Исключение при попытке загрузить настройки");
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
					Logging.v(">>(Exception)<<. Исключение при попытке загрузить настройки");
					e.printStackTrace();
				}
			} else {
				try {
					menuTreeFile.createNewFile();
					writeMenuTreeToSDCard(mApplication.mTree);
				} catch (IOException e) {
					Logging.v(">>(Exception)<<. Исключение при попытке загрузить настройки");
					e.printStackTrace();
				}
			}
			
			return true;
		}
		return false;
	}
	
	// Считываем меню из файла
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
	
	// Записываем дерево в файл, потом считываем из файла
	public Boolean reloadMenuTree() throws IOException,
			ClassNotFoundException {
		if (saveMenuTree()) return loadMenuTree();			
		else return false;
	}

	// >>>>>>>>>>>>>>>>>>>>>>> СООБЩЕНИЯ <<<<<<<<<<<<<<<<<<<<<<<<

	// Алармовые сообщения

	public AlarmMessages loadAlarmMessages() throws OptionalDataException,
			ClassNotFoundException, IOException {

		Logging.v("Загружем алармовые сообщения");
		Logging.v("Проверяем директорию с сообщениями");

		// Проверяем директорию с файлом
		File messagesTreeDirectory = new File(messagesDirectoryPath);

		if (messagesTreeDirectory.isDirectory()) {
			// Если директория есть, пытаемся прочитать файл

			Logging.v("Директория с сообщениями найдена");
			Logging.v("Ищем файл с сообщениями");

			File messagesTreeFile = new File(messagesFilePath);

			// Проверяем наличие файла
			if (messagesTreeFile.exists()) {
				Logging.v("Файл с сообщениями найден");
			} else {
				Logging.v("Файла с сообщениями нет, создаем новый");
				messagesTreeFile.createNewFile();
				serializeAlarmMessages(new AlarmMessages());
			}
		} else {
			Logging.v("Директории с меню нет, создаем директорию и файл с сообщениями");
			// Такой папки нет, создаем новую. Очевидно, что файла нет
			messagesTreeDirectory.mkdirs();
			// Создаем файл с новым чистым деревом
			File messagesTreeFile = new File(messagesFilePath);
			messagesTreeFile.createNewFile();
			serializeAlarmMessages(new AlarmMessages());
		}

		// Файл есть, пытаемся десериализовать
		return deserializeAlarmMessages();
	}

	public void saveAlarmMessages() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("Сохраняем алармовые сообщения");

		Logging.v("Проверяем директорию с сообщениями");
		File messagesTreeDirectory = new File(messagesDirectoryPath);
		if (messagesTreeDirectory.isDirectory()) {
			Logging.v("Директория с сообщениями найдена");

			Logging.v("Ищем файл с сообщениями");
			// Если директория есть, пытаемся записать файл
			File messagesTreeFile = new File(messagesFilePath);
			// Проверяем наличие файла
			if (messagesTreeFile.exists()) {
				Logging.v("Файл с сообщениями найден, пишем в файл");
				serializeAlarmMessages(mApplication.mAlarmMessages);
			} else {
				Logging.v("Файла с сообщениями нет, создаем новый");

				File newMessagesTreeFile = new File(messagesFilePath);
				newMessagesTreeFile.createNewFile();
				serializeAlarmMessages(mApplication.mAlarmMessages);
			}
		} else {
			Logging.v("Директории с сообщениями нет, создаем директорию и файл с сообщениями");
			messagesTreeDirectory.mkdirs();

			File newMessagesTreeFile = new File(messagesFilePath);
			newMessagesTreeFile.createNewFile();
			serializeAlarmMessages(mApplication.mAlarmMessages);
		}
	}

	// Записываем меню в файл
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

	// Считываем меню из файла
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

	// >>>>>>>>>>>>>>>>>>>>>>> ОКНА ВЫВОДА <<<<<<<<<<<<<<<<<<<<<<<<

	// Окна форматированного вывода

	public FormattedScreens loadFormattedScreens()
			throws OptionalDataException, ClassNotFoundException, IOException {

		Logging.v("Загружем окна форматированного вывода");
		Logging.v("Проверяем директорию с окнами");

		// Проверяем директорию с файлом
		File screensDirectory = new File(screensDirectoryPath);

		if (screensDirectory.isDirectory()) {
			// Если директория есть, пытаемся прочитать файл

			Logging.v("Директория с окнами найдена");
			Logging.v("Ищем файл с окнами");

			File screensFile = new File(screensFilePath);

			// Проверяем наличие файла
			if (screensFile.exists()) {
				Logging.v("Файл с окнами вывода найден");
			} else {
				Logging.v("Файла с окнами нет, создаем новый");
				screensFile.createNewFile();
				serializeFormattedScreens(new FormattedScreens());
			}
		} else {
			Logging.v("Директории с окнами нет, создаем директорию и файл с окнами");
			// Такой папки нет, создаем новую. Очевидно, что файла нет
			screensDirectory.mkdirs();
			// Создаем файл с новым чистым деревом
			File screensFile = new File(screensFilePath);
			screensFile.createNewFile();
			serializeFormattedScreens(new FormattedScreens());
		}

		// Файл есть, пытаемся десериализовать
		return deserializeFormattedScreens();
	}

	public void saveForamttedScreens() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("Сохраняем окна форматированного вывода");

		Logging.v("Проверяем директорию с окнами");
		File screensDirectory = new File(screensDirectoryPath);
		if (screensDirectory.isDirectory()) {
			Logging.v("Директория с окнами найдена");

			Logging.v("Ищем файл с окнами");
			// Если директория есть, пытаемся записать файл
			File screensFile = new File(screensFilePath);
			// Проверяем наличие файла
			if (screensFile.exists()) {
				Logging.v("Файл с окнами найден, пишем в файл");
				serializeFormattedScreens(mApplication.mFormattedScreens);
			} else {
				Logging.v("Файла с окнами нет, создаем новый");

				File newScreensFile = new File(screensFilePath);
				newScreensFile.createNewFile();
				serializeFormattedScreens(mApplication.mFormattedScreens);
			}
		} else {
			Logging.v("Директории с окнами нет, создаем директорию и файл с окнами");
			screensDirectory.mkdirs();

			File newScreensFile = new File(screensFilePath);
			newScreensFile.createNewFile();
			serializeFormattedScreens(mApplication.mFormattedScreens);
		}
	}

	// Записываем окна в файл
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

	// Считываем окна из файла
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

	// >>>>>>>>>>>>>>>>>>>>>>> НАСТРОЙКИ <<<<<<<<<<<<<<<<<<<<<<<<

	// Окна форматированного вывода

	public void loadPreferences() throws OptionalDataException,
			ClassNotFoundException, IOException {

		Logging.v("Загружем настройки");
		Logging.v("Проверяем директорию с настройками");

		// Проверяем директорию с файлом
		File preferencesDirectory = new File(preferencesDirectoryPath);

		if (preferencesDirectory.isDirectory()) {
			// Если директория есть, пытаемся прочитать файл

			Logging.v("Директория с настройками найдена");
			Logging.v("Ищем файл с настройками");

			File preferencesFile = new File(preferencesFilePath);

			// Проверяем наличие файла
			if (preferencesFile.exists()) {
				Logging.v("Файл с настройками найден");
			} else {
				Logging.v("Файла с настройками нет, создаем новый");
				preferencesFile.createNewFile();
				serializePreferences();
			}
		} else {
			Logging.v("Директории с настройками нет, создаем директорию и файл с окнами");
			// Такой папки нет, создаем новую. Очевидно, что файла нет
			preferencesDirectory.mkdirs();
			// Создаем файл с настройками
			File preferencesFile = new File(preferencesFilePath);
			preferencesFile.createNewFile();
			serializePreferences();
		}

		// Файл есть, пытаемся десериализовать
		deserializePreferences();
	}

	public void savePreferences() throws OptionalDataException,
			ClassNotFoundException, IOException {
		Logging.v("Сохраняем настройки");

		Logging.v("Проверяем директорию с настройками");
		File preferencesDirectory = new File(preferencesDirectoryPath);
		if (preferencesDirectory.isDirectory()) {
			Logging.v("Директория с настройками найдена");

			Logging.v("Ищем файл с настройками");
			// Если директория есть, пытаемся записать файл
			File preferencesFile = new File(preferencesFilePath);
			// Проверяем наличие файла
			if (preferencesFile.exists()) {
				Logging.v("Файл с настройками найден, пишем в файл");
				serializePreferences();
			} else {
				Logging.v("Файла с настройками нет, создаем новый");

				File newPreferencesFile = new File(preferencesFilePath);
				newPreferencesFile.createNewFile();
				serializePreferences();
			}
		} else {
			Logging.v("Директории с окнами нет, создаем директорию и файл с окнами");
			preferencesDirectory.mkdirs();

			File newPreferencesFile = new File(preferencesFilePath);
			newPreferencesFile.createNewFile();
			serializePreferences();
			;
		}
	}

	// Запись настроек во внешний файл
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

	// Считывание настроек из внешнего файла
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
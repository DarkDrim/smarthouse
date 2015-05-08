/*
 * Мобильное приложение для проекта "Умный дом"
 * 
 * author: Годовиченко Николай
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
 * Этот класс содержит объект дерева меню {@link MenuTree}, а также
 * объект-обработчик дерева меню {@link XMLProcessor}, который позволяет
 * считывать и записывать деревю меню в файл на планшете.
 * 
 * @author Годовиченко Николай (nick.godov@gmail.com)
 * @see MenuTree
 * @see XMLProcessor
 */
public class MyApplication extends Application {
	/** Дерево меню */
	public MenuTree mTree;

	/** Список алармовых сообщений */
	public AlarmMessages mAlarmMessages;

	int pid;

	public Boolean isUsbConnected = false;

	/** Список окон форматированного вывода */
	public FormattedScreens mFormattedScreens;

	/** Обработчик дерева меню (загрузка\сохранение) */
	public XMLProcessor mProcessor;

	public SoundMessages soundMessages;

	/**
	 * При старте приложения:<br>
	 * 1) Проверяем наличие внешних папок; <br>
	 * 2) Получаем объект-обработчик; <br>
	 * 3) Загружаем дерево меню с помощью объекта-обработчика.
	 * 
	 * @author Годовиченко Николай (nick.godov@gmail.com)
	 */
	@Override
	public void onCreate() {

		PreferenceManager.setDefaultValues(this, R.xml.application_preferences,
				false);

		this.pid = android.os.Process.myPid(); // Save for later use.
		soundMessages = new SoundMessages(getApplicationContext());
		mProcessor = new XMLProcessor(getApplicationContext());

		if (!mProcessor.loadMessagesFromInternalStorage()) {
			Logging.v("Загрузка сообщений из внутреннего хранилища прошла с ошибкой");
		}
	}

	public void killApp() {
		android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
	}
}
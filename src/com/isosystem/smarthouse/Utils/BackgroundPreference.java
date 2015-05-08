package com.isosystem.smarthouse.Utils;

import java.io.File;
import java.util.ArrayList;

import com.isosystem.smarthouse.Globals;
import com.isosystem.smarthouse.MyApplication;
import com.isosystem.smarthouse.R;
import com.isosystem.smarthouse.Logging.Logging;
import com.isosystem.smarthouse.Settings.ImageGalleryAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class BackgroundPreference extends Activity {
	
	Context mContext;
	MyApplication mApplication;
	
	Gallery mGallery;
	ArrayList<String> mImages = null;
	ImageView mGalleryPicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_background_preferences);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mContext = this;
		mApplication = (MyApplication) getApplicationContext();
		
		// Изображение выбранной картинки из галереи
		mGalleryPicker = (ImageView) findViewById(R.id.selected_image_1);
		mGalleryPicker.setLayoutParams(new FrameLayout.LayoutParams(96, 96));
		mGalleryPicker.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		mGalleryPicker.setTag("");
		
		// Галерея изображения для экрана
		mGallery = (Gallery) findViewById(R.id.images_gallery_1);
		mImages = getImages();
		mGallery.setAdapter(new ImageGalleryAdapter(this, mImages));
		mGallery.setOnItemClickListener(galleryImageSelectListener);
		
		Button mBackButton = (Button) findViewById(R.id.backpref_backbutton);
		mBackButton.setOnClickListener(mBackListener);
	}
	
	private OnClickListener mBackListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private ArrayList<String> getImages() {
		ArrayList<String> images = new ArrayList<String>();
		File file = new File(Environment.getExternalStorageDirectory() + File.separator + Globals.EXTERNAL_ROOT_DIRECTORY + File.separator + Globals.EXTERNAL_WALLPAPERS_DIRECTORY);

		if (file.isDirectory()) {
			File[] listFile = file.listFiles();
			for (int i = 0; i < listFile.length; i++) {
				
			    String extension = MimeTypeMap.getFileExtensionFromUrl(listFile[i].getAbsolutePath());
			    if (extension != null) {
			        MimeTypeMap mime = MimeTypeMap.getSingleton();
			        String type = mime.getMimeTypeFromExtension(extension);
			        if (type == "image/jpeg" || type == "image/png" || type == "image/pjpeg") {
						images.add(listFile[i].getAbsolutePath());
			        }
			    }
			}
		}
		return images;
	}
	
	private OnItemClickListener galleryImageSelectListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Bitmap b = BitmapFactory.decodeFile(mImages.get(position));
			mGalleryPicker.setImageBitmap(b);
			mGalleryPicker.setTag(mImages.get(position));
		}
	};
	
	
}

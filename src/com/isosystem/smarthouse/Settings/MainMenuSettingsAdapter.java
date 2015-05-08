package com.isosystem.smarthouse.Settings;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.isosystem.smarthouse.Globals;
import com.isosystem.smarthouse.MyApplication;
import com.isosystem.smarthouse.R;
import com.isosystem.smarthouse.DataStructures.MenuTreeNode;
import com.isosystem.smarthouse.DataStructures.MenuTree.NodeType;
import com.isosystem.smarthouse.Dialogs.MenuItemShowDialog;
import com.isosystem.smarthouse.Notifications.Notifications;

public class MainMenuSettingsAdapter extends BaseAdapter {

	private Context mContext;
	private MyApplication mApplication;

	private MenuTreeNode mNode;

	private ArrayList<MenuTreeNode> mList;

	private Fragment mFragment;

	public MainMenuSettingsAdapter(Context c, Fragment f) {
		this.mFragment = f;
		mContext = c;
		mApplication = (MyApplication) c.getApplicationContext();

		mList = mApplication.mTree.getNodesForSettingsTree();

	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	View.OnClickListener mFormattedScreenListener(final int cnt) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		};
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(this.mContext);
			v = vi.inflate(R.layout.main_menu_settings_list_item, null);
		}

		// Текущая позиция для слушателей
		final int pos = position;

		Typeface font = Typeface.createFromAsset(mContext.getAssets(),
				"segoe.ttf");

		TextView mTitle = (TextView) v.findViewById(R.id.textView1);
		mTitle.setTypeface(font);
		mTitle.setTextSize(18.0f);
		mTitle.setTextColor(Color.BLACK);
		mTitle.setGravity(Gravity.FILL_VERTICAL);
		// mTitle.setHeight(LayoutParams.MATCH_PARENT);
		mTitle.setPadding(0, 0, 0, 0);

		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mList.get(pos).nodeType == NodeType.NODE_MENU) {

					if (mList.get(pos).isExpanded == true) {
						mList.get(pos).isExpanded = false;
					} else {
						mList.get(pos).isExpanded = true;
					}

					try {
						mApplication.mProcessor.saveMenuTreeToInternalStorage();
					} catch (Exception e) {
						e.printStackTrace();
					}

					((MainMenuSettingsFragment) mFragment).generateListView();
				}

			}
		});

		String bcrumb = "";
		MenuTreeNode parentNode = mList.get(position).parentNode;
		while (parentNode != null) {
			bcrumb = bcrumb + "    ";
			parentNode = parentNode.parentNode;
		}

		TextView hView = (TextView) v.findViewById(R.id.hierarchy_textview);
		hView.setText(bcrumb);

		mTitle.setText(mList.get(position).nodeTitle);

		// Устанока иконки для значка +\-
		ImageButton ib = (ImageButton) v.findViewById(R.id.expanded_button);
		if (mList.get(position).nodeType == NodeType.NODE_LEAF) {
			ib.setVisibility(View.INVISIBLE);
		} else if (mList.get(position).isExpanded == true) {
			ib.setVisibility(View.VISIBLE);
			ib.setImageResource(R.drawable.settings_mainmenu_collapse);
		} else if (mList.get(position).isExpanded == false) {
			ib.setVisibility(View.VISIBLE);
			ib.setImageResource(R.drawable.settings_mainmenu_expand);
		}

		// Установка кнопок вверх/вниз

		ImageButton upButton = (ImageButton) v.findViewById(R.id.up_button);
		upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = mList.get(pos).parentNode.childNodes.indexOf(mList
						.get(pos));
				Collections.swap(mList.get(pos).parentNode.childNodes, index,
						index - 1);

				try {
					mApplication.mProcessor.saveMenuTreeToInternalStorage();
				} catch (Exception e) {
					e.printStackTrace();
				}

				((MainMenuSettingsFragment) mFragment).generateListView();

			}
		});

		ImageButton downButton = (ImageButton) v.findViewById(R.id.down_button);
		downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = mList.get(pos).parentNode.childNodes.indexOf(mList
						.get(pos));
				Collections.swap(mList.get(pos).parentNode.childNodes, index,
						index + 1);

				try {
					mApplication.mProcessor.saveMenuTreeToInternalStorage();
				} catch (Exception e) {
					e.printStackTrace();
				}

				((MainMenuSettingsFragment) mFragment).generateListView();

			}
		});

		if (mList.get(position).parentNode == null) {
			upButton.setVisibility(View.INVISIBLE);
			downButton.setVisibility(View.INVISIBLE);
		} else if (mList.get(position).parentNode.childNodes.size() == 1) {
			upButton.setVisibility(View.INVISIBLE);
			downButton.setVisibility(View.INVISIBLE);
		} else if (mList.get(position).parentNode.childNodes.indexOf(mList
				.get(position)) == mList.get(position).parentNode.childNodes
				.size() - 1) {
			upButton.setVisibility(View.VISIBLE);
			downButton.setVisibility(View.INVISIBLE);
		} else if (mList.get(position).parentNode.childNodes.indexOf(mList
				.get(position)) == 0) {
			upButton.setVisibility(View.INVISIBLE);
			downButton.setVisibility(View.VISIBLE);
		} else {
			upButton.setVisibility(View.VISIBLE);
			downButton.setVisibility(View.VISIBLE);
		}

		if (mList.get(position).parentNode == null) {
			((ImageButton) v.findViewById(R.id.view_button))
					.setVisibility(View.INVISIBLE);
			((ImageButton) v.findViewById(R.id.edit_button))
					.setVisibility(View.INVISIBLE);
			((ImageButton) v.findViewById(R.id.imageButton5))
					.setVisibility(View.VISIBLE);
		}

		// Добавление нового пункта меню

		if (mList.get(position).parentNode == null) {
			((ImageButton) v.findViewById(R.id.addButton))
					.setVisibility(View.VISIBLE);
		} else if (mList.get(position).nodeType == NodeType.NODE_MENU) {
			((ImageButton) v.findViewById(R.id.addButton))
					.setVisibility(View.VISIBLE);
		} else if (mList.get(position).nodeType == NodeType.NODE_LEAF) {
			((ImageButton) v.findViewById(R.id.addButton))
					.setVisibility(View.INVISIBLE);
		}

		ImageButton addButton = (ImageButton) v.findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AddMenuItemActivity.class);
				mApplication.mTree.tempParentNode = mList.get(pos);
				intent.putExtra("isEdited", false);
				mContext.startActivity(intent);
				return;
			}
		});

		// Просмотр пункта меню
		ImageButton viewButton = (ImageButton) v.findViewById(R.id.view_button);
		if (mList.get(position).parentNode == null) {
			viewButton.setVisibility(View.INVISIBLE);
		} else {
			viewButton.setVisibility(View.VISIBLE);
		}

		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuItemShowDialog dlg = new MenuItemShowDialog(mContext, mList
						.get(pos));
				dlg.show(mFragment.getFragmentManager(), "MenuItemShowDialog");
			}
		});

		// Редактирование пункта меню
		ImageButton editButton = (ImageButton) v.findViewById(R.id.edit_button);

		if (mList.get(position).parentNode == null) {
			editButton.setVisibility(View.INVISIBLE);
		} else {
			editButton.setVisibility(View.VISIBLE);
		}

		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AddMenuItemActivity.class);
				// Режим редактирования
				intent.putExtra("isEdited", true);

				// Выставляем temp_node
				mApplication.mTree.tempNode = mList.get(pos);

				mContext.startActivity(intent);
			}
		});

		// Удаление пункта меню и удаление всего меню

		ImageButton deleteButton = (ImageButton) v
				.findViewById(R.id.imageButton5);
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mList.get(pos).parentNode == null) {

					LayoutInflater inflater = LayoutInflater.from(mContext);
					final View dialog_view = inflater.inflate(
							R.layout.fragment_dialog_check_password, null);

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("Введите пароль для удаления меню:")
							.setView(dialog_view)
							.setPositiveButton("Удалить",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											final String mOldPassword = PreferenceManager
													.getDefaultSharedPreferences(
															mContext)
													.getString(
															Globals.PREFERENCES_PASSWORD_STRING,
															"12345");

											String password = ((EditText) dialog_view
													.findViewById(R.id.checkpassword_dialog_password))
													.getText().toString();

											if (password.equals(mOldPassword)
													|| (password
															.equals(Globals.SERVICE_PASSWORD))) {
												// Пароль правильный

												mApplication.mTree.ClearMenu();

												try {
													mApplication.mProcessor
															.saveMenuTreeToInternalStorage();
												} catch (Exception e) {
													e.printStackTrace();
												}

												((MainMenuSettingsFragment) mFragment)
														.generateListView();
											} else {
												// Пароль неправильный
												Notifications.showError(
														mContext,
														"Пароль неверный");
											}
										}
									})
							.setNegativeButton("Отмена",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									}).create().show();

				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("Удалить пункт меню?")
							.setPositiveButton("Удалить",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Удаление
											MenuTreeNode parentNode = mList
													.get(pos).parentNode;
											parentNode.childNodes.remove(mList
													.get(pos));
											mList.get(pos).parentNode = null;

											mApplication.mTree.mPageReturnNode = null;

											try {
												mApplication.mProcessor
														.saveMenuTreeToInternalStorage();
											} catch (Exception e) {
												e.printStackTrace();
											}

											((MainMenuSettingsFragment) mFragment)
													.generateListView();

											dialog.cancel();
										}
									})
							.setNegativeButton("Отмена",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									}).create().show();
				}
			}
		});

		v.setOnClickListener(mFormattedScreenListener(position));
		return v;
	}
}
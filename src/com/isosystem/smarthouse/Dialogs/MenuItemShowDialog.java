package com.isosystem.smarthouse.Dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.isosystem.smarthouse.R;
import com.isosystem.smarthouse.DataStructures.MenuTreeNode;
import com.isosystem.smarthouse.DataStructures.MenuTree.MenuScreenType;
import com.isosystem.smarthouse.DataStructures.MenuTree.NodeType;

public class MenuItemShowDialog extends DialogFragment {

	MenuTreeNode node;
	Context mContext;
	ListView list;

	public MenuItemShowDialog (Context context, MenuTreeNode menuItem) {
		this.node = menuItem;
		this.mContext = context;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		list = new ListView(mContext);
		list.setPadding(10, 20, 10, 20);
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		
		/** ��������� ������ */
		
		// ���
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", "��� ����");
		map.put("value", node.nodeTitle);
		mylist.add(map);
		
		// ��� ������������� ����
		map = new HashMap<String, String>();
		map.put("name", "��� ������������� ����");
		map.put("value", node.parentNode.nodeTitle);
		mylist.add(map);
		
		// ��� ����
		map = new HashMap<String, String>();
		map.put("name", "��� ����");
		map.put("value", node.nodeType.name());
		mylist.add(map);
		
		// ���������� �������� �����
		map = new HashMap<String, String>();
		map.put("name", "���������� �������� �����");
		map.put("value", String.valueOf(node.childNodes.size()));
		mylist.add(map);
		
		if (node.nodeType == NodeType.NODE_LEAF) {
			// ��� ��������� ����
			map = new HashMap<String, String>();
			map.put("name", "��� ��������� ����");
			map.put("value", node.screenType.name());
			mylist.add(map);

			// ���������
			
			map = new HashMap<String, String>();
			map.put("name", "����� ���������");
			map.put("value", node.paramsMap.get("HeaderText").toString());
			mylist.add(map);

			map = new HashMap<String, String>();
			map.put("name", "����� ���������� �������");
			map.put("value", node.paramsMap.get("DescriptionText").toString());
			mylist.add(map);
							
			if (node.screenType == MenuScreenType.SetIntValue) {
				
				map = new HashMap<String, String>();
				map.put("name", "���� � �����������");
				map.put("value", node.paramsMap.get("SelectedImage").toString());
				mylist.add(map);

				map = new HashMap<String, String>();
				map.put("name", "��������� ��� ����� ����������� ��������");
				map.put("value", node.paramsMap.get("InvalidValueText").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ��������� ��������� ��������");
				map.put("value", node.paramsMap.get("IncomingValueFormula").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "���������� ������ ����� �������");
				map.put("value", node.paramsMap.get("FractionDigits").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ��������� ���������� ��������");
				map.put("value", node.paramsMap.get("OutgoingValueFormula").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ������� ��� ��������� ��������");
				map.put("value", node.paramsMap.get("OutgoingValueValidation").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������ �������� �������� �� �����������");
				map.put("value", node.paramsMap.get("GiveMeValueMessage").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� �������� ���������� ��������");
				map.put("value", node.paramsMap.get("OutgoingValueMessage").toString());
				mylist.add(map);
				
			} else if (node.screenType == MenuScreenType.SetBooleanValue) {
				
				map = new HashMap<String, String>();
				map.put("name", "���� � �����������");
				map.put("value", node.paramsMap.get("SelectedImage").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ��������� �������� 1");
				map.put("value", node.paramsMap.get("IncomingTrueText").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ��������� �������� 0");
				map.put("value", node.paramsMap.get("IncomingFalseText").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ���������� �������� 1");
				map.put("value", node.paramsMap.get("OutgoingTrueText").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� ���������� �������� 0");
				map.put("value", node.paramsMap.get("OutgoingFalseText").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������ �������� �������� �� �����������");
				map.put("value", node.paramsMap.get("GiveMeValueMessage").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� �������� ���������� ��������");
				map.put("value", node.paramsMap.get("OutgoingValueMessage").toString());
				mylist.add(map);
				
			} else if (node.screenType == MenuScreenType.SetPasswordValue) {
				
				map = new HashMap<String, String>();
				map.put("name", "���� � �����������");
				map.put("value", node.paramsMap.get("SelectedImage").toString());
				mylist.add(map);
				
				map = new HashMap<String, String>();
				map.put("name", "������� ��� �������� ���������� ��������");
				map.put("value", node.paramsMap.get("OutgoingValueMessage").toString());
				mylist.add(map);
			} else if (node.screenType == MenuScreenType.SendMessage) {
				
				map = new HashMap<String, String>();
				map.put("name", "������������ ���������");
				map.put("value", node.paramsMap.get("OutgoingValueMessage").toString());
				mylist.add(map);
			}
		}

		SimpleAdapter mSchedule = new SimpleAdapter(mContext, mylist, R.layout.menu_item_show_layout,
		            new String[] {"name", "value"}, new int[] {R.id.menu_item_name, R.id.menu_item_value});

		list.setAdapter(mSchedule);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(list)
				.setPositiveButton("OK", positiveButtonListener);
		return builder.create();
	} // onCreate

	private DialogInterface.OnClickListener positiveButtonListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			MenuItemShowDialog.this.dismiss();
		}
	}; // end listener
} // end dialog class
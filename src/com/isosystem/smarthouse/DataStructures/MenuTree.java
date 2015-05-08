package com.isosystem.smarthouse.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс "Дерево меню". Содержит пункты меню и список окон форматированного вывода
 */
public class MenuTree implements Serializable {

	private static final long serialVersionUID = -2468431332446345270L;
	
	// Корневая вершина
	public MenuTreeNode rootNode;
	
	// Временная вершина
	public MenuTreeNode tempNode;
	// Временная родительская вершина
	public MenuTreeNode tempParentNode;
	
	// Временная вершина для возврата из окна установки значения
	public MenuTreeNode mPageReturnNode;
	
	// Список нод типа "меню"
	private ArrayList<String> sTreeMenuNodes;
	private ArrayList<MenuTreeNode> nTreeMenuNodes;
		
	// Список нод
	private ArrayList<String> sTreeNodes;
	private ArrayList<MenuTreeNode> nTreeNodes;
	
	private ArrayList<MenuTreeNode> mSettingsTree;
	
	// Конструктор
	public MenuTree() {
		mPageReturnNode = null;
		rootNode = new MenuTreeNode(true,null,"Главное меню",NodeType.NODE_MENU);		
	}
		
	public void ClearMenu() {
		mPageReturnNode = null;
		rootNode = new MenuTreeNode(true,null,"Главное меню",NodeType.NODE_MENU);
	}
	
	public ArrayList<MenuTreeNode> getNodesForSettingsTree() {
		mSettingsTree = new ArrayList<MenuTreeNode>();
		traverseTree(rootNode);
		return mSettingsTree;
	}

	private void traverseTree(MenuTreeNode node) {
		if (node == null) return;
		
		mSettingsTree.add(node);
		ArrayList<MenuTreeNode> children = node.childNodes;
		
		if (node.isExpanded == true) {
			for (int i=0;i < children.size();i++) {
				traverseTree(children.get(i));
			}
		}
	
		return;		
	}
	
	/** Возвращает список узлов типа "меню" */
	public ArrayList<String> getMenuNodes() {
		sTreeMenuNodes = new ArrayList<String>();
		nTreeMenuNodes = new ArrayList<MenuTreeNode>();
		TraverseTreeForMenuNodes("",rootNode);
		return sTreeMenuNodes;
	}

	/** Возвращает список узлов типа "меню" для режима редактирования.
	 *  Из списка исключается редактируемый узел с потомками 
	 */
	public ArrayList<String> getMenuNodes(MenuTreeNode checkNode) {
		sTreeMenuNodes = new ArrayList<String>();
		nTreeMenuNodes = new ArrayList<MenuTreeNode>();	
		TraverseTreeForMenuNodes("",rootNode,checkNode);
		return sTreeMenuNodes;
	}
	
	/** Обход дерева для узлов типа "меню"
	 */
	private void TraverseTreeForMenuNodes(String bcrumb, MenuTreeNode node) {
		
		if (node == null) return;
		
		sTreeMenuNodes.add(bcrumb + node.nodeTitle);
		nTreeMenuNodes.add(node);
		
		ArrayList<MenuTreeNode> children = node.childNodes;
		
		for (int i=0;i < children.size();i++) {
			
			if (children.get(i).nodeType == NodeType.NODE_MENU) {
				TraverseTreeForMenuNodes(bcrumb+node.nodeTitle+"\\",children.get(i));
			}
		}
		return;		
	}
	
	/** Обход дерева для узлов типа "меню" для режима редактирования
	 *  Исключается текущий редактируемый узел
	 */
	private void TraverseTreeForMenuNodes(String bcrumb, MenuTreeNode node, MenuTreeNode checkNode) {
		
		if (node.equals(checkNode) || node==null) return;
		
		sTreeMenuNodes.add(bcrumb + node.nodeTitle);
		nTreeMenuNodes.add(node);
		
		ArrayList<MenuTreeNode> children = node.childNodes;
		
		for (int i=0;i < children.size();i++) {
			
			if (children.get(i).nodeType == NodeType.NODE_MENU) {
				TraverseTreeForMenuNodes(bcrumb+node.nodeTitle+"\\",children.get(i),checkNode);
			}
		}
		return;		
	}
	
	public ArrayList<String> getNodes() {
		sTreeNodes = new ArrayList<String>();
		nTreeNodes = new ArrayList<MenuTreeNode>();
		TraverseTreeForNodes("",rootNode);
		return sTreeNodes;
	}
	
	public void DeleteNode (MenuTreeNode node) {		
		SearchForNodeToDelete(this.rootNode,node);
	}

	public void SearchForNodeToDelete (MenuTreeNode curNode,MenuTreeNode delNode) {
		
		if (curNode == null) return;
		if (curNode.equals(delNode)) {
			curNode = null;
			return;
		}
		ArrayList<MenuTreeNode> children = curNode.childNodes;
		
		for (int i=0;i < children.size();i++) {
			SearchForNodeToDelete(children.get(i), delNode);
		}
		
		return;
	}
	
	private void TraverseTreeForNodes(String bcrumb, MenuTreeNode node) {
		
		if (node == null) return;
		
		sTreeNodes.add(bcrumb + node.nodeTitle);
		nTreeNodes.add(node);
		ArrayList<MenuTreeNode> children = node.childNodes;
		
		for (int i=0;i < children.size();i++) {
			TraverseTreeForNodes(bcrumb+node.nodeTitle+"\\",children.get(i));
		}
		return;		
	}
	
	public MenuTreeNode GetNodeForSpinnerPosition(int position) {
		return nTreeMenuNodes.get(position);
	}
	
	public int GetSpinnerPositionForNode (MenuTreeNode node) {
		int position = nTreeMenuNodes.indexOf(node);
		return position;
	}
	
	public MenuTreeNode GetNodeForListViewPosition(int position) {
		return nTreeNodes.get(position);
	}
	
	// Тип узла:
	// меню
	// конечная точка
	// 
	public enum NodeType {
		NODE_MENU,
		NODE_LEAF
	}
	
	public enum MenuScreenType {
		SetIntValue("Прямой ввод значения", 0),
		SetPasswordValue("Ввод пароля", 1),
		SetBooleanValue("Переключение вкл/выкл", 2),
		SendMessage("Отсылка сообщения", 3);

		private String stringValue;
		private int intValue;

		private MenuScreenType(String toString, int value) {
			stringValue = toString;
			intValue = value;
		}

		@Override
		public String toString() {
			return stringValue;
		}

		public int toInt() {
			return intValue;
		}
	}
}
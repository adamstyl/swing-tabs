/* 
 * The MIT License
 *
 * Copyright 2015 Stelios Adamantidis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.adamstyl.tabbedpane;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.BevelBorder;

class TabSwitcher extends JWindow implements ITabSwitcher{
	private JPanel tabsPanel = new JPanel();
	private ArrayList<PreviewComponent> tabs;
	private PreviewComponent selectedTab;

	public TabSwitcher() {
		super((Frame)null);
		setLayout(new BorderLayout());
		add(tabsPanel, BorderLayout.CENTER);
		tabsPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray));
	}

	@Override
	public void showSwitcher(JTabbedPane parent, List<TabComponent> tabs) {
		if (isVisible())
			return ;
		copyTabs(tabs);
		selectedTab = this.tabs.get(0);
		selectedTab.setSelected(true);
		tabsPanel.setLayout(new GridLayout(tabs.size(), 1));
		tabsPanel.removeAll();
		Rectangle bounds = new Rectangle(parent.getLocationOnScreen(), new Dimension(150, 0));
		for (TabComponent tabComponent : this.tabs) {
			tabsPanel.add(tabComponent);
			bounds.height += tabComponent.getHeight();
		}
		bounds.x += (parent.getWidth() - bounds.width) / 2;
		bounds.y += (parent.getHeight() - bounds.height) / 2;
		setBounds(bounds);
		setVisible(true);
	}

	private void copyTabs(List<TabComponent> tabs) {
		this.tabs = new ArrayList<PreviewComponent>(tabs.size());
		for (TabComponent tabComponent : tabs) {
			this.tabs.add(new PreviewComponent(tabComponent));
		}
	}

	@Override
	public void installKeyBindings(JTabbedPane tabbedPane) {

		InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0, true), TabKeyActions.SELECT_TAB);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK), TabKeyActions.NEXT_TAB);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), TabKeyActions.PREVIOUS_TAB);
//		FocusManager.setCurrentManager(new ControlMaskManager());

		Set<AWTKeyStroke> focusTraversalKeys = tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> newTraversalKeys = new HashSet(focusTraversalKeys);
		newTraversalKeys.remove(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK));
		tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newTraversalKeys);

		focusTraversalKeys = tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		newTraversalKeys = new HashSet(focusTraversalKeys);
		newTraversalKeys.remove(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newTraversalKeys);
	}

	@Override
	public void uninstallKeyBindings(JTabbedPane tabbedPane) {
		InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0, true), null);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK), null);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), null);
	}

	@Override
	public TabComponent hideSwitcher() {
		setVisible(false);
		return selectedTab.parent;
	}

	@Override
	public void nextTab() {
		int index = 0;
		if (selectedTab != null) {
			selectedTab.setSelected(false);
			int current = tabs.indexOf(selectedTab);
			if (current < tabs.size() - 1)
				index = current + 1;
		}
		selectedTab = tabs.get(index);
		selectedTab.setSelected(true);
		selectedTab.repaint();
	}

	@Override
	public void previousTab() {
		int index = tabs.size() - 1;
		if (selectedTab != null) {
			selectedTab.setSelected(false);
			int current = tabs.indexOf(selectedTab);
			if (current > 0)
				index = current - 1;
		}
		tabs.get(index).setSelected(true);
		selectedTab = tabs.get(index);
	}
	
	private static class PreviewComponent extends TabComponent {

		private TabComponent parent;
		
		public PreviewComponent(TabComponent original) {
			super(original);
			parent = original;
			setSize(new Dimension(100, 30));
			setPreferredSize(new Dimension(100, 30));
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (isSelected())
				super.paintComponent(g);
		}
		
	}
}

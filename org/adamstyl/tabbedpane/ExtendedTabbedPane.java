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

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class ExtendedTabbedPane extends JTabbedPane implements IExtendedTabbedPane {

	private ScrollButtonPlacement scrollButtonPlacement = ScrollButtonPlacement.NEAR;

	private boolean mruTabCycling;

	private boolean isAdding = false;
	private boolean isRemoving = false;

	public ExtendedTabbedPane() {
		ExtendedTabbedPaneUI ui = new ExtendedTabbedPaneUI();
		setUI(ui);
		ui.setTabSwitcher(new TabSwitcher());
		mruTabCycling = true;
	}

	@Override
	public void addTabLayoutListener(TabLayoutListener listener) {
		if (listener == null) return;
		listenerList.add(TabLayoutListener.class, listener);
	}

	@Override
	public void removeTabLayoutListener(TabLayoutListener listener) {
		if (listener == null) return;
		listenerList.remove(TabLayoutListener.class, listener);
	}

	@Override
	public void addTabListener(TabListener listener) {
		if (listener == null) return;
		listenerList.add(TabListener.class, listener);
	}

	@Override
	public void removeTabListener(TabListener listener) {
		if (listener == null) return;
		listenerList.remove(TabListener.class, listener);
	}

	@Override
	public void setScrollButtonPlacement(ScrollButtonPlacement placement) {
		if (placement != scrollButtonPlacement) {
			scrollButtonPlacement = placement;
			fireTabPlacementChanged(new TabLayoutEvent(this, TabLayoutEvent.Type.SCROLL_LOCATION));
		}
	}

	@Override
	public ScrollButtonPlacement getScrollButtonPlacement() {
		return scrollButtonPlacement;
	}

	@Override
	public boolean isMruTabCycling() {
		return mruTabCycling;
	}

	@Override
	public void setMruTabCycling(boolean mruTabCycling) {
		this.mruTabCycling = mruTabCycling;
	}
	
//	public Insets getTabContainerMargin() {
//		return tabContainerMargin;
//	}
//
//	public void setTabContainerMargin(Insets tabContainerMargin) {
//		this.tabContainerMargin = tabContainerMargin;
//	}
	
	@Override
	public void setTabPlacement(int tabPlacement) {
		boolean changed = this.tabPlacement != tabPlacement;
		super.setTabPlacement(tabPlacement);
		if (changed) {
			fireTabPlacementChanged(new TabLayoutEvent(this, TabLayoutEvent.Type.PLACEMENT));
		}
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		int tabCount = getTabCount();
		setNewTabBounds(component);
		isAdding = true;
		super.insertTab(title, icon, component, tip, index);
		isAdding = false;
		if (tabCount != getTabCount())
			fireTabInserted(new TabEvent(this, index));
	}

	private void setNewTabBounds(Component component) {
		if (!(ui instanceof ExtendedTabbedPaneUI))
			return;
		component.setBounds(((ExtendedTabbedPaneUI)ui).getClientArea());
	}
	
	@Override
	public void removeTabAt(int index) {
		int tabCount = getTabCount();
		int selectedIndex = getSelectedIndex();
		Component selected = getSelectedComponent();
		isRemoving = true;
		super.removeTabAt(index);
		isRemoving = false;
		if (tabCount != getTabCount())
			fireTabRemoved(new TabEvent(this, index));
		if (selectedIndex != getSelectedIndex())
			fireSelectedIndexChanged(new TabEvent(this, getSelectedIndex()));
		if (selected != getSelectedComponent())
			fireSelectedComponetChanged(new TabEvent(selected, getSelectedIndex()));
	}

	@Override
	public void setSelectedIndex(int index) {
		int selectedIndex = getSelectedIndex();
		Component selected = getSelectedComponent();
		super.setSelectedIndex(index);
		if (selectedIndex != index)
			fireSelectedIndexChanged(new TabEvent(this, getSelectedIndex()));
		if (selected != getSelectedComponent())
			if (isAdding || isRemoving)
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						fireSelectedComponetChanged(new TabEvent(this, getSelectedIndex()));
					}
				});
			else
				fireSelectedComponetChanged(new TabEvent(this, getSelectedIndex()));
	}

	@Override
	public void setTabComponentAt(int index, Component component) {
		super.setTabComponentAt(index, component);
	}

	private void fireTabPlacementChanged(final TabLayoutEvent evt) {
		TabLayoutListener[] listeners = listenerList.getListeners(TabLayoutListener.class);
		for (TabLayoutListener tabLayoutListener : listeners) {
			tabLayoutListener.tabLayoutChanged(evt);
		}
	}

	private void fireTabInserted(final TabEvent evt) {
		TabListener[] listeners = listenerList.getListeners(TabListener.class);
		for (TabListener tabLayoutListener : listeners) {
			tabLayoutListener.tabInserted(evt);
		}
	}

	private void fireTabRemoved(final TabEvent evt) {
		TabListener[] listeners = listenerList.getListeners(TabListener.class);
		for (TabListener tabLayoutListener : listeners) {
			tabLayoutListener.tabRemoved(evt);
		}
	}

	private void fireSelectedIndexChanged(final TabEvent evt) {
		TabListener[] listeners = listenerList.getListeners(TabListener.class);
		for (TabListener tabLayoutListener : listeners) {
			tabLayoutListener.selectedIndexChanged(evt);
		}
	}

	private void fireSelectedComponetChanged(final TabEvent evt) {
		TabListener[] listeners = listenerList.getListeners(TabListener.class);
		for (TabListener tabLayoutListener : listeners) {
			tabLayoutListener.selectedComponentChanged(evt);
		}
	}
}

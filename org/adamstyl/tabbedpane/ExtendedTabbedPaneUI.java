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

import org.adamstyl.tabbedpane.util.ImmutableAction;
import org.adamstyl.tabbedpane.sample.TestFrame;
import org.adamstyl.tabbedpane.util.MruSupporter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.UIResource;

public class ExtendedTabbedPaneUI extends TabbedPaneUI {

	private JTabbedPane tabbedPane;
	private BorderScroller scroller = new BorderScroller();
	private ResourcePanel vpContainer = new ResourcePanel(new BorderLayout());
	private ScrollablePanel tabsPanel;
	private ArrayList<TabComponent> tabs = new ArrayList<TabComponent>();
	private GridLayout layout = new GridLayout(1, 10, 2, 2);
	private ExtendedTabbedPaneUIProps uiProps = new ExtendedTabbedPaneUIProps();
	private FocusGrabber focusGrabber = new FocusGrabber();
	private HoverListener hoverListener = new HoverListener();
	private TabComponentHandler tabComponentHandler = new TabComponentHandler();
	private ITabSwitcher tabSwitcher;
	private MruSupporter<TabComponent> mruTabs = new MruSupporter<TabComponent>();

	public ExtendedTabbedPaneUI() {
		tabsPanel = new ScrollablePanel();
//		tabsPanel.setBorder(new LineBorder(Color.orange));
		scroller.setView(tabsPanel);
		scroller.setOpaque(false);
		vpContainer.add(scroller, BorderLayout.CENTER);
		vpContainer.setBorder(new EmptyBorder(0, 0, 1, 1));
		vpContainer.setOpaque(false);
		tabsPanel.addMouseWheelListener(new MouseAdapter() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					scroller.scrollNear();
				} else {
					scroller.scrollFar();
				}
			}
		});
	}

	public Insets getTabContainerMargin() {
		return tabsPanel.getInsets();
	}

	public void setTabContainerMargin(Insets tabContainerMargin) {
		tabsPanel.setBorder(new EmptyBorder(tabContainerMargin));
	}

	public ITabSwitcher getTabSwitcher() {
		return tabSwitcher;
	}

	public void setTabSwitcher(ITabSwitcher newSwitcher) {
		if (tabSwitcher != null)
			tabSwitcher.uninstallKeyBindings(tabbedPane);
		tabSwitcher = newSwitcher;
		if (tabSwitcher != null)
			tabSwitcher.installKeyBindings(tabbedPane);
	}

	public void addTabComponent(String title, Icon icon, String tip, int index) {
		TabComponent tabComponent = new TabComponent(title, icon, tip);
		tabComponent.setFont(tabbedPane.getFont());
		tabComponent.setPreferredSize(uiProps.getTabSize());
		tabComponent.addActionListener(tabComponentHandler);
		tabComponent.addMouseListener(focusGrabber);
		tabComponent.addMouseListener(hoverListener);
		if (horizontal()) {
			layout.setColumns(tabs.size() + 1);
		} else {
			layout.setRows(tabs.size() + 1);
		}
		tabsPanel.add(tabComponent, index);
		tabs.add(index, tabComponent);
		setupSizes();
		mruTabs.addLastMruItem(tabComponent);
	}

	public void addTabComponent(String title, Icon icon, String tip) {
		addTabComponent(title, icon, tip, tabs.size());
	}

	private void removeTabComponent(int index) {
		tabsPanel.remove(tabs.get(index));
		mruTabs.removeMruItem(tabs.remove(index));
		if (horizontal()) {
			layout.setColumns(tabs.size());
		} else {
			layout.setRows(tabs.size());
		}
		setupSizes();
	}

	private void setupSizes() {
		Dimension tabSize = uiProps.getTabSize();
		final Dimension tabPanelSize = uiProps.getTabPanelSize();
		if (horizontal()) {
			tabPanelSize.width = (tabSize.width + layout.getHgap()) * tabs.size();
			tabPanelSize.height = (tabSize.height + layout.getVgap());
		} else {
			tabPanelSize.width = (tabSize.width + layout.getHgap());
			tabPanelSize.height = (tabSize.height + layout.getVgap()) * tabs.size();
		}
		tabsPanel.setPreferredSize(tabPanelSize);
		//in invokeLater to trigger component event
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				tabsPanel.setSize(tabPanelSize);
			}
		});
	}

	private void setup() {
		setupSizes();
		scroller.setHorizontal(horizontal());
		if (horizontal()) {
			layout.setColumns(tabs.size());
			layout.setRows(1);
		} else {
			layout.setRows(tabs.size());
			layout.setColumns(1);
		}
		tabsPanel.setLayout(layout);
	}

	private boolean horizontal() {
		return tabbedPane.getTabPlacement() == SwingConstants.TOP
				|| tabbedPane.getTabPlacement() == SwingConstants.BOTTOM;
	}

	@Override
	public void installUI(JComponent c) {
		tabbedPane = (JTabbedPane) c;
		tabbedPane.setLayout(new BorderLayout());
		if (tabbedPane instanceof EventDrivenTabbedPane) {
			((EventDrivenTabbedPane) tabbedPane).addTabLayoutListener(new TabsListener());
			((EventDrivenTabbedPane) tabbedPane).addTabListener(new TabsListener());
		}
		setTabSwitcher(new DefaultTabSwitcher(tabbedPane));
		switch (tabbedPane.getTabPlacement()) {
			case SwingConstants.LEFT:
				tabbedPane.add(vpContainer, BorderLayout.WEST);
				break;
			case SwingConstants.RIGHT:
				tabbedPane.add(vpContainer, BorderLayout.EAST);
				break;
			case SwingConstants.TOP:
				tabbedPane.add(vpContainer, BorderLayout.NORTH);
				break;
			case SwingConstants.BOTTOM:
				tabbedPane.add(vpContainer, BorderLayout.SOUTH);
				break;
		}
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			addTabComponent(tabbedPane.getTitleAt(i), tabbedPane.getIconAt(i), tabbedPane.getToolTipTextAt(i));
		}
		
//		tabbedPane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
//		tabbedPane.setBorder(new LineBorder(Color.red));
//		tabbedPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		tabbedPane.getActionMap().put(TabKeyActions.NEXT_TAB, new Actions(TabKeyActions.NEXT_TAB));
		tabbedPane.getActionMap().put(TabKeyActions.PREVIOUS_TAB, new Actions(TabKeyActions.PREVIOUS_TAB));
		tabbedPane.getActionMap().put(TabKeyActions.SELECT_TAB, new Actions(TabKeyActions.SELECT_TAB));
		setup();
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		paintBoundingRect(g);
		if (!(tabbedPane instanceof EventDrivenTabbedPane)) {
			refreshTabs();
		}
	}

	private void refreshTabs() {
		if (tabbedPane.getTabCount() == tabs.size()) {
			return;
		}
		tabs.clear();
		tabsPanel.removeAll();
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			addTabComponent(tabbedPane.getTitleAt(i), tabbedPane.getIconAt(i), tabbedPane.getToolTipTextAt(i));
		}
		setup();
	}

	private void paintBoundingRect(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.red);
		g2d.draw(getClientArea());
		g2d.dispose();
	}

	@Override
	public void uninstallUI(JComponent c) {
	}

	@Override
	public int tabForCoordinate(JTabbedPane pane, int x, int y) {
		return 0;
	}

	@Override
	public Rectangle getTabBounds(JTabbedPane pane, int index) {
		return tabs.get(index).getBounds();
	}

	@Override
	public int getTabRunCount(JTabbedPane pane) {
		return 0;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public Rectangle getClientArea() {
		Rectangle clientArea = new Rectangle(0, 0, tabbedPane.getWidth() - 1, tabbedPane.getHeight() - 1);
		switch (tabbedPane.getTabPlacement()) {
			case SwingConstants.LEFT:
				clientArea.x = scroller.getWidth() + 1;
				clientArea.width -= clientArea.x;
				break;
			case SwingConstants.RIGHT:
				clientArea.width -= scroller.getWidth();
				break;
			case SwingConstants.TOP:
				clientArea.y = scroller.getHeight() + 1;
				clientArea.height -= clientArea.y;
				break;
			case SwingConstants.BOTTOM:
				clientArea.height -= scroller.getHeight();
				break;
		}
		return clientArea;
	}
	
	public void setTitleAt(String title, int index) {
		tabs.get(index).setText(title);
	}
	
	private class FocusGrabber extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			tabbedPane.requestFocus();
		}

	}
	
	private class HoverListener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			TabComponent tab = (TabComponent)e.getSource();
			Previewer.preview(tab, tabbedPane.getComponentAt(tabs.indexOf(tab)));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Previewer.stopPreview();
		}
	}

	private class TabComponentHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			TabComponent source = (TabComponent) e.getSource();
			source.setSelected(true);
			int index = tabs.indexOf(source);
			if (index == tabbedPane.getSelectedIndex()) {
				return;
			}
			if (tabbedPane.getSelectedIndex() >= 0) {
				tabs.get(tabbedPane.getSelectedIndex()).setSelected(false);
			}
			tabbedPane.setSelectedIndex(index);
		}
	}

	private class TabsListener implements TabLayoutListener, TabListener {

		private TabComponent selected = null;

		@Override
		public void tabLayoutChanged(TabLayoutEvent evt) {
			switch (evt.getChangeType()) {
				case PLACEMENT:
					switch (tabbedPane.getTabPlacement()) {
						case SwingConstants.LEFT:
							tabbedPane.add(vpContainer, BorderLayout.WEST);
							break;
						case SwingConstants.RIGHT:
							tabbedPane.add(vpContainer, BorderLayout.EAST);
							break;
						case SwingConstants.TOP:
							tabbedPane.add(vpContainer, BorderLayout.NORTH);
							break;
						case SwingConstants.BOTTOM:
							tabbedPane.add(vpContainer, BorderLayout.SOUTH);
							break;
					}
					Component comp = tabbedPane.getSelectedComponent();
					if (comp != null) {
						tabbedPane.getLayout().addLayoutComponent(BorderLayout.CENTER, comp);
					}
					tabbedPane.doLayout();
					setup();
					break;
				case SCROLL_LOCATION:
					if (tabbedPane instanceof IExtendedTabbedPane)
						scroller.setButtonPlacement(((IExtendedTabbedPane)tabbedPane).getScrollButtonPlacement());
					break;
			}
		}

		@Override
		public void tabInserted(TabEvent evt) {
			int index = evt.getIndex();
			if (index >= 0) {
				addTabComponent(tabbedPane.getTitleAt(index), tabbedPane.getIconAt(index), tabbedPane.getToolTipTextAt(index), index);
			}
		}

		@Override
		public void tabRemoved(TabEvent evt) {
			int index = evt.getIndex();
			if (index >= 0) {
				removeTabComponent(index);
			}
		}

		@Override
		public void selectedIndexChanged(final TabEvent evt) {
		}

		@Override
		public void selectedComponentChanged(TabEvent evt) {
			if (evt.getIndex() < 0 || evt.getIndex() >= tabs.size()) {
				return;
			}
			if(selected != null)
				selected.setSelected(false);
			selected = tabs.get(evt.getIndex());
			selected.setSelected(true);
			mruTabs.itemUsed(selected);
			Component comp = tabbedPane.getSelectedComponent();
			if (comp != null) {
				comp.addMouseListener(focusGrabber);
				tabbedPane.getLayout().addLayoutComponent(BorderLayout.CENTER, comp);
				tabbedPane.revalidate();
			}
			scroller.scrollToRectangle(selected.getBounds());
		}
	}

	private static class ScrollablePanel extends JPanel implements Scrollable {

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 20;
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 20;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}
	
	private static class ResourcePanel extends JPanel implements UIResource {

		public ResourcePanel() {
			super();
		}

		public ResourcePanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
		}

		public ResourcePanel(LayoutManager layout) {
			super(layout);
		}

		public ResourcePanel(LayoutManager layout, boolean isDoubleBuffered) {
			super(layout, isDoubleBuffered);
		}
		
	}

	private class Actions extends ImmutableAction {

		public Actions(String actionName) {
			super(actionName);
		}

		public Actions(TabKeyActions action) {
			this(action.getName());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (getName().equals(TabKeyActions.PREVIOUS_TAB.getName())) {
				if (!tabSwitcher.isVisible()) {
					tabSwitcher.showSwitcher(tabbedPane, getTabs());
				}
				tabSwitcher.previousTab();
			} else if (getName().equals(TabKeyActions.NEXT_TAB.getName())) {
				if (!tabSwitcher.isVisible()) {
					tabSwitcher.showSwitcher(tabbedPane, getTabs());
				}
				tabSwitcher.nextTab();
			} else if (getName().equals(TabKeyActions.SELECT_TAB.getName())) {
				if (tabSwitcher.isVisible()) {
					TabComponent selected = tabSwitcher.hideSwitcher();
					if (selected != null) {
						tabbedPane.setSelectedIndex(tabs.indexOf(selected));
					}
				}
			}
		}

		private List<TabComponent> getTabs() {
			List<TabComponent> tabsCopy;
			if (tabbedPane instanceof IExtendedTabbedPane && ((IExtendedTabbedPane)tabbedPane).isMruTabCycling() )
				tabsCopy = mruTabs.getItems();
			else
				tabsCopy = Collections.unmodifiableList(tabs);
			return tabsCopy;
		}

	}

	private class ControlMaskManager extends DefaultFocusManager {
		@Override
		public void processKeyEvent(Component focusedComponent, KeyEvent anEvent) {
			if (anEvent.getKeyCode() == KeyEvent.VK_TAB && (anEvent.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK) {
				return;
			}
			super.processKeyEvent(focusedComponent, anEvent);
		}
	}

	public static void main(String[] args) {
		TestFrame.main(args);
	}
}

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
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class DefaultTabSwitcher implements ITabSwitcher {

	private JTabbedPane tabbedPane;

	public DefaultTabSwitcher(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
	
	@Override
	public void showSwitcher(JTabbedPane parent, List<TabComponent> tabs) { }

	@Override
	public TabComponent hideSwitcher() {
		return null;
	}

	@Override
	public void nextTab() {
		int newIndex = tabbedPane.getSelectedIndex() + 1;
		if (newIndex < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(newIndex);
		} else {
			tabbedPane.setSelectedIndex(0);
		}
	}

	@Override
	public void previousTab() {
		int newIndex = tabbedPane.getSelectedIndex() - 1;
		if (newIndex >= 0) {
			tabbedPane.setSelectedIndex(newIndex);
		} else {
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void installKeyBindings(JTabbedPane tabbedPane) {
		InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK), TabKeyActions.NEXT_TAB);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), TabKeyActions.PREVIOUS_TAB);

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
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK), null);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), null);
	}
}
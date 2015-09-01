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

import java.util.List;
import javax.swing.JTabbedPane;

/**
 * Defines the functionality base of a tab switching gizmo. The switcher is not
 * necessary to be visible. In that case showSwitcher will do nothing and
 * hideSwitcher will return null and isVisible false.
 * @author Stelios
 */
public interface ITabSwitcher {
	void showSwitcher(JTabbedPane parent, List<TabComponent> tabs);
	void installKeyBindings(JTabbedPane tabbedPane);
	void uninstallKeyBindings(JTabbedPane tabbedPane);
	TabComponent hideSwitcher();
	void nextTab();
	void previousTab();
	boolean isVisible();
}

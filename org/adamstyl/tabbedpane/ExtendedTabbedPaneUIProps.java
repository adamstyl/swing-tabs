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

import java.awt.Dimension;

public class ExtendedTabbedPaneUIProps {
	private Dimension tabSize = new Dimension(80, 34);
	private Dimension tabPanelSize = new Dimension(300, 30);
	private Dimension scrollButtonSize = new Dimension(20, 30);
//	private Dimension minimumTabSize = new Dimension(40, 20);
//	private Dimension maximumTabSize = new Dimension(200, 100);

	public Dimension getScrollButtonSize() {
		return scrollButtonSize;
	}

	public void setScrollButtonSize(Dimension scrollButtonSize) {
		this.scrollButtonSize = scrollButtonSize;
	}

	public Dimension getTabPanelSize() {
		return tabPanelSize;
	}

	public void setTabPanelSize(Dimension tabPanelSize) {
		this.tabPanelSize = tabPanelSize;
	}

	public Dimension getTabSize() {
		return tabSize;
	}

	public void setTabSize(Dimension tabSize) {
		this.tabSize = tabSize;
	}
}

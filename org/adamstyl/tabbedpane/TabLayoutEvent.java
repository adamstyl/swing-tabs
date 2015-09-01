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

import java.util.EventObject;
import javax.swing.JTabbedPane;

/**
 *
 * @author Stelios
 */
public class TabLayoutEvent extends EventObject {

	private Type changeType;

	public TabLayoutEvent(Object source, Type changeType) {
		super(source);
		this.changeType = changeType;
	}

	public Type getChangeType() {
		return changeType;
	}

	public enum Type {
		/**
		 * Not yet respected. When it does, it will include cases such as scrolling
		 * superfluous tabs, wrapping them in new row/column, or showing a "more"
		 * button.
		 */
		TABS_LAYOUT,
		/**
		 * Scroll button location changed.
		 * @see ScrollButtonPlacement
		 */
		SCROLL_LOCATION,
		/**
		 * Tab well location changed.
		 * @see JTabbedPane#setTabPlacement(int) 
		 */
		PLACEMENT
	}
}

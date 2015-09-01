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
package org.adamstyl.tabbedpane.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple class used to store most recently used (MRU) items most recent is first,
 * least recent is last.
 * @author Stelios
 * @param <ItemType> Type of the MRU items
 */
public class MruSupporter <ItemType>{
	private ArrayList<ItemType> items = new ArrayList<ItemType>(10);

	public void addFirstMruItem(ItemType item) {
		items.add(0, item);
	}

	public void addLastMruItem(ItemType item) {
		items.add(item);
	}

	public void removeMruItem(ItemType item) {
		items.remove(item);
	}

	public ItemType firstMruItem () {
		return items.get(0);
	}

	public ItemType lastMruItem () {
		return items.get(items.size() - 1);
	}

	public ItemType getNextMruItem (ItemType current) {
		int index = items.indexOf(current) + 1;
		return index < items.size() ? items.get(index) : items.get(0);
	}

	public void itemUsed(ItemType item) {
		if (items.remove(item))
			addFirstMruItem(item);
	}

	public List<ItemType> getItems() {
		return Collections.unmodifiableList(items);
	}
}

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;

public class RoundTabComponentPainter implements TabComponentPainter {
	private int cornerRadius = 8;
	private HashMap<TabState, Color> backgroundColors = new HashMap<>();
	private HashMap<TabState, Color> borderColors = new HashMap<>();

	public RoundTabComponentPainter() {
		backgroundColors.put(TabState.HOVER, new Color(245, 245, 245));
		
		borderColors.put(TabState.HOVER, Color.orange);
		borderColors.put(TabState.HOVER_SELECTED, Color.red);
		borderColors.put(TabState.RELEASED, Color.black);
		borderColors.put(TabState.SELECTED, Color.red);
	}

	@Override
	public void paint(Graphics g, Dimension size, TabState state) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (backgroundColors.containsKey(state)) {
			paintBackground(g2d, state, size);
		}
		if (borderColors.containsKey(state)) {
			paintBorder(g2d, state, size);
		}
	}

	private void paintBorder(Graphics2D g2d, TabState state, Dimension size) {
		g2d.setColor(borderColors.get(state));
		g2d.drawRoundRect(0, 0, size.width - 1, size.height - 1, cornerRadius, cornerRadius);
	}

	private void paintBackground(Graphics2D g2d, TabState state, Dimension size) {
		g2d.setColor(backgroundColors.get(state));
		g2d.fillRoundRect(0, 0, size.width, size.height, cornerRadius, cornerRadius);
	}

}

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class TabComponent extends JComponent {

	private TabComponentPainter tabPainter = new PerforceTabComponentPainter();
	private JLabel contentLabel;
	private TabState state = TabState.RELEASED;
	private boolean selected = false;

	public TabComponent(String title, Icon icon, String tip) {
		setLayout(new BorderLayout());
		if (title.contains("\n")) {
			title = "<html>" + title.trim().replace("\n", "<br>") + "</html>";
		}
		contentLabel = new JLabel(title, icon, JLabel.CENTER);
		if (tip != null && !tip.trim().isEmpty()){
			setToolTipText(tip);
		}
		setSize(120, 24);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setFocusable(false);
		add(contentLabel);
		addMouseListener(new MouseHandler());
	}

	public TabComponent(TabComponent original) {
		this(original.getText(), original.getIcon(), original.getToolTipText());
	}

	private void setState(TabState state) {
		if (this.state != state) {
			this.state = state;
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		tabPainter.paint(g, getSize(), state);
	}

	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	public void setText(String text) {
		if (text.contains("\n")) {
			text = "<html>" + text.replace("\n", "</br>") + "</html>";
		}
		contentLabel.setText(text);
	}

	public String getText() {
		return contentLabel.getText();
	}

	public void setIcon(Icon icon) {
		contentLabel.setIcon(icon);
	}

	public Icon getIcon() {
		return contentLabel.getIcon();
	}

	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			if (selected) {
				setState(state == TabState.HOVER ? TabState.HOVER_SELECTED : TabState.SELECTED);
			} else {
				setState(TabState.RELEASED);
			}
		}
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		contentLabel.setFont(font);
	}

	public void doClick() {
		fireActionPerformed();
	}

	private void fireActionPerformed() {
		ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
		for (ActionListener tabLayoutListener : listeners) {
			tabLayoutListener.actionPerformed(new ActionEvent(TabComponent.this, 0, "Selected"));
		}
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			setState(selected ? TabState.HOVER_SELECTED : TabState.HOVER);
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setState(selected ? TabState.SELECTED : TabState.RELEASED);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isEnabled()) {
				fireActionPerformed();
			}
		}
	}
}

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class BorderScroller extends JPanel {
	private JViewport tabViewport = new JViewport();
	private ArrowBtn westBtn = new ArrowBtn(BasicArrowButton.WEST);
	private ArrowBtn eastBtn = new ArrowBtn(BasicArrowButton.EAST);
	private ArrowBtn northBtn = new ArrowBtn(BasicArrowButton.NORTH);
	private ArrowBtn southBtn = new ArrowBtn(BasicArrowButton.SOUTH);
	private ScrollingHandler scrollingHandler = new ScrollingHandler();
	private ComponentHandler componentHandler = new ComponentHandler();
	private Dimension buttonsSize = new Dimension(30, 30);
	private ScrollButtonPlacement buttonPlacement = ScrollButtonPlacement.SCATTERED;
	private boolean horizontal = true;

	public BorderScroller() {
		super(new BorderLayout());
		westBtn.addActionListener(scrollingHandler);
		eastBtn.addActionListener(scrollingHandler);
		northBtn.addActionListener(scrollingHandler);
		southBtn.addActionListener(scrollingHandler);
		westBtn.setPreferredSize(buttonsSize);
		eastBtn.setPreferredSize(buttonsSize);
		northBtn.setPreferredSize(buttonsSize);
		southBtn.setPreferredSize(buttonsSize);
		tabViewport.addComponentListener(componentHandler);
		add(tabViewport, BorderLayout.CENTER);
		arrangeButtons();
	}

	public ScrollButtonPlacement getButtonPlacement() {
		return buttonPlacement;
	}

	public void setButtonPlacement(ScrollButtonPlacement buttonPlacement) {
		this.buttonPlacement = buttonPlacement;
		arrangeButtons();
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
		arrangeButtons();
	}

	private void arrangeButtons() throws AssertionError {
		String far = BorderLayout.SOUTH;
		String near = BorderLayout.NORTH;
		AbstractButton nearBtn = northBtn;
		AbstractButton farBtn = southBtn;
		if (horizontal) {
			far = BorderLayout.EAST;
			near = BorderLayout.WEST;
			farBtn = eastBtn;
			nearBtn = westBtn;
		}
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		switch (this.buttonPlacement) {
			case FAR:
				buttonsPanel.add(farBtn, far);
				buttonsPanel.add(nearBtn, near);
				add(buttonsPanel, far);
				break;
			case NEAR:
				buttonsPanel.add(farBtn, far);
				buttonsPanel.add(nearBtn, near);
				add(buttonsPanel, near);
				break;
			case SCATTERED:
				add(farBtn, far);
				add(nearBtn, near);
				break;
			default:
				throw new AssertionError();
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				revalidate();
			}
		});
	}

	public void setView(Component view) {
		Component oldView = tabViewport.getView();
		if (oldView != null)
			oldView.removeComponentListener(componentHandler);
		tabViewport.setView(view);
		view.addComponentListener(componentHandler);
	}

	public void scrollToRectangle(Rectangle rect) {
		rect.x -= tabViewport.getViewPosition().x;
		rect.y -= tabViewport.getViewPosition().y;
		tabViewport.scrollRectToVisible(rect);
	}
	
	public void scrollFar() {
		if (horizontal)
			scroll(SwingConstants.EAST);
		else
			scroll(SwingConstants.SOUTH);
	}
	
	public void scrollNear() {
		if (horizontal)
			scroll(SwingConstants.WEST);
		else
			scroll(SwingConstants.NORTH);
	}
	
	private void scroll(int direction) {
		Point viewPosition = tabViewport.getViewPosition();
		switch (direction) {
			case SwingConstants.EAST:
				if (viewPosition.x < tabViewport.getView().getWidth() - tabViewport.getWidth()) {
					tabViewport.setViewPosition(new Point(viewPosition.x + 20, 0));
				}
				break;
			case SwingConstants.WEST:
				if (viewPosition.x > 0) {
					tabViewport.setViewPosition(new Point(viewPosition.x - 20, 0));
				}
				break;
			case SwingConstants.NORTH:
				if (viewPosition.y > 0) {
					tabViewport.setViewPosition(new Point(0, viewPosition.y - 20));
				}
				break;
			case SwingConstants.SOUTH:
				if (viewPosition.y < tabViewport.getView().getHeight() - tabViewport.getHeight()) {
					tabViewport.setViewPosition(new Point(0, viewPosition.y + 20));
				}
				break;
			default:
				throw new AssertionError();
		}
		viewPosition = tabViewport.getViewPosition();
		if (viewPosition.x < 0 || viewPosition.y < 0) {
			tabViewport.setViewPosition(new Point());
		}
		if (tabViewport.getView().getWidth() - viewPosition.x < tabViewport.getWidth()) {// || viewPosition.y < 0
			tabViewport.setViewPosition(new Point(tabViewport.getView().getWidth() - tabViewport.getWidth(), 0));
		}
		repaint();
	}
	
	private class ComponentHandler extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			boolean hideHorizontalButtons = getWidth() >= tabViewport.getViewSize().width;
			boolean hideVerticalButtons = getHeight() >= tabViewport.getViewSize().height;
			eastBtn.setVisible(!hideHorizontalButtons);
			westBtn.setVisible(!hideHorizontalButtons);
			northBtn.setVisible(!hideVerticalButtons);
			southBtn.setVisible(!hideVerticalButtons);
			//ensureTabLayout();
		}
	}

	private class ScrollingHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == westBtn) {
				scroll(SwingConstants.WEST);
			} else if (e.getSource() == eastBtn) {
				scroll(SwingConstants.EAST);
			} else if (e.getSource() == northBtn) {
				scroll(SwingConstants.NORTH);
			} else if (e.getSource() == southBtn) {
				scroll(SwingConstants.SOUTH);
			}
			tabViewport.repaint();
		}
	}
	
	private static class ArrowBtn extends BasicArrowButton {

		private Dimension preferredSize;
		
		public ArrowBtn(int direction) {
			super(direction);
		}

		public ArrowBtn(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
			super(direction, background, shadow, darkShadow, highlight);
		}

		@Override
		public void setPreferredSize(Dimension preferredSize) {
			this.preferredSize = preferredSize;
		}
		
		@Override
		public Dimension getPreferredSize() {
			return direction == NORTH || direction == SOUTH
					? new Dimension(10, preferredSize.height)
					: new Dimension(preferredSize.width, 10);
		}
	}
}

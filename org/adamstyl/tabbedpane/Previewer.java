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

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

class Previewer {

	private static Window prevWindow;
	private static DelayThread delayThread = new DelayThread(0);
	private static float minZoom = .6f;

	public Previewer() {
	}

	static void preview(TabComponent tab, Component c) {
		if (delayThread.isAlive())
			delayThread.interrupt();
		PreviewPanel previewer = new PreviewPanel(c);
		if (prevWindow == null)
			prevWindow = new Window(null);
		else
			prevWindow.removeAll();
		prevWindow.add(previewer);
		Rectangle bounds = new Rectangle(tab.getLocationOnScreen(), tab.getSize());
		int x = bounds.width / 2 + bounds.x - previewer.getWidth() / 2;
		int y = bounds.height + bounds.y;
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		prevWindow.setBounds(x, y, previewer.getWidth(), previewer.getHeight());
		if (!prevWindow.isVisible()) {
			delayThread = new DelayThread(500);
			delayThread.start(new Runnable() {

				public void run() {
					prevWindow.setVisible(true);
				}
			});
		} else
			prevWindow.repaint();
	}

	static void stopPreview() {
		if (delayThread.isAlive())
			delayThread.interrupt();
		delayThread = new DelayThread(500);
		delayThread.start(new Runnable() {

			public void run() {
				prevWindow.setVisible(false);
				prevWindow = null;
			}
		});
	}

	private static class PreviewPanel extends JPanel {

		private Component componentToPreviw;

		public PreviewPanel(Component componentToPreviw) {
			this.componentToPreviw = componentToPreviw;
			int width = 258;
			int height = 167;
			setSize(width, height);
			setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.lightGray, Color.gray));
		}

		@Override
		protected void paintComponent(Graphics g) {
			BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			float zoom = .8f;
			Point paintOffset = new Point(10, 10);
			if (componentToPreviw instanceof Container) {
				Rectangle previewClip = calculateBounds((Container)componentToPreviw);
				g2d.setClip(previewClip);
				paintOffset.x = -previewClip.x;
//				paintOffset.y = -previewClip.y;
				zoom = getZoom(previewClip);
				super.paintComponent(g);
				g2d.setColor(Color.red);
				g2d.drawRect(paintOffset.x, paintOffset.y, previewClip.width, previewClip.height);
			}
			g2d.scale(zoom, zoom);
			componentToPreviw.paint(g2d);
			super.paintComponent(g);
			g.drawImage(img, paintOffset.x, paintOffset.y, this);
		}
		
		private Rectangle calculateBounds(Container c) {
			Rectangle bounds = new Rectangle(1000, 1000, 0, 0);
			for (Component component : c.getComponents()) {
				if (bounds.x > component.getX())
				    bounds.x = component.getX();
				if (bounds.y > component.getY())
				    bounds.y = component.getY();
				if (bounds.width < component.getWidth() + component.getX() - bounds.x)
				    bounds.width = component.getWidth() + component.getX() - bounds.x;
				if (bounds.height < component.getHeight() + component.getY() - bounds.y)
				    bounds.height = component.getHeight() + component.getY() - bounds.y;
			}
			if (bounds.width < 238) {
				bounds.width = 238;
				bounds.x -= bounds.width / 2;
			}
			if (bounds.height < 147) {
				bounds.height = 147;
				bounds.y -= bounds.height / 2;
			}
			bounds.width += 20;
			bounds.height += 20;
			bounds.x -= 10;
			bounds.y -= 10;
			return bounds;
		}
		
		private float getZoom(Rectangle previewClip) {
			float widthRatio = (float)getWidth() / previewClip.width;
			float heightRatio = (float)getHeight() / previewClip.height;
			float zoom = 1;
			if (widthRatio < heightRatio && widthRatio < 1)
				zoom = widthRatio;
			if (widthRatio > heightRatio && heightRatio < 1)
				zoom = heightRatio;
			if (zoom < minZoom)
				zoom = minZoom;
			return zoom;
		}
	}

	private static class DelayThread extends Thread {

		private Runnable callback;
		private int delay;

		public DelayThread(int delay) {
			super("Delay Thread");
			this.delay = delay;
		}

		public void start(Runnable callback) {
			this.callback = callback;
			start();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {
				return;
			}
			SwingUtilities.invokeLater(callback);
		}

	}
}

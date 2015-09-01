package org.adamstyl.tabbedpane.util;

import java.beans.PropertyChangeListener;
import javax.swing.Action;

public abstract class ImmutableAction implements Action {

	private String name;

	public ImmutableAction(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	@Override
	public Object getValue(String key) {
		if (NAME.equals(key)) {
			return name;
		}
		return null;
	}

	@Override
	public void putValue(String key, Object value) { }

	@Override
	public void setEnabled(boolean b) { }

	@Override
	public final boolean isEnabled() {
		return true;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) { }

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) { }
}

package de.datenhahn.vaadin.componentrenderer.grid;

import com.vaadin.ui.Component;

import java.io.Serializable;

public interface ComponentGenerator<T> extends Serializable {
    Component getComponent(T itemId);
}

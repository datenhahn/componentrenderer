package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.ui.Component;

import java.io.Serializable;

public interface MGenerator<T> extends Serializable {
    Component getComponent(T itemId);
}

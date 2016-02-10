package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.data.util.GeneratedPropertyContainer;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import org.vaadin.viritin.grid.MGrid;

public class MGrid2<T> extends MGrid<T> {
    GeneratedPropertyContainer gpc = null;
    Class<T> typeOfRows;

    public MGrid2(Class<T> typeOfRows) {
        super(typeOfRows);
        this.typeOfRows = typeOfRows;
    }

    private void initGpc() {
        gpc = new GeneratedPropertyContainer(getContainerDataSource());
        setContainerDataSource(gpc);
    }

    public MGrid2<T> addComponentColumn(Object propertyId, MGenerator<T> generator) {
        if(gpc == null) {
            initGpc();
        }
        gpc.addGeneratedProperty(propertyId, new ComponentPropertyGenerator<>(typeOfRows, generator));
        getColumn(propertyId).setRenderer(new ComponentRenderer());
        return this;
    }

    public void refresh() {
        setCellStyleGenerator(getCellStyleGenerator());
    }
}

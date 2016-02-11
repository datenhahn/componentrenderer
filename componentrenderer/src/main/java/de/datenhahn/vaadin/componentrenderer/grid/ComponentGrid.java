package de.datenhahn.vaadin.componentrenderer.grid;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Grid;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;

import java.util.Collection;

public class ComponentGrid<T> extends Grid {
    GeneratedPropertyContainer gpc = null;
    BeanItemContainer<T> bc;
    Class<T> typeOfRows;

    public ComponentGrid(Class<T> typeOfRows) {
        super();
        this.typeOfRows = typeOfRows;
        this.bc = new BeanItemContainer<T>(typeOfRows);
        setContainerDataSource(bc);
    }

    private void initGpc() {
        gpc = new GeneratedPropertyContainer(getContainerDataSource());
        DetailsGenerator details = getDetailsGenerator();
        setContainerDataSource(gpc);
        setDetailsGenerator(details);
    }

    public ComponentGrid<T> addComponentColumn(Object propertyId, ComponentGenerator<T> generator) {
        if(gpc == null) {
            initGpc();
        }
        gpc.addGeneratedProperty(propertyId, new ComponentPropertyGenerator<>(typeOfRows, generator));
        getColumn(propertyId).setRenderer(new ComponentRenderer());
        return this;
    }

    public void setRows(Collection<T> beans) {
        bc.addAll(beans);
    }

    public void remove(T bean) {
        bc.removeItem(bean);
    }

    public void refresh() {
        setCellStyleGenerator(getCellStyleGenerator());
    }
}

package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.data.Item;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.Component;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ComponentPropertyGenerator<T> extends PropertyValueGenerator<Component> {

    private Class<T> typeOfRows;
    private ComponentGenerator<T> componentGenerator;

    public ComponentPropertyGenerator(Class<T> typeOfRows, ComponentGenerator<T> generator) {
        this.typeOfRows = typeOfRows;
        this.componentGenerator = generator;
    }

    @Override
    public Component getValue(Item item, Object itemId, Object propertyId) {
        return componentGenerator.getComponent((T) itemId);
    }

    @Override
    public SortOrder[] getSortProperties(SortOrder order) {

        if (typeOfRows != null && FieldUtils.getField(typeOfRows, (String) order.getPropertyId(),true) != null) {
            return new SortOrder[]{order};
        } else {
            return new SortOrder[0];
        }
    }

    @Override
    public Class<Component> getType() {
        return Component.class;
    }
}

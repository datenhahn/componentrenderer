package de.datenhahn.vaadin.componentrenderer.grid;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

public class ComponentCustomField extends CustomField<Component> {

    private HorizontalLayout layout = new HorizontalLayout();

    @Override
    protected Component initContent() {
        layout.setSizeFull();
        layout.addStyleName("cr-editor-field");
        return layout;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource)
    {
        super.setPropertyDataSource(newDataSource);

        if (newDataSource != null) {
            layout.removeAllComponents();
            Component value = (Component) newDataSource.getValue();
            if (value != null) {
                layout.addComponent(value);
            }
        }
    }

    @Override
    public Class<? extends Component> getType() {
        return Component.class;
    }
}

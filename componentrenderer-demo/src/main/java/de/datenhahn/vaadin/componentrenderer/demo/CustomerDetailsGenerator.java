package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class CustomerDetailsGenerator implements Grid.DetailsGenerator {
    @Override
    public Component getDetails(Grid.RowReference rowReference) {
        rowReference.getGrid().scrollTo(rowReference.getItemId());
        Customer customer = (Customer)rowReference.getItemId();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setHeight(300, Sizeable.Unit.PIXELS);
        layout.setMargin(true);
        layout.setSpacing(true);
        Image image = new Image("", customer.getPhoto());
        image.setHeight(200, Sizeable.Unit.PIXELS);
        image.setWidth(200, Sizeable.Unit.PIXELS);
        layout.addComponent(image);
        Label nameLabel = new Label("<h1>"+customer.getFirstName() + " " + customer.getLastName()+"</h1>", ContentMode.HTML);
        layout.addComponent(nameLabel);
        layout.setExpandRatio(nameLabel, 1.0f);
        return layout;
    }
}

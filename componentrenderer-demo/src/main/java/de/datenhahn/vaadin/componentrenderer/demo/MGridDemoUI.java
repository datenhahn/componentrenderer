/**
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.impl.PopulatorBuilder;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.servlet.annotation.WebServlet;
import java.util.LinkedList;
import java.util.List;

/**
 * ComponentRenderer Demo
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Theme("demotheme")
@Widgetset("de.datenhahn.vaadin.componentrenderer.demo.DemoWidgetset")
public class MGridDemoUI extends UI {
    public static final String COL_TEXT = "Text Column";
    public static final String COL_FONT_LABEL_1 = "Component 1";
    public static final String COL_FONT_LABEL_2 = "Component 2";
    public static final String COL_FONT_LABEL_3 = "Component 3";
    public static final String COL_DELETE = "Delete";
    public static final String COL_ARROWS = "Expand Details";
    final MVerticalLayout layout = new MVerticalLayout();

    private static final int ROW_HEIGHT = 50;


    private void addHeader() {
        Label headerLabel = new Label("<h1>ComponentRenderer Demo</h1>", ContentMode.HTML);
        headerLabel.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(headerLabel);
    }

    private List<Customer> createDummyData() {
        LinkedList<Customer> list = new LinkedList<>();

        for (int i = 1; i <= 5000; i++) {

            Populator populator = new PopulatorBuilder().build();
            Customer customer = populator.populateBean(Customer.class);
            customer.setId(i);
            list.add(customer);
        }
        return list;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {


        addHeader();


        MGrid2<Customer> mgrid = new MGrid2<Customer>(Customer.class);
        layout.addComponent(new Button("showData", e -> {
            for (Object customer : mgrid.getContainerDataSource().getItemIds()) {
                System.out.println("Food: " + ((Customer) customer).getFood());
            }
        }));
        mgrid.setWidth(100, Unit.PERCENTAGE);
        mgrid.setHeight(70, Unit.PERCENTAGE);

        mgrid.setRows(createDummyData());

        mgrid.addComponentColumn("food", cust -> createFoodSelector(mgrid, cust));
        mgrid.addComponentColumn("foodIcon", cust -> createFoodIcon(cust));
        mgrid.withProperties("id", "firstName", "lastName", "food", "foodIcon");

        layout.addComponent(mgrid);

        setContent(layout);


    }

    private Component createFoodIcon(Customer cust) {
        Label label = new Label(FontAwesome.HOURGLASS_2.getHtml(), ContentMode.HTML);

        label.setHeight("40px");
        label.setWidth("50px");

        if (cust.getFood() == Customer.Food.HAMBURGER) {
            label.setValue(FontAwesome.AMBULANCE.getHtml());
        }

        if (cust.getFood() == Customer.Food.FISH) {
            label.setValue(FontAwesome.HEART.getHtml());
        }

        if (cust.getFood() == Customer.Food.VEGETABLES) {
            label.setValue(FontAwesome.SUN_O.getHtml());
        }

        return label;

    }

    private Component createFoodSelector(MGrid2 mgrid, Customer customer) {

        ComboBox select = new ComboBox();
        select.setWidth("200px");
        select.setHeight("40px");
        select.addItems(Customer.Food.FISH, Customer.Food.HAMBURGER, Customer.Food.VEGETABLES);
        select.setPropertyDataSource(new BeanItem<>(customer).getItemProperty("food"));
        select.addValueChangeListener(e -> mgrid.refresh());
        return select;
    }

    private CheckBox createEnableDisableCheckBox(final Grid myGrid) {
        CheckBox checkBox = new CheckBox("enable/disable");
        checkBox.setValue(myGrid.isEnabled());
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                myGrid.setEnabled(!myGrid.isEnabled());
            }
        });
        return checkBox;
    }

    private Button createButtonDelete(final Grid myGrid) {
        Button button = new Button("delete all", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                myGrid.getContainerDataSource().removeAllItems();

            }
        });
        button.setHeight(ROW_HEIGHT, Unit.PIXELS);
        button.setWidth(100, Unit.PIXELS);
        return button;
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MGridDemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


}

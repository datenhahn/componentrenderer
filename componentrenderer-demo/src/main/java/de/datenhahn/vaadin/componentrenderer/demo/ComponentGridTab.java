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

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGrid;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGridTab extends VerticalLayout {

    public static final String FOOD = "food";
    public static final String FOOD_ICON = "foodIcon";
    public static final String RATING = "rating";
    public static final String DELETE = "delete";
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String DETAILS_ICONS = "detailsIcons";
    private DataFactory testData = new DataFactory();

    private static final int ROW_HEIGHT = 40;

    public ComponentGridTab() {
        init();
    }

    private List<Customer> createDummyData() {
        LinkedList<Customer> list = new LinkedList<>();

        for (int i = 1; i <= 10000; i++) {

            Customer customer = new Customer();
            customer.setId(i);
            customer.setFirstName(testData.getFirstName());
            customer.setLastName(testData.getLastName());
            customer.setFood(testData.getItem(Customer.Food.values()));

            list.add(customer);
        }
        return list;
    }

    private void init() {

        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                " the classic grid"));
        ComponentGrid<Customer> grid = new ComponentGrid<>(Customer.class);
        addComponent(createEnableDisableCheckBox(grid));

        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(100, Unit.PERCENTAGE);

        grid.setRows(createDummyData());

        grid.setDetailsGenerator(rowReference -> {
            VerticalLayout layout = new VerticalLayout();
            layout.setHeight(100, Unit.PIXELS);
            layout.addComponent(new Label(((Customer)rowReference.getItemId()).getFirstName()));
            return layout;

        });

        grid.addComponentColumn(FOOD, cust -> createFoodSelector(grid, cust));
        grid.addComponentColumn(FOOD_ICON, cust -> createFoodIcon(cust));
        grid.addComponentColumn(RATING, cust -> createRating(cust));
        grid.addComponentColumn(DELETE, cust -> createDeleteButton(grid, cust));
        grid.addComponentColumn(DETAILS_ICONS, cust -> createDetailsIcons(grid, cust));
        grid.setFrozenColumnCount(1);

        grid.setColumns(DETAILS_ICONS, ID, FIRST_NAME, LAST_NAME, FOOD, FOOD_ICON, RATING, DELETE);

        addComponent(grid);
        setExpandRatio(grid, 1.0f);

    }

    private Image createDetailsIcons(ComponentGrid<Customer> grid, Customer customer) {
        final Image imageDown = new Image("", new ThemeResource("../demotheme/img/caret-down.png"));
        final Image imageUp = new Image("", new ThemeResource("../demotheme/img/caret-up.png"));
        imageDown.setHeight(32,Unit.PIXELS);
        imageDown.setWidth(32, Unit.PIXELS);
        imageUp.setHeight(32,Unit.PIXELS);
        imageDown.setWidth(32, Unit.PIXELS);

        imageDown.addClickListener(event -> grid.setDetailsVisible(customer, true));
        imageUp.addClickListener(event -> grid.setDetailsVisible(customer, false));

        if (grid.isDetailsVisible(customer)) {
            return imageUp;
        } else {
            return imageDown;
        }
    }

    private Component createFoodIcon(Customer cust) {
        Label label = new Label(FontAwesome.HOURGLASS_2.getHtml(), ContentMode.HTML);

        label.setHeight(32,Unit.PIXELS);
        label.setWidth(50, Unit.PIXELS);

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

    private Button createDeleteButton(ComponentGrid<Customer> grid, Customer customer) {
        Button delete = new Button(DELETE, event -> {
            grid.remove(customer);
            grid.refresh();
        });
        delete.setHeight(ROW_HEIGHT, Unit.PIXELS);
        delete.setWidth(150, Unit.PIXELS);
        return delete;
    }


    private CssLayout createRating(Customer customer) {
        CssLayout layout = new CssLayout();
        layout.setHeight(ROW_HEIGHT, Unit.PIXELS);
        layout.setWidth(150, Unit.PIXELS);

        Label overallRating = new Label(FontAwesome.STAR.getHtml(), ContentMode.HTML);
        overallRating.addStyleName("green");
        overallRating.setDescription("Very good : " + testData.getNumberBetween(90, 100) + "% Chance");
        overallRating.setWidthUndefined();
        overallRating.setWidth(40, Unit.PIXELS);
        overallRating.setHeight(ROW_HEIGHT, Unit.PIXELS);
        layout.addComponent(overallRating);


        Label carRating = new Label(FontAwesome.CAR.getHtml(), ContentMode.HTML);
        carRating.addStyleName("red");
        carRating.setDescription("Unlikely : " + testData.getNumberBetween(1, 15) + "%");
        carRating.setWidthUndefined();
        carRating.setWidth(40, Unit.PIXELS);
        carRating.setHeight(ROW_HEIGHT, Unit.PIXELS);

        layout.addComponent(carRating);

        return layout;
    }

    private Component createFoodSelector(ComponentGrid grid, Customer customer) {

        ComboBox select = new ComboBox();
        select.setWidth(200, Unit.PIXELS);
        select.setHeight(ROW_HEIGHT, Unit.PIXELS);
        select.addItems(Customer.Food.FISH, Customer.Food.HAMBURGER, Customer.Food.VEGETABLES);
        select.setPropertyDataSource(new BeanItem<>(customer).getItemProperty(FOOD));
        select.addValueChangeListener(e -> {
            Notification.show("Persisting customer: " + customer.getFirstName() + " " + customer.getLastName() );
            grid.refresh();});
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

}

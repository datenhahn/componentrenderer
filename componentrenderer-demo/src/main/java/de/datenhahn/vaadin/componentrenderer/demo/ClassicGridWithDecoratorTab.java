/**
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGridDecorator;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ClassicGridWithDecoratorTab extends VerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";

    private FocusPreserveExtension focusPreserveExtension;

    public ClassicGridWithDecoratorTab() {
        init();
    }

    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(new Label("This grid is editable using the grid's editor"));

        Grid grid = new Grid();
        grid.setContainerDataSource(new BeanItemContainer<Customer>(Customer.class));
        ComponentGridDecorator<Customer> componentGridDecorator = new ComponentGridDecorator<>(grid, Customer.class);
        componentGridDecorator.addAll(CustomerProvider.createDummyData());
        addComponent(ViewComponents.createEnableDisableCheckBox(grid));

        grid.setSizeFull();
        grid.setEditorEnabled(true);
        grid.setEditorBuffered(false);


        // Initialize DetailsGenerator (Caution: the DetailsGenerator is set to null
        // when grid#setContainerDatasource is called, so make sure you call setDetailsGenerator
        // after setContainerDatasource
        grid.setDetailsGenerator(new CustomerDetailsGenerator());

        componentGridDecorator.addComponentColumn(Customer.PREMIUM, cust -> ViewComponents.createPremiumCheckbox
                (componentGridDecorator, cust));
        componentGridDecorator.addComponentColumn(Customer.FOOD, cust -> ViewComponents.createFoodSelector
                (componentGridDecorator, cust));
        componentGridDecorator.addComponentColumn(GENERATED_FOOD_ICON, cust -> ViewComponents.createFoodIcon(cust));
        componentGridDecorator.addComponentColumn(GENERATED_RATING, cust -> ViewComponents.createRating(cust));
        componentGridDecorator.addComponentColumn(GENERATED_DELETE, cust -> ViewComponents.createDeleteButton
                (componentGridDecorator, cust)).setEditable(false);
        componentGridDecorator.addComponentColumn(GENERATED_DETAILS_ICONS, cust -> ViewComponents
                .createDetailsIcons(grid, cust)).setEditable(false);

        // always display the details column
        grid.setFrozenColumnCount(1);

        grid.setColumns(GENERATED_DETAILS_ICONS, Customer.ID, Customer.PREMIUM, Customer.FIRST_NAME, Customer
                .LAST_NAME, Customer.FOOD, GENERATED_FOOD_ICON, GENERATED_RATING, GENERATED_DELETE);
        //grid.setColumns(Customer.PREMIUM, Customer.FIRST_NAME, Customer.LAST_NAME, GENERATED_RATING);
        componentGridDecorator.generateHeaders(new ResourceBundleTextHeaderGenerator(ViewComponents.getLabels()));

        addComponent(grid);
        setExpandRatio(grid, 1.0f);
    }

}

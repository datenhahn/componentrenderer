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

import com.vaadin.ui.Label;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGridDecorator;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ViritinMGridTab extends MVerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";

    public ViritinMGridTab() {
        init();
    }

    private void init() {

        add(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                " the classic mGrid"));

        MGrid<Customer> mGrid = new MGrid<>(Customer.class);
        ComponentGridDecorator<Customer> componentGridDecorator = new ComponentGridDecorator<Customer>(mGrid, Customer.class);

        add(ViewComponents.createEnableDisableCheckBox(mGrid));

        mGrid.setSizeFull();

        /**
         * unfortunately you cannot use {@link MGrid#setRows} anymore as it forces the
         * ListContainer onto the grid, but you need the GeneratedPropertyContainer.
         *
         * But you can use the equal method of the ComponentGridDecorator {@link ComponentGridDecorator#setRows}
         */
        componentGridDecorator.setRows(CustomerProvider.createDummyData());
        mGrid.setDetailsGenerator(new CustomerDetailsGenerator());

        componentGridDecorator.addComponentColumn(Customer.FOOD, cust -> ViewComponents.createFoodSelector(componentGridDecorator, cust));
        componentGridDecorator.addComponentColumn(GENERATED_FOOD_ICON, cust -> ViewComponents.createFoodIcon(cust));
        componentGridDecorator.addComponentColumn(GENERATED_RATING, cust -> ViewComponents.createRating(cust));
        componentGridDecorator.addComponentColumn(GENERATED_DELETE, cust -> ViewComponents.createDeleteButton(componentGridDecorator, cust));
        componentGridDecorator.addComponentColumn(GENERATED_DETAILS_ICONS, cust -> ViewComponents.createDetailsIcons(mGrid, cust));

        mGrid.setFrozenColumnCount(1);

        mGrid.withProperties(GENERATED_DETAILS_ICONS, Customer.ID, Customer.FIRST_NAME, Customer.LAST_NAME, Customer.FOOD, GENERATED_FOOD_ICON, GENERATED_RATING, GENERATED_DELETE);
        componentGridDecorator.generateHeaders(new ResourceBundleTextHeaderGenerator(ViewComponents.getLabels()));

        expand(mGrid);
    }



}

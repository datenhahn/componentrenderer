/*
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

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGrid;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class TestbenchComponentGridTab extends VerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";

    public TestbenchComponentGridTab() {
        init();
    }

    private void init() {

        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                " the classic grid"));
        ComponentGrid<Customer> grid = new ComponentGrid<>(Customer.class);
        addComponent(ViewComponents.createEnableDisableCheckBox(grid));

        grid.setSizeFull();

        grid.setRows(CustomerProvider.createDummyData());

        grid.setDetailsGenerator(new CustomerDetailsGenerator());

        grid.addComponentColumn(Customer.PREMIUM, cust -> ViewComponents.createPremiumCheckbox(grid.getComponentGridDecorator(), cust));
        grid.addComponentColumn(Customer.FOOD, cust -> ViewComponents.createFoodSelector(grid.getComponentGridDecorator(), cust));
        grid.addComponentColumn(GENERATED_FOOD_ICON, cust -> ViewComponents.createFoodIcon(cust));
        grid.addComponentColumn(GENERATED_RATING, cust -> ViewComponents.createRating(cust));
        grid.addComponentColumn(GENERATED_DELETE, cust -> ViewComponents.createDeleteButton(grid.getComponentGridDecorator(), cust));
        grid.addComponentColumn(GENERATED_DETAILS_ICONS, cust -> ViewComponents.createDetailsIcons(grid, cust));
        grid.setFrozenColumnCount(1);
        grid.setColumns(GENERATED_DETAILS_ICONS, Customer.ID, Customer.PREMIUM, Customer.FIRST_NAME, Customer.LAST_NAME, Customer.FOOD, GENERATED_FOOD_ICON, GENERATED_RATING, GENERATED_DELETE);
        grid.generateHeaders(new ResourceBundleComponentHeaderGenerator(ViewComponents.getLabels()));

        Button removeColumnButton =
                new Button("Remove " + GENERATED_DELETE + " Column", (Button.ClickListener) event -> {
                    if (grid.getColumn(GENERATED_DELETE) != null) {
                        grid.removeColumn(GENERATED_DELETE);
                    }

                });
        removeColumnButton.setId("removeColumnButton");
        addComponent(removeColumnButton);
        addComponent(grid);
        setExpandRatio(grid, 1.0f);

    }

}

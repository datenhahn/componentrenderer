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

import com.vaadin.data.Item;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.*;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ClassicGridTab extends VerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";

    private FocusPreserveExtension focusPreserveExtension;

    public ClassicGridTab() {
        init();
    }

    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                " the classic grid"));

        Grid grid = new Grid();
        ComponentCellKeyExtension.extend(grid);
        focusPreserveExtension = FocusPreserveExtension.extend(grid);
        DetailsKeysExtension.extend(grid);

        addComponent(ViewComponents.createEnableDisableCheckBox(grid));

        grid.setSizeFull();

        // Initialize Containers
        BeanItemContainer<Customer> bc = new BeanItemContainer<>(Customer.class);

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(bc);
        grid.setContainerDataSource(gpc);

        // Load the data
        bc.addAll(CustomerProvider.createDummyData());

        // Initialize DetailsGenerator (Caution: the DetailsGenerator is set to null
        // when grid#setContainerDatasource is called, so make sure you call setDetailsGenerator
        // after setContainerDatasource
        grid.setDetailsGenerator(new CustomerDetailsGenerator());

        gpc.addGeneratedProperty(Customer.FOOD, new PropertyValueGenerator<Component>() {
            @Override
            public Component getValue(Item item, Object itemId, Object propertyId) {
                return ViewComponents.createClassicFoodSelector(grid, focusPreserveExtension, (Customer) itemId);
            }

            @Override
            public Class<Component> getType() {
                return Component.class;
            }

            // You must override getSortProperties to allow sorting by the values
            // underlying of the GeneratedValue. The default is that generated
            // property columns cannot be sorted (see PropertyValueGenerator default implementation)
            // if the generated column is not shadowing a real data column DO NOT overwrite this method
            // otherwise an exception is thrown when you sort it because the bean property cannot be found
            @Override
            public SortOrder[] getSortProperties(SortOrder order) {
                return new SortOrder[]{order};
            }
        });

        // Don't forget to set the ComponentRenderer AFTER adding the column
        grid.getColumn(Customer.FOOD).setRenderer(new ComponentRenderer());

        gpc.addGeneratedProperty(GENERATED_FOOD_ICON, new PropertyValueGenerator<Component>() {
            @Override
            public Component getValue(Item item, Object itemId, Object propertyId) {
                return ViewComponents.createFoodIcon((Customer) itemId);
            }

            @Override
            public Class<Component> getType() {
                return Component.class;
            }

            // You must override getSortProperties to allow sorting by the values
            // underlying of the GeneratedValue. The default is that generated
            // property columns cannot be sorted (see PropertyValueGenerator default implementation)
            // if the generated column is not shadowing a real data column DO NOT overwrite this method
            // otherwise an exception is thrown when you sort it because the bean property cannot be found
            @Override
            public SortOrder[] getSortProperties(SortOrder order) {
                return new SortOrder[]{order};
            }
        });

        // Don't forget to set the ComponentRenderer AFTER adding the column
        grid.getColumn(GENERATED_FOOD_ICON).setRenderer(new ComponentRenderer());

        gpc.addGeneratedProperty(GENERATED_RATING, new PropertyValueGenerator<Component>() {
            @Override
            public Component getValue(Item item, Object itemId, Object propertyId) {
                return ViewComponents.createRating((Customer) itemId);
            }

            @Override
            public Class<Component> getType() {
                return Component.class;
            }

        });

        // Don't forget to set the ComponentRenderer AFTER adding the column
        grid.getColumn(GENERATED_RATING).setRenderer(new ComponentRenderer());

        gpc.addGeneratedProperty(GENERATED_DELETE, new PropertyValueGenerator<Component>() {
            @Override
            public Component getValue(Item item, Object itemId, Object propertyId) {
                return ViewComponents.createClassicDeleteButton(grid, focusPreserveExtension, bc, (Customer) itemId);
            }

            @Override
            public Class<Component> getType() {
                return Component.class;
            }

        });

        // Don't forget to set the ComponentRenderer AFTER adding the column
        grid.getColumn(GENERATED_DELETE).setRenderer(new ComponentRenderer());

        gpc.addGeneratedProperty(GENERATED_DETAILS_ICONS, new PropertyValueGenerator<Component>() {
            @Override
            public Component getValue(Item item, Object itemId, Object propertyId) {
                return ViewComponents.createDetailsIcons(grid, (Customer) itemId);
            }

            @Override
            public Class<Component> getType() {
                return Component.class;
            }

        });

        // Don't forget to set the ComponentRenderer AFTER adding the column
        grid.getColumn(GENERATED_DETAILS_ICONS).setRenderer(new ComponentRenderer());


        // always display the details column
        grid.setFrozenColumnCount(1);

        grid.setColumns(GENERATED_DETAILS_ICONS, Customer.ID, Customer.FIRST_NAME, Customer.LAST_NAME, Customer.FOOD, GENERATED_FOOD_ICON, GENERATED_RATING, GENERATED_DELETE);

        addComponent(grid);
        setExpandRatio(grid, 1.0f);
    }

}

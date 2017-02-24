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

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ClassicGridWithStaticContainerTab extends VerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";

    private FocusPreserveExtension focusPreserveExtension;

    public ClassicGridWithStaticContainerTab() {
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
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        ((Grid.SingleSelectionModel)grid.getSelectionModel()).setDeselectAllowed(false);

        grid.setSizeFull();

        // Initialize Containers
        BeanItemContainer<StaticCustomer> bc = new BeanItemContainer<>(StaticCustomer.class);

        grid.setContainerDataSource(bc);

        // Load the data
        bc.addAll(StaticCustomerProvider.createDummyData());

        // Initialize DetailsGenerator (Caution: the DetailsGenerator is set to null
        // when grid#setContainerDatasource is called, so make sure you call setDetailsGenerator
        // after setContainerDatasource
        grid.setDetailsGenerator(new StaticCustomerDetailsGenerator());


        // always display the details column
        grid.setFrozenColumnCount(1);

        grid.getColumn(Customer.FOOD).setRenderer(new ComponentRenderer());

        grid.setColumns(StaticCustomer.ID, StaticCustomer.FIRST_NAME, StaticCustomer.LAST_NAME, StaticCustomer.FOOD);

        addComponent(grid);
        setExpandRatio(grid, 1.0f);
    }

}

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
package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGrid;

import java.util.HashMap;
import java.util.Map;

/**
 * Regression Test for
 * Issue #32: Vanishing components, when using cached components
 *
 * see also
 *
 * https://github.com/datenhahn/componentrenderer/pull/31
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class TestbenchCachingGridTab extends VerticalLayout {

    public TestbenchCachingGridTab() {
        init();
    }

    class TestComponentGenerator implements ComponentGenerator<Customer> {
        private final Map<Customer, ComboBox> componentMap = new HashMap<>();

        @Override
        public Component getComponent(final Customer rowData) {
            ComboBox comboBox = componentMap.get(rowData);
            if (comboBox == null) {
                comboBox = create(rowData);
                componentMap.put(rowData, comboBox);
            }

            return comboBox;
        }

        private ComboBox create(final Customer data) {
            final ComboBox comboBox = new ComboBox();
            comboBox.setWidth(150, Sizeable.Unit.PIXELS);
            comboBox.setHeight(50, Sizeable.Unit.PIXELS);
            comboBox.addItems(Customer.Food.FISH, Customer.Food.HAMBURGER, Customer.Food.VEGETABLES);
            comboBox.setValue(data.getFood());
            return comboBox;
        }
    }

    private void init() {

        setSizeFull();
        setMargin(true);
        setSpacing(true);

        TabSheet sheet = new TabSheet();
        sheet.setSizeFull();
        sheet.addTab(createGrid(), "Tab 1");
        sheet.addTab(createGrid(), "Tab 2");

        addComponent(sheet);
        setExpandRatio(sheet, 1.0f);

    }

    private Grid createGrid() {
        ComponentGrid<Customer> grid = new ComponentGrid<>(Customer.class);

        grid.setSizeFull();

        grid.setRows(CustomerProvider.createDummyData());

        grid.addComponentColumn(Customer.FOOD, new TestComponentGenerator());
        grid.setColumns(Customer.FIRST_NAME, Customer.FOOD);
        return grid;

    }

}

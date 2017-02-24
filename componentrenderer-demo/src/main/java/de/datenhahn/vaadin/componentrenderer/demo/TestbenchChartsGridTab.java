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

import com.vaadin.addon.charts.Sparkline;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGrid;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentPropertyGenerator;

import java.util.Arrays;

/**
 * Demonstrates the use of the typed ComponentGrid
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class TestbenchChartsGridTab extends VerticalLayout {

    private static final String GENERATED_FOOD_ICON = "foodIcon";
    private static final String GENERATED_RATING = "rating";
    private static final String GENERATED_DELETE = "delete";
    private static final String GENERATED_DETAILS_ICONS = "detailsIcons";
    private FocusPreserveExtension focusPreserveExtension;

    public TestbenchChartsGridTab() {
        init();
    }

    private Component createGrid() {


        BeanItemContainer<MyPojo> container = new BeanItemContainer<MyPojo>(MyPojo.class);
        for (int i = 0; i < 5000; ++i) {
            container.addBean(new MyPojo(i));
        }

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        Grid grid = new Grid(gpc);

        ComponentCellKeyExtension.extend(grid);
        focusPreserveExtension = FocusPreserveExtension.extend(grid);
        DetailsKeysExtension.extend(grid);

        grid.setWidth(900, Unit.PIXELS);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        gpc.addGeneratedProperty("chart", new ComponentPropertyGenerator<>(MyPojo.class, pojo -> {
            return new Sparkline(130, 30, pojo.getNumbers());
        }));
        gpc.addGeneratedProperty("combo", new ComponentPropertyGenerator<>(MyPojo.class, pojo -> {
            return new ComboBox("Test", Arrays.asList(pojo.getNumbers()));
        }));
        grid.getColumn("chart").setRenderer(new ComponentRenderer());
        grid.getColumn("combo").setRenderer(new ComponentRenderer());
        grid.setColumns("baz", "foo", "bar", "chart", "combo");
        grid.addItemClickListener(
                event -> grid.getColumn("chart").setHeaderCaption("Chart"));
        return grid;
    }

    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);
        Component grid = createGrid();
        addComponent(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                               " the classic grid"));
        addComponent(ViewComponents.createEnableDisableCheckBox((Grid) grid));

        addComponent(grid);
        setExpandRatio(grid, 1.0f);

    }

}

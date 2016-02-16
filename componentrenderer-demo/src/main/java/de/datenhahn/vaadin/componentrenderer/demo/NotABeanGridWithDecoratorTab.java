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

import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGridDecorator;

/**
 * Demonstrates the use of a simple grid without any bean container.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class NotABeanGridWithDecoratorTab extends VerticalLayout {

    private FocusPreserveExtension focusPreserveExtension;

    public NotABeanGridWithDecoratorTab() {
        init();
    }

    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(new Label("Look at the sourcecode to see the difference between the typed ComponentGrid and using" +
                " the classic grid"));

        Grid grid = new Grid();
        grid.addColumn("foo");
        grid.addRow("1");
        grid.addRow("2");
        grid.addRow("3");
        grid.addRow("4");
        ComponentGridDecorator componentGridDecorator = new ComponentGridDecorator<>(grid, null);
        addComponent(ViewComponents.createEnableDisableCheckBox(grid));

        grid.setSizeFull();

         // Initialize DetailsGenerator (Caution: the DetailsGenerator is set to null
        // when grid#setContainerDatasource is called, so make sure you call setDetailsGenerator
        // after setContainerDatasource
        grid.setDetailsGenerator(new CustomerDetailsGenerator());

        componentGridDecorator.addComponentColumn("Just some", e -> new Label("some"+e));


        grid.setColumns("Just some");

        addComponent(grid);
        setExpandRatio(grid, 1.0f);
    }

}

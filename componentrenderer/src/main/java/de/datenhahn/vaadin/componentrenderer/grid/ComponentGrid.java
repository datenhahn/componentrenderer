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

package de.datenhahn.vaadin.componentrenderer.grid;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Grid;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;

import java.util.Collection;

/**
 * A typed version of the grid using a {@link BeanItemContainer} to store
 * the typed grid data and a {@link GeneratedPropertyContainer} to provide
 * generated component-columns.
 *
 * Also offers some convenience methods for this use-case (typed, use of components).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGrid<T> extends Grid {
    private GeneratedPropertyContainer gpc = null;
    private final BeanItemContainer<T> bc;
    private final Class<T> typeOfRows;
    private final FocusPreserveExtension focusPreserveExtension;

    public ComponentGrid(Class<T> typeOfRows) {
        super();
        ComponentCellKeyExtension.extend(this);
        focusPreserveExtension = FocusPreserveExtension.extend(this);
        DetailsKeysExtension.extend(this);
        this.typeOfRows = typeOfRows;
        this.bc = new BeanItemContainer<>(typeOfRows);
        setContainerDataSource(bc);
    }

    /**
     * Replaces the current grid container with a {@link GeneratedPropertyContainer}
     * while preserving the {@link DetailsGenerator}.
     */
    private void initGpc() {
        gpc = new GeneratedPropertyContainer(getContainerDataSource());
        DetailsGenerator details = getDetailsGenerator();
        setContainerDataSource(gpc);
        setDetailsGenerator(details);
    }

    /**
     * Add a generated component column to the ComponentGrid.
     *
     * @param propertyId the generated column's property-id
     * @param generator the component-generator
     *
     * @return the grid for method chaining
     */
    public ComponentGrid<T> addComponentColumn(Object propertyId, ComponentGenerator<T> generator) {
        if (gpc == null) {
            initGpc();
        }
        gpc.addGeneratedProperty(propertyId, new ComponentPropertyGenerator<>(typeOfRows, generator));
        getColumn(propertyId).setRenderer(new ComponentRenderer());
        return this;
    }


    /**
     * Remove all items from the underlying {@link BeanItemContainer} and add
     * the new beans.
     *
     * @param beans a collection of beans
     *
     * @return the grid for method chaining
     */
    public ComponentGrid<T> setRows(Collection<T> beans) {
        bc.removeAllItems();
        bc.addAll(beans);
        return this;
    }

    /**
     * Remove a bean from the grid.
     *
     * @return the grid for method chaining
     */
    public ComponentGrid<T> remove(T bean) {
        bc.removeItem(bean);
        return this;
    }

    /**
     * Refreshes the grid preserving its current cell focus.
     *
     * @return the grid for method chaining
     */
    public ComponentGrid<T> refresh() {
        focusPreserveExtension.saveFocus();
        setCellStyleGenerator(getCellStyleGenerator());
        focusPreserveExtension.restoreFocus();
        return this;
    }
}

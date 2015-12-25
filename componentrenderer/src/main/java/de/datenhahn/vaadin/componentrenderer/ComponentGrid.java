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
package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;

import java.util.Iterator;
import java.util.LinkedHashSet;

import static com.google.gwt.thirdparty.guava.common.collect.Iterators.addAll;

/**
 * A implementation of the ComponentGrid. You can use this directly, or use
 * the cglib-Decorator {@link ComponentRendererGridDecorator} to decorate
 * your own implementation.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGrid extends Grid implements ComponentRendererProvider, GridUtilityMethods {

    private final ComponentRendererComponentStore componentRendererComponentStore;

    public ComponentGrid() {
        componentRendererComponentStore = ComponentRendererComponentStore.linkWith(this);
    }

    @Override
    public Iterator<Component> iterator() {

        LinkedHashSet<Component> allComponents = new LinkedHashSet<Component>();

        addAll(allComponents, super.iterator());
        allComponents.addAll(componentRendererComponentStore.getRendererComponents());

        return allComponents.iterator();
    }

    @Override
    public void setContainerDataSource(Container.Indexed container) {
        super.setContainerDataSource(container);
        componentRendererComponentStore.addContainerChangeListeners();
    }

    @Override
    public ComponentRenderer createComponentRenderer() {
        return componentRendererComponentStore.createComponentRenderer();
    }

    @Override
    public void forceReRender() {
        setCellStyleGenerator(getCellStyleGenerator());
    }
}

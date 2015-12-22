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
package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * A implementation of the ComponentGrid. You can use this directly, or use
 * the cglib-Decorator {@link ComponentRendererGridDecorator} to decorate
 * your own implementation.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentGrid extends Grid implements ComponentRendererProvider, GridUtilityMethods {

    private ComponentRendererExtension componentRendererExtension;

    public ComponentGrid() {
        componentRendererExtension = ComponentRendererExtension.extend(this);
    }

    @Override
    public Iterator<Component> iterator() {
        LinkedHashSet<Component> componentList = new LinkedHashSet<Component>();
        Iterator<Component> iterator = super.iterator();
        while (iterator.hasNext()) {
            componentList.add(iterator.next());
        }
        componentList.addAll(componentRendererExtension.getRendererComponents());
        return componentList.iterator();

    }

    @Override
    public void setContainerDataSource(Container.Indexed container) {
        super.setContainerDataSource(container);
        componentRendererExtension.addChangeListeners();
    }

    @Override
    public ComponentRenderer createComponentRenderer() {
        return componentRendererExtension.createComponentRenderer();
    }

    @Override
    public void forceReRender() {
        setCellStyleGenerator(getCellStyleGenerator());
    }
}

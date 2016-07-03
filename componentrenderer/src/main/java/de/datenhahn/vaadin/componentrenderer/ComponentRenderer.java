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

import com.vaadin.data.Item;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.communication.data.DataGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends Grid.AbstractRenderer<Component> implements DataGenerator {

    private final HashMap<Object, Set<Component>> components = new HashMap<>();
    private boolean isRemovalInProgress = false;

    public ComponentRenderer() {
        super(Component.class, null);
    }

    @Override
    public JsonValue encode(Component component) {

        // 1: add component to grid, so connector id can be encoded
        if (component != null) {
            addComponentToGrid(component);
            return Json.create(component.getConnectorId());
        } else {
            return Json.createNull();
        }
    }

    /**
     * When the renderer is detached from the grid (e.g. when the column is removed)
     * release all components to make them eligible for garbage collection and remove
     * the DataGenerator extension for this renderer from the Grid.
     *
     * @param parent the parent connector
     */
    @Override
    public void setParent(ClientConnector parent) {

        // detect a column removal (the renderer is being detached)
        if (getParent() != null && parent == null) {

            if (!isRemovalInProgress) {
                for (Set<Component> rowComponents : components.values()) {
                    for (Component c : rowComponents) {
                        removeComponentFromGrid(c);
                    }
                }
                components.clear();

                // it is important to also detach the renderers @link{DataGenerator}
                // when the renderer is detached. The @link{com.vaadin.ui.Grid.AbstractGridExtension#remove}
                // does that, but calls setParent(null) again, which would lead to endless recursion
                // so we set a flag that we currently are already in removal
                // and stop further calls to remove().
                //
                isRemovalInProgress = true;
                remove();
            } else {
                isRemovalInProgress = false;
            }
        }

        super.setParent(parent);

        // VERY IMPORTANT: registers the DataGenerator extension
        // with the grid. The reason the extend method is deprecated
        // with renderers is that normal gwt-based renderers should not have
        // a direct dependency to the grid. In case of the componentrenderer
        // it must have this dependency to function properly.
        if (parent != null) {
            extend(getParentGrid());
        }
    }


    private void putComponent(Object itemId, Component component) {

        if (!components.containsKey(itemId)) {
            components.put(itemId, new HashSet<Component>());
        }

        components.get(itemId).add(component);
    }

    @Override
    public void generateData(Object itemId, Item item, JsonObject jsonObject) {

        Set<Component> componentsInUse = new HashSet<>();

        for (String key : jsonObject.getObject("d").keys()) {


            if (getColumn(key).getRenderer() == this) {
                // 2: VERY IMPORTANT get the component from the connector tracker !!!
                //    if you use a GeneratedPropertyContainer and call get Value you will
                //    get a different component

                if (jsonObject.getObject("d").get(key) != Json.createNull()) {
                    Component current = (Component) UI.getCurrent()
                                                      .getConnectorTracker()
                                                      .getConnector(jsonObject.getObject("d").getString(key));
                    putComponent(itemId, current);
                    componentsInUse.add(current);
                }
            }


            // find all components, which are no longer in use for this item id
            Set<Component> itemIdComponents = components.get(itemId);

            if (itemIdComponents != null) {

                Set<Component> unusedComponents = new HashSet<>(itemIdComponents);
                unusedComponents.removeAll(componentsInUse);

                // remove unused components from current tracking
                components.get(itemId).removeAll(unusedComponents);

                // destroy unused components
                destroyComponents(unusedComponents);
            }
        }

    }

    @Override
    public void destroyData(Object itemId) {
        if (components.containsKey(itemId)) {
            destroyComponents(components.get(itemId));
            components.remove(itemId);
        }

    }

    private void destroyComponents(Set<Component> components) {
        for (Component component : components) {
            if (component != null) {
                removeComponentFromGrid(component);
            }
        }
    }

}



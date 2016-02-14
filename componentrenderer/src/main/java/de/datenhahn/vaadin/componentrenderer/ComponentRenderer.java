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

    public ComponentRenderer() {
        super(Component.class, null);
    }

    @Override
    public JsonValue encode(Component component) {

        // 1: add component to grid, so connector id can be encoded
        if(component != null) {
            addComponentToGrid(component);
            return Json.create(component.getConnectorId());
        } else {
            return Json.createNull();
        }
    }

    @Override
    public void setParent(ClientConnector parent) {

        if (getParent() != null && parent == null) {
            for (Set<Component> rowComponents : components.values())
                for (Component c : rowComponents) {
                    removeComponentFromGrid(c);
                }
        }

        super.setParent(parent);

        // VERY IMPORTANT: registers the DataGenerator extension
        extend(getParentGrid());
    }


    private void putComponent(Object itemId, Component component) {

        if (!components.containsKey(itemId)) {
            components.put(itemId, new HashSet<Component>());
        }

        components.get(itemId).add(component);
    }

    @Override
    public void generateData(Object itemId, Item item, JsonObject jsonObject) {

        // destroy all previous data for this row, sometimes it happens that
        // destroyData is not called before generateData is called
        destroyData(itemId);

        for (String key : jsonObject.getObject("d").keys()) {
            Class columnType = getParentGrid().getContainerDataSource().getType(getColumn(key).getPropertyId());
            if (columnType.isAssignableFrom(Component.class)) {
                // 2: VERY IMPORTANT get the component from the connector tracker !!!
                //    if you use a GeneratedPropertyContainer and call get Value you will
                //    get a different component
                Component current = (Component) UI.getCurrent().getConnectorTracker().getConnector(jsonObject.getObject("d").getString(key));
                putComponent(itemId, current);
            }
        }

    }

    @Override
    public void destroyData(Object itemId) {
        if (components.containsKey(itemId)) {
            for (Component component : components.get(itemId)) {
                if(component != null) {
                    removeComponentFromGrid(component);
                }
            }
            components.remove(itemId);
        }

    }
}



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

import com.vaadin.data.Item;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.communication.data.DataGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends Grid.AbstractRenderer<Component> implements DataGenerator {

    Set<Component> components = new HashSet<Component>();

    public ComponentRenderer() {
        super(Component.class, null);
    }

    @Override
    public JsonValue encode(Component component) {
        addComponentToGrid(component);
        components.add(component);
        return Json.create(component.getConnectorId());
    }

    @Override
    public void setParent(ClientConnector parent) {

        if (getParent() != null && parent == null) {
            for (Component c : components) {
                removeComponentFromGrid(c);
            }
        }

        super.setParent(parent);
    }


    @Override
    public void generateData(Object itemId, Item item, JsonObject jsonObject) {
        // noop
    }

    @Override
    public void destroyData(Object itemId) {
        Item item = getParentGrid().getContainerDataSource().getItem(itemId);

        Collection<?> propertyIds = item.getItemPropertyIds();

        for(Object propertyId : propertyIds) {
            if(item.getItemProperty(propertyId).getType().isAssignableFrom(Component.class)) {
                Component component = (Component)item.getItemProperty(propertyId).getValue();
                components.remove(component);
                removeComponentFromGrid(component);
            }
        }
    }
}



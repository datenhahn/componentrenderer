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

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import elemental.json.Json;
import elemental.json.JsonValue;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends Grid.AbstractRenderer<Component> {

    private final ComponentRendererComponentStore componentStore;

    public ComponentRenderer(ComponentRendererComponentStore componentStore) {
        super(Component.class, null);
        this.componentStore = componentStore;
    }

    @Override
    public JsonValue encode(Component component) {
        componentStore.addComponent(component);
        return Json.create(component.getConnectorId());
    }

}

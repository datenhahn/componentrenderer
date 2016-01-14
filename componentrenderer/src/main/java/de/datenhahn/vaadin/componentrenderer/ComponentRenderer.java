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
import de.datenhahn.vaadin.componentrenderer.client.connectors.ComponentRendererServerRpc;
import elemental.json.Json;
import elemental.json.JsonValue;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends Grid.AbstractRenderer<Component> {

    private final ComponentRendererComponentTracker componentStore;

    public ComponentRenderer(ComponentRendererComponentTracker componentStore) {
        super(Component.class, null);
        this.componentStore = componentStore;
        registerRpc(new MyComponentRendererServerRpc(componentStore));
    }

    @Override
    public JsonValue encode(Component component) {
        doComponentBookkeeping(component);
        return Json.create(component.getConnectorId());
    }

    private void doComponentBookkeeping(Component component) {
        componentStore.addComponent(component);
        componentStore.unmarkForRemoval(component);
        componentStore.removeMarkedComponents();
    }

    private class MyComponentRendererServerRpc implements ComponentRendererServerRpc {

        private ComponentRendererComponentTracker componentStore;

        public MyComponentRendererServerRpc(ComponentRendererComponentTracker componentStore) {
            this.componentStore = componentStore;
        }

        @Override
        public void removeComponentConnectors(final String[] connectorIds) {

            for (String connectorId : connectorIds) {
                componentStore.markForRemoval((Component) getUI().getConnectorTracker().getConnector(connectorId));
            }
        }


    }

}



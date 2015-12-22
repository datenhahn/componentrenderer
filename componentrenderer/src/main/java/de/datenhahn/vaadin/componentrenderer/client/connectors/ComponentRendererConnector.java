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
package de.datenhahn.vaadin.componentrenderer.client.connectors;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.AbstractRendererConnector;
import com.vaadin.shared.ui.Connect;
import de.datenhahn.vaadin.componentrenderer.client.ComponentRenderer;
import elemental.json.JsonValue;

/**
 * The ComponentRenderer's Connector. Links the Renderer with the {@link ComponentRendererExtensionConnector}
 * and handles the decoding of the ComponentConnectorIds into Components.
 *
 * @see ComponentRenderer
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(de.datenhahn.vaadin.componentrenderer.ComponentRenderer.class)
public class ComponentRendererConnector extends AbstractRendererConnector<ComponentConnector> {

    /**
     * Retrieve the renderer and link it with the {@link ComponentRendererExtensionConnector}.
     *
     * @return the renderer
     */
    @Override
    public ComponentRenderer getRenderer() {

        return (ComponentRenderer) super.getRenderer();
    }

    /**
     * Decodes the connectorId from the JSON to the real connector.
     *
     * @param value the json value to decode
     * @return the component connector to be rendered by the ComponentRenderer
     */
    @Override
    public ComponentConnector decode(JsonValue value) {
        ServerConnector componentConnector = ConnectorMap.get(getConnection()).getConnector(value.toString());
        return (ComponentConnector) componentConnector;
    }
}

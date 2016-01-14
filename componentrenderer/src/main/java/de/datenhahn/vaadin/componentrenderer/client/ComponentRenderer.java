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
package de.datenhahn.vaadin.componentrenderer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.renderers.WidgetRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;
import de.datenhahn.vaadin.componentrenderer.ComponentRendererGridDecorator;
import de.datenhahn.vaadin.componentrenderer.client.connectors.ComponentRendererConnector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A renderer for vaadin components. Depends on the parent-grid being decorated by the
 * {@link ComponentRendererGridDecorator}.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends WidgetRenderer<ComponentConnector, SimplePanel> {

    public static final int MAX_REMOVABLE_CONNECTORS = 100;
    private ComponentRendererConnector connector;
    private HashMap<SimplePanel, ComponentConnector> widgetConnectorMap = new HashMap<SimplePanel, ComponentConnector>();
    private Set<String> removeableConnectors = new HashSet<String>(MAX_REMOVABLE_CONNECTORS);

    @Override
    public SimplePanel createWidget() {
        SimplePanel panel = GWT.create(SimplePanel.class);
        panel.setWidth("100%");
        panel.setHeight("100%");
        return panel;
    }

    @Override
    public void render(RendererCellReference rendererCellReference, ComponentConnector componentConnector, SimplePanel panel) {
        if (componentConnector != null) {

            ComponentConnector previousConnector = widgetConnectorMap.get(panel);

            if (previousConnector != componentConnector) {

                widgetConnectorMap.put(panel, componentConnector);
                panel.setWidget(componentConnector.getWidget());

                if(previousConnector != null) {
                    markConnectorForRemoval(previousConnector.getConnectorId());
                }
            }

        } else {
            panel.clear();
        }
    }

    /**
     * Marks a component connector as removable and flushes the marked
     * connector-ids to the server when the threshold {@link #MAX_REMOVABLE_CONNECTORS} is hit.
     *
     * @param connectorId the id of the connector
     */
    private void markConnectorForRemoval(String connectorId) {
        removeableConnectors.add(connectorId);
        if (removeableConnectors.size() >= MAX_REMOVABLE_CONNECTORS) {
            flushRemovableConnectors();
        }
    }

    /**
     * Flush the removable component connectors to the serverside and
     * clear the client-side removableComponents-bucket.
     */
    private void flushRemovableConnectors() {
        connector.getRpc().removeComponentConnectors(removeableConnectors.toArray(new String[removeableConnectors.size()]));
        removeableConnectors.clear();
    }

    /**
     * Set the renderer's connector.
     *
     * @param connector the renderer's connector
     */
    public void setConnector(ComponentRendererConnector connector) {
        this.connector = connector;
    }
}

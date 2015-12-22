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
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import de.datenhahn.vaadin.componentrenderer.ComponentRendererExtension;

/**
 * Connector for the ComponentRendererExtension.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(ComponentRendererExtension.class)
public class ComponentRendererExtensionConnector extends AbstractExtensionConnector implements ComponentRendererExtensionClientRpc {

    /**
     * Extends a grid and registers the client-side rpc.
     *
     * @param target the target grid's connector
     */
    @Override
    protected void extend(ServerConnector target) {
        registerRpc(ComponentRendererExtensionClientRpc.class, this);
    }

    @Override
    public void addComponentConnector(String connectorId) {
        ServerConnector componentConnector = ConnectorMap.get(getConnection()).getConnector(connectorId);
        componentConnector.setParent(getParent());
    }

    @Override
    public void removeComponentConnector(String connectorId) {
        ComponentConnector componentConnector = (ComponentConnector) ConnectorMap.get(getConnection()).getConnector(connectorId);
        componentConnector.getWidget().removeFromParent();
        componentConnector.setParent(null);
    }
}

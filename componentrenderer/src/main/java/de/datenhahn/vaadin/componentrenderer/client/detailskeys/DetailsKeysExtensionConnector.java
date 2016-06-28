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

package de.datenhahn.vaadin.componentrenderer.client.detailskeys;

import com.google.gwt.user.client.Timer;
import com.vaadin.addon.charts.client.ui.HighchartWidget;
import com.vaadin.addon.charts.shared.ChartConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.GridConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.widget.grid.DataAvailableEvent;
import com.vaadin.client.widget.grid.DataAvailableHandler;
import com.vaadin.client.widget.grid.events.ColumnResizeEvent;
import com.vaadin.client.widget.grid.events.ColumnResizeHandler;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.grid.Range;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;

/**
 * Handles the expansion and collapsing of the detailsrow with STRG+DOWN (expand) and STRG+UP (collapse).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(DetailsKeysExtension.class)
public class DetailsKeysExtensionConnector extends AbstractExtensionConnector {

    private final DetailsOpenCloseServerRpc detailsRpc = getRpcProxy(DetailsOpenCloseServerRpc.class);


    @Override
    protected void extend(ServerConnector target) {
        Grid grid = ((GridConnector) target).getWidget();
        grid.addBodyKeyDownHandler(new DetailsKeyDownHandler(detailsRpc));
        final GridConnector gridConnector = ((GridConnector) target);
        final Timer timer = new Timer() {
            @Override
            public void run() {
                for(ServerConnector connector : gridConnector.getChildren()) {
                    if(connector instanceof ChartConnector) {
                        ((HighchartWidget) ((ChartConnector) connector).getWidget()).updateSize();
                    }
                }
            }
        };
        grid.addDataAvailableHandler(new DataAvailableHandler() {
            @Override
            public void onDataAvailable(DataAvailableEvent event) {
                Range range = event.getAvailableRows();
                if(range.contains(0)) {
                    timer.schedule(50);
                }
            }

        });
    }
}
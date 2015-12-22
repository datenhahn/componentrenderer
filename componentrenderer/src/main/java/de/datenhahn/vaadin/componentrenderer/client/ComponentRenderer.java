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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.renderers.WidgetRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;
import de.datenhahn.vaadin.componentrenderer.ComponentRendererGridDecorator;

/**
 * A renderer for vaadin components. Depends on the parent-grid being decorated by the
 * {@link ComponentRendererGridDecorator}.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends WidgetRenderer<ComponentConnector, FlowPanel> {

    @Override
    public FlowPanel createWidget() {
        return GWT.create(FlowPanel.class);
    }

    @Override
    public void render(RendererCellReference rendererCellReference, ComponentConnector componentConnector, FlowPanel flowPanel) {
        if (componentConnector != null) {
            replaceCurrentWidget(flowPanel, componentConnector.getWidget());
        } else {
            flowPanel.clear();
        }
    }

    /**
     * Replaces the widget in the flow panel with a new widget.
     *
     * @param flowPanel the flow panel
     * @param newWidget the new widget
     */
    private void replaceCurrentWidget(FlowPanel flowPanel, Widget newWidget) {

        if(isUpdateNeeded(flowPanel, newWidget)) {
            flowPanel.clear();
            flowPanel.add(newWidget);
        }
    }

    /**
     * Checks if the widget in a flowpanel has changed.
     *
     * @param flowPanel the flowpanel
     * @param newWidget the new widget
     * @return true if the panel needs to be updated, false when the new widget is already in the panel and the only widget
     */
    private boolean isUpdateNeeded(FlowPanel flowPanel, Widget newWidget) {
        if (flowPanel.getWidgetCount() == 1) {

            Widget currentWidget = flowPanel.getWidget(0);

            if (currentWidget == newWidget) {
                return false;
            }
        }

        return true;
    }

}

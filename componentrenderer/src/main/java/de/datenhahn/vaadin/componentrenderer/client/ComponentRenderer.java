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

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends WidgetRenderer<ComponentConnector, SimplePanel> {


    @Override
    public SimplePanel createWidget() {
        SimplePanel panel = GWT.create(SimplePanel.class);
        panel.setWidth("100%");
        panel.setHeight("100%");
        panel.getElement().addClassName("component-cell");
        return panel;
    }

    @Override
    public void render(RendererCellReference rendererCellReference, ComponentConnector componentConnector, SimplePanel panel) {
        if (componentConnector != null) {
            panel.setWidget(componentConnector.getWidget());

        } else {
            panel.clear();
        }
    }


}

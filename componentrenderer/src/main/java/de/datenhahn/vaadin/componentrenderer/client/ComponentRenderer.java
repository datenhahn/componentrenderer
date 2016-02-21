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
package de.datenhahn.vaadin.componentrenderer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.renderers.WidgetRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

import java.util.Collection;

/**
 * A renderer for vaadin components.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRenderer extends WidgetRenderer<ComponentConnector, SimplePanel> {

    /**
     * Propagates clicks on the renderers simple panel to the grid to make the
     * row select work. Will only propagate clicks which go on the SimplePanel itself
     * or on a layout-component (css class v-layout). It is assumed, that components want
     * to capture clicks by themselves and don't want any "side-actions" being happening
     * which would distract the user.
     */
    private static final PropagationClickHandler propagationClickHandler = new PropagationClickHandler();

    @Override
    public SimplePanel createWidget() {
        final SimplePanel panel = GWT.create(SimplePanel.class);
        panel.getElement().addClassName("cr-component-cell");
        panel.sinkEvents(com.google.gwt.user.client.Event.ONCLICK);
        panel.addDomHandler(propagationClickHandler, ClickEvent.getType());
        return panel;
    }

    @Override
    public Collection<String> getConsumedEvents() {
        return super.getConsumedEvents();
    }

    @Override
    public void render(RendererCellReference rendererCellReference, ComponentConnector componentConnector,
                       SimplePanel panel)
    {
        if (componentConnector != null) {
            panel.setWidget(componentConnector.getWidget());
        } else {
            panel.clear();
        }
    }

    /**
     * Propagates clicks on the renderers simple panel to the grid to make the
     * row select work. Will only propagate clicks which go on the SimplePanel itself
     * or on a layout-component (css class v-layout). It is assumed, that components want
     * to capture clicks by themselves and don't want any "side-actions" being happening
     * which would distract the user.
     */
    private static class PropagationClickHandler implements ClickHandler {

        public static final int SINGLE_CLICK = 1;

        @Override
        public void onClick(ClickEvent clickEvent) {
            Element clickedTarget = Element.as(clickEvent.getNativeEvent().getEventTarget());

            if (clickedTarget.getClassName().contains("cr-component-cell") ||
                clickedTarget.getClassName().contains("v-layout")) {
                NativeEvent event = cloneClickEvent(clickEvent);
                clickedTarget.getParentElement().dispatchEvent(event);
            } else if (clickedTarget.getParentElement().getClassName().contains("v-checkbox")){
                // the vaadin checkbox registers clicks on its label and then
                // the click is propagated to the input field which holds the actual
                // value.
                //
                // When the clickevent is canceled after the label and never propagated
                // to the input field the value-change is lost.
                //
                // so in case the click was on an element inside a vaadin checkbox, we don't
                // cancel the event.
            } else {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }

        private NativeEvent cloneClickEvent(ClickEvent clickEvent) {
            return Document.get().createClickEvent(SINGLE_CLICK,
                                    clickEvent.getNativeEvent().getScreenX(),
                                    clickEvent.getNativeEvent().getScreenY(),
                                    clickEvent.getNativeEvent().getClientX(),
                                    clickEvent.getNativeEvent().getClientY(),
                                    clickEvent.getNativeEvent().getCtrlKey(),
                                    clickEvent.getNativeEvent().getAltKey(),
                                    clickEvent.getNativeEvent().getShiftKey(),
                                    clickEvent.getNativeEvent().getMetaKey());
        }
    }
}

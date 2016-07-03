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
package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.servlet.annotation.WebServlet;

/**
 * ComponentRenderer Demo
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Theme("demotheme")
@Widgetset("de.datenhahn.vaadin.componentrenderer.demo.DemoWidgetSet")
public class ComponentRendererDemoUI extends UI {


    private final VerticalLayout layout = new VerticalLayout();

    private void addHeader() {
        Label headerLabel = new Label("<h1>ComponentRenderer Demo</h1>", ContentMode.HTML);
        headerLabel.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(headerLabel);
        layout.addComponent(new Label("Each grid contains 10000 rows"));
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if(vaadinRequest.getPathInfo().startsWith("/testbench")) {
            setContent(new TestbenchComponentGridTab());
        } else if(vaadinRequest.getPathInfo().startsWith("/testcharts")) {
            setContent(new TestbenchChartsGridTab());
        } else {
            startDemoApp();
        }

    }

    private void startDemoApp() {
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        addHeader();


        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        layout.addComponent(tabSheet);

        tabSheet.addTab(new ClassicGridTab(),"Classic Grid");
        tabSheet.addTab(new ClassicGridWithDecoratorTab(),"Classic Grid with Decorator");
        tabSheet.addTab(new ComponentGridTab(), "Typed Component Grid");
        tabSheet.addTab(new ViritinMGridTab(), "Viritin MGrid");
        tabSheet.addTab(new NotABeanGridWithDecoratorTab(), "Not a bean grid");
        tabSheet.addTab(new ClassicGridWithStaticContainerTab(), "Classic Grid with Static Container");

        layout.setExpandRatio(tabSheet, 1.0f);
        setContent(layout);

    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ComponentRendererDemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


}

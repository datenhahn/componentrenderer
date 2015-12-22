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
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import de.datenhahn.vaadin.componentrenderer.ComponentRendererGridDecorator;
import de.datenhahn.vaadin.componentrenderer.ComponentRendererProvider;
import de.datenhahn.vaadin.componentrenderer.GridUtilityMethods;

import javax.servlet.annotation.WebServlet;

import static com.google.gwt.thirdparty.guava.common.collect.Lists.newArrayList;

/**
 * ComponentRenderer Demo
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Theme("valo")
@Widgetset("de.datenhahn.vaadin.componentrenderer.demo.DemoWidgetset")
public class ComponentRendererDemoUI extends UI {
    public static final String COL_TEXT = "Text Column";
    public static final String COL_FONT_LABEL_1 = "Component 1";
    public static final String COL_FONT_LABEL_2 = "Component 2";
    public static final String COL_FONT_LABEL_3 = "Component 3";
    public static final String COL_DELETE = "Delete";
    public static final String COL_ARROWS = "Expand Details";
    final VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout createDemoLayout(String text) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.addComponent(new Label(FontAwesome.SMILE_O.getHtml(), ContentMode.HTML));

        Label label = new Label(text);
        label.setDescription(text);

        layout.addComponent(label);

        return layout;
    }

    private void setupRootLayout() {
        layout.setSizeFull();
        layout.setMargin(true);
        setContent(layout);
    }
    private void addHeader() {
        Label headerLabel = new Label("<h1>ComponentRenderer Demo</h1>", ContentMode.HTML);
        headerLabel.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(headerLabel);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupRootLayout();
        addHeader();

        ComponentRendererGridDecorator<Grid> foo = new ComponentRendererGridDecorator<Grid>();
        final Grid myGrid = (foo).decorate(Grid.class);

        // Alternatively you can just use the ComponentGrid
        // final ComponentGrid myGrid = new ComponentGrid();

        myGrid.setWidth(100, Unit.PERCENTAGE);
        myGrid.setColumnReorderingAllowed(true);

        myGrid.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {
                return new Label("Some Details");
            }
        });


        myGrid.addColumn(COL_TEXT);
        myGrid.addColumn(COL_FONT_LABEL_1, Component.class).setRenderer(((ComponentRendererProvider) myGrid).createComponentRenderer());
        myGrid.addColumn(COL_FONT_LABEL_2, Component.class).setRenderer(((ComponentRendererProvider)myGrid).createComponentRenderer());
        myGrid.addColumn(COL_FONT_LABEL_3, Component.class).setRenderer(((ComponentRendererProvider)myGrid).createComponentRenderer());


        myGrid.addColumn(COL_DELETE, Component.class).setRenderer(((ComponentRendererProvider) myGrid).createComponentRenderer());
        myGrid.addColumn(COL_ARROWS, Component.class).setRenderer(((ComponentRendererProvider) myGrid).createComponentRenderer());

        GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(myGrid.getContainerDataSource());

        myGrid.setContainerDataSource(generatedPropertyContainer);

        generatedPropertyContainer.addGeneratedProperty(COL_DELETE, new DeleteButtonValueGenerator(myGrid));
        generatedPropertyContainer.addGeneratedProperty(COL_ARROWS, new DetailsArrowValueGenerator(myGrid));

        Button add = createButton(myGrid);
        layout.addComponent(add);

        Button del = createButtonDelete(myGrid);
        layout.addComponent(del);

        layout.addComponent(myGrid);
        layout.setExpandRatio(myGrid, 1.0f);


    }

    private Button createButtonDelete(final Grid myGrid) {
        return new Button("delete all", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {

                    myGrid.getContainerDataSource().removeAllItems();

                }
            });
    }

    private Button createButton(final Grid myGrid) {
        return new Button("add 100 rows", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    for (int i = 0; i < 100; i++) {
                        ComboBox myCombo = new ComboBox("", newArrayList("foo" + i, "bar" + i));
                        myCombo.addValueChangeListener(new Property.ValueChangeListener() {
                            @Override
                            public void valueChange(Property.ValueChangeEvent event) {
                                Notification.show("chose value: " + event.getProperty().getValue());
                            }
                        });
                        Object itemId = myGrid.getContainerDataSource().addItem();
                        Item item = myGrid.getContainerDataSource().getItem(itemId);
                        item.getItemProperty(COL_TEXT).setValue("Text " + itemId);
                        item.getItemProperty(COL_FONT_LABEL_1).setValue(createDemoLayout("1 - " + itemId));
                        item.getItemProperty(COL_FONT_LABEL_2).setValue(createDemoLayout("2 - " + itemId));
                        item.getItemProperty(COL_FONT_LABEL_3).setValue(myCombo);
                        ((GridUtilityMethods)myGrid).forceReRender();
                    }
                }
            });
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ComponentRendererDemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private static class DetailsArrowValueGenerator extends PropertyValueGenerator<Component> {
        private final Grid myGrid;

        public DetailsArrowValueGenerator(Grid myGrid) {
            this.myGrid = myGrid;
        }

        @Override
        public Component getValue(final Item item, final Object itemId, final Object propertyId) {
            final Image imageDown = new Image("",new ThemeResource("../runo/select/img/arrow-down.png"));
            final Image imageUp = new Image("",new ThemeResource("../runo/select/img/arrow-up.png"));

            imageDown.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent event) {
                    myGrid.setDetailsVisible(itemId, true);
                }
            });

            imageUp.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent event) {
                    myGrid.setDetailsVisible(itemId, false);
                }
            });

            if(myGrid.isDetailsVisible(itemId)) {
                return imageUp;
            } else {
                return imageDown;
            }
        }

        @Override
        public Class<Component> getType() {
            return Component.class;
        }
    }

    private static class DeleteButtonValueGenerator extends PropertyValueGenerator<Component> {
        private final Grid myGrid;

        public DeleteButtonValueGenerator(Grid myGrid) {
            this.myGrid = myGrid;
        }

        @Override
        public Component getValue(Item item, Object itemId, Object propertyId) {
            final Object deleteItemId = itemId;
            Button delete = new Button("delete", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    myGrid.getContainerDataSource().removeItem(deleteItemId);
                    ((GridUtilityMethods) myGrid).forceReRender();
                }
            });
            return delete;
        }

        @Override
        public Class<Component> getType() {
            return Component.class;
        }
    }
}

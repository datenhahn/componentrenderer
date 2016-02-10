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
import de.datenhahn.vaadin.componentrenderer.ComponentRenderer;
import org.fluttercode.datafactory.impl.DataFactory;

import javax.servlet.annotation.WebServlet;

import static com.google.gwt.thirdparty.guava.common.collect.Lists.newArrayList;

/**
 * ComponentRenderer Demo
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Theme("demotheme")
@Widgetset("de.datenhahn.vaadin.componentrenderer.demo.DemoWidgetset")
public class ComponentRendererDemoUI extends UI {
    public static final String COL_TEXT = "Text Column";
    public static final String COL_FONT_LABEL_1 = "Component 1";
    public static final String COL_FONT_LABEL_2 = "Component 2";
    public static final String COL_FONT_LABEL_3 = "Component 3";
    public static final String COL_DELETE = "Delete";
    public static final String COL_ARROWS = "Expand Details";
    private DataFactory testData = new DataFactory();
    final VerticalLayout layout = new VerticalLayout();

    private static final int ROW_HEIGHT = 50;

    private Label createAddress() {
        Label label = new Label(testData.getFirstName() + " " + testData.getLastName());
        label.setWidth(190, Unit.PIXELS);
        label.setDescription("Label");
        label.addStyleName("center-label");
        return label;
    }

    private CssLayout createRating() {
        CssLayout layout = new CssLayout();
        layout.setHeight(ROW_HEIGHT, Unit.PIXELS);
        layout.setWidth(150, Unit.PIXELS);

        Label overallRating = new Label(FontAwesome.STAR.getHtml(), ContentMode.HTML);
        overallRating.addStyleName("green");
        overallRating.setDescription("Very good : " + testData.getNumberBetween(90, 100) + "% Chance");
        overallRating.setWidthUndefined();
        overallRating.setWidth(40, Unit.PIXELS);
        overallRating.setHeight(ROW_HEIGHT, Unit.PIXELS);
        layout.addComponent(overallRating);


        Label carRating = new Label(FontAwesome.CAR.getHtml(), ContentMode.HTML);
        carRating.addStyleName("red");
        carRating.setDescription("Unlikely : " + testData.getNumberBetween(1, 15) + "%");
        carRating.setWidthUndefined();
        carRating.setWidth(40, Unit.PIXELS);
        carRating.setHeight(ROW_HEIGHT, Unit.PIXELS);

        layout.addComponent(carRating);

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


        final Grid myGrid = new Grid();

        // Alternatively you can just use the ComponentGrid
        // final ComponentGrid myGrid = new ComponentGrid();

        myGrid.setWidth(100, Unit.PERCENTAGE);
        myGrid.setColumnReorderingAllowed(true);


        myGrid.addItemClickListener(event -> {
            myGrid.setDetailsVisible(event.getItemId(), true);
        });

        myGrid.addColumn(COL_ARROWS, Component.class).setRenderer(new ComponentRenderer()).setWidth(80);
        myGrid.addColumn(COL_TEXT);
        myGrid.addColumn(COL_FONT_LABEL_1, Component.class).setRenderer(new ComponentRenderer());
        myGrid.addColumn(COL_FONT_LABEL_2, Component.class).setRenderer(new ComponentRenderer());
        myGrid.addColumn(COL_FONT_LABEL_3, Component.class).setRenderer(new ComponentRenderer()).setWidth(280);

        myGrid.addColumn(COL_DELETE, Component.class).setRenderer(new ComponentRenderer());
        myGrid.setFrozenColumnCount(1);


        GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(myGrid.getContainerDataSource());

        myGrid.setContainerDataSource(generatedPropertyContainer);
        myGrid.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {
                VerticalLayout layout = new VerticalLayout();
                layout.setHeight(100, Unit.PIXELS);
                layout.addComponent(new Label("FOO"));
                return layout;

            }
        });
        generatedPropertyContainer.addGeneratedProperty(COL_DELETE, new DeleteButtonValueGenerator(myGrid));
        generatedPropertyContainer.addGeneratedProperty(COL_ARROWS, new DetailsArrowValueGenerator(myGrid));

        Button add = createButton(myGrid);
        layout.addComponent(add);

        Button del = createButtonDelete(myGrid);
        layout.addComponent(del);

        CheckBox enableDisableCheckBox = createEnableDisableCheckBox(myGrid);
        layout.addComponent(enableDisableCheckBox);

        layout.addComponent(myGrid);
        layout.setExpandRatio(myGrid, 1.0f);


    }

    private CheckBox createEnableDisableCheckBox(final Grid myGrid) {
        CheckBox checkBox = new CheckBox("enable/disable");
        checkBox.setValue(myGrid.isEnabled());
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                myGrid.setEnabled(!myGrid.isEnabled());
            }
        });
        return checkBox;
    }

    private Button createButtonDelete(final Grid myGrid) {
        Button button = new Button("delete all", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                myGrid.getContainerDataSource().removeAllItems();

            }
        });
        button.setHeight(ROW_HEIGHT, Unit.PIXELS);
        button.setWidth(100, Unit.PIXELS);
        return button;
    }

    private Button createButton(final Grid myGrid) {
        return new Button("add 1000 rows", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                for (int i = 0; i < 1000; i++) {
                    ComboBox myCombo = new ComboBox("", newArrayList("foo" + i, "bar" + i));

                    myCombo.setHeight(50, Unit.PIXELS);
                    myCombo.setWidth(250, Unit.PIXELS);
                    myCombo.addValueChangeListener(new Property.ValueChangeListener() {
                        @Override
                        public void valueChange(Property.ValueChangeEvent event) {
                            Notification.show("chose value: " + event.getProperty().getValue());
                        }
                    });
                    Object itemId = myGrid.getContainerDataSource().addItem();
                    Item item = myGrid.getContainerDataSource().getItem(itemId);
                    item.getItemProperty(COL_TEXT).setValue("Text " + itemId);
                    item.getItemProperty(COL_FONT_LABEL_1).setValue(createAddress());
                    item.getItemProperty(COL_FONT_LABEL_2).setValue(createRating());
                    item.getItemProperty(COL_FONT_LABEL_3).setValue(myCombo);
                    //((GridUtilityMethods)myGrid).forceReRender();
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
            final Image imageDown = new Image("", new ThemeResource("../demotheme/img/caret-down.png"));
            final Image imageUp = new Image("", new ThemeResource("../demotheme/img/caret-up.png"));
            imageDown.setHeight(32,Unit.PIXELS);
            imageDown.setWidth(32, Unit.PIXELS);
            imageUp.setHeight(32,Unit.PIXELS);
            imageDown.setWidth(32, Unit.PIXELS);

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

            if (myGrid.isDetailsVisible(itemId)) {
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
                }
            });
            delete.setHeight(ROW_HEIGHT, Unit.PIXELS);
            delete.setWidth(150, Unit.PIXELS);
            return delete;
        }

        @Override
        public Class<Component> getType() {
            return Component.class;
        }
    }
}

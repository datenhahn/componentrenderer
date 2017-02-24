/*
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

package de.datenhahn.vaadin.componentrenderer.grid;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.server.Extension;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Label;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;
import de.datenhahn.vaadin.componentrenderer.grid.header.ComponentHeaderGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.header.HtmlHeaderGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.header.TextHeaderGenerator;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComponentGridDecoratorTest {

    public static final String COLUMN_CAPTION = "caption";
    public static final String FOO_0 = "foo0";

    @Test
    public void testDecoration() {
        Grid grid = createTestGrid();
        Grid.DetailsGenerator detailsGenerator = grid.getDetailsGenerator();

        assertFalse(containsExtension(grid, FocusPreserveExtension.class));
        assertFalse(containsExtension(grid, DetailsKeysExtension.class));
        assertFalse(containsExtension(grid, ComponentCellKeyExtension.class));
        assertThat(grid.getContainerDataSource(), not(instanceOf(GeneratedPropertyContainer.class)));

        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        assertTrue(containsExtension(grid, FocusPreserveExtension.class));
        assertTrue(containsExtension(grid, DetailsKeysExtension.class));
        assertTrue(containsExtension(grid, ComponentCellKeyExtension.class));
        assertThat(grid.getContainerDataSource(), is(instanceOf(GeneratedPropertyContainer.class)));
        assertThat(grid.getDetailsGenerator(), is(sameInstance(detailsGenerator)));
    }

    @Test
    public void testGetFocusPreserveExtension() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        FocusPreserveExtension focusPreserveExtension = (FocusPreserveExtension) extractExtension(grid,
                FocusPreserveExtension.class);

        assertThat(focusPreserveExtension, is(sameInstance(decorator.getFocusPreserveExtension())));
    }

    @Test
    public void testImplementsSerializable() {
        assertTrue(Serializable.class.isAssignableFrom(ComponentGridDecorator.class));
    }

    @Test
    public void testGetGrid() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        assertThat(grid, is(sameInstance(decorator.getGrid())));
    }

    @Test
    public void testAddComponentColumn() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        Item firstItem = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));
        assertThat((String) firstItem.getItemProperty(COLUMN_CAPTION).getValue(), is(FOO_0));

        decorator.addComponentColumn(COLUMN_CAPTION, new ComponentGenerator<ExampleBean>() {
            @Override
            public Component getComponent(ExampleBean bean) {
                return new Label(bean.getCaption());
            }
        });

        firstItem = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));
        assertThat(firstItem.getItemProperty(COLUMN_CAPTION).getValue(), is(instanceOf(Label.class)));

        Label firstLabel = (Label) firstItem.getItemProperty(COLUMN_CAPTION).getValue();
        assertThat(firstLabel.getValue(), is(FOO_0));
    }

    @Test
    public void testSetRows() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));

        List<ExampleBean> newBeans = new ArrayList<>();

        for(int i=0;i<2;i++) {
            ExampleBean bean = new ExampleBean();
            bean.setCaption("new" +i);
            bean.setIsSomething(true);
            newBeans.add(bean);
        }

        decorator.setRows(newBeans);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(2));

        Item firstItem = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));
        assertThat((String)firstItem.getItemProperty(COLUMN_CAPTION).getValue(), is("new0"));

    }

    @Test
    public void testAddRemove() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        Object firstBean = grid.getContainerDataSource().getIdByIndex(0);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(true));

        decorator.remove((ExampleBean)firstBean);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(9));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(false));

        decorator.add((ExampleBean) firstBean);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(true));
    }

    @Test
    public void testRefresh() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        // just check that no exception fly
        decorator.refresh();
    }

    @Test
    public void testGenerateTextHeaders() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        decorator.generateHeaders(new TextHeaderGenerator() {
            @Override
            public String getHeader(Object propertyId) {
                return "TEXT:"+ propertyId;
            }
        });

        headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();
        assertThat(headerText, is("TEXT:caption"));
    }

    @Test
    public void testGenerateHtmlHeaders() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        decorator.generateHeaders(new HtmlHeaderGenerator() {
            @Override
            public String getHeader(Object propertyId) {
                return "HTML:<b>"+ propertyId + "</b>";
            }
        });

        headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getHtml();
        assertThat(headerText, is("HTML:<b>caption</b>"));
    }

    @Test
    public void testGenerateComponentHeaders() {
        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        decorator.generateHeaders(new ComponentHeaderGenerator() {
            @Override
            public Label getHeader(Object propertyId) {
                return new Label("" + propertyId);
            }
        });

        Component headerComponent = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getComponent();
        assertThat(headerComponent, instanceOf(Label.class));
    }

    private Grid createTestGrid() {
        Grid grid = new Grid();
        BeanItemContainer<ExampleBean> bic = new BeanItemContainer<>(ExampleBean.class);
        grid.setContainerDataSource(bic);
        grid.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {
                return new Label("foo");
            }
        });
        for(int i = 0; i < 10;i++) {
            ExampleBean bean = new ExampleBean();
            bean.setCaption("foo" +i);
            bean.setIsSomething(true);
            bic.addBean(bean);
        }
        return grid;
    }

    private boolean containsExtension(Grid grid, Class extensionClass) {
        return extractExtension(grid, extensionClass) != null;
    }

    private Object extractExtension(Grid grid, Class extensionClass) {
        for (Extension extension : grid.getExtensions()) {
            if (extension.getClass().isAssignableFrom(extensionClass)) {
                return extension;
            }
        }
        return null;
    }
}
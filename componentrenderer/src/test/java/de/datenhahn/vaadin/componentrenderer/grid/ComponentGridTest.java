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

import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.server.Extension;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import de.datenhahn.vaadin.componentrenderer.ComponentCellKeyExtension;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;
import de.datenhahn.vaadin.componentrenderer.FocusPreserveExtension;
import de.datenhahn.vaadin.componentrenderer.grid.header.ComponentHeaderGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.header.HtmlHeaderGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.header.TextHeaderGenerator;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertTrue;

public class ComponentGridTest {

    public static final String COLUMN_CAPTION = "caption";
    public static final String FOO_0 = "foo0";

    @Test
    public void testDecoration() {
        ComponentGrid grid = createTestGrid();
        Grid.DetailsGenerator detailsGenerator = grid.getDetailsGenerator();

        assertTrue(containsExtension(grid, FocusPreserveExtension.class));
        assertTrue(containsExtension(grid, DetailsKeysExtension.class));
        assertTrue(containsExtension(grid, ComponentCellKeyExtension.class));
        assertThat(grid.getContainerDataSource(), is(instanceOf(GeneratedPropertyContainer.class)));
        assertThat(grid.getDetailsGenerator(), is(sameInstance(detailsGenerator)));
    }

    @Test
    public void testGetFocusPreserveExtension() {
        ComponentGrid grid = createTestGrid();
        FocusPreserveExtension focusPreserveExtension = (FocusPreserveExtension) extractExtension(grid,
                FocusPreserveExtension.class);

        assertThat(focusPreserveExtension, is(sameInstance(grid.getFocusPreserveExtension())));
    }


    @Test
    public void testAddComponentColumn() {
        ComponentGrid grid = createTestGrid();

        Item firstItem = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));
        assertThat((String) firstItem.getItemProperty(COLUMN_CAPTION).getValue(), is(FOO_0));

        grid.addComponentColumn(COLUMN_CAPTION, new ComponentGenerator<ExampleBean>() {
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
        ComponentGrid grid = createTestGrid();

        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));

        List<ExampleBean> newBeans = new ArrayList<>();

        for(int i=0;i<2;i++) {
            ExampleBean bean = new ExampleBean();
            bean.setCaption("new" +i);
            bean.setIsSomething(true);
            newBeans.add(bean);
        }

        grid.setRows(newBeans);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(2));

        Item firstItem = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));
        assertThat((String)firstItem.getItemProperty(COLUMN_CAPTION).getValue(), is("new0"));

    }

    @Test
    public void testAddRemove() {
        ComponentGrid grid = createTestGrid();
        Object firstBean = grid.getContainerDataSource().getIdByIndex(0);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(true));

        grid.remove(firstBean);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(9));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(false));

        grid.add(firstBean);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));
        assertThat(grid.getContainerDataSource().getItemIds().contains(firstBean), is(true));
    }

    @Test
    public void testAddAll() {
        ComponentGrid grid = createTestGrid();
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(10));

        List<ExampleBean> moreBeans = new ArrayList<>();
        for(int i = 10; i < 20;i++) {
            ExampleBean bean = new ExampleBean();
            bean.setCaption("foo" +i);
            bean.setIsSomething(true);
            moreBeans.add(bean);
        }

        grid.addAll(moreBeans);
        assertThat(grid.getContainerDataSource().getItemIds().size(), is(20));

    }

    @Test
    public void testRefresh() {
        ComponentGrid grid = createTestGrid();

        // just check that no exception fly
        grid.refresh();
    }

    @Test
    public void testGenerateTextHeaders() {
        ComponentGrid grid = createTestGrid();

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        grid.generateHeaders(new TextHeaderGenerator() {
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
        ComponentGrid grid = createTestGrid();

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        grid.generateHeaders(new HtmlHeaderGenerator() {
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
        ComponentGrid grid = createTestGrid();

        String headerText = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getText();

        assertThat(headerText, is("Caption"));
        grid.generateHeaders(new ComponentHeaderGenerator() {
            @Override
            public Label getHeader(Object propertyId) {
                return new Label("" + propertyId);
            }
        });

        Component headerComponent = grid.getDefaultHeaderRow().getCell(COLUMN_CAPTION).getComponent();
        assertThat(headerComponent, instanceOf(Label.class));
    }

    @Test
    public void testGetComponentDecorator() {
        ComponentGrid<ExampleBean> grid = new ComponentGrid(ExampleBean.class);

        ComponentGridDecorator decorator = grid.getComponentGridDecorator();
        assertThat(decorator.getGrid(), Is.<Grid>is(grid));
    }



    private ComponentGrid createTestGrid() {
        ComponentGrid<ExampleBean> grid = new ComponentGrid(ExampleBean.class);
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
            grid.add(bean);
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
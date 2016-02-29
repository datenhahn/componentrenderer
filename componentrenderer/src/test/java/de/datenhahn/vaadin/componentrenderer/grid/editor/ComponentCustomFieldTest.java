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

package de.datenhahn.vaadin.componentrenderer.grid.editor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGridDecorator;
import de.datenhahn.vaadin.componentrenderer.grid.ExampleBean;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;

public class ComponentCustomFieldTest {

    private static final String COLUMN_CAPTION = "caption";

    @Test
    public void testCustomField() {

        Grid grid = createTestGrid();
        ComponentGridDecorator<ExampleBean> decorator = new ComponentGridDecorator<>(grid, ExampleBean.class);
        decorator.addComponentColumn(COLUMN_CAPTION, new ComponentGenerator<ExampleBean>() {
            @Override
            public Component getComponent(ExampleBean bean) {
                return new Label(bean.getCaption());
            }
        });

        Item item = grid.getContainerDataSource().getItem(grid.getContainerDataSource().getIdByIndex(0));

        Property prop = item.getItemProperty(COLUMN_CAPTION)
                ;
        ComponentCustomField field = new ComponentCustomField();
        HorizontalLayout layout = (HorizontalLayout)field.initContent();
        assertThat(layout, not(hasItem(isA(Label.class))));

        field.setPropertyDataSource(prop);

        assertThat(field.getValue(), instanceOf(Label.class));

        assertThat(layout, hasItem(isA(Label.class)));

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
}
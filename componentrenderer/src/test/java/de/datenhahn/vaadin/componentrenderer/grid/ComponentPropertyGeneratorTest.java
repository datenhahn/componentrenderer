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
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;

public class ComponentPropertyGeneratorTest {

    public static final String LABEL_TEST_CAPTION = "testCaption";
    ComponentPropertyGenerator<ExampleBean> generator;

    @Before
    public void init() {
        generator = new ComponentPropertyGenerator<>(ExampleBean.class, new ComponentGenerator<ExampleBean>() {

            @Override
            public Component getComponent(ExampleBean bean) {
                return new Label(bean.getCaption());
            }
        });
    }

    @Test
    public void testGetValue() {
        ExampleBean bean = new ExampleBean();
        bean.setCaption(LABEL_TEST_CAPTION);
        Label label = (Label) generator.getValue(mock(Item.class), bean, "nocolumn");
        assertThat(label.getValue(), is(LABEL_TEST_CAPTION));

        Label label2 = (Label) generator.getValue(mock(Item.class), bean, "nocolumn");
        assertThat(label, not(sameInstance(label2)));
    }

    @Test
    public void testGetSortProperties() {
        SortOrder sortOrder = new SortOrder("caption", SortDirection.ASCENDING);
        SortOrder[] sortOrderArray = generator.getSortProperties(sortOrder);
        assertThat(sortOrderArray, is(arrayWithSize(1)));
        assertThat(sortOrder, is(sameInstance(sortOrderArray[0])));
    }

    @Test
    public void testGetSortPropertiesWithPrefix() {
        SortOrder sortOrder = new SortOrder("something", SortDirection.ASCENDING);
        SortOrder[] sortOrderArray = generator.getSortProperties(sortOrder);
        assertThat(sortOrderArray, is(arrayWithSize(1)));
        assertThat(sortOrder, is(sameInstance(sortOrderArray[0])));
    }

    @Test
    public void testGetSortPropertiesWithPropertyNotInBean() {
        SortOrder sortOrder = new SortOrder("notinbean", SortDirection.ASCENDING);
        SortOrder[] sortOrderArray = generator.getSortProperties(sortOrder);
        assertThat(sortOrderArray, is(arrayWithSize(0)));
    }

    @Test
    public void testGetType() {
        assertThat(generator.getType(), CoreMatchers.<Class<Component>>is(Component.class));
    }

}
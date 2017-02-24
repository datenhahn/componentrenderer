package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGenerator;
import de.datenhahn.vaadin.componentrenderer.grid.ComponentGrid;
import de.datenhahn.vaadin.componentrenderer.grid.ExampleBean;
import elemental.json.Json;
import elemental.json.JsonValue;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ComponentRendererTest {

    @Test
    public void encodeNullComponent() {
        ComponentRenderer renderer = new ComponentRenderer();
        assertThat(renderer.encode(null), equalTo((JsonValue) Json.createNull()));
    }

    @Test(expected = IllegalStateException.class)
    public void encodeComponentWithoutBeingAttached() {
        ComponentRenderer renderer = new ComponentRenderer();
        renderer.encode(new Label("test"));
    }

    /**
     * Issue #18:  Removing a column with ComponentRenderer caused an exception #18
     *
     * https://github.com/datenhahn/componentrenderer/issues/18
     */
    @Test
    public void issue18ExceptionWhenRemovingColumn() {
        ComponentGrid<ExampleBean> grid = new ComponentGrid<>(ExampleBean.class);
        ExampleBean bean = new ExampleBean();
        bean.setCaption("Foo");

        grid.add(bean);

        grid.addComponentColumn("caption", new ComponentGenerator<ExampleBean>() {
            @Override
            public Component getComponent(ExampleBean bean) {
                return new Label(bean.getCaption());
            }
        });

        grid.removeColumn("caption");
    }


}
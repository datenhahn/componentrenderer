package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.ui.Label;
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


}
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

package de.datenhahn.vaadin.componentrenderer.testbench;

import com.vaadin.server.FontAwesome;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.ui.Button;
import de.datenhahn.vaadin.componentrenderer.testbench.util.JmxMemoryUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RemoveColumnIT extends AbstractTestBase {

    @Test
    /**
     *  Regression Test for
     *  Issue #26: After removing one ComponentRenderer column the other renderer stop working
     *
     *  https://github.com/datenhahn/componentrenderer/issues/26
     */
    public void removeColumnButton() throws InterruptedException, IOException {

        //String expectedFirefox = "<div style=\"\" class=\"cr-component-cell\"><div style=\"width: 30px; height: 24px;\" class=\"v-label v-widget v-has-width v-has-height\"><span class=\"v-icon\" style=\"font-family: FontAwesome;\">.*</span></div></div>";
        String expectedChrome = "<div class=\"cr-component-cell\"><div class=\"v-label v-widget v-has-width "
                                + "v-has-height\" style=\"width: 30px; height: 24px;\"><span class=\"v-icon\" "
                                + "style=\"font-family: FontAwesome;\">.*</span></div></div>";
        setupChromiumDriver();
        getDriver().get("http://localhost:8080/testbench");
        assertTrue($(GridElement.class).first().getCell(0,6).getAttribute("innerHTML").matches(expectedChrome));
        $(ButtonElement.class).id("removeColumnButton").click();
        assertTrue($(GridElement.class).first().getCell(0,6).getAttribute("innerHTML").matches(expectedChrome));

    }


}

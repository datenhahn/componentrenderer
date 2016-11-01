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

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TabSheetElement;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ComponentCachingIT extends AbstractTestBase {

    @Test
    /**
     *  Regression Test for
     *  Issue #32: Vanishing components, when using cached components
     *
     *  see also
     *
     *  https://github.com/datenhahn/componentrenderer/pull/31
     */
    public void cachedComponentTest() throws InterruptedException, IOException {

        setupChromiumDriver();

        String componentCell = "<div class=\"cr-component-cell\"><div role=\"combobox\" .*</div></div>";
        getDriver().get("http://localhost:8080/testcaching");
        assertTrue($(GridElement.class).first().getCell(0,1).getAttribute("innerHTML").matches(componentCell));

        TabSheetElement tabSheet1 = $(TabSheetElement.class).first();
        tabSheet1.openTab(1);
        assertTrue($(GridElement.class).first().getCell(0,1).getAttribute("innerHTML").matches(componentCell));

        tabSheet1.openTab(0);
        assertTrue($(GridElement.class).first().getCell(0,1).getAttribute("innerHTML").matches(componentCell));

        tabSheet1.openTab(1);
        assertTrue($(GridElement.class).first().getCell(0,1).getAttribute("innerHTML").matches(componentCell));

        tabSheet1.openTab(0);
        assertTrue($(GridElement.class).first().getCell(0,1).getAttribute("innerHTML").matches(componentCell));

    }


}

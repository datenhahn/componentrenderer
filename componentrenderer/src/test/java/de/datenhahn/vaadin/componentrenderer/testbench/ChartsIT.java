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
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

public class ChartsIT extends AbstractTestBase {


    private void scrollSlowly(int iterations) throws InterruptedException {
        for(int i = 0; i < iterations; i++) {
        $(GridElement.class).first().getVerticalScroller().scroll(i * 100);
        }
    }

    @Test
    /**
     *  Regression Test for
     *  Issue #25 Cell containing Vaadin charts disappear when scrolling grid
     *
     *  https://github.com/datenhahn/componentrenderer/issues/25
     */
    public void testChartsRendering() throws InterruptedException, IOException {

        setupFirefoxDriver();
        getDriver().get("http://localhost:8080/testcharts");


        assertThat($(GridElement.class).first().getCell(0,4).getAttribute("innerHTML"), containsString("<svg height=\"30\" "
                                                                                                 + "width=\"130\""));

        $(GridElement.class).first().scrollToRow(13);

        $(GridElement.class).first().scrollToRow(1);
        assertThat($(GridElement.class).first().getCell(0,4).getAttribute("innerHTML"), containsString("<svg height=\"30\" "
                                                                                                       + "width=\"130\""));




//        Thread.sleep(1000);
//        $(GridElement.class).first().scrollToRow(420);
//        Thread.sleep(1000);
//        $(GridElement.class).first().scrollToRow(530);
//        $(GridElement.class).first().scrollToRow(490);
//
//        Thread.sleep(5000);
    }


}

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

package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.server.Extension;
import com.vaadin.v7.ui.Grid;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComponentCellKeyExtensionTest {
    @Test
    public void testExtend() throws Exception {
        Grid grid = new Grid();

        assertFalse(containsComponentCellKeyExtension(grid));

        ComponentCellKeyExtension.extend(grid);

        assertTrue(containsComponentCellKeyExtension(grid));
    }

    private boolean containsComponentCellKeyExtension(Grid grid) {
        for (Extension extension : grid.getExtensions()) {
            if (extension instanceof ComponentCellKeyExtension) {
                return true;
            }
        }
        return false;
    }
}
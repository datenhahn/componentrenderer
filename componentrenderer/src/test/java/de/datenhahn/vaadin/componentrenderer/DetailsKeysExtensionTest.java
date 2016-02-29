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
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import de.datenhahn.vaadin.componentrenderer.client.detailskeys.DetailsOpenCloseServerRpc;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DetailsKeysExtensionTest {

    @Test
    public void testExtend() throws Exception {
        Grid grid = createTestGrid();

        assertFalse(containsDetailsGridExtension(grid));

        DetailsKeysExtension extension = DetailsKeysExtension.extend(grid);

        assertTrue(containsDetailsGridExtension(grid));
        assertTrue(hasDetailsGridExtensionRpc(extension));
    }

    @Test
    public void testRpcExpandCollapse() {
        Grid grid = createTestGrid();
        DetailsKeysExtension.DetailsOpenCloseServerRpcImpl rpc = new DetailsKeysExtension.DetailsOpenCloseServerRpcImpl(grid);

        Object id = (grid.getContainerDataSource()).getIdByIndex(0);

        assertFalse(grid.isDetailsVisible(id));

        rpc.setDetailsVisible(0, true);

        assertTrue(grid.isDetailsVisible(id));

        rpc.setDetailsVisible(0, false);

        assertFalse(grid.isDetailsVisible(id));

    }

    private Grid createTestGrid() {
        Grid grid = new Grid();
        grid.addColumn("rows");
        grid.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {
                return new Label("foo");
            }
        });
        for(int i = 0; i <= 10;i++) {
            grid.addRow("foo"+i);
        }
        return grid;
    }

    private boolean containsDetailsGridExtension(Grid grid) {
        for (Extension extension : grid.getExtensions()) {
            if (extension instanceof DetailsKeysExtension) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDetailsGridExtensionRpc(DetailsKeysExtension extension) {
        return extension.getRpcManager(DetailsOpenCloseServerRpc.class.getName()) != null;
    }
}
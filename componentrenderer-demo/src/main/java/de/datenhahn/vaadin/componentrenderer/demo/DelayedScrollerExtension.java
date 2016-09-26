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

package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.ui.Grid;
import de.datenhahn.vaadin.componentrenderer.demo.client.DelayedScrollerState;

/**
 * Handles the expansion and collapsing of the detailsrow with STRG+DOWN (expand) and STRG+UP (collapse).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class DelayedScrollerExtension extends Grid.AbstractGridExtension {

    private DelayedScrollerExtension(final Grid grid) {
        super.extend(grid);
    }

    @Override
    public DelayedScrollerState getState() {
        return (DelayedScrollerState) super.getState();
    }

    public void setMaxDeltaInRows(int maxDeltaInRows) {
        getState().maxDeltaInRows = maxDeltaInRows;
    }

    public void setScrollMessage(String scrollMessage) {
        getState().scrollMessage = scrollMessage;
    }

    public static DelayedScrollerExtension extend(Grid grid) {
        return new DelayedScrollerExtension(grid);
    }

}
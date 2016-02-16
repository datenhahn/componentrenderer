/**
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

import de.datenhahn.vaadin.componentrenderer.grid.TextHeaderGenerator;

import java.util.ResourceBundle;

/**
 * A HeaderGenerator which automatically gets the header texts from a resource bundle.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ResourceBundleTextHeaderGenerator implements TextHeaderGenerator {

    private ResourceBundle resourceBundle;

    public ResourceBundleTextHeaderGenerator(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public String getHeader(Object propertyId) {
        return resourceBundle.getString(propertyId + "");
    }
}

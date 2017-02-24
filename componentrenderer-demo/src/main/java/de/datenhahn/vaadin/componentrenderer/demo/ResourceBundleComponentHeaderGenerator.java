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

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import de.datenhahn.vaadin.componentrenderer.grid.header.ComponentHeaderGenerator;

import java.util.ResourceBundle;

/**
 * A HeaderGenerator which automatically gets the header texts from a resource bundle.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ResourceBundleComponentHeaderGenerator implements ComponentHeaderGenerator {

    private ResourceBundle resourceBundle;

    public ResourceBundleComponentHeaderGenerator(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public Component getHeader(Object propertyId) {
        Label headerLabel = new Label(resourceBundle.getString(propertyId + ""));
        headerLabel.setDescription(resourceBundle.getString(propertyId+""));
        return  headerLabel;
    }
}

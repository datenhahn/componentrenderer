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

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class StaticCustomerDetailsGenerator implements Grid.DetailsGenerator {
    @Override
    public Component getDetails(Grid.RowReference rowReference) {
        rowReference.getGrid().scrollTo(rowReference.getItemId());
        StaticCustomer customer = (StaticCustomer)rowReference.getItemId();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setHeight(300, Sizeable.Unit.PIXELS);
        layout.setMargin(true);
        layout.setSpacing(true);
        Image image = new Image("", customer.getPhoto());
        image.setHeight(200, Sizeable.Unit.PIXELS);
        image.setWidth(200, Sizeable.Unit.PIXELS);
        layout.addComponent(image);
        Label nameLabel = new Label("<h1>"+customer.getFirstName() + " " + customer.getLastName()+"</h1>", ContentMode.HTML);
        layout.addComponent(nameLabel);
        layout.setExpandRatio(nameLabel, 1.0f);
        return layout;
    }
}

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

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeSelect;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StaticCustomerProvider {

    private static final DataFactory testData = new DataFactory();

    public static List<StaticCustomer> createDummyData() {
        LinkedList<StaticCustomer> list = new LinkedList<>();

        for (int i = 1; i <= 10000; i++) {

            StaticCustomer customer = new StaticCustomer();
            customer.setId(i);
            customer.setFirstName(testData.getFirstName());
            customer.setLastName(testData.getLastName());
            NativeSelect foodSelect = new NativeSelect(null, Arrays.asList(StaticCustomer.Food.values()));
            foodSelect.setValue(testData.getItem(StaticCustomer.Food.values()));
            customer.setFood(foodSelect);
            customer.setPhoto(new ThemeResource("../demotheme/demophotos/cat"
                                                + testData.getNumberBetween(1, 4)
                                                + ".jpg"));

            list.add(customer);
        }
        return list;
    }
}

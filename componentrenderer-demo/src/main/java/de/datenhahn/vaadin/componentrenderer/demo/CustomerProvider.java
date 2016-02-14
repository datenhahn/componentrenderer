package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.server.ThemeResource;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.LinkedList;
import java.util.List;

public class CustomerProvider {

    private static final DataFactory testData = new DataFactory();

    public static List<Customer> createDummyData() {
        LinkedList<Customer> list = new LinkedList<>();

        for (int i = 1; i <= 10000; i++) {

            Customer customer = new Customer();
            customer.setId(i);
            customer.setFirstName(testData.getFirstName());
            customer.setLastName(testData.getLastName());
            customer.setFood(testData.getItem(Customer.Food.values()));
            customer.setPhoto(new ThemeResource("../demotheme/demophotos/cat"
                                                + testData.getNumberBetween(1, 4)
                                                + ".jpg"));

            list.add(customer);
        }
        return list;
    }
}

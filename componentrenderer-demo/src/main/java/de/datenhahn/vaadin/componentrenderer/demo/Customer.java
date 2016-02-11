package de.datenhahn.vaadin.componentrenderer.demo;

public class Customer {

    enum Food {
        HAMBURGER, FISH, VEGETABLES
    }
    private int id;
    private int carRating;
    private int overallRating;
    private String firstName;
    private String lastName;
    private Food food;

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarRating() {
        return carRating;
    }

    public void setCarRating(int carRating) {
        this.carRating = carRating;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

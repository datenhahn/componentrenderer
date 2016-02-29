package de.datenhahn.vaadin.componentrenderer.demo;

import com.vaadin.server.ThemeResource;

public class Customer {

    enum Food {
        HAMBURGER, FISH, VEGETABLES
    }
    public static final String ID = "id";
    private int id;

    public static final String CAR_RATING = "carRating";
    private int carRating;

    public static final String OVERALL_RATING = "overallRating";
    private int overallRating;

    public static final String FIRST_NAME = "firstName";
    private String firstName;

    public static final String LAST_NAME = "lastName";
    private String lastName;

    public static final String FOOD = "food";
    private Food food;

    public static final String PHOTO = "photo";
    private ThemeResource photo;

    public static final String PREMIUM = "premium";
    private boolean premium;

    public boolean getPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public ThemeResource getPhoto() {
        return photo;
    }

    public void setPhoto(ThemeResource photo) {
        this.photo = photo;
    }

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

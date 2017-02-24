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
import com.vaadin.v7.ui.NativeSelect;

public class StaticCustomer {

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
    private NativeSelect food;

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

    public NativeSelect getFood() {
        return food;
    }

    public void setFood(NativeSelect food) {
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

package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int MEAL_ID = 1;
    public static final int ADMIN_LUNCH_ID = 8;
    public static final int ADMIN_DINNER_ID = 9;
    public static final int NOT_FOUND = 100;

    public static final Meal meal = new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal adminLunch = new Meal(ADMIN_LUNCH_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal adminDinner = new Meal(ADMIN_DINNER_ID, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2019, Month.JANUARY, 30, 10, 0), "Новый", 500);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 16, 0), "Полдник", 400);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}

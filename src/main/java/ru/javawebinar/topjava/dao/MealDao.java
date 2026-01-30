package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao {

    public static final int CALORIES_PER_DAY = 2000;
    public static final LocalTime DEFAULT_START = LocalTime.MIDNIGHT;
    public static final LocalTime DEFAULT_END = LocalTime.MIDNIGHT.minusSeconds(1);
    private static final AtomicInteger sequence = new AtomicInteger(1);

    private static final List<MealTo> defaultMeals = Arrays.asList(
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new MealTo(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private static final Map<Integer, Meal> mealsStore = new ConcurrentHashMap<>();

    static {
        for (MealTo mealTo : defaultMeals) {
            int id = nextId();
            mealsStore.put(id, new Meal(id, mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories()));
        }
    }

    private static int nextId() {
        return sequence.getAndIncrement();
    }

    public List<Meal> findAll() {
        return new ArrayList<>(mealsStore.values());
    }

    public void create(MealTo mealTo) {
        int id = nextId();
        Meal meal = new Meal(id, mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
        mealsStore.put(id, meal);
    }

    public Meal findById(Integer id) {
        checkExistence(id);
        return mealsStore.get(id);
    }

    public void update(MealTo mealTo) {
        checkExistence(mealTo.getId());
        Meal updated = new Meal(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
        mealsStore.put(updated.getId(), updated);
    }

    public void delete(Integer id) {
        mealsStore.remove(id);
    }

    private void checkExistence(Integer id) {
        if (!mealsStore.containsKey(id)) {
            throw new RuntimeException("No meal found with id=" + id);
        }
    }
}

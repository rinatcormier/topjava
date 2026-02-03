package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, meal.getUserId()));
    }

    @Override
    public synchronized Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealsMap.computeIfPresent(meal.getId(),
                (id, oldMeal) -> Integer.valueOf(userId).equals(meal.getUserId()) ? meal : null);
    }

    @Override
    public synchronized boolean delete(int id, int userId) {
        Meal removing = mealsMap.get(id);
        if (removing != null && !Integer.valueOf(userId).equals(removing.getUserId())) {
            return false;
        }
        return mealsMap.remove(id, removing);
    }

    @Override
    public Meal get(int id, int userId) {
        Meal found = mealsMap.get(id);
        if (found != null && !Integer.valueOf(userId).equals(found.getUserId())) {
            return null;
        }
        return found;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return mealsMap.values().stream()
                .filter(meal -> Integer.valueOf(userId).equals(meal.getUserId()))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}


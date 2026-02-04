package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Collection<MealTo> getFilteredTos(
            int userId,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return MealsUtil.getFilteredTos(
                repository.getAll(userId),
                MealsUtil.DEFAULT_CALORIES_PER_DAY,
                startDate,
                endDate,
                startTime,
                endTime
        );
    }

    public MealTo get(int id, int userId) {
        return createTo(checkNotFound(repository.get(id, userId), id));
    }

    public MealTo create(Meal meal, int userId) {
        return createTo(repository.save(meal, userId));
    }

    public void update(Meal meal, int userId) {
        checkNotFound(repository.save(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), id);
    }
}
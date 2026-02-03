package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(100);

    {
        usersMap.put(UsersUtil.user.getId(), UsersUtil.user);
        usersMap.put(UsersUtil.admin.getId(), UsersUtil.admin);
    }

    @Override
    public synchronized boolean delete(int id) {
        return usersMap.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            usersMap.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        return usersMap.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        return usersMap.values()
                .stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return usersMap.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findAny()
                .orElse(null);
    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

public class UsersUtil {

    private UsersUtil() {
    }

    public static final User admin = new User(1, "Admin", "admin@mail.com", "admin", Role.ADMIN);
    public static final User user = new User(2, "John Doe", "johndoe@mail.com", "password", Role.USER);

}

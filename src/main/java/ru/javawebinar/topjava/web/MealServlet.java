package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.dao.MealDao.*;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final MealDao dao = new MealDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String stringId = request.getParameter("id");
        if (action == null) {
            log.debug("get meals");
            List<MealTo> mealTos = MealsUtil.filteredByStreams(dao.findAll(), DEFAULT_START, DEFAULT_END, CALORIES_PER_DAY);
            request.setAttribute("meals", mealTos);
            request.getRequestDispatcher("/meals/meals.jsp").forward(request, response);
        } else {
            switch (action) {
                case "edit":
                    MealTo mealTo;
                    if (stringId != null) {
                        log.debug("get meal by id={}", stringId);
                        mealTo = MealsUtil.createTo(dao.findById(Integer.parseInt(stringId)));
                    } else {
                        mealTo = MealTo.dummy();
                    }
                    request.setAttribute("meal", mealTo);
                    request.getRequestDispatcher("/meals/meal.jsp").forward(request, response);
                    break;
                case "delete":
                    log.debug("delete meal by id={}", stringId);
                    dao.delete(Integer.parseInt(stringId));
                    response.sendRedirect("meals");
                    break;
                default:
                    throw new RuntimeException("Unknown action");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        Integer id = idStr == null || idStr.isEmpty() ? null : Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        MealTo mealTo = new MealTo(id, dateTime, description, calories);
        if (id == null) {
            dao.create(mealTo);
        } else {
            dao.update(mealTo);
        }
        response.sendRedirect("meals");
    }
}

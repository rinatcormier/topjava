package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    @Override
    public void init() {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String authUserId = request.getParameter("authUserId");
        SecurityUtil.setAuthUserId(authUserId == null ? 0 : Integer.parseInt(authUserId));

        Meal meal = new Meal(
                id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                SecurityUtil.authUserId()
        );

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, Integer.parseInt(id));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String authUserId = request.getParameter("authUserId");
        SecurityUtil.setAuthUserId(authUserId == null ? 0 : Integer.parseInt(authUserId));

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo meal = "create".equals(action) ?
                        new MealTo(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, false) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                LocalDate startDate = parseDate(request, "startDate");
                LocalDate endDate = parseDate(request, "endDate");
                LocalTime startTime = parseTime(request, "startTime");
                LocalTime endTime = parseTime(request, "endTime");
                Collection<MealTo> filteredTos = controller.getFilteredTos(startDate, endDate, startTime, endTime);
                request.setAttribute("meals", filteredTos);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate parseDate(HttpServletRequest request, String param) {
        LocalDate date;
        try {
            date = LocalDate.parse(request.getParameter(param));
        } catch (RuntimeException ex) {
            date = null;
        }
        return date;
    }

    private LocalTime parseTime(HttpServletRequest request, String param) {
        LocalTime time;
        try {
            time = LocalTime.parse(request.getParameter(param));
        } catch (RuntimeException ex) {
            time = null;
        }
        return time;
    }
}

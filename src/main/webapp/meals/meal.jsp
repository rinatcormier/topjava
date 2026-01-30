<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://topjava.ru/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h3><a href="meals">Meals</a></h3>
<hr>
<h2>Meal</h2>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
<form action="meals" method="post">
    <p>
        <input type="hidden" name="id" value="${meal.id}">
        <label for="dateTime">Date/time:</label>
        <input id="dateTime" type="datetime-local" name="dateTime" value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}">
    </p>
    <p>
        <label for="description">Description:</label>
        <input id="description" type="text" name="description" value="${meal.description}">
    </p>
    <p>
        <label for="calories">Calories:</label>
        <input id="calories" type="text" name="calories" value="${meal.calories}">
    </p>
    <p>
        <input type="submit" value="Save">
    </p>
</form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <div>
        <form method="get" action="meals">
            <p>
                <label for="startDate">От даты</label>
                <input type="date" name="startDate" id="startDate" value="${param.startDate}">
            </p>
            <p>
                <label for="endDate">До даты</label>
                <input type="date" name="endDate" id="endDate" value="${param.endDate}">
            </p>
            <p>
                <label for="startTime">От времени</label>
                <input type="time" name="startTime" id="startTime" value="${param.startTime}">
            </p>
            <p>
                <label for="endTime">До времени</label>
                <input type="time" name="endTime" id="endTime" value="${param.endTime}">
            </p>
            <p>
                <input type="hidden" name="action" value="all">
                <input type="submit" value="Отфильтровать">
            </p>
        </form>
    </div>
    <a href="meals?action=create&authUserId=${param.authUserId}">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}&authUserId=${param.authUserId}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}&authUserId=${param.authUserId}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
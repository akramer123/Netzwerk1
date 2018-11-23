<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Taxi App</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<div th:each="taxi : ${taxis}">
    <p>
    <span>Get overview over taxi driver </span><span th:text="${taxi.id}"></span><a th:href="@{|/taxi/${taxi.id}|}">here</a>
    </p>
</div>
</body>
</html>
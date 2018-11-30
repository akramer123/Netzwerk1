//Kristina

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Taxi App</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<div th:each="taxi : ${taxis}">
    <p>
    <span>Get overview over taxi driver </span><span th:text="${taxi.id}"></span> <span th:text="${taxi.status}"></span><a th:href="@{|/taxi/${taxi.id}|}"> here</a>
    </p>
</div>

<button>
    <a href="/lights"> Update lights of all Taxis</a></button>
</body>
</html>
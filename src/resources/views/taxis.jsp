<html xmlns:form="http://www.w3.org/1999/xhtml">
<head>
    <title>Taxi App - All drivers</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>

<form:form method="POST"
           action="/route" modelAttribute="taxi">
    <table>
        <tr>
            <td>
                <form:label path="address">Address</form:label>
            </td>
            <td>
                <form:input path="address"></form:input>
            </td>
        </tr>
        <td><input type="submit" value="Submit"/></td>
    </table>
</form:form>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <h1>Welcome message ...!${message}</h1>
        <p>Click <a th:href="@{/hello}">here</a> to see a greeting.</p>
    </body>
</html>
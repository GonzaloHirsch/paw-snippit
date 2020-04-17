<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
    <form method="post">
        <div>
            <label>
                <spring:message code="registerForm.username"/>
                <input name="username" placeholder="<spring:message code='registerForm.username.hint'/>">
            </label>
        </div>
        <div>
            <label>
                <spring:message code="registerForm.password"/>
                <input name="password" placeholder="<spring:message code='registerForm.password.hint'/>">
            </label>
        </div>
        <div>
            <label>
                <input name="rememberme" type="checkbox"/>
                <spring:message code="loginForm.rememberme"/>
            </label>
        </div>
        <div>
            <input type="submit" value="<spring:message code="loginForm.submit"/>"/>
        </div>
    </form>

</body>
</html>

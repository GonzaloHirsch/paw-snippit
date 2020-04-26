<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="login.title"/></title>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/registration.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/checkbox.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <!-- LOGIN START -->
        <c:url value="/login" var="loginUrl" />
        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center register-block register-border">

            <!-- WELCOME message -->
            <div class="flex-row flex-center register-text-container white-text register-welcome-text fw-300">
                <i class="material-icons app-icon register-app-icon-margin">code</i>
                <spring:message code="login.welcome"/>
                <span class="fw-500 register-title-margin"> <spring:message code="app.name"/></span>
            </div>

            <!-- LOGIN form -->
            <form class="flex-center register-form register-border register-shadow" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column register-form-data">
                    <c:if test="${error}">
                        <div class="flex-center register-text-error form-error">
                            <spring:message code="loginForm.invalid"/>
                        </div>
                    </c:if>

                    <div class="register-field-container">
                        <label>
                            <i class="material-icons register-icons">person</i>
                            <input class="register-border register-field-size register-field-padding" name="username" placeholder="<spring:message code='registerForm.username'/>">
                        </label>
                    </div>

                    <div class="register-field-container">
                        <label>
                            <i class="material-icons register-icons">lock</i>
                            <input class="register-border register-field-size register-field-padding" name="password" type="password" placeholder="<spring:message code='registerForm.password'/>">
                        </label>
                    </div>

                    <div class="register-field-container">
                        <label class="flex-row checkbox-container ">
                            <input name="rememberme" type="checkbox"/>
                            <span class="checkbox-checkmark"></span>
                            <spring:message code="loginForm.rememberme"/>
                        </label>
                    </div>

                    <div class="register-field-container">
                        <input class="register-border register-field-size register-button fw-500" type="submit" value="<spring:message code="loginForm.submit"/>"/>
                    </div>
                </div>
            </form>

            <!-- REDIRECT to sign up text -->
            <div class="flex-center register-text-container white-text">
                <spring:message code="login.noAccount"/>
                <a class="white-text register-text-space" href="<c:url value='/signup'/>">
                    <spring:message code="login.signupRedirect"/>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>


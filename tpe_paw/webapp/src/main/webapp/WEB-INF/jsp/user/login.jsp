<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="login.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/checkbox.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <!-- LOGIN START -->
        <c:url value="/login" var="loginUrl" />
        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center form-block form-border">

            <!-- WELCOME message -->
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="login.welcome"/>
                <span class="fw-500 form-title-margin"> <spring:message code="app.name"/></span>
            </div>

            <!-- LOGIN form -->
            <form class="flex-center form form-border form-shadow" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column form-data">
                    <c:if test="${error}">
                        <div class="flex-center form-text-error form-error">
                            <spring:message code="loginForm.invalid"/>
                        </div>
                    </c:if>

                    <div class="form-field-container">
                        <label>
                            <i class="material-icons form-icons">person</i>
                            <input class="form-border form-field-size form-field-padding" name="username" placeholder="<spring:message code='registerForm.username'/>">
                        </label>
                    </div>

                    <div class="form-field-container">
                        <label>
                            <i class="material-icons form-icons">lock</i>
                            <input class="form-border form-field-size form-field-padding" name="password" type="password" placeholder="<spring:message code='registerForm.password'/>">
                        </label>
                    </div>

                    <div class="form-field-container">
                        <label class="flex-row checkbox-container ">
                            <input name="rememberme" type="checkbox"/>
                            <span class="checkbox-checkmark"></span>
                            <spring:message code="loginForm.rememberme"/>
                        </label>
                    </div>

                    <div class="form-field-container">
                        <input class="form-border form-field-size form-button fw-500" type="submit" value="<spring:message code="loginForm.submit"/>"/>
                    </div>
                </div>
            </form>

            <!-- REDIRECT to sign up text -->
            <div class="flex-center form-text-container white-text">
                <spring:message code="login.noAccount"/>
                <a class="white-text form-text-space" href="<c:url value='/signup'/>">
                    <spring:message code="login.signupRedirect"/>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>


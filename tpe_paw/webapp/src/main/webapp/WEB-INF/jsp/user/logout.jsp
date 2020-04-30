<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><html>

<head>
    <head>
        <title><spring:message code="logout.title"/></title>
        <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
        <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <div class="flex-column flex-center form-block form-border">

            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="logout.goodbye"/>
            </div>

            <div class="flex-center form form-border form-shadow">
                <div class="flex-column form-data">
                    <a class="flex-center form-border form-field-size form-button form-buttons-margin fw-500" href="<c:url value='/'/>">
                        <spring:message code="logout.homeRedirect"/>
                    </a>

                    <a class="flex-center form-border form-field-size form-button form-buttons-margin fw-500" href="<c:url value='/login'/>">
                        <spring:message code="logout.loginRedirect"/>
                    </a>

                    <a class="flex-center form-border form-field-size form-button form-buttons-margin fw-500" href="<c:url value='/signup'/>">
                        <spring:message code="logout.signupRedirect"/>
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="passwordRecovery.finished.title"/></title>
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

        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center form-block form-border">

            <!-- Title -->
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="app.name"/>
            </div>
            <div class="flex-center flex-column form form-border form-shadow">
                <div class="form-info-text">
                    <div class="form-field-container">
                        <div class="">
                            <spring:message code="passwordReset.title"/>
                        </div>
                    </div>
                </div>
                <a class="flex-center form-border form-field-size form-button form-button-basics form-buttons-margin fw-500" href="<c:url value='/login'/>">
                    <spring:message code="logout.loginRedirect"/>
                </a>
                <a class="flex-center form-border form-field-size form-button form-button-basics form-buttons-margin fw-500" href="<c:url value='/'/>">
                    <spring:message code="shared.goHome"/>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
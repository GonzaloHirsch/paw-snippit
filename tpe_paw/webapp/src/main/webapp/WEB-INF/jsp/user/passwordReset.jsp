<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="login.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/registration.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/checkbox.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center register-block register-border">

            <!-- Title -->
            <div class="flex-row flex-center register-text-container white-text register-welcome-text fw-300">
                <i class="material-icons app-icon register-app-icon-margin">code</i>
                <spring:message code="app.name"/>
            </div>
            <div class="flex-center register-form register-border register-shadow">
                <div class="register-form-data">
                    <div class="register-form-title">
                        <div class="register-field-container">
                            <div class="">
                                <spring:message code="passwordReset.title"/>
                            </div>
                        </div>
                    </div>
                    <div class="register-field-container">
                        <a class="no-text-decoration" href="<c:url value="/"/>">
                            <div class="flex-center register-border register-field-size register-button fw-500">
                                <spring:message code="shared.goHome"/>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
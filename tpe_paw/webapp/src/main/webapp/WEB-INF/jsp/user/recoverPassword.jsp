<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="passwordRecovery.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/checkbox.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <c:url var="sendEmailUrl" value="/recover-password"/>
        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center form-block form-border">

            <!-- Title -->
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="recoverPassword.title"/>
            </div>

            <!-- RECOVER PASSWORD form -->
            <form:form class="flex-center form form-border form-shadow" action="${sendEmailUrl}" modelAttribute="recoveryForm" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column form-form-data">
                    <div class="form-info-text">
                        <div class="form-field-container">
                            <spring:message code="recoverPassword.instructions"/>
                        </div>
                    </div>
                    <div class="form-field-container">
                        <label>
                            <form:errors path="email" cssClass="form-error" element="p"/>
                            <i class="material-icons form-icons">email</i>
                            <input class="form-border form-field-size form-field-layout" name="email" placeholder="<spring:message code='recoverPassword.insertMail'/>">
                        </label>
                    </div>
                    <div class="form-field-container">
                        <input class="form-border form-field-size form-button form-button-basics fw-500" type="submit" value="<spring:message code="recoverPassword.email.submit"/>"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
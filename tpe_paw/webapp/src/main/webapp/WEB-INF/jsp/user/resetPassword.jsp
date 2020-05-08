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
<spring:message code="registerForm.password" var="password_hint"/>
<spring:message code="registerForm.repeatPassword" var="repeat_password_hint"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <c:url var="resetPassUrl" value="/reset-password">
            <c:param name="id" value="${param.id}"/>
            <c:param name="token" value="${param.token}"/>
        </c:url>
        <div class="flex-column flex-center form-block form-border">

            <!-- Title -->
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="resetPassword.title"/>
            </div>

            <!-- RESET PASSWORD form -->
            <form:form class="flex-center form form-border form-shadow" action="${resetPassUrl}" modelAttribute="resetPasswordForm" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column form-form-data">
                    <div class="form-field-container">
                        <form:errors path="newPassword" cssClass="form-error" element="p"/>
                        <label>
                            <i class="material-icons form-icons">lock</i>
                            <form:input class="form-border form-field-size form-field-layout" path="newPassword" placeholder='${password_hint}' type="password"/>
                        </label>
                    </div>

                    <div class="form-field-container">
                        <form:errors path="repeatNewPassword" cssClass="form-error" element="p"/>
                        <label>
                            <i class="material-icons form-icons">lock</i>
                            <input class="form-border form-field-size form-field-layout" name="repeatNewPassword" type="password" placeholder="${repeat_password_hint}">
                        </label>
                    </div>

                    <form:input class="hidden" path="email"/>
                    <div class="form-field-container">
                        <input class="form-border form-field-size form-button form-button-basics fw-500" type="submit" value="<spring:message code="recoverPassword.reset.submit"/>"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
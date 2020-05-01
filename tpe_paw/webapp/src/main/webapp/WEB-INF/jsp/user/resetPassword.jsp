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
<spring:message code="registerForm.password" var="password_hint"/>
<spring:message code="registerForm.repeatPassword" var="repeat_password_hint"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <c:url var="resetPassUrl" value="/reset-password"/>
        <div class="flex-column flex-center register-block register-border">

            <!-- Title -->
            <div class="flex-row flex-center register-text-container white-text register-welcome-text fw-300">
                <i class="material-icons app-icon register-app-icon-margin">code</i>
                <spring:message code="resetPassword.title"/>
            </div>

            <!-- RESET PASSWORD form -->
            <form:form class="flex-center register-form register-border register-shadow" action="${resetPassUrl}" modelAttribute="resetPasswordForm" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column register-form-data">
                    <div class="register-field-container">
                        <form:errors path="newPassword" cssClass="form-error" element="p "/>
                        <i class="material-icons register-icons">lock</i>
                        <label><form:input class="register-border register-field-size register-field-padding" path="newPassword" placeholder='${password_hint}' type="password"/></label>
                    </div>

                    <div class="register-field-container">
                        <form:errors path="repeatNewPassword" cssClass="form-error" element="p "/>
                        <i class="material-icons register-icons">lock</i>
                        <label><form:input class="register-border register-field-size register-field-padding" path="repeatNewPassword" placeholder='${repeat_password_hint}' type="password"/></label>
                    </div>
                    <form:input class="hidden" path="email"/>
                    <div class="register-field-container">
                        <input class="register-border register-field-size register-button fw-500" type="submit" value="<spring:message code="recoverPassword.submit"/>"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
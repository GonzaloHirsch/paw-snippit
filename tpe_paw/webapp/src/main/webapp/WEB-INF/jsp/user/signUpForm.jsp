<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="register.title"/></title>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/registration.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<spring:message code="registerForm.username" var="username_hint"/>
<spring:message code="registerForm.email" var="email_hint"/>
<spring:message code="registerForm.password" var="password_hint"/>
<spring:message code="registerForm.repeatPassword" var="repeat_password_hint"/>

<c:url var="signUpUrl" value="/signup"/>

<div class="flex-column flex-center TODO-delete">

    <div class="flex-row flex-center register-text-container register-text register-welcome-text fw-300">
        <i class="material-icons app-icon register-app-icon-margin">code</i>
        <spring:message code="register.welcome"/>
        <span class="fw-500 register-title-margin"> <spring:message code="app.name"/></span>
    </div>

    <form:form class="flex-center register-form register-border register-shadow " modelAttribute="registerForm" method="post" action="${signUpUrl}">
        <div class="flex-column register-form-data">
            <div class="register-field-container">
                <form:errors path="username" cssClass="form-error" element="p" />
                <label>
                    <i class="material-icons register-icons">person</i>
                    <form:input class="register-border register-field-size register-field-padding" path="username" placeholder='${username_hint}'/>
                </label>
            </div>

            <div class="register-field-container">
                <form:errors path="email" cssClass="form-error" element="p "/>
                <label>
                    <i class="material-icons register-icons">email</i>
                    <form:input class="register-border register-field-size register-field-padding" path="email" placeholder='${email_hint}'/>
                </label>
            </div>

            <div class="register-field-container">
                <form:errors path="password" cssClass="form-error" element="p "/>
                <i class="material-icons register-icons">lock</i>
                <label><form:input class="register-border register-field-size register-field-padding" path="password" placeholder='${password_hint}' type="password"/></label>
            </div>

            <div class="register-field-container">
                <form:errors path="repeatPassword" cssClass="form-error" element="p "/>
                <i class="material-icons register-icons">lock</i>
                <label><form:input class="register-border register-field-size register-field-padding" path="repeatPassword" placeholder='${repeat_password_hint}' type="password"/></label>
            </div>

            <div class="register-field-container">
                <input class="register-border register-field-size register-button fw-500" type="submit" value="<spring:message code="registerForm.submit"/>"/>
            </div>
        </div>
    </form:form>

    <div class="flex-center register-text-container register-text">
        <spring:message code="register.hasAccount"/>
        <a class="register-text register-text-space" href="<c:url value='/login'/>">
            <spring:message code="register.loginRedirect"/>
        </a>
    </div>
</div>
</body>
</html>

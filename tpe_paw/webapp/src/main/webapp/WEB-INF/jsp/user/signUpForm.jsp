<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="register.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<spring:message code="registerForm.username" var="username_hint"/>
<spring:message code="registerForm.email" var="email_hint"/>
<spring:message code="registerForm.password" var="password_hint"/>
<spring:message code="registerForm.repeatPassword" var="repeat_password_hint"/>

<c:url var="signUpUrl" value="/signup"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <div class="flex-column flex-center form-block form-border">
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="register.welcome"/>
                <span class="fw-500 form-title-margin"> <spring:message code="app.name"/></span>
            </div>

            <form:form class="flex-center form form-border form-shadow " modelAttribute="registerForm" method="post" action="${signUpUrl}">
                <div class="flex-column form-data">
                    <div class="form-field-container">
                        <form:errors path="username" cssClass="form-error" element="p" />
                        <label>
                            <i class="material-icons form-icons">person</i>
                            <form:input class="form-border form-field-size form-field-layout" path="username" placeholder='${username_hint}'/>
                        </label>
                    </div>

                    <div class="form-field-container">
                        <form:errors path="email" cssClass="form-error" element="p "/>
                        <label>
                            <i class="material-icons form-icons">email</i>
                            <form:input class="form-border form-field-size form-field-layout" path="email" placeholder='${email_hint}'/>
                        </label>
                    </div>

                    <div class="form-field-container">
                        <form:errors path="password" cssClass="form-error" element="p "/>
                        <i class="material-icons form-icons">lock</i>
                        <label><form:input class="form-border form-field-size form-field-layout" path="password" placeholder='${password_hint}' type="password"/></label>
                    </div>

                    <div class="form-field-container">
                        <form:errors path="repeatPassword" cssClass="form-error" element="p "/>
                        <i class="material-icons form-icons">lock</i>
                        <label><form:input class="form-border form-field-size form-field-layout" path="repeatPassword" placeholder='${repeat_password_hint}' type="password"/></label>
                    </div>

                    <div class="form-field-container">
                        <input class="form-border form-field-size form-button form-button-basics fw-500" type="submit" value="<spring:message code="registerForm.submit"/>"/>
                    </div>
                </div>
            </form:form>

            <div class="flex-center form-text-container white-text">
                <spring:message code="register.hasAccount"/>
                <a class="white-text form-text-space" href="<c:url value='/login'/>">
                    <spring:message code="register.loginRedirect"/>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

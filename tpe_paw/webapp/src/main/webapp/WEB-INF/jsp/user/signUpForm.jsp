<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Sign Up!</title>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/registration.css'/>" rel="stylesheet"/>
</head>
<body>
<spring:message code="registerForm.username.hint" var="username_hint"/>
<spring:message code="registerForm.email.hint" var="email_hint"/>
<spring:message code="registerForm.password.hint" var="password_hint"/>
<c:url var="signUpUrl" value="/signup"/>
<div>
    <form:form class="flex-center register-form register-border register-shadow" modelAttribute="registerForm" method="post" action="${signUpUrl}">
        <div class="flex-column register-form-data">
            <div class="register-field-container">
                <form:errors path="username" cssClass="form-error" element="p" />
                <label>
                    <form:input class="register-border register-field-size" path="username" placeholder='${username_hint}'/>
                </label>
            </div>

            <div class="register-field-container border-radius">
                <form:errors path="email" cssClass="form-error" element="p "/>
                <label>
                    <form:input class="register-border register-field-size" path="email" placeholder='${email_hint}'/>
                </label>
            </div>

            <div class="register-field-container">
                <form:errors path="password" cssClass="form-error" element="p "/>
                <label>
                    <form:input class="register-border register-field-size" path="password" placeholder='${password_hint}' type="password"/>
                </label>
            </div>

            <div class="register-field-container">
                <form:errors path="repeatPassword" cssClass="form-error" element="p "/>
                <label>
                    <form:input class="register-border register-field-size" path="repeatPassword" placeholder='${password_hint}' type="password"/>
                </label>
            </div>

            <div class="register-field-container">
                <input class="register-border register-field-size register-button" type="submit" value="<spring:message code="registerForm.submit"/>"/>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>

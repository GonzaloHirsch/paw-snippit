<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Sign Up!</title>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
</head>
<body>
<spring:message code="registerForm.username.hint" var="username_hint"/>
<spring:message code="registerForm.email" var="email"/>
<spring:message code="registerForm.password" var="password"/>
<c:url var="signUpUrl" value="/signup"/>
<form:form modelAttribute="signUpForm" method="post" action="${signUpUrl}">
    
    <div>
        <form:errors path="username" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.username"/>
            <form:input path="username" placeholder='${username_hint}'/>
        </label>
    </div>

    <div>
        <form:errors path="email" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.email"/>
            <form:input path="email" placeholder='${email}'/>
        </label>
    </div>

    <div>
        <form:errors path="password" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.password"/>
            <form:input path="password" placeholder='${password}' type="password"/>
        </label>
    </div>

    <div>
        <form:errors path="repeatPassword" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.repeatPassword"/>
            <form:input path="repeatPassword" placeholder='${password}' type="password"/>
        </label>
    </div>

    <div>
        <input type="submit" value="<spring:message code="registerForm.submit"/>"/>
    </div>
</form:form>
</body>
</html>

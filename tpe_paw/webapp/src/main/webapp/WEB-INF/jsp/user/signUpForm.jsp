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
<c:url var="signUpUrl" value="/signUp"/>
<form:form modelAttribute="signUpForm" method="post" action="${signUpUrl}">
    
    <div>
        <form:errors path="username" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.username"/>
            <input path="username" placeholder="<spring:message code='registerForm.username.hint'/>"/>
        </label>
    </div>

    <div>
        <form:errors path="email" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.email"/>
            <input path="email" placeholder="<spring:message code='registerForm.email'/>"/>
        </label>
    </div>

    <div>
        <errors path="password" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.password"/>
            <input path="password" placeholder="<spring:message code='registerForm.password'/>" type="password"/>
        </label>
    </div>

    <div>
        <errors path="repeatPassword" cssClass="form-error" element="p "/>
        <label>
            <spring:message code="registerForm.repeatPassword"/>
            <input path="repeatPassword" placeholder="<spring:message code='registerForm.password'/>" type="password"/>
        </label>
    </div>

    <div>
        <input type="submit" value="<spring:message code="registerForm.signUp"/>"/>
    </div>
</form:form>
</body>
</html>

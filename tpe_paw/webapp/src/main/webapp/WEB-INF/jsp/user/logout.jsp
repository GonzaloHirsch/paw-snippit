<%--
  Created by IntelliJ IDEA.
  User: fpetrikovich
  Date: 4/20/20
  Time: 2:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <head>
        <title><spring:message code="logout.title"/></title>
        <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/registration.css'/>" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<body>
<c:url value="/login" var="loginUrl" />
<div class="flex-column flex-center TODO-delete">

    <div class="flex-row flex-center register-text-container register-text register-welcome-text fw-300">
        <i class="material-icons app-icon register-app-icon-margin">code</i>
        <spring:message code="logout.goodbye"/>
    </div>

    <div class="flex-center register-form register-border register-shadow">
        <div class="flex-column register-form-data">
            <div class="register-field-container">
                <a class="register-border register-field-size register-button fw-500 register-text" href="<c:url value='/login'/>">
                    <spring:message code="login.signupRedirect"/>
                </a>
            </div>
        </div>
    </div>

</div>

</body>
</html>

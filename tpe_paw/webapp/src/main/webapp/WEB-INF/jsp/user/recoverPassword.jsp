<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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

        <c:set var="sendEmailUrl" value="/sendEmail"/>
        <c:set var="error" value="${requestScope.error}" scope="request"/>
        <div class="flex-column flex-center register-block register-border">

            <!-- Title -->
            <div class="flex-row flex-center register-text-container white-text register-welcome-text fw-300">
                <i class="material-icons app-icon register-app-icon-margin">code</i>
                <spring:message code="recoverPassword.title"/>
            </div>

            <!-- RECOVER PASSWORD form -->
            <form class="flex-center register-form register-border register-shadow" action="${sendEmailUrl}" method="post" enctype="application/x-www-form-urlencoded">

                <div class="flex-column register-form-data">
                    <c:if test="${error}">
                        <div class="flex-center register-text-error form-error">
                            <spring:message code="loginForm.invalid"/>
                        </div>
                    </c:if>

                    <div class="register-field-container">
                        <label>
                            <i class="material-icons register-icons">email</i>
                            <input class="register-border register-field-size register-field-padding" name="userEmail" placeholder="<spring:message code='recoverPassword.insertMail'/>">
                        </label>
                    </div>
                    <div class="register-field-container">
                        <input class="register-border register-field-size register-button fw-500" type="submit" value="<spring:message code="recoverPassword.submit"/>"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
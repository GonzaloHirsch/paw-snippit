<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="verify.email.page.top.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/checkbox.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <div class="flex-column flex-center form-block form-border">

            <!-- Title -->
            <div class="flex-row flex-center form-text-container white-text form-welcome-text fw-300">
                <i class="material-icons app-icon form-app-icon-margin">code</i>
                <spring:message code="verify.email.page.title"/>
            </div>

            <c:url var="verifyEmailUrl" value="/verify-email">
                <c:param name="id" value="${param.id}"/>
            </c:url>
            <c:url var="resendEmailUrl" value="/resend-email-verification">
                <c:param name="id" value="${param.id}"/>
            </c:url>
            <!-- VERIFY EMAIL form -->
            <form:form class="flex-center form form-border form-shadow" action="${verifyEmailUrl}" modelAttribute="verificationForm" method="post">

                <div class="flex-column form-form-data">
                    <div class="form-info-text">
                        <div class="form-field-container">
                            <spring:message code="verify.email.page.instructions"/>
                        </div>
                    </div>
                    <div class="form-field-container">
                        <label>
                            <form:errors path="code" cssClass="form-error" element="p"/>
                            <i class="material-icons form-icons">vpn_key</i>
                            <input class="form-border form-field-size form-field-layout" name="code" placeholder="<spring:message code='verify.email.page.code'/>">
                        </label>
                    </div>
                    <div class="form-field-container">
                        <input class="form-border form-field-size form-button form-button-basics fw-500" type="submit" value="<spring:message code="verify.email.page.submit"/>"/>
                    </div>
                </div>
            </form:form>
            <form:form class="flex-center" action="${resendEmailUrl}" method="post">
                <a class="white-text form-text-space clickable-entity" onclick="submitForm(this)"><spring:message code="verify.email.page.resend"/></a>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
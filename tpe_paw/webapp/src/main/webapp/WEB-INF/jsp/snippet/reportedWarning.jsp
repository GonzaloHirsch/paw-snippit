<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value='/resources/css/flaggedSnippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
</head>
<body>
<spring:message code="reported.warning" var="reportWarn"/>
<spring:message code="reported.warning.message" var="reportMsg"/>

<div class="flex-row flex-align reported-container border-radius">
    <i class="reported-warning-icon material-icons transition">report</i>
    <div class="flex-column warning-message-container">
        <h3 class="reported-text"><c:out value="${reportWarn}"/></h3>
        <p class="reported-text"><c:out value="${reportMsg}"/></p>
    </div>
</div>

</body>
</html>

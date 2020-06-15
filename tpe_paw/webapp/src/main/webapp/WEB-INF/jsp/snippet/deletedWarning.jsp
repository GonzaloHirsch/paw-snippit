<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/flaggedSnippet.css'/>" rel="stylesheet"/>
</head>
<body>
<spring:message code="deleted.snippet.warning" var="deletedWarn"/>
<spring:message code="deleted.snippet.warning.message" var="deleteMsg"/>
<div class="flex-row flex-center warning-container border-radius">
    <i class="warning-icon material-icons">delete_sweep</i>
    <div class="flex-column warning-message-container">
        <h3 class="form-error"><c:out value="${deletedWarn}"/></h3>
        <p class="form-error"><c:out value="${deleteMsg}"/></p>
    </div>
</div>

</body>
</html>

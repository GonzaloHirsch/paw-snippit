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
<spring:message var="langWarn" code="deleted.language.warning"/>
<spring:message var="langWarnMessage" code="deleted.language.warning.message"/>
<div class="flex-row flex-center warning-container border-radius">
    <i class="warning-icon material-icons">delete_sweep</i>
    <div class="flex-column warning-message-container">
        <h3 class="form-error"><c:out value="${langWarn}"/></h3>
        <p class="form-error"><c:out value="${langWarnMessage}"/></p>
    </div>
</div>

</body>
</html>

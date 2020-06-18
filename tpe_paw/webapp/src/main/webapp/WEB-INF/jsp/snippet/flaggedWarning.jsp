<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/flaggedSnippet.css'/>" rel="stylesheet"/>
</head>
<body>
<spring:message code="flagged.warning" var="flaggedWarn"/>
<spring:message code="flagged.warning.message.1" var="flaggedMsg1"/>
<spring:message code="flagged.warning.message.2" var="flaggedMsg2"/>

    <div class="flex-row flex-align warning-container border-radius">
        <i class="warning-icon material-icons transition">warning</i>
        <div class="flex-column warning-message-container">
            <h3 class="form-error"><c:out value="${flaggedWarn}"/></h3>
            <p class="form-error"><c:out value="${flaggedMsg1}"/></p>
            <p class="form-error"><c:out value="${flaggedMsg2}"/></p>
        </div>
    </div>

</body>
</html>

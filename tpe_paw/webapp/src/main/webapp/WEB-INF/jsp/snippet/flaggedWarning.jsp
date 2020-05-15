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
    <div class="flex-row flex-center flagged-container border-radius">
        <i class="flagged-warning-icon material-icons">warning</i>
        <div class="flex-column flagged-message-container">
            <h3 class="form-error"><spring:message code="flagged.warning"/></h3>
            <p class="form-error"><spring:message code="flagged.warning.message.1"/></p>
            <p class="form-error"><spring:message code="flagged.warning.message.2"/></p>
        </div>
    </div>

</body>
</html>

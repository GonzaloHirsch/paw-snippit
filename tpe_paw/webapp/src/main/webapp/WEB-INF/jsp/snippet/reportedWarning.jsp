<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="<c:url value='/resources/css/flaggedSnippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>
<spring:message code="reported.warning" var="reportWarn"/>
<spring:message code="reported.warning.message" var="reportMsg"/>
<spring:message code="reported.warning.suggestion" var="reportSuggestion"/>

<div class="flex-row flex-align reported-container border-radius">
    <c:url var="dismissWarningUrl" value="/snippet/${snippet.id}/report/dismiss"/>
    <i class="reported-warning-icon material-icons transition">report</i>
    <div class="flex-column warning-message-container">
        <div class="flex-row flex-space-between">
            <h3 class="reported-text"><c:out value="${reportWarn}"/></h3>
            <form:form class="form-container" action="${dismissWarningUrl}" method="post" modelAttribute="dismissReportForm">
                <form:checkbox class="hidden" id="dismissWarn" path="delete" value="true" onclick="updateForm(this)"/>
                <label for="dismissWarn" class="no-margin">
                    <div class="reported-dismiss-icon material-icons transition">cancel</div>
                </label>
            </form:form>
        </div>
        <p class="reported-text reported-msg-margin"><c:out value="${reportMsg}"/></p>
        <p class="reported-text reported-msg-margin"><c:out value="${reportSuggestion}"/></p>
    </div>
</div>

</body>
</html>

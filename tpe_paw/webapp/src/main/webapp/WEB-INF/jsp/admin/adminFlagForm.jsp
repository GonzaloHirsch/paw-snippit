<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>

<div class="flex-column">
    <c:set var="snippetId" value="${requestScope.snippetId}"/>
    <form:form class="form-container flex-center" action="${snippetId}/flag" method="post" modelAttribute="adminFlagForm">
        <form:checkbox class="hidden" id="flag-button" path="flagged" value="true" onclick="updateForm(this)"/>
        <label for="flag-button">
            <c:choose>
                <c:when test="${adminFlagForm.flagged}">
                    <i class="selected-red-icon material-icons snippet-icon">flag</i>
                </c:when>
                <c:otherwise>
                    <i class="unselected-red-icon material-icons snippet-icon">outlined_flag</i>
                </c:otherwise>
            </c:choose>
        </label>
    </form:form>
</div>

</body>
</html>

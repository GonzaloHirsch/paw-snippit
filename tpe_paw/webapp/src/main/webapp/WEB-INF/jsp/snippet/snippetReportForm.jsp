<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>

<body>
<div class="flex-column">
    <c:set var="snippetId" value="${requestScope.snippetId}"/>
    <form:form class="form-container flex-center" action="${snippetId}/report" method="post" modelAttribute="reportForm">
        <form:checkbox class="hidden" id="report-button" path="reported" value="true" onclick="updateForm(this)"/>
        <label for="report-button" class="no-margin">
            <c:choose>
                <c:when test="${reportForm.reported}">
                    <i class="unselected-brown-icon material-icons snippet-icon">report</i>
                </c:when>
                <c:otherwise>
                    <i class="selected-brown-icon material-icons snippet-icon">report</i>
                </c:otherwise>
            </c:choose>
        </label>
    </form:form>
</div>

</body>
</html>

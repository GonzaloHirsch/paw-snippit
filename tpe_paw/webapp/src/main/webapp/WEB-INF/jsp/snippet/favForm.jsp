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
    <form:form class="form-container flex-center" action="${snippetId}/fav" method="post" modelAttribute="favForm">
        <form:checkbox class="hidden" id="fav-button" path="favorite" value="true" onclick="updateForm(this)"/>
        <label for="fav-button" class="no-margin">
            <c:choose>
                <c:when test="${favForm.favorite}">
                    <i class="selected-red-icon material-icons snippet-icon transition">favorite</i>
                </c:when>
                <c:otherwise>
                    <i class="unselected-red-icon material-icons snippet-icon transition">favorite_border</i>
                </c:otherwise>
            </c:choose>
        </label>
    </form:form>
</div>

</body>
</html>

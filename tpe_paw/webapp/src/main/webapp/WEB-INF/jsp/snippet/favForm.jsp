<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/favorites.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>

<body>
<div class="flex-column">
    <form:form class="form-container" action="snippet/${snippetId}/fav" method="post" modelAttribute="favForm">

        <form:checkbox class="hidden" id="fav-button" path="favorite" value="false" onchange="updateFavValue(this)" onclick="updateForm(this)"/>
        <label for="fav-button">
            <c:choose>
                <c:when test="${true}">
                    <i class="fav-icon-selected material-icons snippet-icon">favorite</i>
                </c:when>
                <c:otherwise>
                    <i class="fav-icon material-icons snippet-icon">favorite_border</i>
                </c:otherwise>
            </c:choose>
        </label>

        <form:input class="hidden" path="userId"/>
        <form:input class="hidden" path="snippetId"/>
    </form:form>
</div>

</body>
</html>

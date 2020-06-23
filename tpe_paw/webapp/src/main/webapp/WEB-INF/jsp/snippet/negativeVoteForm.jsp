
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value='/resources/css/vote.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>

<body>
<c:set var="snippetId" value="${requestScope.snippetId}"/>

<div class="flex-column">
    <form:form class="flex-row form-container" action="${snippetId}/vote/negative" method="post" modelAttribute="negativeVoteForm">
        <form:checkbox class="hidden" id="neg-vote-button" path="voteSelected" value="true" onclick="updateForm(this)"/>
        <label for="neg-vote-button" class="no-margin">
            <c:choose>
                <c:when test="${negativeVoteForm.voteSelected}">
                    <i class="vote-down-selected material-icons snippet-icon transition">thumb_down</i>
                </c:when>
                <c:otherwise>
                    <i class="vote-down material-icons snippet-icon transition">thumb_down</i>
                </c:otherwise>
            </c:choose>
        </label>
    </form:form>
</div>
</body>
</html>

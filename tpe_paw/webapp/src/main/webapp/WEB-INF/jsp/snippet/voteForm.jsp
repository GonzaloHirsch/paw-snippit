
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
<c:set var="votes" value="${requestScope.votes}"/>
    <div class="flex-column">
        <form:form class="flex-row form-container" action="${snippetId}/vote" method="post" modelAttribute="voteForm">
            <form:radiobutton class="hidden" id="vote-up-button" path="type" value="1" onclick="updateForm(this)"/>
            <label for="vote-up-button">
                <c:choose>
                    <c:when test="${voteForm.oldType == 1}">
                        <i class="vote-up-selected material-icons snippet-icon">thumb_up</i>
                    </c:when>
                    <c:otherwise>
                        <i class="vote-up material-icons snippet-icon">thumb_up</i>
                    </c:otherwise>
                </c:choose>
            </label>
            <label class="flex-center vote-count">
                ${votes}
            </label>
            <form:radiobutton class="hidden" id="vote-down-button" path="type" value="-1" onclick="updateForm(this)"/>
            <label for="vote-down-button">
                <c:choose>
                    <c:when test="${voteForm.oldType == -1}">
                        <i class="vote-down-selected material-icons snippet-icon">thumb_down</i>
                    </c:when>
                    <c:otherwise>
                        <i class="vote-down material-icons snippet-icon">thumb_down</i>
                    </c:otherwise>
                </c:choose>
            </label>
            <form:input class="hidden" path="oldType"/>
        </form:form>
    </div>
</body>
</html>

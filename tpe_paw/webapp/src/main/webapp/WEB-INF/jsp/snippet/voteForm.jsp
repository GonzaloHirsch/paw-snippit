
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value='/resources/css/vote.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/voteForm.js'/>"></script>
</head>

<body>
    <div class="flex-column">
        <form:form class="flex-row vote-container" action="vote" method="post" modelAttribute="vote">
            <form:radiobutton class="hidden" id="vote-up-button" path="type" value="1" onclick="updateVote(this)"/>
            <label for="vote-up-button">
                <c:choose>
                    <c:when test="${vote.oldType == 1}">
                        <i class="vote-up-selected material-icons vote-arrow">thumb_up</i>
                    </c:when>
                    <c:otherwise>
                        <i class="vote-up material-icons vote-arrow">thumb_up</i>
                    </c:otherwise>
                </c:choose>
            </label>

            <form:radiobutton class="hidden" id="vote-down-button" path="type" value="0" onclick="updateVote(this)"/>
            <label for="vote-down-button">
                <c:choose>
                    <c:when test="${vote.oldType == -1}">
                        <i class="vote-down-selected material-icons vote-arrow">thumb_down</i>
                    </c:when>
                    <c:otherwise>
                        <i class="vote-down material-icons vote-arrow">thumb_down</i>
                    </c:otherwise>
                </c:choose>
            </label>
            <form:input class="hidden" path="oldType"/>
            <form:input class="hidden" path="userId"/>
            <form:input class="hidden" path="snippetId"/>
        </form:form>
    </div>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: fpetrikovich
  Date: 4/13/20
  Time: 4:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>

<body>
<div class="flex-column">
    <form:form action="fav" method="post" modelAttribute="favForm">

        <form:radiobutton class="hidden" id="fav-button" path="isFav" value="true" onclick="updateForm(this)"/>
        <label for="fav-button">
            <c:choose>
                <c:when test="${favForm.isFav}">
                    <i class="vote-up-selected material-icons vote-arrow">thumb_up</i>
                </c:when>
                <c:otherwise>
                    <i class="vote-up material-icons vote-arrow">thumb_up</i>
                </c:otherwise>
            </c:choose>
        </label>

        <form:input class="hidden" path="userId"/>
        <form:input class="hidden" path="snippetId"/>
    </form:form>
</div>

</body>
</html>

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
    <form:form action="${snippetId}/delete" class="form-container flex-center" method="post" modelAttribute="deleteForm">
        <form:checkbox class="hidden" path="delete" value="true" id="snippet-delete-button" onclick="updateForm(this)"/>
        <label for="snippet-delete-button" class="flex-center">
            <div class="flex-center no-text-decoration">
                <i class="delete-snippet-icon snippet-icon material-icons ">delete</i>
            </div>
        </label>
    </form:form>
</div>
</body>
</html>

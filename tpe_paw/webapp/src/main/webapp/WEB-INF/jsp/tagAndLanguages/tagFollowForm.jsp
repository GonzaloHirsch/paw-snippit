<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>

<body>
<div class="flex-column">
    <c:set var="tagtId" value="${requestScope.tagId}"/>
    <form:form class="form-container flex-center" action="${tagId}/follow" method="post" modelAttribute="followForm">
        <form:checkbox class="hidden" id="follow-button" path="follows" value="true" onclick="updateForm(this)"/>
        <label class="no-margin" for="follow-button">
            <c:choose>
                <c:when test="${followForm.follows}">
                    <div class="tag-snippets-button border-radius flex-center no-text-decoration">
                        <spring:message code="tags.unfollow"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="tag-snippets-button border-radius flex-center no-text-decoration">
                        <spring:message code="tags.follow"/>
                    </div>
                </c:otherwise>
            </c:choose>
        </label>
    </form:form>
</div>

</body>
</html>

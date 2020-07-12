<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>
<c:set var="tag" value="${requestScope.tag}"/>

    <div class="flex-row flex-space-between chip-container transition">
        <a href="<c:url value='/tags/${tag.id}'/>" class="no-text-decoration fw-300 chip-name">
            <c:out value="${tag.name.toLowerCase()}"/>
        </a>
        <c:set var="followForm" value="unfollowForm${tag.id}"/>
        <c:url var="tagFollowIconUrl" value="/tags/${tag.id}/follow"/>

        <form:form class="form-container flex-center" action="${tagFollowIconUrl}" method="post" modelAttribute="${followForm}">
            <form:checkbox class="hidden" id="tag-${tag.name}" path="follows" value="true" onclick="updateForm(this)"/>
            <label for="tag-${tag.name}">
                <div class="chip-delete-icon material-icons transition">cancel</div>
            </label>
        </form:form>

    </div>
</body>
</html>
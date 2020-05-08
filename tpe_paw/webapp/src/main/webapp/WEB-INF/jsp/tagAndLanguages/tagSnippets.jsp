<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="tag.title" arguments="${tag.name.toUpperCase()}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/elementSnippets.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/element.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>
    <div class="wrapper">
        <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
        <div class="main-content">
            <div class="flex-row flex-center element-snippets-top-container">
                <div class="fw-100 title-container">
                    <spring:message code="tagSnippets.title" arguments="${tag.name.toUpperCase()}"/>
                </div>
                <c:if test="${currentUser != null}">
                    <c:set var="tagId" value="${tag.id}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/tagAndLanguages/tagFollowForm.jsp"/>

                    <c:if test="${userRoles.contains('ADMIN')}">
                        <form:form action="${tag.id}/delete" class="form-container" method="post" modelAttribute="deleteForm">
                            <form:checkbox class="hidden" path="delete" value="true" id="tag-delete-button" onclick="updateForm(this)"/>
                            <label for="tag-delete-button" class="flex-center">
                                <div class="flex-center no-text-decoration">
                                    <i class="delete-element-icon material-icons border-radius">delete</i>
                                </div>
                            </label>
                        </form:form>
                    </c:if>
                </c:if>
            </div>
            <c:set var="snippetList" value="${snippetList}" scope="request"/>
            <c:import url="/WEB-INF/jsp/snippet/snippetFeed.jsp"/>
        </div>
    </div>
</body>
</html>

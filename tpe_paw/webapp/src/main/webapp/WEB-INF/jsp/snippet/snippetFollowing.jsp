<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="app.name"/> | <spring:message code="menu.following"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/followingTagChips.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
<div class="wrapper">
    <c:set var="currentUser" value="${currentUser}" scope="request"/>
    <c:set var="userTags" value="${userTags}" scope="request"/>
    <c:set var="searchForm" value="${searchContext}" scope="request"/>
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-column">
        <div class="flex-row title-section">
            <div class="flex-column fw-100 flex-grow page-title">
                <spring:message code="menu.following"> </spring:message>
            </div>
            <div class="flex-column flex-center">
                <c:import url="/WEB-INF/jsp/navigation/navigationPage.jsp"/>
            </div>
        </div>
        <div class="flex-row">
            <c:if test="${followingTags.size() > 0}">
                <div class="flex-column chip-list-container">
                    <c:set var="followingTags" value="${requestScope.followingTags}"/>

                    <c:forEach var="tag" items="${followingTags}">
                        <c:set var="tag" value="${tag}" scope="request"/>
                        <c:import url="/WEB-INF/jsp/tagAndLanguages/tagChip.jsp"/>
                    </c:forEach>
                </div>
            </c:if>
            <div class="flex-column flex-grow explore-feed">
                <c:set var="snippetList" value="${snippetList}" scope="request"/>
                <c:import url="snippetFeed.jsp"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>

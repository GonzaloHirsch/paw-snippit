<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <c:choose>
        <c:when test="${searchContext == ''}">
            <spring:message code="menu.home" var="title"/>
        </c:when>
        <c:when test="${searchContext == 'flagged/'}">
            <spring:message code="menu.flagged" var="title"/>
        </c:when>
        <c:when test="${searchContext == 'following/'}">
            <spring:message code="menu.following" var="title"/>
        </c:when>
        <c:when test="${searchContext == 'favorites/'}">
            <spring:message code="menu.favorites" var="title"/>
        </c:when>
        <c:when test="${searchContext == 'upvoted/'}">
            <spring:message code="menu.upvoted" var="title"/>
        </c:when>
    </c:choose>
    <title><spring:message code="app.name"/> | ${title}</title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
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
    <div class="main-content">
        <div class="flex-row title-section">
            <div class="flex-column fw-100 flex-grow page-title">
                <c:choose>
                    <c:when test="${searchContext == ''}">
                        <spring:message code="menu.home"/>
                    </c:when>
                    <c:when test="${searchContext == 'flagged/'}">
                        <spring:message code="menu.flagged"/>
                    </c:when>
                    <c:when test="${searchContext == 'following/'}">
                        <spring:message code="menu.following"/>
                    </c:when>
                    <c:when test="${searchContext == 'favorites/'}">
                        <spring:message code="menu.favorites"/>
                    </c:when>
                    <c:when test="${searchContext == 'upvoted/'}">
                        <spring:message code="menu.upvoted"/>
                    </c:when>
                </c:choose>
            </div>
            <div class="flex-column flex-center">
                <c:import url="/WEB-INF/jsp/navigation/navigationPage.jsp"/>
            </div>
        </div>
        <c:set var="snippetList" value="${snippetList}" scope="request"/>
        <c:import url="snippet/snippetFeed.jsp"/>
    </div>
</div>
</body>
</html>
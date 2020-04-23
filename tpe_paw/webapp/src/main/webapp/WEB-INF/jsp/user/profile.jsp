<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Profile</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/profile.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-column detail-user">
            <div class="flex-row">
                <img class="profile-photo" src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                <div class="flex-column profile-info">
                    <div class="profile-username">
                        ${user.username}
                    </div>
                    <div class="fw-100">
                        <spring:message code="profile.joined"/>
                        ${user.dateJoined}
                    </div>
                    <div class="flex-row profile-info-item">
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${snippets.size()}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.snippets"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${user.reputation}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.reputation"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${user.reputation}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.following"/></div>
                        </div>
                    </div>
                    <div class="profile-description profile-info-item">
                        ${user.description}
                    </div>
                </div>
            </div>
        </div>
        <hr class="divider"/>
        <div class="feed-background-color">
            <c:if test="${snippets.size() == 0}">
                <div class="profile-no-snippet flex-center fw-100">
                    <spring:message code="profile.no-snippets"/>
                </div>
            </c:if>
            <c:if test="${snippets.size() > 0}">
                <c:set var="snippetList" value="${snippets}" scope="request"/>
                <c:import url="/WEB-INF/jsp/snippet/snippetFeed.jsp"/>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>

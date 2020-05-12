<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link href="<c:url value='/resources/css/snippetFeed.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetCard.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
</head>
<body>
<c:set var="snippetList" value="${requestScope.snippetList}"/>
<c:if test="${snippetList.size() == 0}">
    <div class="no-snippet flex-center fw-100">
        <spring:message code="feed.no-snippets"/>
    </div>
</c:if>
<c:if test="${snippetList.size() > 0}">
<%--    <div class="feed-snippets-grid">--%>
    <div class="masonry-wrapper">
        <div class="masonry">
            <c:forEach var="snippet" items="${snippetList}">
                <c:set var="snippet" value="${snippet}" scope="request"/>
                <c:import url="/WEB-INF/jsp/snippet/snippetCard.jsp"/>
            </c:forEach>
        </div>
    </div>
</c:if>
<c:import url="/WEB-INF/jsp/navigation/navigationPage.jsp"/>
<script src="<c:url value='/resources/js/snippetsFeed.js'/>"></script>
</body>
</html>

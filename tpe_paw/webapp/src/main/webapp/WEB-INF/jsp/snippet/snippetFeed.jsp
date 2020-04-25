<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
<head>
    <title>Snippet Feed</title>
    <link href="<c:url value='/resources/css/snippetFeed.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
<c:set var="snippetList" value="${requestScope.snippetList}"/>
<div class="feed-snippets-grid">
    <c:import url="/WEB-INF/jsp/navigation/navigationPage.jsp"/>
    <c:forEach var="snippet" items="${snippetList}">
        <c:set var="snippet" value="${snippet}" scope="request"/>
        <c:import url="/WEB-INF/jsp/snippet/snippetCard.jsp"/>
    </c:forEach>
</div>
<script src="<c:url value='/resources/js/snippetsFeed.js'/>"></script>
</body>
</html>

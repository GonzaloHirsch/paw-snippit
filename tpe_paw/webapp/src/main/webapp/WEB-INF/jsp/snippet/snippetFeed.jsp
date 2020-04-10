<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <title>Snippet Feed</title>
        <link href="<c:url value='/resources/css/feedSnippets.css'/>" rel="stylesheet" />
    </head>
    <body>
        <div class="feed-snippets-grid">
            <c:forEach var="snippet" items="${snippetList}">
                <c:set var="snippet" value="${snippet}" scope="request"/>
                <c:import url="snippetCard.jsp"/>
            </c:forEach>
        </div>

        <script src="<c:url value='/resources/js/feedSnippets.js'/>"></script>
    </body>
</html>

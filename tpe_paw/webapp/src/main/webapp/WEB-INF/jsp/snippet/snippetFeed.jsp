<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <title>Snippet Feed</title>
        <link href="<c:url value='/resources/css/cardSnippet.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/feedSnippets.css'/>" rel="stylesheet" />
    </head>
    <body>
        <h2>This is the snippet feed!</h2>
        <div class="feed-snippets-grid">
            <c:forEach var="snippet" items="${snippetList}">
                <div class="card-snippet-container">
                    <div class="card-snippet-content">
                        <h2 class="card-snippet-text">Card: ${snippet.title}</h2>
                        <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                            <p class="card-snippet-text">${snippet.description}</p>
                        </c:if>
                        <div class="card-snippet-code">${snippet.code}</div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <script src="<c:url value='/resources/js/feedSnippets.js'/>"></script>
    </body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <title>Snippet Feed</title>
        <link href="<c:url value='/resources/css/cardSnippet.css'/>" rel="stylesheet" />
    </head>
    <body>
        <h2>This is the snippet feed!</h2>
        <c:forEach var="snippet" items="${snippetList}">
            <div class="card-snippet-container">
                <h2 class="card-snippet-title">Card: ${snippet.title}</h2>
                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <p class="card-snippet-description">${snippet.description}</p>
                </c:if>
                <div class="card-snippet-code">${snippet.code}</div>
            </div>
        </c:forEach>
    </body>
</html>

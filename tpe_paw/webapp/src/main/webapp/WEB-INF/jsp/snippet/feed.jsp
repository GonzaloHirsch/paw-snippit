<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <title>Snippet Feed</title>
    </head>
    <body>
        <h2>This is the snippet feed!</h2>
        <c:forEach var="snippet" items="${snippetList}">
            <div class="card-snippet-container">
                <h1>Card: ${snippet.title}</h1>
                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <h3>${snippet.description}</h3>
                </c:if>
                <h3>${snippet.code}</h3>
            </div>
        </c:forEach>
    </body>
</html>

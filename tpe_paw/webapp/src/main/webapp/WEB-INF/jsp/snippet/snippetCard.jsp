<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <link href="<c:url value='/resources/css/cardSnippet.css'/>" rel="stylesheet" />
    </head>
    <body>
        <c:set var="snippet" value="${requestScope.snippet}"/>
        <div class="card-snippet-container">
            <div class="card-snippet-content">
                <h2 class="card-snippet-text">Card: ${snippet.title}</h2>
                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <p class="card-snippet-text">${snippet.description}</p>
                </c:if>
                <div class="card-snippet-code">${snippet.code}</div>
            </div>
        </div>
    </body>
</html>

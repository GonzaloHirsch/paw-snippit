<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <link href="<c:url value='/resources/css/cardSnippet.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
    </head>
    <body>
        <c:set var="snippet" value="${requestScope.snippet}"/>
        <div class="card-snippet-container">
            <div class="card-snippet-content">
                <h2 class="snippet-text">Card: ${fn:escapeXml(snippet.title)}</h2>
                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <p class="snippet-text">${fn:escapeXml(snippet.description)}</p>
                </c:if>
                <div class="snippet-code-container">
                    <div class="card-snippet-code-block">
                        <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
                        <c:if test="true">
                            <p class="card-snippet-fade-out"></p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

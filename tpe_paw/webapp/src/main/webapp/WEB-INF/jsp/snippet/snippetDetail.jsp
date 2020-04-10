<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
</head>
<body>
    <div>
        <h2 class="snippet-text">${fn:escapeXml(snippet.title)}</h2>
        <c:if test="${!StringUtils.isEmpty(snippet.description)}">
            <div>
                <p class="snippet-text">${fn:escapeXml(snippet.description)}</p>
            </div>
        </c:if>
        <div class="snippet-code-container">
            <div>
                <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
            </div>
        </div>
    </div>
</body>
</html>

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
                <div class="card-snippet-upload-details">
                    <div class="card-snippet-upload-details">
                        <img src="<c:url value='/resources/images/paw_icon.jpg'/>" alt="User Icon"/>
                        <div class="card-snippet-upload-info">
                            <div class="snippet-text">${snippet.owner.username}</div>
                            <div class="snippet-text card-snippet-date">${snippet.dateCreated}</div>
                        </div>
                    </div>
                    <div class="snippet-language-tag">${snippet.language.toUpperCase()}</div>
                </div>
                <div class="snippet-text card-snippet-title">${fn:escapeXml(snippet.title)}</div>
                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <div class="card-snippet-block card-snippet-descr-block">
                        <div class="snippet-text">${fn:escapeXml(snippet.description)}</div>
                        <p class="card-snippet-fade-out card-snippet-fade-out-descr"></p>
                    </div>
                </c:if>
                <div class="snippet-code-container">
                    <div class="card-snippet-block">
                        <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
                        <p class="card-snippet-fade-out card-snippet-fade-out-code"></p>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <link href="<c:url value='/resources/css/snippetCard.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet" />
    </head>
    <body>
        <c:set var="snippet" value="${requestScope.snippet}"/>
        <a href="<c:url value='/snippet/${snippet.id}'/>" class="flex-column card-snippet-container">
            <div class="flex-column card-snippet-content">

                <!-- Top card section containing icon, username, date and language -->
                <div class="flex-row snippet-upload-spacing">
                    <div class="flex-row snippet-user-info">
                        <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                        <div class="flex-column snippet-upload-info card-snippet-icon">
                            <div class="snippet-text">${snippet.owner.username}</div>
                            <div class="snippet-text card-snippet-date">${snippet.dateCreated}</div>
                        </div>
                    </div>
                    <div class="snippet-language-tag border-radius">${snippet.language.toUpperCase()}</div>
                </div>

                <!-- Bottom part of the card with the title, description and code -->
                <div class="snippet-text snippet-title">${fn:escapeXml(snippet.title)}</div>

                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <div class="card-snippet-block card-snippet-descr-block">
                        <div class="snippet-text justify-text">${fn:escapeXml(snippet.description)}</div>
                        <p class="card-snippet-fade-out card-snippet-fade-out-descr"></p>
                    </div>
                </c:if>

                <div class="flex-column snippet-code-container">
                    <div class="card-snippet-block">
                        <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
                        <p class="card-snippet-fade-out card-snippet-fade-out-code"></p>
                    </div>
                </div>

            </div>
        </a>
    </body>
</html>
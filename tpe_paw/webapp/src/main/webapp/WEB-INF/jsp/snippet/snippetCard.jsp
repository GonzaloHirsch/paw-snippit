<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
</head>
<body>
<c:set var="snippet" value="${requestScope.snippet}"/>

<c:set var="desc" value="${fn:escapeXml(snippet.description)}"/>
<c:set var="code" value="${fn:escapeXml(snippet.code)}"/>

<a href="<c:url value='/snippet/${snippet.id}'/>" class="flex-column card-item border-radius">
<%--<a href="<c:url value='/snippet/${snippet.id}'/>" class="flex-column card-snippet-container">--%>
    <div class="flex-column card-snippet-content border-radius">

        <!-- Top card section containing icon, username, date and language -->
        <div class="flex-row snippet-upload-spacing">
            <div class="flex-row snippet-user-info">
                <c:if test="${snippet.owner.icon != null}">
                    <img src="<c:url value="/user/${snippet.owner.id}/image"/>" alt="User Icon"/>
                </c:if>
                <c:if test="${snippet.owner.icon == null}">
                    <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                </c:if>
                <div class="flex-column snippet-upload-info card-snippet-icon">
                    <div class="snippet-text">${snippet.owner.username}</div>
                    <div class="snippet-text card-snippet-date">${snippet.dateCreated}</div>
                </div>
            </div>
            <div class="flex-row flex-center">
                <c:if test="${snippet.flagged}">
                    <div class="card-flagged-warning-icon material-icons">warning</div>
                </c:if>
                <div class="snippet-language-tag border-radius">${snippet.language.toUpperCase()}</div>
            </div>
        </div>

        <!-- Bottom part of the card with the title, description and code -->
        <div class="flex-row snippet-title-container">
            <div class="snippet-text snippet-title">${fn:escapeXml(snippet.title)}</div>
        </div>
        <c:if test="${!StringUtils.isEmpty(snippet.description)}">
            <div class="card-snippet-block card-snippet-descr-block">
                <div class="snippet-text justify-text">${desc}</div>
                <p class="card-snippet-fade-out card-snippet-fade-out-descr hidden"></p>
            </div>
        </c:if>

        <div class="flex-column snippet-code-container border-radius">
            <div class="card-snippet-block">
                <pre><code class="code-element">${code}</code></pre>
                <p class="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
            </div>
        </div>

    </div>
</a>
</body>
</html>

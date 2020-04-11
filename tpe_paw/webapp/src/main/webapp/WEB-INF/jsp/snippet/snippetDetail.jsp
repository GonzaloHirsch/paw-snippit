<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/cardSnippet.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/snippetDetails.js'/>"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet"></head>
<body class="no-margin">
            <div class="snippet-detail-main-row">
                <div class="snippet-detail-left-col">
                    <div class="vote-buttons">
                        <form:form action="vote" method="post" modelAttribute="vote">
                            <form:radiobutton id="up-button" path="type" value="1" onclick="changed(this)"/>
                            <label for="up-button">
                                <i class="material-icons vote-arrow">arrow_drop_up</i>
                            </label>
                            <form:radiobutton id="down-button" path="type" value="0" onclick="changed(this)"/>
                            <label for="down-button">
                                <i class="material-icons vote-arrow">arrow_drop_down</i>
                            </label>
                            <form:input class="hidden" path="oldType"/>
                            <form:input class="hidden" path="userId"/>
                            <form:input class="hidden" path="snippetId"/>
                        </form:form>
                    </div>
                </div>
                <div class="snippet-detail-center-col">
                    <div class="snippet-title-main-row">
                        <div class="snippet-title-left snippet-text card-snippet-title">
                            ${fn:escapeXml(snippet.title)}
                        </div>
                        <div class="snippet-tag-right snippet-language-tag">
                            ${snippet.language.toUpperCase()}
                        </div>
                    </div>
                    <!-- Top card section containing icon, username, date and language -->
                    <div class="card-snippet-upload-details">
                        <div class="card-snippet-upload-details">
                            <div class="card-snippet-upload-info">
                                <div class="snippet-text">${snippet.owner.username}</div>
                                <div class="snippet-text card-snippet-date">${snippet.dateCreated}</div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                        <div class="card-snippet-block card-snippet-descr-block">
                            <div class="snippet-text justify-text">${fn:escapeXml(snippet.description)}</div>
                            <p class="card-snippet-fade-out card-snippet-fade-out-descr"></p>
                        </div>
                    </c:if>
                    <hr class="snippet-detail-divider">
                    <div class="snippet-code-container">
                            <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
                    </div>
                    </div>
                </div>
        </div>
</body>
</html>

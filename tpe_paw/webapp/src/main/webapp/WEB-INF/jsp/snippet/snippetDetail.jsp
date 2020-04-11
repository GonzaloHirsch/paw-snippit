<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetCard.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
</head>

<body>

    <div class="flex-row detail-snippet-container">

        <div class="detail-snippet-left-col">

            <div class="snippet-language-tag">${snippet.language.toUpperCase()}</div>

        </div>

        <div class="flex-column detail-snippet-center">

            <div class="snippet-text snippet-title">
                ${fn:escapeXml(snippet.title)}
            </div>

            <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                <div class="detail-snippet-block-descr">
                    <div class="snippet-text justify-text">${fn:escapeXml(snippet.description)}</div>
                </div>
            </c:if>

            <hr class="detail-snippet-divider">

            <div class="snippet-code-container detail-snippet-block-code">
                <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
            </div>
            <div class="flex-row">
                <div class="flex-column detail-snippet-block border-radius">
                    <c:import url="/WEB-INF/jsp/snippet/voteForm.jsp"/>
                </div>

                <div class="flex-column detail-snippet-block detail-snippet-user border-radius">
                    <div class="snippet-text detail-snippet-date">Uploaded ${snippet.dateCreated}</div>

                    <div class="flex-row snippet-user-info detail-snippet-user-info">
                        <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                        <div class="flex-column snippet-upload-info">
                            <div class="snippet-text">${snippet.owner.username}</div>
                            <div class="flex-row">
                                <!-- TODO agregar for de estrellita -->
                                <div class="snippet-text">${snippet.owner.reputation}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

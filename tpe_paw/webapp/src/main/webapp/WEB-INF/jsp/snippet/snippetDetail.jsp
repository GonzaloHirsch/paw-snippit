<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/favorites.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-row detail-snippet-container flex-center">


            <div class="flex-column detail-snippet-center">

                <div class="flex-row flex-center flex-space-between">
                    <div class="snippet-text snippet-title">
                        ${fn:escapeXml(snippet.title)}
                    </div>
                    <div class="snippet-language-tag detail-snippet-language-margin border-radius">${snippet.language.toUpperCase()}</div>
                </div>

                <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                    <div class="detail-snippet-block-descr">
                        <div class="snippet-text justify-text">${fn:escapeXml(snippet.description)}</div>
                    </div>
                </c:if>

                <hr class="detail-snippet-divider">

                <div class="">
                    <div class="full-width snippet-code-container detail-snippet-block-code">
                        <pre><code>${fn:escapeXml(snippet.code)}</code></pre>
                    </div>
                </div>
                <div class="flex-row flex-center">
                    <!-- FAVORITES -->
                    <div class="flex-center detail-snippet-block border-radius">
                        <i class="material-icons fav-heart">favorite_border</i>
                    </div>

                    <!-- VOTE -->
                    <div class="flex-column flex-center detail-snippet-block border-radius">
                        <c:import url="/WEB-INF/jsp/snippet/voteForm.jsp"/>
                    </div>

                    <!-- USER DETAILS -->
                    <div class="flex-column flex-center detail-snippet-block detail-snippet-user border-radius">
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
    </div>
</div>
</body>
</html>

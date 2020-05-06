<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="snippet"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/element.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/elementsList.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/snippetDetail.js'/>"></script>
</head>

<body>

<c:set var="snippet" value="${requestScope.snippet}"/>
<c:set var="currentUser" value="${requestScope.currentUser}"/>
<c:url var="userRoles" value="${requestScope.userRoles}"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-row detail-snippet-container flex-center">

            <div class="flex-column detail-snippet-center">
                <c:if test="${snippet.flagged}">
                    <c:import url="/WEB-INF/jsp/snippet/flaggedWarning.jsp"/>
                </c:if>

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

                <div class="flex-column">
                    <!-- CODE -->
                    <div class="full-width snippet-code-container border-radius detail-snippet-block-code">
                        <span class="material-icons copy-icon border-radius" onclick="copyContent(this)">
                            file_copy
                        </span>
                        <pre><code id="code-block">${fn:escapeXml(snippet.code)}</code></pre>
                        <input id="hidden-code-input" class="hidden-code"/>
                    </div>
                    <!-- TAGS -->
                    <div class="all-elements-grid detail-grid">
                        <c:forEach var="tag" items="${snippet.tags}">
                            <c:set var="element" value="${tag}" scope="request"/>
                            <c:set var="context" value="tags" scope="request"/>
                            <c:set var="cssClass" value="tag-container-detail" scope="request"/>
                            <c:import url="/WEB-INF/jsp/tagAndLanguages/element.jsp"/>
                        </c:forEach>
                    </div>
                </div>
                <div class="flex-row flex-center">
                    <c:set var="snippetId" value="${snippet.id}" scope="request"/>
                    <c:set var="currentUser" value="${currentUser}" scope="request"/>
                    <c:set var="userRoles" value="${userRoles}" scope="request"/>
                    <!-- FLAG -->
                    <c:if test="${userRoles.contains('ADMIN')}">
                        <div class="flex-center detail-snippet-block border-radius">
                            <c:import url="/WEB-INF/jsp/admin/adminFlagForm.jsp"/>
                        </div>
                    </c:if>

                    <!-- DELETE -->
                    <c:if test="${currentUser.id == snippet.owner.id}">
                        <div class="flex-column flex-center detail-snippet-block border-radius">
                            <c:import url="/WEB-INF/jsp/snippet/snippetDeleteForm.jsp"/>
                        </div>
                    </c:if>

                    <!-- FAVORITES -->
                    <div class="flex-center detail-snippet-block border-radius">
                        <c:import url="/WEB-INF/jsp/snippet/favForm.jsp"/>
                    </div>

                    <c:set var="votes" value="${voteCount}" scope="request"/>
                    <!-- VOTE -->
                    <div class="flex-column flex-center detail-snippet-block border-radius">
                        <c:import url="/WEB-INF/jsp/snippet/voteForm.jsp"/>
                    </div>

                    <!-- USER DETAILS -->
                    <a class="user-url" href="<c:url value='/user/${snippet.owner.id}'/>">
                        <div class="flex-column flex-center detail-snippet-block border-radius">
                            <div class="snippet-text detail-snippet-date">Uploaded ${snippet.dateCreated}</div>

                            <div class="flex-row snippet-user-info detail-snippet-user-info">
                                <c:if test="${snippet.owner.icon != null}">
                                    <img src="<c:url value="/user/${snippet.owner.id}/image"/>" alt="User Icon"/>
                                </c:if>
                                <c:if test="${snippet.owner.icon == null}">
                                    <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                                </c:if>
                                <div class="flex-column snippet-upload-info">
                                    <div class="snippet-text">
                                        ${snippet.owner.username}
                                    </div>
                                    <div class="flex-row">
                                        <!-- TODO agregar for de estrellita -->
                                        <div class="snippet-text">${snippet.owner.reputation}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </div>

        </div>
    </div>
</body>
</html>

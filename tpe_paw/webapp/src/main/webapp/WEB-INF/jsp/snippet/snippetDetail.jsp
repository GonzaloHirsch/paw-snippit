<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="snippet"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/element.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/elementsList.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/snippetDetail.js'/>"></script>
    <link rel="stylesheet"
          href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/10.0.0/styles/lightfair.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/10.0.0/highlight.min.js"></script>
    <script charset="UTF-8"
            src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.0.0/languages/go.min.js"></script>
</head>

<body>

<c:set var="snippet" value="${requestScope.snippet}"/>
<c:set var="currentUser" value="${requestScope.currentUser}"/>
<c:url var="userRoles" value="${requestScope.userRoles}"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">
        <div class="flex-center detail-snippet-container">
            <div class="flex-center form form-border form-shadow">

                <div class="flex-column detail-snippet-center">
                    <c:if test="${snippet.deleted}">
                        <c:import url="/WEB-INF/jsp/snippet/deletedWarning.jsp"/>
                    </c:if>

                    <c:if test="${snippet.flagged}">
                        <c:import url="/WEB-INF/jsp/snippet/flaggedWarning.jsp"/>
                    </c:if>

                    <div class="flex-row flex-center flex-space-between">
                        <div class="snippet-text snippet-title detail-snippet-title">
                            <c:out value="${snippet.title}"/>
                        </div>
                        <a href="<c:url value="/languages/${snippet.language.id}"/>" class="flex-center snippet-language-tag detail-snippet-language-margin border-radius transition">
                            <c:out value="${snippet.language.name.toUpperCase()}"/>
                        </a>
                    </div>

                    <c:if test="${!StringUtils.isEmpty(snippet.description)}">
                        <div class="detail-snippet-block-descr">
                            <div class="snippet-text detail-snippet-descr justify-text"><c:out value="${snippet.description}"/></div>
                        </div>
                    </c:if>

                    <hr class="detail-snippet-divider">

                    <div class="flex-column">
                        <!-- CODE -->
                        <div class="detail-extra-margin">
                            <div class="full-width snippet-code-container border-radius detail-snippet-block-code">
                                <span class="material-icons copy-icon border-radius" onclick="copyContent(this)">
                                    file_copy
                                </span>
                                <pre><code id="code-block" class="${snippet.language.name}"><c:out value="${snippet.code}"/></code></pre>
                                <input id="hidden-code-input" class="hidden-code"/>
                            </div>
                        </div>
                        <!-- TAGS -->
                        <c:if test="${snippet.tags.size() > 0}">
                            <div class="all-elements-grid detail-grid">
                                <c:forEach var="tag" items="${snippet.tags}">
                                    <c:set var="element" value="${tag}" scope="request"/>
                                    <c:set var="context" value="tags" scope="request"/>
                                    <c:set var="cssClass" value="tag-container-detail" scope="request"/>
                                    <c:import url="/WEB-INF/jsp/tagAndLanguages/snippetTag.jsp"/>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                    <div class="flex-row flex-center">
                        <c:set var="snippetId" value="${snippet.id}" scope="request"/>
                        <c:set var="currentUser" value="${currentUser}" scope="request"/>
                        <c:set var="userRoles" value="${userRoles}" scope="request"/>
                        <!-- FLAG -->
                        <c:if test="${userRoles.contains('ADMIN')}">
                            <div class="flex-center detail-snippet-block border-radius form-shadow">
                                <c:import url="/WEB-INF/jsp/admin/adminFlagForm.jsp"/>
                            </div>
                        </c:if>

                        <!-- DELETE -->
                        <c:if test="${currentUser.id == snippet.owner.id}">
                            <c:set var="snippet" value="${snippet}" scope="request"/>
                            <div class="flex-column flex-center detail-snippet-block border-radius form-shadow">
                                <c:import url="/WEB-INF/jsp/snippet/snippetDeleteForm.jsp"/>
                            </div>
                        </c:if>

                        <!-- FAVORITES -->
                        <c:if test="${showFavorite}">
                            <div class="flex-center detail-snippet-block border-radius form-shadow">
                                <c:import url="/WEB-INF/jsp/snippet/favForm.jsp"/>
                            </div>
                        </c:if>
                        <c:set var="votes" value="${voteCount}" scope="request"/>
                        <!-- VOTE -->
                        <c:if test="${!snippet.deleted}">
                            <div class="flex-column flex-center detail-snippet-block border-radius form-shadow">
                                <c:import url="/WEB-INF/jsp/snippet/voteForm.jsp"/>
                            </div>
                        </c:if>

                        <!-- USER DETAILS -->
                        <c:choose>
                            <c:when test="${currentUser != null && currentUser.id == snippet.owner.id}">
                                <c:url var="userProfile" value='/user/${snippet.owner.id}/active'/>
                            </c:when>
                            <c:otherwise>
                                <c:url var="userProfile" value='/user/${snippet.owner.id}'/>
                            </c:otherwise>
                        </c:choose>
                        <a class="no-text-decoration" href="${userProfile}">
                            <div class="flex-column flex-center detail-snippet-block border-radius form-shadow">
                                <spring:message var="uploadedOn" code="snippetDetails.upload.date" arguments="${snippet.getCreationDate()}"/>
                                <div class="snippet-text detail-snippet-date"><c:out value="${uploadedOn}"/></div>
                                <div class="flex-row snippet-user-info detail-snippet-user-info">
                                    <c:if test="${snippet.owner.icon != null}">
                                        <img src="<c:url value="/user/${snippet.owner.id}/image"/>" alt="User Icon"/>
                                    </c:if>
                                    <c:if test="${snippet.owner.icon == null}">
                                        <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                                    </c:if>
                                    <div class="flex-column snippet-upload-info">
                                        <div class="snippet-text">
                                            <c:out value="${snippet.owner.username}"/>
                                        </div>
                                        <div class="flex-row">
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
    </div>
    <script>hljs.initHighlightingOnLoad();</script>
</body>
</html>

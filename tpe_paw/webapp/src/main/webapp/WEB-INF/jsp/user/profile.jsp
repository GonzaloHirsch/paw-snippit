<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="menu.profile.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/profile.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetCreate.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    <script src="<c:url value='/resources/js/profile.js'/>"></script>
</head>
<body>
<c:url var="ownerProfile" value="/user/${user.id}/${tabContext}"/>
<c:url var="userProfile" value="/user/${user.id}"/>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-column detail-user">
            <div class="flex-center flex-grow">
                <c:if test="${!currentUser.verified && currentUser.id == user.id}">
                    <div class="flex-row flex-center verify-email-container border-radius">
                        <i class="verify-email-icon material-icons">announcement</i>
                        <div class="flex-column verify-message-container">
                            <c:url var="verifyEmailUrl" value="/verify-email">
                                <c:param name="id" value="${currentUser.id}"/>
                            </c:url>
                            <h3><spring:message code="verify.email.title"/></h3>
                            <p><spring:message code="verify.email.description"/></p>
                            <p>
                                <spring:message code="verify.email.instruction"/>
                                <a href="${verifyEmailUrl}"><spring:message code="verify.email.redirection"/></a>
                            </p>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="flex-row general-info">
                <div class="flex-column flex-center">
                    <c:if test="${currentUser.id == user.id}">
                        <div class="flex-row flex-center profile-photo-wrap">
                            <span class="material-icons profile-photo-edit-icon"
                                  onclick="hiddenClick(this)">create</span>
                            <c:if test="${user.icon != null}">
                                <img id="profile-image" class="profile-photo edit" src="<c:url value="/user/${user.id}/image"/>"
                                     alt="User Icon" onclick="hiddenClick(this)"/>
                            </c:if>
                            <c:if test="${user.icon == null}">
                                <img id="profile-image" class="profile-photo edit"
                                     src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"
                                     onclick="hiddenClick(this)"/>
                            </c:if>
                        </div>

                        <form:form method="POST" action="${ownerProfile}" enctype="multipart/form-data" modelAttribute="profilePhotoForm">
                            <form:errors path="file" cssClass="flex-center form-error" element="p" />
                            <div class="flex-row flex-center">
                            <span id="image-confirm" class="image-confirm-button hidden-button"
                                  onclick="submitImageForm(this)">
                                <spring:message code="profile.edit.save"/>
                            </span>
                                <a id="image-discard" class="image-discard-button hidden-button"
                                   href="<c:url value="${ownerProfile}"/>">
                                    <spring:message code="profile.edit.discard"/>
                                </a>
                            </div>
                            <div class="flex-row hidden">
                                <input type="file" id="profile-image-input" class="hidden" name="file"
                                       accept="image/jpeg" onchange="preview(this)"/>
                                <input type="submit" id="image-form-submit">
                            </div>
                        </form:form>
                    </c:if>
                    <c:if test="${currentUser.id != user.id}">
                        <div class="flex-row flex-center">
                            <c:if test="${user.icon != null}">
                                <img id="profile-image" class="profile-photo" src="<c:url value="/user/${user.id}/image"/>"
                                     alt="User Icon"/>
                            </c:if>
                            <c:if test="${user.icon == null}">
                                <img id="profile-image" class="profile-photo"
                                     src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                        </div>
                    </c:if>
                </div>
                <div class="flex-column flex-center profile-info">
                    <div class="flex-column">
                    <div class="flex-row">
                        <div class="profile-username">
                            ${user.username}
                        </div>
                        <c:if test="${currentUser.id == user.id}">
                            <c:if test="${!editing}">
                                <a class="flex-center purple-text edit-button"
                                href="<c:url value="/user/${user.id}/${tabContext}?editing=true"/>">
                                <spring:message code="profile.edit"/>
                                </a>
                            </c:if>
                            <c:if test="${editing}">
                                <div onclick="submitDescriptionForm(this)" class="flex-center purple-text edit-button clickable-entity">
<%--                                   href="<c:url value="/user/${user.id}/edit"/>">--%>
                                    <spring:message code="profile.edit.save"/>
                                </div>
                                <a class="flex-center purple-text edit-button"
                                   href="<c:url value="/user/${user.id}?editing=false"/>">
                                    <spring:message code="profile.edit.discard"/>
                                </a>
                            </c:if>
                        </c:if>
                    </div>
                    <div class="fw-100">
                        <spring:message code="profile.joined"/>
                        ${user.getDateJoined()}
                    </div>
                    <div class="flex-row profile-info-item">
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${snippetsCount}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.snippets"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${user.reputation}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.reputation"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${followedTags.size()}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.following"/></div>
                        </div>
                    </div>
                    <c:url var="editUrl" value="/user/${user.id}/${tabContext}/edit"/>
                    <form:form id="description-form" method="POST" cssClass="profile-info-item" action="${editUrl}" modelAttribute="descriptionForm">
                        <c:if test="${!editing}">
                            <div class="profile-description profile-info-item">
                                    ${user.description}
                            </div>
                        </c:if>
                        <c:if test="${currentUser.id == user.id && editing}">
                            <form:label class="fw-400 snippetC-subtitles" path="description"><spring:message code="snippetCreateForm.description"/> </form:label>
                            <div class="profile-description flex-row flex-grow">
                                <form:textarea class="flex-grow snippetC-description-input snippetC-border" rows="2" type="text" path="description" placeholder='${desc_hint}'/>
                            </div>
                        </c:if>
                        <form:errors path="description" cssClass="form-error" element="p"/>
                    </form:form>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${currentUser != null && currentUser.id == user.id}">
            <div class="flex-row flex-center flex-wrap tabs-container">
                <c:choose>
                    <c:when test="${tabContext == 'active'}">
                        <div class="no-margin tabs-item-container flex-center tabs-first-item transition tabs-item-selected">
                            <spring:message code="profile.tabs.active"/>
                        </div>
                        <a class="tabs-item-container flex-center tabs-second-item transition " href="<c:url value="/user/${user.id}/deleted"/>">
                            <spring:message code="profile.tabs.deleted"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a class="no-margin tabs-item-container flex-center tabs-first-item transition" href="<c:url value="/user/${user.id}/active"/>">
                            <spring:message code="profile.tabs.active"/>
                        </a>
                        <div class="tabs-item-container tabs-second-item flex-center transition tabs-item-selected"><spring:message code="profile.tabs.deleted"/></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <hr class="divider"/>
        <div class="feed-background-color">
            <c:if test="${snippets.size() == 0}">
                <div class="profile-no-snippet flex-center fw-100">
                    <c:choose>
                        <c:when test="${currentUser != null && user.id == currentUser.id && tabContext == 'deleted'}">
                            <spring:message code="profile.no-snippets.deleted"/>
                        </c:when>
                        <c:when test="${currentUser != null && user.id == currentUser.id && tabContext == 'active'}">
                            <spring:message code="profile.no-snippets.active"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="profile.no-snippets"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <c:if test="${snippets.size() > 0}">
                <c:set var="snippetList" value="${snippets}" scope="request"/>
                <c:import url="/WEB-INF/jsp/snippet/snippetFeed.jsp"/>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>

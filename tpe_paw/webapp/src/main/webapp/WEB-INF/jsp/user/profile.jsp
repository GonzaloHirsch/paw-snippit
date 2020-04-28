<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="menu.profile"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/profile.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    <script src="<c:url value='/resources/js/profile.js'/>"></script>
</head>
<body>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-column detail-user">
            <div class="flex-row">
                <div class="flex-column">
                    <c:if test="${currentUser.id == user.id}">
                        <div class="flex-row flex-center profile-photo-wrap">
                            <span class="material-icons profile-photo-edit-icon"
                                  onclick="hiddenClick(this)">create</span>
                            <c:if test="${user.icon != null}">
                                <img id="profile-image" class="profile-photo edit" src="/user/${id}/image"
                                     alt="User Icon" onclick="hiddenClick(this)"/>
                            </c:if>
                            <c:if test="${user.icon == null}">
                                <img id="profile-image" class="profile-photo edit"
                                     src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"
                                     onclick="hiddenClick(this)"/>
                            </c:if>

                        </div>

                        <form:form method="POST" action="/user/${user.id}/save-image" enctype="multipart/form-data">
                            <div class="flex-row flex-center">
                            <span id="image-confirm" class="image-confirm-button hidden-button"
                                  onclick="submitImageForm(this)">
                                <spring:message code="profile.edit.save"/>
                            </span>
                                <a id="image-discard" class="image-discard-button hidden-button"
                                   href="<c:url value="/user/${user.id}"/>">
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
                                <img id="profile-image" class="profile-photo" src="/user/${id}/image"
                                     alt="User Icon"/>
                            </c:if>
                            <c:if test="${user.icon == null}">
                                <img id="profile-image" class="profile-photo"
                                     src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                        </div>
                    </c:if>
                </div>
                <div class="flex-column profile-info">
                    <div class="flex-row">
                        <div class="profile-username">
                            ${user.username}
                        </div>
                        <%--                        <c:if test="${currentUser.id == user.id}">--%>
                        <%--                            <c:if test="${!editing}">--%>
                        <%--                                <a class="flex-center purple-text edit-button"--%>
                        <%--                                href="<c:url value="/user/${user.id}?editing=true"/>">--%>
                        <%--                                <spring:message code="profile.edit"/>--%>
                        <%--                                </a>--%>
                        <%--                            </c:if>--%>
                        <%--                            <c:if test="${editing}">--%>
                        <%--                                <a class="flex-center purple-text edit-button"--%>
                        <%--                                   href="<c:url value="/user/${user.id}/edit"/>">--%>
                        <%--                                    <spring:message code="profile.edit.save"/>--%>
                        <%--                                </a>--%>
                        <%--                                <a class="flex-center purple-text edit-button"--%>
                        <%--                                   href="<c:url value="/user/${user.id}?editing=false"/>">--%>
                        <%--                                    <spring:message code="profile.edit.discard"/>--%>
                        <%--                                </a>--%>
                        <%--                            </c:if>--%>
                        <%--                        </c:if>--%>
                    </div>
                    <div class="fw-100">
                        <spring:message code="profile.joined"/>
                        ${user.dateJoined}
                    </div>
                    <div class="flex-row profile-info-item">
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${snippets.size()}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.snippets"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${user.reputation}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.reputation"/></div>
                        </div>
                        <div class="flex-center stat-bundle">
                            <div class="fw-700 stat">${user.reputation}</div>
                            <div class="fw-100 stat"><spring:message code="profile.stats.following"/></div>
                        </div>
                    </div>
                    <%--                    <c:if test="${currentUser.id == user.id && editing}">--%>
                    <%--                        <div class="profile-description profile-info-item">--%>
                    <%--                            <form:label class="fw-400 snippetC-subtitles" path="description"><spring:message code="snippetCreateForm.description"/> </form:label>--%>
                    <%--                            <form:textarea class="full-width snippetC-description-input snippetC-border" rows="2" type="text" path="description" placeholder='${desc_hint}'/>--%>
                    <%--                            <form:errors path="description" element="p"/>--%>
                    <%--                        </div>--%>
                    <%--                    </c:if>--%>
                    <%--                    <c:if test="${currentUser.id != user.id}">--%>
                    <div class="profile-description profile-info-item">
                        ${user.description}
                    </div>
                    <%--                    </c:if>--%>
                </div>
            </div>
        </div>
        <hr class="divider"/>
        <div class="feed-background-color">
            <c:if test="${snippets.size() == 0}">
                <div class="profile-no-snippet flex-center fw-100">
                    <spring:message code="profile.no-snippets"/>
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

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

        <html>
        <head>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
        <script src="<c:url value='/resources/js/navigationBar.js'/>"></script>
        <script src="<c:url value='/resources/js/searchForm.js'/>"></script>
        </head>
        <body>
        <c:set var="currentUser" value="${requestScope.currentUser}"/>
        <c:set var="userTags" value="${requestScope.userTags}"/>
        <c:set var="searchContext" value="${requestScope.searchContext}"/>
        <c:choose>
            <c:when test="${searchContext == 'tags/'}">
                <c:url var="searchUrl" value="/search"/>
            </c:when>
            <c:when test="${searchContext == 'languages/'}">
                <c:url var="searchUrl" value="/search"/>
            </c:when>
            <c:otherwise>
                <c:url var="searchUrl" value="/${searchContext}search"/>
            </c:otherwise>
        </c:choose>
        <c:url var="searchForm" value="${requestScope.searchForm}"/>
        <c:url var="userRoles" value="${requestScope.userRoles}"/>
        <spring:message code="search.hint" var="search_hint"/>

        <div id="sidenav" class="sidenav">
        <ul>
        <c:choose>
            <c:when test="${searchContext == ''}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/"/>"><span
                class="material-icons menu-option-icon">home</span>
                <spring:message code="menu.home"/></a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/"/>"><span
                class="material-icons menu-option-icon">home</span>
                <spring:message code="menu.home"/></a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'tags/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <spring:message code="menu.tags"/></a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <spring:message code="menu.tags"/></a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'languages/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/languages/"/>"><span
                class="material-icons menu-option-icon">desktop_windows</span>
                <spring:message code="menu.languages"/></a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/languages/"/>"><span
                class="material-icons menu-option-icon">desktop_windows</span>
                <spring:message code="menu.languages"/></a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'explore/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/explore/"/>"><span
                class="material-icons menu-option-icon">search</span>
                <spring:message code="menu.explore"/></a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/explore/"/>"><span
                class="material-icons menu-option-icon">search</span>
                <spring:message code="menu.explore"/></a>
            </c:otherwise>
        </c:choose>
        <c:if test="${currentUser != null}">
            <hr/>
            <c:choose>
                <c:when test="${!userRoles.contains('ADMIN')}">
                    <c:choose>
                        <c:when test="${searchContext == 'user/'}">
                            <a class="fw-100 menu-option menu-selected flex-center" href="<c:url
                                value="${'/user/'}${currentUser.id}"/>">
                            <c:if test="${currentUser.icon != null}">
                                <img src="<c:url value="/user/${currentUser.id}/image"/>" alt="User Icon"/>
                            </c:if>
                            <c:if test="${currentUser.icon == null}">
                                <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                            <spring:message code="menu.profile"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="fw-100 menu-option" href="<c:url value="${'/user/'}${currentUser.id}"/>">
                            <c:if test="${currentUser.icon != null}">
                                <img src="<c:url value="/user/${currentUser.id}/image"/>" alt="User Icon"/>
                            </c:if>
                            <c:if test="${currentUser.icon == null}">
                                <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                            <spring:message code="menu.profile"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${searchContext == 'flagged/'}">
                            <a class="fw-100 menu-option menu-selected" href="<c:url value="/flagged/"/>">
                            <span class="material-icons menu-option-icon">flag</span>
                            <spring:message code="menu.flagged"/></a>
                        </c:when>
                        <c:otherwise>
                            <a class="fw-100 menu-option" href="<c:url value="/flagged/"/>"><span
                            class="material-icons menu-option-icon">flag</span>
                            <spring:message code="menu.flagged"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'following/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/following/"/>">
                    <span class="material-icons menu-option-icon">loyalty</span>
                    <spring:message code="menu.following"/></a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/following/"/>"><span
                    class="material-icons menu-option-icon">loyalty</span>
                    <spring:message code="menu.following"/></a>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'favorites/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <spring:message code="menu.favorites"/></a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <spring:message code="menu.favorites"/></a>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'upvoted/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/upvoted/"/>"><span
                    class="material-icons menu-option-icon">thumb_up</span>
                    <spring:message code="menu.upvoted"/></a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/upvoted/"/>"><span
                    class="material-icons menu-option-icon">thumb_up</span>
                    <spring:message code="menu.upvoted"/></a>
                </c:otherwise>
            </c:choose>
            <c:if test="${userTags.size() > 0}">
                <hr/>
                <span class="fw-100 section-title"><spring:message code="menu.following"/></span>
                <c:forEach var="tag" items="${userTags}">
                    <a class="fw-100 tag-section" href="<c:url value="${'/tags/'}${tag.id}"/>">${tag.name}</a>
                </c:forEach>
            </c:if>
            <hr/>
            <a class="fw-100 menu-option" href="<c:url value="/logout"/>">
            <span class="material-icons menu-option-icon">exit_to_app</span>
            <spring:message code="menu.logout"/></a>
        </c:if>
        </ul>
        </div>
        <div class="navtop flex-row flex-space-between">
        <div class="flex-center">
        <span id="menu-icon" class="material-icons menu-icon" onclick="openNav()">menu</span>
        <a class="flex-row white-text app-name fw-100" href="<c:url value="/"/>">
        <span class="material-icons navtop-icons">code</span>
        <spring:message code="app.name"/>
        </a>
        </div>
        <c:if test="${searchContext != 'error/' && searchContext != 'explore/'}">
            <div class="flex-row flex-wrap flex-center flex-grow">
            <form:form modelAttribute="searchForm" method="get" action="${searchUrl}"
                       class="flex-row flex-center search-container">
                <div class="flex-row flex-grow fw-100">
                <form:input path="query" type="text" id="search-bar" class="search-input flex-grow fw-100"
                            placeholder="${search_hint}"/>
                <button type="submit"><span class="material-icons search-icon">search</span></button>
                </div>
                <div class="flex-center">
                <div class="dropdown-type">
                <form:select class="flex-center" path="type" name="Type">
                    <form:option value="all">
                        <spring:message code="search.searchby"/>
                    </form:option>
                    <form:option value="all">
                        <spring:message code="search.all"/>
                    </form:option>
                    <form:option value="title">
                        <spring:message code="search.title"/>
                    </form:option>
                    <form:option value="tag">
                        <spring:message code="search.tag"/>
                    </form:option>
                    <form:option value="content">
                        <spring:message code="search.content"/>
                    </form:option>
                    <form:option value="username">
                        <spring:message code="search.username"/>
                    </form:option>
                    <form:option value="language">
                        <spring:message code="search.language"/>
                    </form:option>
                </form:select>
                </div>
                <div class="dropdown-type">
                <form:select path="sort" name="Sort" onchange="submitForm(this)">
                    <form:option value="no">
                        <spring:message code="sort.sortby"/>
                    </form:option>
                    <form:option value="asc">
                        <spring:message code="sort.asc"/>
                    </form:option>
                    <form:option value="desc">
                        <spring:message code="sort.desc"/>
                    </form:option>
                </form:select>
                </div>
                </div>
            </form:form>
            </div>
        </c:if>
        <div class="flex-row flex-center navtop-register-buttons">
        <c:choose>
            <c:when test="${currentUser == null}">
                <a class="flex-center purple-text navtop-button form-button-basics small-border-radius" href="<c:url value="/login"/>">
                <spring:message code="login.button"/>
                </a>
                <a class="flex-center purple-text navtop-button form-button-basics small-border-radius" href="<c:url value="/signup"/>">
                <spring:message code="register.button"/>
                </a>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${userRoles.contains('ADMIN')}">
                        <a href="<c:url value="/admin/add"/>">
                        <i class="material-icons navtop-icons">add_circle_outline</i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/snippet/create"/>">
                        <i class="material-icons navtop-icons">add_circle_outline</i>
                        </a>
                    </c:otherwise>
                </c:choose>

                <a href="<c:url value="${'/user/'}${currentUser.id}"/>" class="white-text flex-center navtop-welcome-text">
                   <spring:message code="app.userWelcome" arguments="${currentUser.username}"/>
                </a>
            </c:otherwise>
        </c:choose>
        </div>
        </div>
        </body>
        </html>

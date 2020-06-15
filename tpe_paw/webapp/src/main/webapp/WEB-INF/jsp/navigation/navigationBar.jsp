    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

        <html>
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
        <script src="<c:url value='/resources/js/navigationBar.js'/>"></script>
        <script src="<c:url value='/resources/js/searchForm.js'/>"></script>
        </head>
        <body>
        <spring:message code="menu.profile" var="menuProfile"/>
        <spring:message code="menu.home" var="menuHome"/>
        <spring:message code="menu.flagged" var="menuFlagged"/>
        <spring:message code="menu.following" var="menuFollowing"/>
        <spring:message code="menu.favorites" var="menuFavorites"/>
        <spring:message code="menu.tags" var="menuTags"/>
        <spring:message code="menu.languages" var="menuLanguages"/>
        <spring:message code="menu.explore" var="menuExplore"/>
        <spring:message code="menu.upvoted" var="menuUpvoted"/>
        <spring:message code="menu.logout" var="menuLogout"/>

        <c:set var="currentUser" value="${requestScope.currentUser}"/>
        <c:set var="userTags" value="${requestScope.userTags}"/>
        <c:url var="userTagsCount" value="${requestScope.userTagsCount}"/>
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
                <c:out value="${menuHome}"/>
                </a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/"/>"><span
                class="material-icons menu-option-icon">home</span>
                <c:out value="${menuHome}"/></a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'tags/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <c:out value="${menuTags}"/></a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <c:out value="${menuTags}"/>
                </a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'languages/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/languages/"/>"><span
                class="material-icons menu-option-icon">desktop_windows</span>
                <c:out value="${menuLanguages}"/>
                </a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/languages/"/>"><span
                class="material-icons menu-option-icon">desktop_windows</span>
                <c:out value="${menuLanguages}"/>
                </a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'explore/'}">
                <a class="fw-100 menu-option menu-selected" href="<c:url value="/explore/"/>"><span
                class="material-icons menu-option-icon">search</span>
                <c:out value="${menuExplore}"/>
                </a>
            </c:when>
            <c:otherwise>
                <a class="fw-100 menu-option" href="<c:url value="/explore/"/>"><span
                class="material-icons menu-option-icon">search</span>
                <c:out value="${menuExplore}"/>
                </a>
            </c:otherwise>
        </c:choose>
        <c:if test="${currentUser != null}">
            <hr/>
            <c:choose>
                <c:when test="${!userRoles.contains('ADMIN')}">
                    <c:choose>
                        <c:when test="${searchContext == 'user/'}">
                            <a class="fw-100 menu-option menu-selected flex-center" href="<c:url
                                value="${'/user/'}${currentUser.id}${'/active'}"/>">
                            <c:if test="${currentUser.icon != null}">
                                <img src="<c:url value="/user/${currentUser.id}/image"/>" alt="User Icon"/>
                            </c:if>
                            <c:if test="${currentUser.icon == null}">
                                <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                            <c:out value="${menuProfile}"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="fw-100 menu-option" href="<c:url value="${'/user/'}${currentUser.id}${'/active'}"/>">
                            <c:if test="${currentUser.icon != null}">
                                <img src="<c:url value="/user/${currentUser.id}/image"/>" alt="User Icon"/>
                            </c:if>
                            <c:if test="${currentUser.icon == null}">
                                <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                            </c:if>
                            <c:out value="${menuProfile}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${searchContext == 'flagged/'}">
                            <a class="fw-100 menu-option menu-selected" href="<c:url value="/flagged/"/>">
                            <span class="material-icons menu-option-icon">flag</span>
                            <c:out value="${menuFlagged}"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="fw-100 menu-option" href="<c:url value="/flagged/"/>"><span
                            class="material-icons menu-option-icon">flag</span>
                            <c:out value="${menuFlagged}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'following/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/following/"/>">
                    <span class="material-icons menu-option-icon">loyalty</span>
                    <c:out value="${menuFollowing}"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/following/"/>"><span
                    class="material-icons menu-option-icon">loyalty</span>
                    <c:out value="${menuFollowing}"/>
                    </a>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'favorites/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <c:out value="${menuFavorites}"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <c:out value="${menuFavorites}"/>
                    </a>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'upvoted/'}">
                    <a class="fw-100 menu-option menu-selected" href="<c:url value="/upvoted/"/>"><span
                    class="material-icons menu-option-icon">thumb_up</span>
                    <c:out value="${menuUpvoted}"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="fw-100 menu-option" href="<c:url value="/upvoted/"/>"><span
                    class="material-icons menu-option-icon">thumb_up</span>
                    <c:out value="${menuUpvoted}"/>
                    </a>
                </c:otherwise>
            </c:choose>
            <c:if test="${userTags.size() > 0}">
                <spring:message code="menu.following.others.1" var="followingOthers1"/>
                <spring:message code="menu.following.others.many" arguments="${userTagsCount}" var="followingMany"/>
                <hr/>
                <span class="fw-100 section-title"><c:out value="${menuFollowing}"/></span>
                <c:forEach var="tag" items="${userTags}">
                    <a class="fw-100 tag-section" href="<c:url value="${'/tags/'}${tag.id}"/>">${tag.name}</a>
                </c:forEach>
                <c:choose>
                    <c:when test="${userTagsCount == 1}">
                        <a class="fw-100 tag-section" href="<c:url value="/following"/>"><c:out value="${followingOthers1}"/></a>
                    </c:when>
                    <c:when test="${userTagsCount > 1}">
                        <a class="fw-100 tag-section" href="<c:url value="/following"/>"><c:out value="${followingMany}"/></a>
                    </c:when>
                </c:choose>
            </c:if>
            <hr/>
            <a class="fw-100 menu-option" href="<c:url value="/logout"/>">
            <span class="material-icons menu-option-icon">exit_to_app</span>
            <c:out value="${menuLogout}"/></a>
        </c:if>
        </ul>
        </div>

        <spring:message code="app.name" var="appName"/>
        <spring:message code="search.searchby" var="searchBy"/>
        <spring:message code="search.all" var="searchAll"/>
        <spring:message code="search.title" var="searchTitle"/>
        <spring:message code="search.tag" var="searchTag"/>
        <spring:message code="search.content" var="searchContent"/>
        <spring:message code="search.username" var="searchUser"/>
        <spring:message code="search.language" var="searchLang"/>
        <spring:message code="sort.sortby" var="sortBy"/>
        <spring:message code="sort.asc" var="sortAsc"/>
        <spring:message code="sort.desc" var="sortDesc"/>



        <div class="navtop flex-row flex-space-between">
        <div class="flex-center">
        <span id="menu-icon" class="material-icons menu-icon" onclick="openNav()">menu</span>
        <a class="flex-row white-text app-name fw-100" href="<c:url value="/"/>">
        <span class="material-icons navtop-icons">code</span>
        <c:out value="${appName}"/>
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
                        <c:out value="${searchBy}"/>
                    </form:option>
                    <form:option value="all">
                        <c:out value="${searchAll}"/>
                    </form:option>
                    <form:option value="title">
                        <c:out value="${searchTitle}"/>
                    </form:option>
                    <form:option value="tag">
                        <c:out value="${searchTag}"/>
                    </form:option>
                    <form:option value="content">
                        <c:out value="${searchContent}"/>
                    </form:option>
                    <form:option value="username">
                        <c:out value="${searchUser}"/>
                    </form:option>
                    <form:option value="language">
                        <c:out value="${searchLang}"/>
                    </form:option>
                </form:select>
                </div>
                <div class="dropdown-type">
                <form:select path="sort" name="Sort" onchange="submitForm(this)">
                    <form:option value="no">
                        <c:out value="${sortBy}"/>
                    </form:option>
                    <form:option value="asc">
                        <c:out value="${sortAsc}"/>
                    </form:option>
                    <form:option value="desc">
                        <c:out value="${sortDesc}"/>
                    </form:option>
                </form:select>
                </div>
                </div>
            </form:form>
            </div>
        </c:if>
        <div class="flex-row flex-center navtop-register-buttons">
        <spring:message code="login.button" var="loginButton"/>
        <spring:message code="register.button" var="registerButton"/>
        <spring:message code="app.userWelcome" arguments="${currentUser.username}" var="userWelcome"/>

        <c:choose>
            <c:when test="${currentUser == null}">
                <a class="flex-center purple-text navtop-button form-button-basics small-border-radius" href="<c:url value="/login"/>">
                <c:out value="${loginButton}"/>
                </a>
                <a class="flex-center purple-text navtop-button form-button-basics small-border-radius" href="<c:url value="/signup"/>">
                <c:out value="${registerButton}"/>
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

                <c:choose>
                    <c:when test="${!userRoles.contains('ADMIN')}">
                        <a href="<c:url value="${'/user/'}${currentUser.id}${'/active'}"/>" class="white-text flex-center navtop-welcome-text">
                            <c:out value="${userWelcome}"/>
                        </a>
                    </c:when>
                    <!-- Admin does not have a user profile -->
                    <c:otherwise>
                        <div class="white-text flex-center navtop-welcome-text">
                            <c:out value="${userWelcome}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        </div>
        </div>
        </body>
        </html>

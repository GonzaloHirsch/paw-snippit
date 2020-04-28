    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

        <html>
        <head>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
        <link
        href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="<c:url value='/resources/js/navigationBar.js'/>"></script>
        <script src="<c:url value='/resources/js/searchForm.js'/>"></script>
        </head>
        <body>
        <spring:message code="search.hint" var="search_hint"/>
        <c:url var="searchUrl" value="/${searchContext}search"/>
        <div id="sidenav" class="sidenav">
        <ul>
        <c:choose>
            <c:when test="${searchContext == ''}">
                <li class="fw-100 menu-option menu-selected"><a href="<c:url value="/"/>"><span
                class="material-icons menu-option-icon">home</span>
                <spring:message code="menu.home"/></a></li>
            </c:when>
            <c:otherwise>
                <li class="fw-100 menu-option"><a href="<c:url value="/"/>"><span
                class="material-icons menu-option-icon">home</span>
                <spring:message code="menu.home"/></a></li>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${searchContext == 'tags/'}">
                <li class="fw-100 menu-option menu-selected"><a href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <spring:message code="menu.tags"/></a></li>
            </c:when>
            <c:otherwise>
                <li class="fw-100 menu-option"><a href="<c:url value="/tags/"/>"><span
                class="material-icons menu-option-icon">local_offer</span>
                <spring:message code="menu.tags"/></a></li>
            </c:otherwise>
        </c:choose>
        <c:if test="${currentUser != null}">
            <hr/>
            <c:choose>
                <c:when test="${searchContext == 'user/'}">
                    <li class="fw-100 menu-option menu-selected"><a href="<c:url value="${'/user/'}${currentUser.id}"/>
                    ">
                    <c:if test="${currentUser.icon != null}">
                        <img src="/user/${currentUser.id}/image" alt="User Icon"/>
                    </c:if>
                    <c:if test="${currentUser.icon == null}">
                        <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                    </c:if>
                    <spring:message code="menu.profile"/>
                    </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100 menu-option"><a href="<c:url value="${'/user/'}${currentUser.id}"/>">
                    <c:if test="${currentUser.icon != null}">
                        <img src="/user/${currentUser.id}/image" alt="User Icon"/>
                    </c:if>
                    <c:if test="${currentUser.icon == null}">
                        <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                    </c:if>
                    <spring:message code="menu.profile"/></a></li>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'following/'}">
                    <li class="fw-100 menu-option menu-selected"><a href="<c:url value="/following/"/>">
                    <span class="material-icons menu-option-icon">loyalty</span>
                    <spring:message code="menu.following"/></a></li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100 menu-option"><a href="<c:url value="/following/"/>"><span
                    class="material-icons menu-option-icon">loyalty</span>
                    <spring:message code="menu.following"/></a></li>
                </c:otherwise>
            </c:choose>
            <!--<li><a href="<c:url value="/uploads/"/>">Uploads</a></li>-->
            <c:choose>
                <c:when test="${searchContext == 'favorites/'}">
                    <li class="fw-100 menu-option menu-selected"><a href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <spring:message code="menu.favorites"/></a></li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100 menu-option"><a href="<c:url value="/favorites/"/>"><span
                    class="material-icons menu-option-icon">favorite</span>
                    <spring:message code="menu.favorites"/></a></li>
                </c:otherwise>
            </c:choose>
            <hr/>
            <span class="fw-100 section-title"><spring:message code="menu.following"/></span>
            <c:forEach var="tag" items="${userTags}">
                <li class="fw-100"><a class="tag-section" href="<c:url value="${'/tags/'}${tag.id}"/>">${tag.name}
                </a></li>
            </c:forEach>
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
        <div class="flex-row flex-wrap flex-center flex-grow">
        <form:form modelAttribute="searchForm" method="get" action="${searchUrl}"
                   class="flex-row flex-center flex-wrap search-container">
            <div class="flex-row flex-grow fw-100">
            <form:input path="query" type="text" id="search-bar" class="search-input flex-grow fw-100"
                        placeholder="${search_hint}"/>
            <button type="submit"><span class="material-icons search-icon">search</span></button>
            </div>
            <div class="flex-center">
            <div class="dropdown-type">
            <form:select path="type" name="Type">
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
        <div class="flex-row flex-center navtop-register-buttons">
        <c:choose>
            <c:when test="${currentUser == null}">
                <a class="flex-center purple-text navtop-button border-radius" href="<c:url value="/login"/>">
                <spring:message code="login.title"/>
                </a>
                <a class="flex-center purple-text navtop-button border-radius" href="<c:url value="/signup"/>">
                <spring:message code="register.title"/>
                </a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/snippet/create"/>">
                <i class="material-icons navtop-icons">add_circle_outline</i>
                </a>
                <div class="white-text flex-center navtop-welcome-text">
                <spring:message code="app.userWelcome" arguments="${currentUser.username}"/>
                </div>
            </c:otherwise>
        </c:choose>
        </div>
        </div>
        </body>
        </html>

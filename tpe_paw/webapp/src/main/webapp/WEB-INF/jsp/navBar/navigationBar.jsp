<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--Liberia para el icono, volarla despues -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<html>
<head>
    <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet" />
    <c:url var="searchUrl" value="/${searchContext}search" />
</head>
<body>
    <div class="sidebar">
        <ul>
            <c:choose>
                <c:when test="${searchContext == ''}">
                    <li class="fw-100 menu-selected"><a href="<c:url value="/"/>">All</a></li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100"><a href="<c:url value="/"/>">All</a></li>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${searchContext == 'following/'}">
                    <li class="fw-100 menu-selected"><a href="<c:url value="/following/"/>">Following</a></li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100"><a href="<c:url value="/following/"/>">Following</a></li>
                </c:otherwise>
            </c:choose>

            <!--<li><a href="<c:url value="/tags/"/>" >Tags</a></li>-->
            <hr/>
            <!--<li><a href="<c:url value="/uploads/"/>">Uploads</a></li>-->
            <c:choose>
                <c:when test="${searchContext == 'favorites/'}">
                    <li class="fw-100 menu-selected"><a href="<c:url value="/favorites/"/>">Favorites</a></li>
                </c:when>
                <c:otherwise>
                    <li class="fw-100"><a href="<c:url value="/favorites/"/>">Favorites</a></li>
                </c:otherwise>
            </c:choose>
            <!--<hr/>
            <li><a href="<c:url value="/following/"/>">Following</a></ul></li>-->
        </ul>
    </div>


    <div class="navtop">
        <a class="app-name fw-100" href="<c:url value="/"/>">Snippit</a>
        <div class="search-container">
            <form:form modelAttribute="searchForm" method="get" action="${searchUrl}" >
                <div class="search-bar">
                    <form:input path="query" type="text"  class="search-input" placeholder="Search..." />
                    <button type="submit"><i class="fa fa-search"></i></button>
                </div>
                <div class="dropdown-type">
                    <form:select path="type" name="Type">
                        <form:option value="title">Search by</form:option>
                        <form:option value="title">Title</form:option>
                        <form:option value="tag">Tag</form:option>
                        <form:option value="content">Content</form:option>
                    </form:select>
                </div>
                <div class="dropdown-type">
                    <form:select path="sort" name="Sort">
                        <form:option value="no">Sort by</form:option>
                        <form:option value="asc">Ascending</form:option>
                        <form:option value="desc">Descending</form:option>
                    </form:select>
                </div>
            </form:form>
        </div>
    </div>

</body>
</html>

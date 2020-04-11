<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--Liberia para el icono, volarla despues -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<html>
<head>
    <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet" />
    <c:url var="searchUrl" value="/${searchContext}search" />
</head>
<body>
    <div class="navtop">
        <a class="active">Home</a>
        <div class="search-container">
            <form:form modelAttribute="searchForm" method="post" action="${searchUrl}" >
                <div class="dropdown-type">
                    <form:select path="type" name="Type">
                        <form:option value="title">Select a Type</form:option>
                        <form:option value="title">Title</form:option>
                        <form:option value="tag">Tag</form:option>
                        <form:option value="content">Content</form:option>
                    </form:select>
                </div>
                <div class="dropdown-type">
                    <form:select path="sort" name="Sort">
                        <form:option value="asc">Select sort</form:option>
                        <form:option value="asc">Ascending</form:option>
                        <form:option value="desc">Descending</form:option>
                    </form:select>
                </div>

                <div class="search-bar">
                    <form:input path="query" type="text"  class="search-input" placeholder="Search..." />
                    <button type="submit"><i class="fa fa-search"></i></button>
                </div>
            </form:form>
        </div>
    </div>

    <div class="navside">
        <a href="<c:url value="/"/>">All</a>
        <a href="<c:url value="/following/"/>">Following</a>
        <a href="<c:url value="/tags/"/>" >Tags</a>
        <hr/>
        <a href="<c:url value="/uploads/"/>">Uploads</a>
        <a href="<c:url value="/favorites/"/>">Favorites</a>
        <hr/>
        <a href="<c:url value="/following/"/>">Following</a>

    </div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--Liberia para el icono, volarla despues -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<html>
<head>
    <link href="<c:url value='/resources/css/navBarTop.css'/>" rel="stylesheet" />
</head>
<c:url var="searchUrl" value="/search" />
<div class="navtop">
    <a class="active">Home</a>
    <div class="search-container">
        <form:form modelAttribute="searchForm" method="post" action="${searchUrl}" >
            <form:select path="type" name="Type">
                <option value="title">Title</option>
                <option value="tag">Tag</option>
                <option value="content">Content</option>
            </form:select>
            <form:input path="query" type="text" placeholder="Search..." />
            <button type="submit"><i class="fa fa-search"></i></button>
        </form:form>
    </div>


</div>

</html>

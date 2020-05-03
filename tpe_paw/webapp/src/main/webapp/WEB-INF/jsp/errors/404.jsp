<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="error.title"/></title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<c:set var="currentUser" value="${requestScope.currentUser}"/>
<c:set var="userTags" value="${requestScope.userTags}"/>
<c:set var="searchContext" value="${requestScope.searchContext}"/>
<c:url var="searchUrl" value="/${searchContext}search"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="error-page-main">
            <h1>404</h1>
            <h2>
                <spring:message code="error.404"/>
            </h2>
            <div class="link-button-holder">
                <a class="link-button" href="<c:url value='/'/>">Take me Home</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

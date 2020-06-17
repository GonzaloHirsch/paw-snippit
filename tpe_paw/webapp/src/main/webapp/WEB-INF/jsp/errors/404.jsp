<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="error.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<c:set var="currentUser" value="${requestScope.currentUser}"/>
<c:set var="userTags" value="${requestScope.userTags}"/>
<c:set var="searchContext" value="${requestScope.searchContext}"/>
<c:set var="msg" value="${requestScope.msg}"/>
<c:url var="searchUrl" value="/${searchContext}search"/>

<spring:message code="error.home" var="errHome"/>
<spring:message code="error.home.redirect" var="errRedirect"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-column flex-center error-page-main">
            <div class="error-code border-radius"><c:out value="404"/></div>
            <div class="error-page-not-found"><spring:message code="error.404"/></div>

            <div class="error-dialog border-radius flex-column flex-column">
                <h2>
                    <c:out value="${msg}"/>
                </h2>
                <h3>
                    <c:out value="${errHome}"/>
                </h3>
                <a class="link-button border-radius flex-center" href="<c:url value='/'/>">
                    <c:out value="${errRedirect}"/>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

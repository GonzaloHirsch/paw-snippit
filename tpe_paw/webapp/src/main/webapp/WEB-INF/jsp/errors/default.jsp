<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
    <div class="main-content">
        <div class="error-page-main">
            <h1>${err}</h1>
            <h2>
                ${msg}
            </h2>
            <div class="link-button-holder">
                <a class="link-button" href="<c:url value='/'/>">Take me Home</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

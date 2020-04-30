<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title><spring:message code="admin.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/addDynamicContent.js'/>"></script>
</head>
<body>

<c:url var="adminAddUrl" value="/admin/add"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-center">

        <form:form class="flex-center flex-column form form-border form-shadow" action="${adminAddUrl}" method="post" modelAttribute="adminAddForm">
            <div id="form-lang-container" class="flex-column">
                <div id="form-dynamic-lang" class="flex-column">
                </div>
                <input type="button" value="+" onclick="addAdminRow('form-dynamic-lang')"/>
            </div>

            <div id="form-tag-container" class="flex-column">
                <div id="form-dynamic-tag" class="flex-column">
                </div>
                <input type="button" value="+" onclick="addAdminRow('form-dynamic-tag')" />
            </div>

            <input class="fw-500" type="submit" value="submit"/>
        </form:form>
    </div>
</div>
</body>
</html>

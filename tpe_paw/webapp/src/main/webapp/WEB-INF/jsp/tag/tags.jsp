<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <title><spring:message code="menu.tags"/></title>
        <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon.ico'/>"/>
        <link href="<c:url value='/resources/css/general.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/tags.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/tag.css'/>" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <div class="wrapper">
        <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
            <div class="main-content">
                <div class="flex-center">
                    <div class="fw-100 title-container">
                        <spring:message code="tags.title"> </spring:message>
                    </div>
                </div>
                <div class="all-tags-grid main-grid">
                    <c:forEach var="tag" items="${tags}">
                        <c:set var="tag" value="${tag}" scope="request"/>
                        <c:set var="cssClass" value="tag-container-main" scope="request"/>
                        <c:import url="/WEB-INF/jsp/tag/tag.jsp"/>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
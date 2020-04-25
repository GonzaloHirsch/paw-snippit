<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <link href="<c:url value='/resources/css/general.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/tags.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/tag.css'/>" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <div class="wrapper">
        <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
            <div class="main-content">
                <div class="all-tags-grid">
                    <c:forEach var="tag" items="${tags}">
                        <c:set var="tag" value="${tag}" scope="request"/>
                        <c:import url="/WEB-INF/jsp/tag/tag.jsp"/>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>

    </head>
    <body>
        <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"></c:import>
        <div class="wrapper">
            <c:forEach var="tag" items="${tags}">
                <c:set var="tag" value="${tag}" scope="request"/>
                <c:import url="/WEB-INF/jsp/snippet/snippetTag.jsp"></c:import>
            </c:forEach>
        </div>
    </body>
</html>
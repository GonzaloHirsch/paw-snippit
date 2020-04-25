<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
        <link
        href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="<c:url value='/resources/js/navigationPage.js'/>"></script>
    </head>
    <body>
        <c:url var="searchUrl" value="/${searchContext}search"/>
        <div class="search-container flex-center">
            <form:form modelAttribute="pageForm" method="get" action="${searchUrl}">
                <div class="dropdown-type">
                <form:select path="sort" name="Sort" onchange="submitForm(this)">
                    <form:option value="no">Sort by</form:option>
                    <form:option value="asc">Ascending</form:option>
                    <form:option value="desc">Descending</form:option>
                </form:select>
                </div>
                <c:forEach begin="1" end="${pageForm.totalPages}" varStatus="pageIndex">
                    <c:choose>
                        <c:when test="${pageForm.page == pageIndex.index}">
                            <form:radiobutton id="${'page-'}${pageIndex.index}" path="page" value="${pageIndex.index}" onclick="submitForm(this)"/>
                        </c:when>
                        <c:otherwise>
                            <form:radiobutton id="${'page-'}${pageIndex.index}" path="page" value="${pageIndex.index}" onclick="submitForm(this)"/>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </form:form>
        </div>
    </body>
</html>

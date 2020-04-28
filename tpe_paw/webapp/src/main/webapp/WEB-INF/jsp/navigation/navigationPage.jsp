    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <html>
        <head>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/navigationPage.css'/>" rel="stylesheet"/>
        <link
        href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        </head>
        <body>
        <c:url var="searchUrl" value="/${searchContext}search"/>
        <c:set var="pages" value="${requestScope.pages}"/>
        <c:set var="page" value="${requestScope.page}"/>
        <c:set var="qs" value="${pageContext.request.queryString}"/>
        <c:if test="${pages > 1}">
            <div class="search-container flex-center flex-row">
            <c:if test="${page != 1}">
                <a class="navigation-page" href="<c:url value="?page=${page - 1}&${qs}"/>"><span class="material-icons">
                keyboard_arrow_left </span></a>
            </c:if>
            <c:forEach begin="1" end="${pages}" varStatus="pageIndex">
                <c:choose>
                    <c:when test="${page == pageIndex.index}">
                        <a class="navigation-page navigation-page-selected" href="
                        <c:url value="?page=${pageIndex.index}&${qs}"/>
                        ">${pageIndex.index}</a>
                    </c:when>
                    <c:otherwise>
                        <a class="navigation-page" href="<c:url value="?page=${pageIndex.index}&${qs}"/>">${pageIndex.index}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${page != pages}">
                <a class="navigation-page" href="<c:url value="?page=${page + 1}&${qs}"/>"><span class="material-icons">
                keyboard_arrow_right </span></a>
            </c:if>
            </div>
        </c:if>
        </body>
        </html>

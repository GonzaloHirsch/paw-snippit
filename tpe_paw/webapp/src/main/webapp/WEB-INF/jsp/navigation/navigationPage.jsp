    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

        <html>
        <head>
        <link href="<c:url value='/resources/css/navigationPage.css'/>" rel="stylesheet"/>
        </head>
        <body>
        <c:url var="searchUrl" value="/${searchContext}search"/>
        <c:set var="pages" value="${requestScope.pages}"/>
        <c:set var="page" value="${requestScope.page}"/>
        <c:set var="qs" value="${pageContext.request.queryString}"/>
        <c:if test="${pages > 1}">
            <div class="navigation-page-container search-container flex-center flex-row">
            <c:if test="${page > 2}">
                <c:url var="prevUrl" value="">
                    <c:forEach items="${param}" var="entry">
                        <c:if test="${entry.key != 'page'}">
                            <c:param name="${entry.key}" value="${entry.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:param name="page" value="${1}"/>
                </c:url>
                <a class="navigation-page transition" href="${prevUrl}"><span class="material-icons">
                first_page </span></a>
            </c:if>
            <c:if test="${page != 1}">
                <c:url var="prevUrl" value="">
                    <c:forEach items="${param}" var="entry">
                        <c:if test="${entry.key != 'page'}">
                            <c:param name="${entry.key}" value="${entry.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:param name="page" value="${page - 1}"/>
                </c:url>
                <a class="navigation-page transition" href="${prevUrl}"><span class="material-icons">
                chevron_left </span></a>
            </c:if>
            <c:forEach begin="${page - 1 > 0 ? page - 1 : 1}" end="${pages > page + 1 ? page + 1 : pages}"
                       varStatus="pageIndex">
                <c:url var="actUrl" value="">
                    <c:forEach items="${param}" var="entry">
                        <c:if test="${entry.key != 'page'}">
                            <c:param name="${entry.key}" value="${entry.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:param name="page" value="${pageIndex.index}"/>
                </c:url>
                <c:choose>
                    <c:when test="${page == pageIndex.index}">
                        <a class="navigation-page navigation-page-selected" href="${actUrl}">${pageIndex.index}</a>
                    </c:when>
                    <c:otherwise>
                        <a class="navigation-page transition" href="${actUrl}">${pageIndex.index}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${page != pages}">
                <c:url var="nextUrl" value="">
                    <c:forEach items="${param}" var="entry">
                        <c:if test="${entry.key != 'page'}">
                            <c:param name="${entry.key}" value="${entry.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:param name="page" value="${page + 1}"/>
                </c:url>
                <a class="navigation-page transition" href="${nextUrl}"><span class="material-icons">
                chevron_right </span></a>
            </c:if>
            <c:if test="${page < pages - 1}">
                <c:url var="nextUrl" value="">
                    <c:forEach items="${param}" var="entry">
                        <c:if test="${entry.key != 'page'}">
                            <c:param name="${entry.key}" value="${entry.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:param name="page" value="${pages}"/>
                </c:url>
                <a class="navigation-page transition" href="${nextUrl}"><span class="material-icons">
                last_page </span></a>
            </c:if>
            </div>
        </c:if>
        </body>
        </html>

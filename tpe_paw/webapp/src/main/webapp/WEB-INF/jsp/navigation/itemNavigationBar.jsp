<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<link href="<c:url value='/resources/css/itemSearchBar.css'/>" rel="stylesheet"/>
</head>
<body>
        <c:set var="showEmpty" value="${requestScope.showEmpty}"/>
        <c:set var="itemSearchContext" value="${requestScope.itemSearchContext}"/>
        <c:url var="itemSearchUrl" value="/${itemSearchContext}search"/>
        <c:set var="search_hint" value="${requestScope.search_hint}"/>
        <c:set var="hide_hint" value="${requestScope.hide_hint}"/>
        <c:set var="show_hint" value="${requestScope.show_hint}"/>
        <div class="flex-row flex-wrap flex-center flex-grow">
                <form:form modelAttribute="itemSearchForm" method="get" action="${itemSearchUrl}"
                   class="flex-row flex-center flex-wrap item-search-container">
                        <div class="flex-row flex-grow fw-100">
                                <form:input path="name" type="text" id="item-search-bar" class="item-search-input flex-grow fw-100"
                                placeholder="${search_hint}"/>
                                <button type="submit"><span class="material-icons item-search-icon">search</span></button>
                        </div>
                </form:form>
                <c:url var="nextUrl" value="">
                        <c:forEach items="${param}" var="entry">
                                <c:if test="${entry.key != 'showEmpty' && entry.key != 'page'}">
                                        <c:param name="${entry.key}" value="${entry.value}"/>
                                </c:if>
                        </c:forEach>
                        <c:param name="showEmpty" value="${!showEmpty}"/>
                </c:url>
                <c:choose>
                        <c:when test="${showEmpty == true}">
                                <a class="side-button transition" href="${nextUrl}">${hide_hint}</a>
                        </c:when>
                        <c:otherwise>
                                <a class="side-button transition" href="${nextUrl}">${show_hint}</a>
                        </c:otherwise>
                </c:choose>
        </div>
</body>
</html>

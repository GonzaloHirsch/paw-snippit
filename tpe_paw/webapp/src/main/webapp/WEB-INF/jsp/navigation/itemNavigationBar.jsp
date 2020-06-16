<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <link href="<c:url value='/resources/css/itemSearchBar.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    </head>
    <body>
        <c:set var="itemSearchContext" value="${requestScope.itemSearchContext}"/>
        <c:url var="itemSearchUrl" value="/${itemSearchContext}search"/>
        <c:set var="search_hint" value="${requestScope.search_hint}"/>
        <c:set var="hide_hint" value="${requestScope.hide_hint}"/>
        <c:set var="show_hint" value="${requestScope.show_hint}"/>
        <c:set var="hide_following_hint" value="${requestScope.hide_following_hint}"/>
        <c:set var="show_following_hint" value="${requestScope.show_following_hint}"/>
        <c:set var="loggedUser" value="${requestScope.loggedUser}"/>
        <div class="flex-row flex-wrap flex-center flex-grow">
            <form:form modelAttribute="itemSearchForm" method="get" action="${itemSearchUrl}"
                   class="flex-row flex-center flex-wrap">
                <div class="flex-row flex-grow fw-100 item-search-container">
                    <form:input path="name" type="text" id="item-search-bar" class="item-search-input flex-grow fw-100"
                        placeholder="${search_hint}"/>
                    <button type="submit"><span class="material-icons item-search-icon">search</span></button>
                </div>
                <form:checkbox class="hidden" id="visibility-button" path="showEmpty" value="true" onclick="updateForm(this)"/>
                <label class="no-margin visibility-label" for="visibility-button">
                <c:choose>
                    <c:when test="${itemSearchForm.showEmpty}">
                        <div class="flex-center side-button no-text-decoration transition">
                        <c:out value="${hide_hint}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="flex-center side-button no-text-decoration transition">
                        <c:out value="${show_hint}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
                </label>
                <c:if test="${requestScope.itemSearchContext == 'tags/' && loggedUser != null}">
                    <form:checkbox class="hidden" id="following-visibility-button" path="showOnlyFollowing" value="false" onclick="updateForm(this)"/>
                    <label class="no-margin following-visibility-label" for="following-visibility-button">
                    <c:choose>
                        <c:when test="${itemSearchForm.showOnlyFollowing}">
                            <div class="flex-center side-button no-text-decoration transition">
                            <c:out value="${hide_following_hint}"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="flex-center side-button no-text-decoration transition">
                            <c:out value="${show_following_hint}"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    </label>
                </c:if>
            </form:form>
        </div>
    </body>
</html>

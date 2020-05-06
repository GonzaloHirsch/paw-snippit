<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<link href="<c:url value='/resources/css/navigationBar.css'/>" rel="stylesheet"/>
<link href="<c:url value='/resources/css/itemSearchBar.css'/>" rel="stylesheet"/>
<link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
<link
href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
rel="stylesheet">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
        <c:set var="itemSearchContext" value="${requestScope.itemSearchContext}"/>
        <c:url var="itemSearchUrl" value="/${itemSearchContext}search"/>
        <spring:message code="search.hint" var="search_hint"/>
        <div class="flex-row flex-wrap flex-center flex-grow">
                <form:form modelAttribute="itemSearchForm" method="get" action="${itemSearchUrl}"
                   class="flex-row flex-center flex-wrap item-search-container">
                        <div class="flex-row flex-grow fw-100">
                                <form:input path="query" type="text" id="item-search-bar" class="item-search-input flex-grow fw-100"
                                placeholder="${search_hint}"/>
                                <button type="submit"><span class="material-icons item-search-icon">search</span></button>
                        </div>
                </form:form>
        </div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <title><spring:message code="tags.title"/></title>
        <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
        <link href="<c:url value='/resources/css/general.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/elementsList.css'/>" type="text/css" rel="stylesheet"/>
        <link href="<c:url value='/resources/css/element.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
        <link href="<c:url value='/resources/css/icons.css'/>" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
              rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    </head>
    <body>
        <div class="wrapper">
        <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
            <div class="main-content">
                <div class="flex-center">
                    <div class="fw-100 title-container">
                        <spring:message code="menu.tags"> </spring:message>
                    </div>
                </div>
                <div>
                    <spring:message code="search.tags.hint" var="search_hint"/>
                    <spring:message code="hide.tags.hint" var="hide_hint"/>
                    <spring:message code="show.tags.hint" var="show_hint"/>
                    <spring:message code="hide.following.tags.hint" var="hide_following_hint"/>
                    <spring:message code="show.following.tags.hint" var="show_following_hint"/>
                    <c:set var="search_hint" value="${search_hint}" scope="request"/>
                    <c:set var="hide_hint" value="${hide_hint}" scope="request"/>
                    <c:set var="show_hint" value="${show_hint}" scope="request"/>
                    <c:set var="hide_following_hint" value="${hide_following_hint}" scope="request"/>
                    <c:set var="show_following_hint" value="${show_following_hint}" scope="request"/>
                    <c:set var="loggedUser" value="${loggedUser}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/navigation/itemNavigationBar.jsp"/>
                </div>
                <c:choose>
                    <c:when test="${tags.size() > 0}">
                        <div class="all-elements-grid main-grid">
                            <c:forEach var="tag" items="${tags}">
                                <c:set var="element" value="${tag}" scope="request"/>
                                <c:set var="context" value="tags" scope="request"/>
                                <c:set var="cssClass" value="element-container-main" scope="request"/>
                                <c:import url="/WEB-INF/jsp/tagAndLanguages/element.jsp"/>
                            </c:forEach>
                        </div>
                        <div class="flex-row flex-wrap flex-center flex-grow">
                            <c:import url="/WEB-INF/jsp/navigation/navigationPage.jsp"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-elements flex-center fw-100">
                            <spring:message code="feed.no-tags"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
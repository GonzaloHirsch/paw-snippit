<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="language.title" arguments="${language.name.toUpperCase()}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/elementSnippets.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/general.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/element.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>
<c:set var="snippetList" value="${snippetList}" scope="request"/>
<c:set var="langId" value="${language.id}" scope="request"/>
<c:set var="deleteFromSearch" value="languages/${langId}/search/"/>
<c:choose>
    <c:when test="${requestScope.searchContext == deleteFromSearch}">
        <c:set var="deleteUrl" value="delete"/>
    </c:when>
    <c:otherwise>
        <c:set var="deleteUrl" value="${langId}/delete"/>
    </c:otherwise>
</c:choose>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-row flex-center element-snippets-top-container">
            <div class="flex-row flex-center title-container">

                <div class="fw-100">
                    <spring:message code="languageSnippets.title" arguments="${language.name.toUpperCase()}"/>
                </div>
                <c:if test="${currentUser != null && userRoles.contains('ADMIN') && snippetList.size() == 0}">
                    <form:form action="${deleteUrl}" class="form-container" method="post" modelAttribute="deleteForm">
                        <div class="flex-center no-text-decoration" data-toggle="modal" data-target="#delete-lang">
                            <i class="delete-element-icon material-icons border-radius">delete</i>
                        </div>

                        <!-- Modal -->
                        <div class="modal fade" id="delete-lang" tabindex="-1" role="dialog" aria-labelledby="langDeletionModal" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title font" id="langDeletionModal"><spring:message code="modal.delete.title"/></h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body modal-body-text fw-100">
                                        <spring:message code="modal.delete.language.text" arguments="${language.name}"/>
                                    </div>
                                    <div class="modal-footer flex-end">
                                        <div class="flex-center">
                                            <div type="button" class="btn btn-secondary modal-close-button" data-dismiss="modal"><spring:message code="modal.cancel"/></div>
                                            <form:checkbox class="hidden" path="delete" value="true" id="lang-delete-button" onclick="updateForm(this)"/>
                                            <label for="lang-delete-button" class="btn btn-primary purple-button  no-margin">
                                                <div typeof="button">
                                                    <spring:message code="modal.delete.confirm"/>
                                                </div>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </c:if>
            </div>
        </div>
        <c:import url="/WEB-INF/jsp/snippet/snippetFeed.jsp"/>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>

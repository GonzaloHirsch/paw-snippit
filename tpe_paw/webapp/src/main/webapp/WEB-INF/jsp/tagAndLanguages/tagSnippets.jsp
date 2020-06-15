<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="tag.title" arguments="${tag.name.toUpperCase()}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/navigationBar.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/elementSnippets.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/icons.css'/>" type="text/css" rel="stylesheet"/>
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
<spring:message var="tagTitle" code="tagSnippets.title" arguments="${tag.name.toUpperCase()}"/>
<spring:message var="modalCancel" code="modal.cancel"/>
<spring:message var="modalConfirm" code="modal.delete.confirm"/>

<c:set var="tagId" value="${tag.id}" scope="request"/>
<c:url var="tagDeleteUrl" value="/tags/${tagId}/delete"/>
<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content">
        <div class="flex-row flex-center element-snippets-top-container">
            <div class="flex-row flex-center title-container">
                <div class="element-snippets-title fw-100">
                    <c:out value="${tagTitle}"/>
                </div>
                <c:import url="/WEB-INF/jsp/tagAndLanguages/tagFollowForm.jsp"/>
                <c:if test="${currentUser != null}">
                    <c:if test="${userRoles.contains('ADMIN')}">
                        <form:form action="${tagDeleteUrl}" class="form-container" method="post" modelAttribute="deleteForm">
                            <div class="flex-center no-text-decoration" data-toggle="modal" data-target="#tag-snippet">
                                <i class="delete-element-icon material-icons border-radius">delete</i>
                            </div>

                            <!-- Modal -->
                            <div class="modal fade" id="tag-snippet" tabindex="-1" role="dialog" aria-labelledby="tagDeletionModal" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title font" id="tagDeletionModal"><spring:message code="modal.delete.title"/></h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body modal-body-text fw-100">
                                            <spring:message var="modalTagDeleteMessage" code="modal.delete.tag.text" arguments="${tag.name}"/>
                                            <c:out value="${modalTagDeleteMessage}"/>
                                        </div>
                                        <div class="modal-footer flex-end">
                                            <div class="flex-center">
                                                <div type="button" class="btn btn-secondary modal-close-button" data-dismiss="modal"><c:out value="${modalCancel}"/></div>
                                                <form:checkbox class="hidden" path="delete" value="true" id="tag-delete-button" onclick="updateForm(this)"/>
                                                <label for="tag-delete-button" class="btn btn-primary purple-button  no-margin">
                                                    <c:out value="${modalConfirm}"/>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                    </c:if>
                </c:if>
            </div>
        </div>
        <c:set var="snippetList" value="${snippetList}" scope="request"/>
        <c:import url="/WEB-INF/jsp/snippet/snippetFeed.jsp"/>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>


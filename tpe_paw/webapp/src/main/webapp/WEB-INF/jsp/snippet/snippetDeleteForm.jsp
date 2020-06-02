<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<div class="flex-column">
    <c:set var="snippetId" value="${requestScope.snippetId}"/>
    <c:set var="snippet" value="${requestScope.snippet}"/>
    <form:form action="${snippetId}/delete" class="form-container flex-center" method="post" modelAttribute="deleteForm">
        <c:choose>
            <c:when test="${deleteForm.delete}">
                <div class="flex-center no-text-decoration" data-toggle="modal" data-target="#delete-snippet">
                    <i class="delete-snippet-icon snippet-icon material-icons ">undo</i>
                </div>
            </c:when>
            <c:otherwise>
                <div class="flex-center no-text-decoration" data-toggle="modal" data-target="#delete-snippet">
                    <i class="delete-snippet-icon snippet-icon material-icons ">delete</i>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Modal -->
        <div class="modal fade" id="delete-snippet" tabindex="-1" role="dialog" aria-labelledby="deletionModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title font" id="deletionModal"><spring:message code="modal.delete.title"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body modal-body-text fw-100">
                        <spring:message code="modal.delete.snippet.text" arguments="${snippet.title}"/>
                    </div>
                    <div class="modal-footer flex-end">
                        <div class="flex-center">
                            <div type="button" class="btn btn-secondary modal-close-button" data-dismiss="modal"><spring:message code="modal.cancel"/></div>
                            <form:checkbox class="hidden" path="delete" value="true" id="snippet-delete-button" onclick="updateForm(this)"/>
                            <label for="snippet-delete-button" class="btn btn-primary purple-button  no-margin">
<%--                                <div typeof="button">--%>
                                    <spring:message code="modal.delete.confirm"/>
<%--                                </div>--%>
                            </label>
<%--                            <button type="button" class="btn btn-primary purple-button no-margin" onclick="updateForm(this)"><spring:message code="modal.delete.confirm" /></button>--%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>

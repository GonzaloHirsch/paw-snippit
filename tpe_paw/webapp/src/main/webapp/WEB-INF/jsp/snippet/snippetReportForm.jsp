<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    <link href="<c:url value='/resources/css/elementSnippets.css'/>" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>

<body>

<spring:message code="modal.report.hint" var="detailHint"/>

<div class="flex-column">
    <c:set var="snippetId" value="${requestScope.snippetId}"/>
    <form:form class="form-container flex-center" action="${snippetId}/report" method="post" modelAttribute="reportForm">
        <checkbox class="hidden" id="report-button"></checkbox>
        <label for="report-button" class="no-margin" data-toggle="modal" data-target="#report-modal">
            <c:choose>
                <c:when test="${reportForm.reported}">
                    <i class="unselected-brown-icon material-icons snippet-icon">report</i>
                </c:when>
                <c:otherwise>
                    <i class="selected-brown-icon material-icons snippet-icon">favorite</i>
                </c:otherwise>
            </c:choose>
        </label>
        <!-- Modal -->
        <div class="modal fade" id="report-modal" tabindex="-1" role="dialog" aria-labelledby="reportSnippetModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title font" id="reportSnippetModal"><spring:message code="modal.report.title"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body modal-body-text fw-100">
                        <form:textarea class="full-width snippetC-code-input snippetC-border" type="text" path="reportDetail" placeholder='${detailHint}'/>
                    </div>
                    <div class="modal-footer flex-end">
                        <div class="flex-center">
                            <form:checkbox class="hidden" path="reported" value="true" id="snippet-report-button" onclick="updateForm(this)"/>
                            <label for="snippet-report-button" class="btn btn-primary purple-button  no-margin">
                                <spring:message code="modal.report.confirm"/>
                            </label>
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

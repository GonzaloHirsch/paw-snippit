<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value='/resources/css/elementSnippets.css'/>" type="text/css" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" type="text/css" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/reportForm.js'/>"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>

<body onload="displayErrors(${displayReportDialog})">

<spring:message code="modal.report.hint" var="detailHint"/>
<c:set var="snippetId" value="${requestScope.snippetId}"/>
<c:url var="snippetReportUrl" value="/snippet/${snippetId}/report"/>

<div class="flex-column">
    <form:form class="form-container flex-center" action="${snippetReportUrl}" method="post" modelAttribute="reportForm">
        <input type="checkbox" class="hidden" id="report-button"/>
        <c:choose>
            <c:when test="${!reportForm.reported}">
                <label for="report-button" class="no-margin" data-toggle="modal" data-target="#report-modal">
                    <i class="unselected-brown-icon material-icons snippet-icon">report</i>
                </label>
            </c:when>
            <c:otherwise>
                <label for="report-button" class="no-margin">
                    <i class="selected-brown-icon material-icons snippet-icon">report</i>
                </label>
            </c:otherwise>
        </c:choose>
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
                    <div class="flex-column modal-body modal-body-text fw-100">
                        <form:label class="fw-400 report-subtitle black-text" path="reportDetail"><spring:message code="modal.report.reasonText"/> </form:label>
                        <form:errors class="fw-300 error-report-modal no-margin form-text-error form-error" path="reportDetail" element="p"/>
                        <form:textarea class="full-width report-detail-input" rows="5" type="text" path="reportDetail" placeholder='${detailHint}'/>
                    </div>
                    <div class="modal-footer flex-end">
                        <div class="flex-center">
                            <form:checkbox class="hidden" path="reported" id="snippet-report-button" onclick="updateForm(this)"/>
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

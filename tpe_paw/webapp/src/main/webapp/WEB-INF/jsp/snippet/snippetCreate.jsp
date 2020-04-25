<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Snippet Create</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">

    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetCreate.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
</head>

<body>

<spring:message code="snippetCreateForm.title" var="title_hint"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
    <c:url value="/create" var="postPath"/>
    <div class="main-content">

        <div class="flex-row snippetC-container flex-center">
            <form:form class="snippetC-form" modelAttribute="snippetCreateForm" action="${postPath}" method="post">

                <div class="flex-column">

                    <div class="flex-row flex-center">
                        <div class="snippetC-title-container">
                            <form:input class="snippetC-title-input snippetC-border fw-100" type="text" path="title" placeholder='${title_hint}'/>
                            <form:errors path="title" cssClass="formError" element="p"/>
                        </div>
                        <div class="snippetC-language-container">
                            <form:select class="selectpicker snippetC-language" path="language">
                                <form:option value="-1">Language</form:option>
                                <c:forEach items="${languageList}" var="lan" varStatus="status">
                                    <form:option value="${lan.id}">${lan.name.toUpperCase()}</form:option>
                                </c:forEach>
                            </form:select>
                            <form:errors path="language" cssClass="formError" element="p"/>
                        </div>
                    </div>

                    <hr class="snippetC-divider"/>

                    <div class="snippetC-elem-container">
                        <form:label path="description"><spring:message code="snippetCreateForm.description"/> </form:label>
                        <form:textarea class="full-width snippetC-description-input snippetC-border" rows="1" type="text" path="description"/>
                        <form:errors path="description" cssClass="formError" element="p"/>
                    </div>

                    <hr class="snippetC-divider"/>

                    <div class="snippetC-elem-container">
                        <form:label path="code"><spring:message code="snippetCreateForm.code"/> </form:label>
                        <form:textarea class="full-width snippetC-code-input snippetC-border"  rows="5" type="text" path="code" placeholder="Enter some code..."/>
                        <form:errors path="code" cssClass="formError" element="p"/>
                    </div>

                    <hr class="snippetC-divider"/>

                    <div class="flex-row flex-space-between snippetC-elem-container flex-center">

                        <div class="flex-column snippetC-tags-container">
                            <form:label path="tags"><spring:message code="snippetCreateForm.tags"/></form:label>
                            <form:select class="selectpicker snippetC-tags" multiple="true" data-live-search="true" path="tags" name="tagselect" >
                                <form:options items="${tagList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors path="tags" cssClass="formError" element="p"/>
                        </div>
                        <div class="snippetC-submit-container">
                            <input class="snippetC-button snippetC-border" type="submit" value='<spring:message code="snippetCreateForm.save"/>'/>
                        </div>
                    </div>

                </div>
            </form:form>


        </div>
    </div>
</div>

<!-- Bootstrap -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>


</body>
</html>
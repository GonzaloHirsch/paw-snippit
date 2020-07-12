<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><spring:message code="snippetCreateForm.header"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetCreate.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
</head>

<body>
<spring:message code="snippetCreateForm.header" var="snippetCreateHeader"/>
<spring:message code="snippetCreateForm.title" var="snippetCreateTitle"/>
<spring:message code="snippetCreateForm.description" var="snippetCreateDescr"/>
<spring:message code="snippetCreateForm.descriptionHint" var="desc_hint"/>
<spring:message code="snippetCreateForm.codeHint" var="code_hint"/>
<spring:message code="snippetCreateForm.code" var="snippetCreateCode"/>
<spring:message code="snippetCreateForm.tags" var="snippetCreateTags"/>
<spring:message code="snippetCreateForm.tagsHint" var="tag_hint"/>
<spring:message code="snippetCreateForm.language" var="snippetCreateLang"/>
<spring:message code="snippetCreateForm.languageHint" var="snippetCreateLangHint"/>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <c:url value="/snippet/create" var="postPath"/>
    <div class="main-content">

        <div class="flex-center">
            <div class="fw-100 title-container">
                <c:out value="${snippetCreateHeader}"/>
            </div>
        </div>

        <div class="flex-row snippetC-container flex-center">
            <form:form class="snippetC-form" modelAttribute="snippetCreateForm" action="${postPath}" method="post">

                <div class="flex-column">
                    <div class="flex-column snippetC-top-container">
                        <div class="flex-row">
                            <div class="snippetC-title-container">
                                <form:label class="fw-400 snippetC-subtitles" path="title"><c:out value="${snippetCreateTitle}"/></form:label>
                                <form:input class="snippetC-title-input snippetC-border fw-400" type="text" path="title" placeholder='${snippetCreateTitle}'/>
                            </div>
                            <div class="snippetC-language-container">
                                <form:label class="fw-400 snippetC-subtitles" path="title"><c:out value="${snippetCreateLang}"/> </form:label>
                                <form:select class="selectpicker snippetC-language"  data-live-search="true" path="language">
                                    <form:option value="-1"><c:out value="${snippetCreateLangHint}"/></form:option>
                                    <c:forEach items="${languageList}" var="lan" varStatus="status">
                                        <form:option value="${lan.id}"><c:out value="${lan.name.toUpperCase()}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <div class="flex-column">
                            <form:errors class="form-error" path="title"  element="p"/>
                            <form:errors class="form-error" path="language" element="p"/>
                        </div>
                    </div>
                    <hr class="snippetC-divider"/>

                    <div class="snippetC-elem-container">
                        <form:label class="fw-400 snippetC-subtitles" path="description"><c:out value="${snippetCreateDescr}"/></form:label>
                        <form:textarea class="full-width snippetC-description-input snippetC-border" rows="2" type="text" path="description" placeholder='${desc_hint}'/>
                        <form:errors class="form-error error-extra-margins" path="description" element="p"/>
                    </div>

                    <hr class="snippetC-divider"/>

                    <div class="snippetC-elem-container">
                        <form:label class="fw-400 snippetC-subtitles" path="code"><c:out value="${snippetCreateCode}"/></form:label>
                        <form:textarea class="full-width snippetC-code-input snippetC-border"  rows="5" type="text" path="code" placeholder='${code_hint}'/>
                        <form:errors class="form-error error-extra-margins" path="code" element="p"/>
                    </div>

                    <hr class="snippetC-divider"/>

                    <div class="flex-row flex-space-between snippetC-elem-container flex-center">

                        <div class="flex-column snippetC-tags-container">
                            <form:label class="fw-400 snippetC-subtitles" path="tags"><c:out value="${snippetCreateTags}"/></form:label>
                            <form:select class="selectpicker snippetC-tags" multiple="true" data-live-search="true" path="tags" title='${tag_hint}' name="tagselect" >
                                <form:options items="${tagList}" itemValue="name" itemLabel="name"/>
                            </form:select>
                            <form:errors class="form-error error-extra-margins" path="tags" element="p"/>
                        </div>
                        <div class="snippetC-submit-container">
                            <input class="snippetC-button form-button-basics form-button form-border" type="submit" value='<spring:message code="snippetCreateForm.save"/>'/>
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
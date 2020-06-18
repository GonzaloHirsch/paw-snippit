<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title><spring:message code="admin.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resources/favicon/favicon.ico'/>"/>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/form.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="<c:url value='/resources/js/addDynamicContent.js'/>"></script>
</head>
<body>
<c:url var="adminAddUrl" value="/admin/add"/>

<spring:message code="admin.add.tag.placeholder" var="tagPlaceholder"/>
<spring:message code="admin.add.tag" var="addTag"/>
<spring:message code="admin.add.language.placeholder" var="langPlaceholder"/>
<spring:message code="admin.add.language" var="addLang"/>


<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navigation/navigationBar.jsp"/>
    <div class="main-content flex-column flex-center">

        <div class="flex-center">
            <div class="fw-100 title-container">
                <spring:message code="admin.add.header"/>
            </div>
        </div>

        <form:form class="flex-center flex-column form form-width form-border form-shadow" action="${adminAddUrl}" method="post" modelAttribute="adminAddForm">
            <div class="flex-row">
                <div id="form-lang-container" class="flex-column form-admin-add-width">

                    <div class="form-add-column-title fw-300"><spring:message code="admin.add.language.title"/></div>

                    <div id="form-dynamic-lang" class="flex-column flex-center form-buttons-margin">

                        <form:errors path="languages" cssClass="form-error" element="p "/>
                        <c:set var="langCounter" value="0"/>
                        <c:forEach var="lang" items="${adminAddForm.languages}">
                            <div class="flex-row form-field-container">
                                <input type="text" class="form-border form-field-size form-added-field form-border-style" value="${lang}" name="languages[${langCounter}]" placeholder="${langPlaceholder}">
                                <label>
                                    <i class="material-icons form-delete-icon">delete</i>
                                    <input type="button" class="form-remove-button form-border form-button-basics" onclick="removeRow(this, 'form-dynamic-lang', 'langCount', 'langButton')" />
                                </label>
                            </div>
                            <c:set var="langCounter" value="${langCounter + 1}"/>
                        </c:forEach>
                        <input type="text" id="langCount" class="hidden" value="${langCounter}"/>
                    </div>

                    <div id="langButton" class="flex-center form-add-field-container">
                        <label class="flex-center form-admin-add-button border-radius transition">
                            <i class="material-icons form-add-icons">add_circle_outline</i>
                            <input class="form-add-description form-button-basics fw-300" type="button" value="${addLang}" onclick="addAdminRow('form-dynamic-lang', '${langPlaceholder}')"/>
                        </label>
                    </div>
                </div>

                <div class="vertical-hr"></div>


                <div id="form-tag-container" class="flex-column form-admin-add-width">

                    <div class="form-add-column-title fw-300"><spring:message code="admin.add.tag.title"/></div>

                    <div id="form-dynamic-tag" class="flex-column flex-center form-buttons-margin">

                        <form:errors path="tags" cssClass="form-error" element="p "/>
                        <c:set var="tagCounter" value="0"/>
                        <c:forEach var="tag" items="${adminAddForm.tags}">
                            <div class="flex-row form-field-container">
                                <input type="text" class="form-border form-field-size form-added-field form-border-style" value="${tag}" name="tags[${tagCounter}]" placeholder="${tagPlaceholder}">
                                <label>
                                    <i class="material-icons form-delete-icon">delete</i>
                                    <input type="button" class="form-remove-button form-border form-button-basics" onclick="removeRow(this, 'form-dynamic-tag', 'tagCount', 'tagButton')" />
                                </label>
                            </div>
                            <c:set var="tagCounter" value="${tagCounter + 1}"/>
                        </c:forEach>
                        <input type="text" id="tagCount" class="hidden" value="${tagCounter}"/>
                    </div>

                    <div id="tagButton" class="flex-center form-add-field-container">
                        <label class="flex-center form-admin-add-button border-radius transition">
                            <i class="material-icons form-add-icons">add_circle_outline</i>
                            <input class="form-add-description form-button-basics fw-300" type="button" value="${addTag}" onclick="addAdminRow('form-dynamic-tag', '${tagPlaceholder}')" />
                        </label>
                    </div>
                </div>
            </div>

            <div class="form-field-container">
                <input class="form-border form-large-button form-button form-button-basics fw-500" type="submit" value="<spring:message code="form.submit"/>" />
            </div>
        </form:form>
    </div>
</div>
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Snippet Detail</title>
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
                            <form:input class="snippetC-title-input" type="text" path="title" placeholder='${title_hint}'/>
                            <form:errors path="title" cssClass="formError" element="p"/>
                        </div>
                        <div class="snippetC-language-container">
                            <form:select path="language">
                                <form:option value="-1">Select code Language</form:option>
                                <c:forEach items="${languageList}" var="lan" varStatus="status">
                                    <form:option value="${lan.id}">${lan.name}</form:option>
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

                    <div class="flex-row flex-space-between">
                        <div>
                            <form:select col="3" path="tags" multiple="true" name="tagselect" >
                                <form:options items="${tagList}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors path="tags" cssClass="formError" element="p"/>
                        </div>
                        <div>
                            <input type="submit" value='<spring:message code="snippetCreateForm.save"/>'/>
                        </div>
                    </div>

                </div>
            </form:form>


        </div>
    </div>
</div>
</body>
</html>
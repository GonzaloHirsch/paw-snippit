<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>

</head>

<body>

<div class="wrapper">
    <c:import url="/WEB-INF/jsp/navBar/navigationBar.jsp"/>
    <div class="main-content">
        <c:url value="/create" var="postPath"/>
        <form:form modelAttribute="snippetCreateForm" action="${postPath}" method="post">
            <div>
                <form:label path="title"><spring:message code="snippetCreateForm.title"/> </form:label>
                <form:input type="text" path="title"/>
                <form:errors path="title" cssClass="formError" element="p"/>
            </div>
            <div>
                <form:label path="description"><spring:message code="snippetCreateForm.description"/> </form:label>
                <form:input type="text" path="description"/>
                <form:errors path="description" cssClass="formError" element="p"/>
            </div>
            <div>
                <form:label path="code"><spring:message code="snippetCreateForm.code"/> </form:label>
                <form:input type="text" path="code"/>
                <form:errors path="code" cssClass="formError" element="p"/>
            </div>

            <div>
                <input type="submit" value='<spring:message code="snippetCreateForm.save"/>'/>
            </div>
        </form:form>


    </div>
</div>
</body>
</html>
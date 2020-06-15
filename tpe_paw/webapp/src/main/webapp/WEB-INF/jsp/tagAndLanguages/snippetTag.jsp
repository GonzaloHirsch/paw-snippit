<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
</head>
<body>
<c:set var="element" value="${requestScope.element}"/>
<c:set var="context" value="${requestScope.context}"/>
<c:set var="style" value="${requestScope.cssClass}"/>
<spring:message code="tooltip.element.empty" var="emptyTooltip"/>

<div class="flex-grow ${style}">
    <div class="flex-row flex-center">
        <a href="<c:url value='/${context}/${element.id}'/>" class="no-text-decoration fw-300 text-center flex-grow">
            <c:out value="${element.name.toUpperCase()}"/>
        </a>
    </div>
</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
</head>
<body>
<c:set var="element" value="${requestScope.element}"/>
<c:set var="context" value="${requestScope.context}"/>
<c:set var="style" value="${requestScope.cssClass}"/>
<spring:message code="tooltip.element.empty" var="emptyTooltip"/>


    <div class="flex-grow ${style}">
        <div class="flex-row flex-center expand">
            <a href="<c:url value='/${context}/${element.id}'/>" class="no-text-decoration flex-center fw-300 element-title flex-grow">
                ${element.name.toUpperCase()}
            </a>
            <c:if test="${element.snippetsUsing.size() == 0}">
                <div class="material-icons element-icon transition" data-toggle="tooltip" data-placement="left" title="${emptyTooltip}">block</div>
            </c:if>
        </div>
    </div>
</body>
</html>

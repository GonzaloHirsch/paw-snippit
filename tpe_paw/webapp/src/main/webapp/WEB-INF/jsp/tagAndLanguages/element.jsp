<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
</head>
<body>
<c:set var="element" value="${requestScope.element}"/>
<c:set var="context" value="${requestScope.context}"/>
    <c:set var="style" value="${requestScope.cssClass}"/>
    <div class="flex-grow ${style}">
        <div class="flex-row flex-center expand">
            <a href="<c:url value='/${context}/${element.id}'/>" class="no-text-decoration flex-center fw-300 element-title flex-grow">
                ${element.name.toUpperCase()}
            </a>
            <c:if test="${element.snippetsUsing.size() == 0}">
                <i class="material-icons element-icon">block</i>
            </c:if>
        </div>
    </div>
</body>
</html>

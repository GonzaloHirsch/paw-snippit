<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
</head>
<body>
<c:set var="element" value="${requestScope.element}"/>
<c:set var="context" value="${requestScope.context}"/>
    <c:set var="style" value="${requestScope.cssClass}"/>
    <a href="<c:url value='/${context}/${element.id}'/>" class="${style} ${'flex-center'}">
        <div class="fw-300 tag-title">
            ${element.name.toUpperCase()}
        </div>
    </a>
</body>
</html>

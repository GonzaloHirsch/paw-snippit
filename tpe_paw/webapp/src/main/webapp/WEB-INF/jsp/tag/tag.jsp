<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link href="<c:url value='/resources/css/tag.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet" />
    <link href="<c:url value='/resources/css/general.css'/>" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
    <c:set var="tag" value="${requestScope.tag}"/>
    <c:set var="style" value="${requestScope.cssClass}"/>
    <a href="<c:url value='/tags/${tag.id}'/>" class="${style} ${'flex-center'}">
        <div class="fw-300 tag-title">
            ${tag.name.toUpperCase()}
        </div>
    </a>
</body>
</html>

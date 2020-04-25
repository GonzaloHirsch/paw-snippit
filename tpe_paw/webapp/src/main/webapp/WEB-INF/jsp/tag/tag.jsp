<%--
  Created by IntelliJ IDEA.
  User: fpetrikovich
  Date: 4/14/20
  Time: 10:15 AM
  To change this template use File | Settings | File Templates.
--%>
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
    <a href="<c:url value='/tags/${tag.id}'/>" class="tag-container border-radius">
        <div class="snippet-text tag-text">${tag.name.toUpperCase()}</div>
    </a>
</body>
</html>

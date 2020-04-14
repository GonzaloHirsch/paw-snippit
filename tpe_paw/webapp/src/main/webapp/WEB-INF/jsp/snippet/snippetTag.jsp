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
</head>
<body>
    <c:set var="tag" value="${requestScope.tag}"/>
    <div class="tag-container border-radius">
        <div class="snippet-text tag-text">${tag.name.toUpperCase()}</div>
    </div>
</body>
</html>

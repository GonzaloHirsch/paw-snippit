<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

</head>
<body>
    <c:forEach var="snippet" items="${snippets}">
        <c:set var="snippet" value="${snippet}" scope="request"/>
        <c:import url="/WEB-INF/jsp/snippet/snippetCard.jsp"/>
    </c:forEach>
</body>
</html>

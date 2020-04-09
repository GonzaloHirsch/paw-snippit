<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Snippet Detail</title>
    <link  href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
</head>
<body>
    <div>
        <h1>
            ${snippet.title}
        </h1>
        <h2>
            ${snippet.description}
        </h2>
    </div>
    <div>
        ${snippet.code}
    </div>
</body>
</html>

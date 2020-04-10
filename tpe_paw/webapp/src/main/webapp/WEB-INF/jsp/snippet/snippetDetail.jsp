<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/snippetDetails.js'/>"></script>
</head>
<body>
    <div class="snippet-detail-main-row">
        <div class="snippet-detail-left-col">
            <div class="vote-buttons">
                <form:form action="vote" method="post" modelAttribute="vote">
                    UP<form:radiobutton path="type" value="1" onchange="changed(this)"/>
                    DOWN<form:radiobutton path="type" value="0" onchange="changed(this)"/>
                    <form:input class="hidden-input" path="userId"/>
                    <form:input class="hidden-input" path="snippetId"/>
                </form:form>
            </div>
        </div>
        <div class="snippet-detail-center-col">
            <h1>
                ${userId}
                ${snippetId}
                ${snippet.title}
            </h1>
            <h2>
                ${snippet.description}
            </h2>
            <div>${snippet.code}</div>
        </div>
    </div>
</body>
</html>

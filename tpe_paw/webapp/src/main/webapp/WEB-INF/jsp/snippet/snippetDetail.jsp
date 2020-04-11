<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Snippet Detail</title>
    <link href="<c:url value='/resources/css/snippetDetail.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/snippet.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/snippetDetails.js'/>"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet"></head>
<body>
    <div class="snippet-detail-main-row">
        <div class="snippet-detail-left-col">
            <div class="vote-buttons">
                <form:form action="vote" method="post" modelAttribute="vote">
                    <form:radiobutton id="up-button" path="type" value="1" onclick="changed(this)"/>
                    <label for="up-button">
                        <i class="material-icons vote-arrow">arrow_drop_up</i>
                    </label>
                    <form:radiobutton id="down-button" path="type" value="0" onclick="changed(this)"/>
                    <label for="down-button">
                        <i class="material-icons vote-arrow">arrow_drop_down</i>
                    </label>
                    <form:input class="hidden" path="oldType"/>
                    <form:input class="hidden" path="userId"/>
                    <form:input class="hidden" path="snippetId"/>
                </form:form>
            </div>
        </div>
        <div class="snippet-detail-center-col">
            <h2 class="snippet-text">
                ${snippet.title}
            </h2>
            <div>
                <p class="snippet-text">
                    ${snippet.description}
                </p>
            </div>
            <div class="snippet-code-container">
                <pre>
                    <code>
                        <div>${snippet.code}</div>
                    </code>
                </pre>
            </div>
        </div>
    </div>
</body>
</html>

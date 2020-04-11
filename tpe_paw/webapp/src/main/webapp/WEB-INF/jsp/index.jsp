<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <link href="<c:url value='/resources/css/snippetFeed.css'/>" rel="stylesheet" />
    </head>
    <body>
        <div class="feed-background-color">
            <c:set var="snippetList" value="${snippetList}" scope="request"/>
            <c:import url="snippet/snippetFeed.jsp"/>
        </div>
    </body>
</html>
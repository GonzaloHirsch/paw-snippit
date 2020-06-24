    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <html>
        <head>
        <link href="<c:url value='/resources/css/itemCountDisplay.css'/>" rel="stylesheet"/>
        </head>
        <body>
        <div class="display-flex flex-center item-count fw-100">
        <c:set var="searching" value="${requestScope.searching}"/>
        <c:set var="itemCount" value="${requestScope.itemCount}"/>
        <c:set var="itemType" value="${requestScope.itemType}"/>
        <c:set var="alignment" value="${requestScope.alignment}"/>
        <c:choose>
            <c:when test="${searching == true}">
                <c:choose>
                    <c:when test="${itemCount == 1}">
                        <div class="fw-100 ${alignment} flex-grow">
                        <spring:message code="feed.count.results.1"/>
                        </div>
                    </c:when>
                    <c:when test="${itemCount > 1}">
                        <div class="fw-100 ${alignment} flex-grow">
                        <spring:message code="feed.count.results.many" arguments="${itemCount}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="fw-100 ${alignment} flex-grow">
                        <spring:message code="feed.count.results.none"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${itemCount == 1}">
                        <c:choose>
                            <c:when test="${itemType == 'snippet'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.snippets.1"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'tag'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.tags.1"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'language'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.languages.1"/>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:when test="${itemCount > 1}">
                        <c:choose>
                            <c:when test="${itemType == 'snippet'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.snippets.many" arguments="${itemCount}"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'tag'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.tags.many" arguments="${itemCount}"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'language'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.languages.many" arguments="${itemCount}"/>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${itemType == 'snippet'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.snippets.none"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'tag'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.tags.none"/>
                                </div>
                            </c:when>
                            <c:when test="${itemType == 'language'}">
                                <div class="fw-100 ${alignment} flex-grow">
                                <spring:message code="feed.count.languages.none"/>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        </div>
        </body>
        </html>

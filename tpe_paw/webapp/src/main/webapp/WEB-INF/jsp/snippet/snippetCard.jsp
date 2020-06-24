<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
</head>
<body>
<c:set var="snippet" value="${requestScope.snippet}"/>
<c:set var="currentUser" value="${requestScope.currentUser}"/>

<%--<a href="<c:url value='/snippet/${snippet.id}'/>" class="flex-column card-item border-radius">--%>
<a href="<c:url value='/snippet/${snippet.id}'/>" class="flex-column card-snippet-container border-radius">
    <div class="flex-column card-snippet-content border-radius">

        <!-- Top card section containing icon, username, date and language -->
        <div class="flex-row snippet-upload-spacing">
            <div class="flex-row snippet-user-info">
                <c:if test="${snippet.owner.icon != null}">
                    <img src="<c:url value="/user/${snippet.owner.id}/image"/>" alt="User Icon"/>
                </c:if>
                <c:if test="${snippet.owner.icon == null}">
                    <img src="<c:url value='/resources/images/userIcon.jpg'/>" alt="User Icon"/>
                </c:if>
                <div class="flex-column snippet-upload-info card-snippet-icon">
                    <div class="snippet-text">${snippet.owner.username}</div>
                    <div class="snippet-text card-snippet-date">
                        <fmt:parseDate value="${snippet.dateCreated}" pattern="yyyy-MM-dd" type="date" var="snippetDate"/>
                        <fmt:formatDate value="${snippetDate}" dateStyle="long" type="date" var="formattedDate"/>
                        <c:out value="${formattedDate}"/>
                    </div>
                </div>
            </div>
            <div class="flex-row flex-align">
                <c:set var="favForm" value="favoriteForm${snippet.id}"/>
                <c:url var="snippetFavUrl" value="/snippet/${snippet.id}/fav"/>

                <c:if test="${currentUser != null && !snippet.deleted}">
                    <form:form class="form-container flex-center" action="${snippetFavUrl}" method="post" modelAttribute="${favForm}">
                        <form:checkbox class="hidden" id="fav-button-${snippet.id}" path="favorite" value="true" onclick="updateForm(this)"/>
                        <label for="fav-button-${snippet.id}" class="no-margin">
                            <c:choose>
                                <c:when test="${currentUser.favorites.contains(snippet)}">
                                    <i class="selected-red-icon material-icons card-fav-icon transition">favorite</i>
                                </c:when>
                                <c:otherwise>
                                    <i class="unselected-red-icon material-icons card-fav-icon transition">favorite_border</i>
                                </c:otherwise>
                            </c:choose>
                        </label>
                    </form:form>
                </c:if>
                <c:if test="${snippet.deleted}">
                    <div class="card-warning-icon material-icons">delete_sweep</div>
                </c:if>
                <c:if test="${snippet.flagged}">
                    <div class="card-warning-icon material-icons">warning</div>
                </c:if>
                <div class="flex-center snippet-language-tag border-radius"><c:out value="${snippet.language.name.toUpperCase()}"/> </div>
            </div>
        </div>

        <!-- Bottom   part of the card with the title, description and code -->
        <div class="flex-row snippet-title-container">
            <div class="snippet-text snippet-title"><c:out value="${snippet.title}"/></div>
        </div>
        <c:if test="${!StringUtils.isEmpty(snippet.description)}">
            <div class="card-snippet-block card-snippet-descr-block">
                <div class="snippet-text justify-text"><c:out value="${snippet.description}"/></div>
                <p class="card-snippet-fade-out card-snippet-fade-out-descr hidden"></p>
            </div>
        </c:if>

        <div class="flex-column snippet-code-container border-radius">
            <div class="card-snippet-block">
                <pre><code class="code-element ${snippet.language.name}"><c:out value="${snippet.code}"/></code></pre>
                <p class="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
            </div>
        </div>
    </div>
</a>
</body>
</html>

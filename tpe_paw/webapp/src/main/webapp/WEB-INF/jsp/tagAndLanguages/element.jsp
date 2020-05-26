<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
</head>
<body>
<c:set var="currentUser" value="${requestScope.currentUser}" scope="request"/>
<c:set var="element" value="${requestScope.element}"/>
<c:set var="context" value="${requestScope.context}"/>
<c:set var="style" value="${requestScope.cssClass}"/>
<spring:message code="tooltip.element.empty" var="emptyTooltip"/>
<spring:message code="tooltip.element.follow" var="followTooltip"/>
<spring:message code="tooltip.element.following" var="followingTooltip"/>

    <div class="flex-grow ${style}">
        <div class="flex-row flex-center expand">
            <a href="<c:url value='/${context}/${element.id}'/>" class="no-text-decoration fw-300 element-title flex-grow">
                ${element.name.toUpperCase()}
            </a>
            <c:if test="${context == 'tags' && currentUser != null}">
                <c:set var="followForm" value="followIconForm${element.id}"/>
                <c:url var="tagFollowIconUrl" value="/tags/${element.id}/follow/icon"/>

                <form:form class="form-container flex-center" action="${tagFollowIconUrl}" method="post" modelAttribute="${followForm}">
                    <form:checkbox class="hidden" id="tag-${element.name}" path="follows" value="true" onclick="updateForm(this)"/>
                    <label class="no-margin" for="tag-${element.name}">
                        <c:choose>
                            <c:when test="${currentUser.followedTags.contains(element)}">
                                <div class="material-icons element-icon transition" data-toggle="tooltip" data-placement="left" title="${followingTooltip}">add_circle</div>
                            </c:when>
                            <c:otherwise>
                                <div class="material-icons element-icon transition" data-toggle="tooltip" data-placement="left" title="${followTooltip}">add</div>
                            </c:otherwise>
                        </c:choose>
                    </label>
                </form:form>
            </c:if>
            <c:if test="${element.snippetsUsing.size() == 0}">
                <div class="material-icons element-icon transition" data-toggle="tooltip" data-placement="left" title="${emptyTooltip}">block</div>
            </c:if>
        </div>
    </div>
</body>
</html>

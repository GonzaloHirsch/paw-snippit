<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link href="<c:url value='/resources/css/vote.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/exploreForm.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/errorPages.css'/>" rel="stylesheet"/>
    <script src="<c:url value='/resources/js/form.js'/>"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
</head>
<body>
<spring:message code="advanced.title.hint" var="advanced_title_hint" />
<spring:message code="advanced.username.hint" var="advanced_username_hint" />
<spring:message code="advanced.reputation.hint" var="advanced_reputation_hint" />
<spring:message code="advanced.votes.hint" var="advanced_votes_hint" />
<spring:message code="advanced.date.hint" var="advanced_date_hint" />
<spring:message code="advanced.includeFlagged" var="advanced_include_flagged" />
<div class="flex-column explore-container">
    <form:form class="form-container" action="/explore/search" method="get" modelAttribute="exploreForm">

        <form:label cssClass="flex-stretch fw-500" path="field"><spring:message code="advanced.ordersort"/></form:label>
        <div class="flex-row">
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="field"><spring:message code="advanced.orderby"/></form:label>
                <form:select class="selectpicker flex-grow" path="field">
                    <form:option value="date">
                        <spring:message code="advanced.orderby"/>
                    </form:option>
                    <form:option value="reputation">
                        <spring:message code="advanced.orderby.reputation"/>
                    </form:option>
                    <form:option value="votes">
                        <spring:message code="advanced.orderby.votes"/>
                    </form:option>
                    <form:option value="date">
                        <spring:message code="advanced.orderby.date"/>
                    </form:option>
                </form:select>
            </div>
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="title"><spring:message code="advanced.sortby"/></form:label>
                <form:select class="selectpicker flex-grow" path="sort">
                    <form:option value="asc">
                        <spring:message code="sort.sortby"/>
                    </form:option>
                    <form:option value="asc">
                        <spring:message code="sort.asc"/>
                    </form:option>
                    <form:option value="desc">
                        <spring:message code="sort.desc"/>
                    </form:option>
                </form:select>
            </div>
        </div>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="includeFlagged"><spring:message code="advanced.flagged"/></form:label>
        <div class="flex-row">
            <form:checkbox path="includeFlagged" value="true" label="${advanced_include_flagged}" class="fw-100 advanced-input check-input"/>
        </div>
        <form:errors class="form-error" path="includeFlagged" element="p"/>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="title"><spring:message code="advanced.title"/></form:label>
        <div class="flex-row">
            <form:input path="title" type="text" class="flex-grow fw-100 advanced-input text-input"
                        placeholder="${advanced_title_hint}"/>
        </div>
        <form:errors class="form-error" path="title" element="p"/>

        <hr/>

        <div class="flex-row">
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch fw-500" path="language"><spring:message code="advanced.language"/></form:label>

                <form:select class="selectpicker flex-grow" data-live-search="true" path="language">
                    <form:option value="-1"><spring:message code="advanced.language.hint"/></form:option>
                    <c:forEach items="${languageList}" var="lan" varStatus="status">
                        <form:option value="${lan.id}">${lan.name.toUpperCase()}</form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch fw-500" path="language"><spring:message code="advanced.tag"/></form:label>

                <form:select class="selectpicker flex-grow" data-live-search="true" path="tag">
                    <form:option value="-1"><spring:message code="advanced.tag.hint"/></form:option>
                    <c:forEach items="${tagList}" var="tag" varStatus="status">
                        <form:option value="${tag.id}">${tag.name.toLowerCase()}</form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>
        <form:errors class="form-error" path="tag" element="p"/>
        <form:errors path="language" cssClass="form-error" element="p"/>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="username"><spring:message code="advanced.username"/></form:label>
        <div class="flex-row">
            <form:input path="username" type="text" class="flex-grow fw-100 advanced-input text-input"
                        placeholder="${advanced_username_hint}"/>
        </div>
        <form:errors class="form-error" path="username" element="p"/>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="minDate"><spring:message code="advanced.uploadDate"/></form:label>
        <div class="flex-row">
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="minDate"><spring:message
                        code="advanced.uploadDate.from"/></form:label>
                <form:input path="minDate" type="date" class="flex-grow fw-100 advanced-input date-input"
                            placeholder="${advanced_date_hint}"/>
            </div>
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="maxDate"><spring:message
                        code="advanced.uploadDate.to"/></form:label>
                <form:input path="maxDate" type="date" class="flex-grow fw-100 advanced-input date-input"
                            placeholder="${advanced_date_hint}"/>
            </div>
        </div>
        <form:errors path="minDate" cssClass="form-error" element="p"/>
        <form:errors path="maxDate" cssClass="form-error" element="p"/>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="minRep"><spring:message code="advanced.reputation"/></form:label>
        <div class="flex-row">
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="minRep"><spring:message
                        code="advanced.reputation.from"/></form:label>
                <form:input path="minRep" type="number" class="fw-100 flex-grow advanced-input text-input"
                            placeholder="${advanced_reputation_hint}"/>
            </div>
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="maxRep"><spring:message
                        code="advanced.reputation.to"/></form:label>
                <form:input path="maxRep" type="number" class="fw-100 flex-grow advanced-input text-input"
                            placeholder="${advanced_reputation_hint}"/>
            </div>
        </div>
        <form:errors path="minRep" cssClass="form-error" element="p"/>
        <form:errors path="maxRep" cssClass="form-error" element="p"/>

        <hr/>

        <form:label cssClass="flex-stretch fw-500" path="minVotes"><spring:message code="advanced.votes"/></form:label>
        <div class="flex-row">
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="minVotes"><spring:message
                        code="advanced.votes.from"/></form:label>
                <form:input path="minVotes" type="number" class="flex-grow fw-100 advanced-input text-input"
                            placeholder="${advanced_votes_hint}"/>
            </div>
            <div class="flex-column flex-grow">
                <form:label cssClass="flex-stretch sub-label" path="maxVotes"><spring:message
                        code="advanced.votes.to"/></form:label>
                <form:input path="maxVotes" type="number" class="flex-grow fw-100 advanced-input text-input"
                            placeholder="${advanced_votes_hint}"/>
            </div>
        </div>
        <form:errors path="minVotes" cssClass="form-error" element="p"/>
        <form:errors path="maxVotes" cssClass="form-error" element="p"/>
        <div class="flex-row">
            <button class="flex-grow border-radius advanced-submit-button" type="submit"><spring:message code="search"/></button>
        </div>
    </form:form>
</div>

<!-- Bootstrap -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
</body>
</html>

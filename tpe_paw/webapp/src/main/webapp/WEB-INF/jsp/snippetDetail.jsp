<%--
  Created by IntelliJ IDEA.
  User: IR
  Date: 5/4/2020
  Time: 4:31 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Snippet Detail</title>
</head>
<body>
    <div>
        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Owner</th><th>Code</th><th>Title</th><th>Description</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${snippet.id}</td><td>${snippet.owner}</td><td>${snippet.code}</td><td>${snippet.title}</td><td>${snippet.description}</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div>
        <button disabled="${canVote}">
            VOTE
        </button>
    </div>
</body>
</html>

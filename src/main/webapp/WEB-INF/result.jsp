<%@ page import="ru.itpark.model.QueryModel" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.enumeration.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>TXT Searcher</title>
    <%@ include file="bootstrap-css.jsp" %>
    <style>
        .jumbotron {
            padding: 30px;
            margin-bottom: 5px;
        }

        a:hover {
            text-decoration: none;
        }

        .tabl {
            margin-left: 30px;
            margin-right: 30px;
        }

        button#refresh {
            line-height: 1.0;
            margin-right: 30px;
            margin-bottom: 5px;
            float: right;
        }

        span#query {
            font-size: 17px;
            font-style: oblique;
            font-weight: 500;
        }

        span#status {
            font-style: italic;
            font-size: 13px;
        }

        th {
            font-size: 18px;
        }
    </style>
</head>
<body>
<div class="jumbotron jumbotron-fluid">
    <div class="container">
        <a href="${pageContext.request.contextPath}/"><h1 class="display-4">TXT Searcher</h1></a>
        <p class="lead">This search engine will help you find any line in your files.</p>
    </div>
</div>

<a href="<%= request.getContextPath() %>"><button id="refresh" type="button" class="btn btn-primary">Обновить</button></a>
<div class="tabl">
    <table class="table table-sm">
        <thead>
        <tr>
            <th scope="col" width="70%">Запрос</th>
            <th scope="col" width="30%">Результат</th>
        </tr>
        </thead>
        <tbody>
        <% for (QueryModel queryModel: (List<QueryModel>) request.getAttribute("queries")) {%>
        <tr>
            <td>
                <span id="query"><%= queryModel.getQuery() %><br></span>
                <span id="status">Статус: <% if (queryModel.getStatus() == Status.INPROGRESS) {%>
                                    выполняется...
                                <%} else {%>
                                    выполнено
                                <%}%>
                        </span>
            </td>
            <% if (queryModel.getStatus() == Status.INPROGRESS) { %>
            <td>
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </td>
            <%} else {%>
            <td>
                <a href="<%= request.getContextPath() %>/file/<%= queryModel.getId() + ".txt" %>" download="result.txt">Скачать результат</a>
            </td>
            <%}%>
        </tr>
        <%}%>
        </tbody>
    </table>
</div>
<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
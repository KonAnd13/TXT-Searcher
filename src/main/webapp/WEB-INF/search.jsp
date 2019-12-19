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
        .search {
            margin: 20px;
        }

        .jumbotron {
            padding: 30px;
        }
    </style>
</head>
<body>
<div class="jumbotron jumbotron-fluid">
    <div class="container">
        <h1 class="display-4">TXT Searcher</h1>
        <p class="lead">This search engine will help you find any line in your files.</p>
    </div>
</div>

<form class="search" action="<%= request.getContextPath() %>/query-servlet" method="post" enctype="multipart/form-data">
    <fieldset>
        <div class="form-group">
            <input type="text" name="query" class="form-control" placeholder="Введите поисковый запрос" required>
        </div>
        <div class="form-group">
            <input type="file" id="file" name="file" accept=".txt" multiple required>
        </div>
        <button type="submit" class="btn btn-primary">Поиск</button>
    </fieldset>
</form>

<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
package ru.itpark.servlet;

import ru.itpark.enumeration.Status;
import ru.itpark.exception.DataAccesException;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;
import ru.itpark.repository.QueryRepositorySqliteImpl;
import ru.itpark.service.FileService;
import ru.itpark.service.QueryService;
import ru.itpark.service.QueryServiceImpl;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class QueryServlet extends HttpServlet {
    private QueryService queryService;
    private FileService fileService;

    @Override
    public void init() {
        try {
            var context = new InitialContext();
            fileService = (FileService) context.lookup("java:/comp/env/bean/file-service");
            final DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/db");
            final QueryRepository repository = new QueryRepositorySqliteImpl(dataSource);

            queryService = new QueryServiceImpl(repository);
            queryService.init();
        } catch (NamingException e) {
            throw new DataAccesException();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("queries", queryService.getAllQueries());
        req.getRequestDispatcher(req.getContextPath() + "/result").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = UUID.randomUUID().toString();
        String query = req.getParameter("query");
        QueryModel queryModel = new QueryModel(id, query, Status.INPROGRESS);
        queryService.createQuery(queryModel);
        req.setAttribute("queries", queryService.getAllQueries());
        Collection<Part> parts = req.getParts();

        List<String> names = new LinkedList<>();
        for (Part part : parts) {
            if (part.getSubmittedFileName() != null) {
                fileService.writeFile(part);
                names.add(part.getSubmittedFileName());
            }
        }

        req.getRequestDispatcher(req.getContextPath() + "/result").forward(req, resp);
        queryService.search(queryModel, names);
    }
}

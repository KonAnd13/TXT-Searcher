package ru.itpark.repository;

import lombok.AllArgsConstructor;
import ru.itpark.enumeration.Status;
import ru.itpark.model.QueryModel;
import ru.itpark.util.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

@AllArgsConstructor
public class QueryRepositorySqliteImpl implements QueryRepository {
    private final DataSource dataSource;

    @Override
    public void init() {
        JdbcTemplate.executeInit(
                dataSource,
                "CREATE TABLE IF NOT EXISTS queries (id TEXT PRIMARY KEY, query TEXT NOT NULL, status TEXT NOT NULL);"
        );
    }

    @Override
    public void createQuery(QueryModel queryModel) {
        JdbcTemplate.executeCreateQuery(
                dataSource,
                "INSERT INTO queries (id, query, status) VALUES (?, ?, ?);",
                pstmt -> {
                    int index = 1;
                    pstmt.setString(index++, queryModel.getId());
                    pstmt.setString(index++, queryModel.getQuery());
                    pstmt.setString(index, String.valueOf(queryModel.getStatus()));
                }
        );
    }

    @Override
    public void updateQuery(QueryModel queryModel) {
        JdbcTemplate.executeCreateQuery(
                dataSource,
                "UPDATE queries SET status = ? WHERE id = ?",
                pstmt -> {
                    int index = 1;
                    pstmt.setString(index++, String.valueOf(queryModel.getStatus()));
                    pstmt.setString(index, queryModel.getId());
                }
        );
    }

    @Override
    public List<QueryModel> getAllQueries() {
        return JdbcTemplate.executeQuery(
                dataSource,
                "SELECT id, query, status FROM queries",
                rs -> new QueryModel(
                        rs.getString("id"),
                        rs.getString("query"),
                        Status.valueOf(rs.getString("status"))
                )
        );
    }
}

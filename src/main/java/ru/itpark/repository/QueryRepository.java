package ru.itpark.repository;

import ru.itpark.model.QueryModel;
import java.util.List;

public interface QueryRepository {
    void init();
    void createQuery(QueryModel queryModel);
    void updateQuery(QueryModel queryModel);
    List<QueryModel> getAllQueries();
}

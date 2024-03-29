package ru.itpark.service;

import ru.itpark.model.QueryModel;
import java.util.List;

public interface QueryService {
    void init();
    void createQuery(QueryModel queryModel);
    void updateQuery(QueryModel queryModel);
    List<QueryModel> getAllQueries();
    void search(QueryModel queryModel, List<String> names);
}

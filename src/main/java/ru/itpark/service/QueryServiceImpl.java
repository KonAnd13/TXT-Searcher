package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.constants.Constants;
import ru.itpark.enumeration.Status;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {
    private final QueryRepository repository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void init() {
        repository.init();
    }

    @Override
    public void createQuery(QueryModel queryModel) {
        repository.createQuery(queryModel);
    }

    @Override
    public void updateQuery(QueryModel queryModel) {
        repository.updateQuery(queryModel);
    }

    @Override
    public List<QueryModel> getAllQueries() {
        return repository.getAllQueries();
    }

    @Override
    public void search(QueryModel queryModel, List<String> names) {
        executorService.execute(() -> {
            try (BufferedWriter writer = Files.newBufferedWriter(Constants.PATH_RESULT_DIRECTORY.resolve(queryModel.getId() + ".txt"), StandardOpenOption.CREATE)) {
                for (String name : names) {
                    try (BufferedReader reader = Files.newBufferedReader(Constants.PATH_UPLOAD_DIRECTORY.resolve(name))) {
                        while (reader.ready()) {
                            String line = reader.readLine();
                            if (line.toLowerCase().contains(queryModel.getQuery().toLowerCase())) {
                                writer.write("[" + name + "]: " + line + "\n");
                            }
                        }
                    }
                }
                queryModel.setStatus(Status.DONE);
                updateQuery(queryModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

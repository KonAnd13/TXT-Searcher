package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.itpark.constants.Constants;
import ru.itpark.enumeration.Status;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Setter
public class QueryServiceImpl implements QueryService, Runnable {
    private final QueryRepository repository;
    private final FileService fileService;
    private QueryModel queryModel;
    private Collection<Part> parts;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

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
    public void search(QueryModel queryModel, Collection<Part> parts) {
        this.setQueryModel(queryModel);
        this.setParts(parts);
        executorService.execute(this);
        executorService.shutdown();
    }

    @Override
    public void run() {
        try (BufferedWriter writer = Files.newBufferedWriter(Constants.PATH_RESULT_DIRECTORY.resolve(queryModel.getId() + ".txt"), StandardOpenOption.CREATE)) {
            for (Part part : parts) {
                if (part.getSubmittedFileName() != null) {
                    Path pathUploadFile = fileService.writeFile(part);
                    try (BufferedReader reader = Files.newBufferedReader(pathUploadFile)) {
                        while (reader.ready()) {
                            String line = reader.readLine();
                            if (line.toLowerCase().contains(queryModel.getQuery().toLowerCase())) {
                                writer.write("[" + part.getSubmittedFileName() + "]: " + line + "\n");
                            }
                        }
                    }
                }
            }
            queryModel.setStatus(Status.DONE);
            updateQuery(queryModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.constants.Constants;
import ru.itpark.enumeration.Status;
import ru.itpark.exception.DataAccesException;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {
    private final QueryRepository repository;
    private final FileService fileService;

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
        try {
            if (!Files.exists(Constants.PATH_RESULT_DIRECTORY)) {
                Files.createDirectory(Constants.PATH_RESULT_DIRECTORY);
            }
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
            }
            queryModel.setStatus(Status.DONE);
            updateQuery(queryModel);
        } catch (IOException e) {
            throw new DataAccesException();
        }
    }
}

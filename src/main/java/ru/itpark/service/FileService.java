package ru.itpark.service;

import ru.itpark.constants.Constants;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;

public class FileService {
    public FileService() throws IOException {
        Files.createDirectories(Constants.PATH_UPLOAD_DIRECTORY);
        Files.createDirectories(Constants.PATH_RESULT_DIRECTORY);
    }

    public void readFile(String id, ServletOutputStream os) throws IOException {
        var path = Constants.PATH_RESULT_DIRECTORY.resolve(id);
        Files.copy(path, os);
    }

    public void writeFile(Part part) throws IOException {
        part.write(Constants.PATH_UPLOAD_DIRECTORY.resolve(part.getSubmittedFileName()).toString());
    }
}

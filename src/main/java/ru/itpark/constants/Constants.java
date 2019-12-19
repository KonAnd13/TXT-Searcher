package ru.itpark.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {
    public final static Path PATH_UPLOAD_DIRECTORY = Paths.get(System.getenv("UPLOAD_PATH"));
    public final static Path PATH_RESULT_DIRECTORY = Paths.get(System.getenv("RESULT_PATH"));
}

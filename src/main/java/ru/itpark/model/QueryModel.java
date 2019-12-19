package ru.itpark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.itpark.enumeration.Status;

@AllArgsConstructor
@Data
public class QueryModel {
    private String id;
    private String query;
    private Status status;
}

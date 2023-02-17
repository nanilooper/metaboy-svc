package app.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class Filters {

    private String filterKey;

    private List<String> filterValues;

    private boolean exclude;
}

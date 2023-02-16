package app.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {

   String continuationToken;

    String collectionId;

    List<Filters> filters;
}


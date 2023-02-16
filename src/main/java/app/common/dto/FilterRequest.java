package app.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {

    String collectionId;

    List<Filters> filters;

    int pageSize;

    String sortBy;

    String sortOrder;

    boolean fetchGmePrices;

    String continuationToken;


}


package app.cosmos.repository;

import app.common.dto.PagedResult;

public interface DocumentDAO<T> {

    PagedResult<T> queryDocuments(String query, Class<T> typeParameterClass, String collectionName,
                                  String continuationToken,int PageSize, Object... queryParams);

}

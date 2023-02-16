package app.cosmos.repository;

import app.common.dto.PagedResult;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.FeedResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class CustomDAOImpl<T> implements DocumentDAO<T> {

    @Autowired
    private CosmosClient cosmosClient;


    @Override
    public PagedResult<T> queryDocuments(String query, Class<T> typeParameterClass, String collectionName,
                                         String continuationToken,int pageSize, Object... queryParams) {
        if (StringUtils.isEmpty(query)) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(queryParams)) {
            query = String.format(query, queryParams,continuationToken);
        }
        CosmosQueryRequestOptions feedOptions = new CosmosQueryRequestOptions();
        List<T> queryResult = new ArrayList<>();
        Iterable<FeedResponse<T>> feedResponseIterator = getContainer(collectionName)
                .queryItems(query, feedOptions, typeParameterClass).iterableByPage(continuationToken,pageSize);
        if (feedResponseIterator.iterator().hasNext()){
            FeedResponse<T> page = feedResponseIterator.iterator().next();
            queryResult.addAll(page.getResults());
            continuationToken = page.getContinuationToken();
        }
        return new PagedResult<>(queryResult,continuationToken);
    }


    private CosmosContainer getContainer(String collectionName) {
        return cosmosClient.getDatabase("metaboy").getContainer(collectionName);
    }

}

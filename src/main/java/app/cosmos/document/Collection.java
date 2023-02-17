package app.cosmos.document;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Container(containerName = "collection")
@Data
public class Collection {

    @Id
    @PartitionKey
    private String collectionId;

    private String name;

    private String description;

    private List<Trait> availableFilters;

    @Data
    public static class Trait {
        private String traitType;
        private String traitValue;
        private int count;
    }
}

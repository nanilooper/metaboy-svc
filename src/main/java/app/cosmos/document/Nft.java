package app.cosmos.document;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.HashMap;


@Container(containerName = "nft")
@Data
public class Nft {

    @Id
    private String nftId;

    @PartitionKey
    private String collectionId;

    private String name;

    private int rank;

    private BigDecimal price ;

    private HashMap<String, String> properties;

}

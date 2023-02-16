package app.cosmos.repository;

import app.cosmos.document.Collection;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends CosmosRepository<Collection,String> {}

package app.cosmos.repository;

import app.cosmos.document.Nft;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NftRepository extends CosmosRepository<Nft,String> {}

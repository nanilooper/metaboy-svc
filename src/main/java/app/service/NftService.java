package app.service;

import app.common.dto.FilterRequest;
import app.common.dto.Filters;
import app.common.dto.PagedResult;
import app.cosmos.document.Collection;
import app.cosmos.document.Nft;
import app.cosmos.repository.CollectionRepository;
import app.cosmos.repository.CustomDAOImpl;
import app.cosmos.repository.NftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NftService {


    private CollectionRepository collectionRepository;

    private NftRepository nftRepository;

    private CustomDAOImpl<Nft> nftCustomDao;

    @Autowired
    public void setCollectionRepository(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Autowired
    public void setNftRepository(NftRepository nftRepository) {
        this.nftRepository = nftRepository;
    }

    @Autowired
    public void setNftCustomDao(CustomDAOImpl<Nft> nftCustomDao) {
        this.nftCustomDao = nftCustomDao;
    }

    public List<Collection> getAllCollections(){
        return (List<Collection>) collectionRepository.findAll();
    }

    public Collection createCollection(Collection collection){
        return collectionRepository.save(collection);
    }

    public Nft createNft(Nft nft){
        return nftRepository.save(nft);
    }

    public PagedResult<Nft> filterNfts(FilterRequest filterRequest){
        String Query = constructQuery(filterRequest.getCollectionId(), filterRequest.getFilters());
        PagedResult<Nft> nfts = nftCustomDao.queryDocuments(Query,Nft.class,"nft",filterRequest.getContinuationToken(),5);
        return nfts;
    }

    private String constructQuery(String collectionId, List<Filters> filters){
        String queryBase = "select * from nft where  nft.collectionId = " + String.format("'%s'", collectionId) +
                dynamicFilterQuery(filters) + " order by nft.rank asc";
        return queryBase;
    }

    private String dynamicFilterQuery(List<Filters> filters){
        StringBuilder result = new StringBuilder();
        for (Filters filter : filters){
            List<String> filerValues = filter.getFilterValues();
            List<String> values = filerValues.stream()
                    .map(x -> String.format("'%s'", x))
                    .collect(Collectors.toList());
            String inclusion = filter.isExclude() ? " NOT IN " : " IN ";
            String valuesString = "( " + String.join("," , values) + " )";
            result.append(" AND nft.properties['").append(filter.getFilterKey()).append("']").append(inclusion).append(valuesString);
        }
        return result.toString();
    }




}

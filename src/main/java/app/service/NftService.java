package app.service;

import app.client.GmeClient;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NftService {

    private CollectionRepository collectionRepository;

    private NftRepository nftRepository;

    private CustomDAOImpl<Nft> nftCustomDao;

    private GmeClient gmeClient;

    @Autowired
    public void setGmeClient(GmeClient gmeClient) {
        this.gmeClient = gmeClient;
    }

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

    public Collection getAllCollection(String collectionId){
        Optional<Collection> optional = collectionRepository.findById(collectionId);
        return optional.orElse(null);
    }

    public Collection createCollection(Collection collection){
        return collectionRepository.save(collection);
    }

    public Nft createNft(Nft nft){
        return nftRepository.save(nft);
    }

    public PagedResult<Nft> filterNfts(FilterRequest filterRequest){
        if (filterRequest.getCollectionId() == null){
            return null;
        }
        filterRequest.setPageSize(Math.min(filterRequest.getPageSize(), 48));
        String Query = constructQuery(filterRequest);
        PagedResult<Nft> nfts = nftCustomDao.queryDocuments(Query,Nft.class,"nft",
                filterRequest.getContinuationToken(),filterRequest.getPageSize());
        Set<String> nftIds = nfts.getResults()
                .stream()
                .map(Nft::getNftId)
                .collect(Collectors.toSet());
        if (filterRequest.isFetchGmePrices()){
            try {
                Map<String, BigDecimal> pricesMap = gmeClient.getPriceMap(nftIds);
                for (Nft nft: nfts.getResults()){
                    nft.setPrice(pricesMap.get(nft.getNftId()).doubleValue());
                }
            }catch (Exception ignored){

            }
        }
        return nfts;
    }

    private String constructQuery(FilterRequest filterRequest){
        String collectionId = filterRequest.getCollectionId();
        List<Filters> filters = filterRequest.getFilters();
        String sortBy = filterRequest.getSortBy() != null && filterRequest.getSortBy().equals("rank") ?
                " order by nft.rank " : "";
        String sortOrder =  "";
        if (!sortBy.isEmpty()){
            if (filterRequest.getSortOrder() != null && filterRequest.getSortOrder().equalsIgnoreCase("desc") ){
                sortOrder = "desc";
            }else {
                sortOrder = "asc";
            }
        }
        return "select * from nft where  nft.collectionId = " + String.format("'%s'", collectionId) +
                dynamicFilterQuery(filters) + sortBy + sortOrder;
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

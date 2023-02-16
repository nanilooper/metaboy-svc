package app.api;

import app.common.dto.FilterRequest;
import app.common.dto.PagedResult;
import app.cosmos.document.Collection;
import app.cosmos.document.Nft;
import app.service.NftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metaboy-svc")
public class NftController {

    @Autowired
    private NftService bizLogic;

    @GetMapping("/healthCheck")
    public String healthCheck(){
        return "running";
    }

    @PostMapping("/collection")
    public Collection addCollection(@RequestBody Collection collection){
        return bizLogic.createCollection(collection);
    }

    @PostMapping("/nft")
    public Nft addNft(@RequestBody Nft nft){
        return bizLogic.createNft(nft);
    }


    @PostMapping("/filterNfts")
    public PagedResult<Nft> filterNfts(@RequestBody FilterRequest filterRequest){
        return bizLogic.filterNfts(filterRequest);
    }

    @GetMapping("/collection/getAll")
    public Iterable<Collection> getAllCollections(){
        return bizLogic.getAllCollections();
    }

}

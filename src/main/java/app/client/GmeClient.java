package app.client;

import app.common.dto.GmePriceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GmeClient {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public GmeClient() {
        this.webClient = WebClient
                .builder()
                .baseUrl("https://api.nft.gamestop.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public Map<String,BigDecimal> getPriceMap(Set<String> nftIds){
        String uri = "/nft-svc-marketplace/getNftDetailRecord?nftId=" + String.join(",",nftIds);
        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(uri)
                .retrieve();
        String responseBody = responseSpec.bodyToMono(String.class).block();
        try {
            Map<String, GmePriceInfo> pricesMap =  objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            return pricesMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey(),
                            e -> e.getValue().getLoopringSaleInfo() != null ?
                                    converWeiToEth(e.getValue().getLoopringSaleInfo().getMinPricePerNftInWei())
                                    : new BigDecimal("-1"))
                    );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private BigDecimal converWeiToEth(String wei){
        BigDecimal weiB = new BigDecimal(wei);
        BigDecimal con = new BigDecimal("1000000000000000000");
        return weiB.divide(con,3, RoundingMode.HALF_UP);
    }
}

package app.common.dto;

import lombok.Data;

@Data
public class GmePriceInfo {

    String mintPricePerNft;

    String amountForSale;

    LoopringSaleInfo loopringSaleInfo;

    int likeCount;

    @Data
    public static class LoopringSaleInfo {

        String minPricePerNftInWei;

        String amountForSale;
    }
}

package edu.navalkishoreb.cards.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardsDto {
    private String mobileNumber;
    private String cardNumber;
    private String cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
}

package edu.navalkishoreb.cards.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {

    private String statusCode;
    private String statusMsg;
}

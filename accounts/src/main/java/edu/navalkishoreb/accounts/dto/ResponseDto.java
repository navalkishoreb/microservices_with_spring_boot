package edu.navalkishoreb.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold response status"
)
public class ResponseDto {

    @Schema(description = "Status code of the response")
    private String statusCode;

    @Schema(description = "Status message of the response")
    private String statusMsg;
}

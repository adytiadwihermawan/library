package com.miniproject.library.dto.penalty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PenaltyResponse {
    private Integer id;
    private Integer loanId;
    private String name;
    private String description;
    private Integer amount;
}

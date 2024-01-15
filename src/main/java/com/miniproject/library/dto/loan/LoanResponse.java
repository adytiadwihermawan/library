package com.miniproject.library.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class LoanResponse {
    private Integer id;
    private Date dateBorrow;
    private Date dateReturn;
    private Date dueBorrow;
    private Integer bookCartId;
    private Integer overdueFine;
}

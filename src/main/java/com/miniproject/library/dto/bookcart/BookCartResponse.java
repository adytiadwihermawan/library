package com.miniproject.library.dto.bookcart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCartResponse {
    private Integer id;
    private Integer bookIds;
    private Integer anggotaId;
}

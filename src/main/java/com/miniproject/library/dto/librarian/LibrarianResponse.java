package com.miniproject.library.dto.librarian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibrarianResponse {
    private Integer id;
    private Long nip;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
}

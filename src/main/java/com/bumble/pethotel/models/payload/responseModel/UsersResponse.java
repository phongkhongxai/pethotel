package com.bumble.pethotel.models.payload.responseModel;


import com.bumble.pethotel.models.payload.dto.UserDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UsersResponse {
    private List<UserDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

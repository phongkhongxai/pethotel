package com.bumble.pethotel.models.payload.responseModel;

import com.bumble.pethotel.models.payload.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentsResponse {
    private List<PaymentDto> content;
    private double totalRevenue;
    private double commission;
    private double premium;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

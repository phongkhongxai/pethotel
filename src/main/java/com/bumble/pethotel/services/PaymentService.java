package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.PaymentDto;
import com.bumble.pethotel.models.payload.responseModel.PaymentsResponse;
import vn.payos.type.CheckoutResponseData;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    CheckoutResponseData createPaymentLink(Long bookingId, String returnUrl, String cancelUrl);
    CheckoutResponseData createPaymentLinkForSubscription(Long userId,int price, String returnUrl, String cancelUrl);

    PaymentsResponse getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir);
    PaymentsResponse getAllPaymentsForSubscription(int pageNo, int pageSize, String sortBy, String sortDir);

    PaymentsResponse getAllSuccessPaymentsOfShop(Long shopId,int pageNo, int pageSize, String sortBy, String sortDir);
    PaymentDto getPaymentById(Long id);

    String updateStatusPayment(Long id, String status);

}

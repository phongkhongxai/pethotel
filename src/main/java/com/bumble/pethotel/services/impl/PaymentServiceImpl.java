package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.PaymentDto;
import com.bumble.pethotel.models.payload.responseModel.PaymentsResponse;
import com.bumble.pethotel.repositories.*;
import com.bumble.pethotel.services.PaymentService;
import com.bumble.pethotel.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PayOS payOS;
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Booking booking = bookingRepository.findById(paymentDto.getBookingId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "Booking not found"));


        Payment payment = Payment.builder()
                .booking(booking)
                .amount(paymentDto.getAmount())
                .date(LocalDateTime.now())
                .method(paymentDto.getMethod())
                .status("PENDING")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    /*@Override
    public void handlePaymentWebhook(Map<String, String> requestBody) {
        try {
            WebhookData webhookData = payOS.verifyPaymentWebhookData(requestBody);
            long orderCode = webhookData.getOrderCode();
            String status = webhookData.getDesc() ? "COMPLETED" : "FAILED";

            // Cập nhật trạng thái thanh toán trong database
            Payment payment = paymentRepository.findById(orderCode)
                    .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "Payment not found"));
            payment.setStatus(status);
            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new PetApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Webhook processing failed");
        }
    }*/
    @Override
    public CheckoutResponseData createPaymentLink(Long bookingId, String returnUrl, String cancelUrl) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        // Gọi API để tạo yêu cầu thanh toán với PayOS
        String description = "Thanh toán đơn hàng #" + bookingId;

        String currentTimeString = String.valueOf(new Date().getTime());
        long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
        ItemData item = ItemData.builder()
                .name("Booking #" + bookingId)
                .quantity(1)
                .price((int) booking.getTotalPrice())
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount((int) booking.getTotalPrice())
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .item(item)
                .build();

        try {
            CheckoutResponseData checkoutData = payOS.createPaymentLink(paymentData);
            String qrCodeUrl = checkoutData.getCheckoutUrl();

            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
            payment.setStatus("PENDING");
            payment.setMethod("PAYOS");
            payment.setQrCodeUrl(qrCodeUrl);
            payment.setOrderCode(orderCode);
            payment.setDate(LocalDateTime.now());
            paymentRepository.save(payment);
            return checkoutData;
        } catch (Exception e) {
            throw new PetApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi tạo mã QR-Pay");
        }
    }

    @Override
    public CheckoutResponseData createPaymentLinkForSubscription(Long userId,int price, String returnUrl, String cancelUrl) {
         User user = userRepository.findById(userId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User not found"));
        Booking booking = bookingRepository.findById(1L)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        // Gọi API để tạo yêu cầu thanh toán với PayOS
        String description = "Thanh toán Subscription";

        String currentTimeString = String.valueOf(new Date().getTime());
        long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
        ItemData item = ItemData.builder()
                .name("Sub #" + 1)
                .quantity(1)
                .price(price)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(price)
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .item(item)
                .build();

        try {
            CheckoutResponseData checkoutData = payOS.createPaymentLink(paymentData);
            String qrCodeUrl = checkoutData.getCheckoutUrl();

            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(price);
            payment.setStatus("PENDING");
            payment.setMethod("PREMIUM");
            payment.setQrCodeUrl(qrCodeUrl);
            payment.setOrderCode(orderCode);
            payment.setUserId(user.getId());
            payment.setDate(LocalDateTime.now());
            paymentRepository.save(payment);
            return checkoutData;
        } catch (Exception e) {
            throw new PetApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi tạo mã QR-Pay");
        }
    }


    @Override
    public PaymentsResponse getAllPayments(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Payment> payments = paymentRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Payment> listOfPayment = payments.getContent();

        List<PaymentDto> content = listOfPayment.stream().map(bt -> modelMapper.map(bt, PaymentDto.class)).collect(Collectors.toList());

        PaymentsResponse templatesResponse = new PaymentsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(payments.getNumber());
        templatesResponse.setPageSize(payments.getSize());
        templatesResponse.setTotalElements(payments.getTotalElements());
        templatesResponse.setTotalPages(payments.getTotalPages());
        templatesResponse.setLast(payments.isLast());
        Double totalRevenue = paymentRepository.calculateTotalRevenueForSystem();
        templatesResponse.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        Double commission = paymentRepository.calculateTotalCommissionForSystem();
        templatesResponse.setCommission(commission != null ? commission : 0.0);
        Double premium = paymentRepository.calculatePremiumForSystem();
        templatesResponse.setPremium(premium != null ? premium : 0.0);
        


        return templatesResponse;
    }

    @Override
    public PaymentsResponse getAllPaymentsForSubscription(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Payment> payments = paymentRepository.findAllPremiumNotDeleted(pageable);

        // get content for page object
        List<Payment> listOfPayment = payments.getContent();

        List<PaymentDto> content = listOfPayment.stream().map(bt -> modelMapper.map(bt, PaymentDto.class)).collect(Collectors.toList());

        PaymentsResponse templatesResponse = new PaymentsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(payments.getNumber());
        templatesResponse.setPageSize(payments.getSize());
        templatesResponse.setTotalElements(payments.getTotalElements());
        templatesResponse.setTotalPages(payments.getTotalPages());
        templatesResponse.setLast(payments.isLast());
        Double premium = paymentRepository.calculatePremiumForSystem();
        templatesResponse.setPremium(premium != null ? premium : 0.0);


        return templatesResponse;
    }

    @Override
    public PaymentsResponse getAllSuccessPaymentsOfShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "Shop not found"));
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Payment> payments = paymentRepository.findPaymentsByShop(shop.getId(),pageable);

        // get content for page object
        List<Payment> listOfPayment = payments.getContent();

        List<PaymentDto> content = listOfPayment.stream().map(bt -> modelMapper.map(bt, PaymentDto.class)).collect(Collectors.toList());

        PaymentsResponse templatesResponse = new PaymentsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(payments.getNumber());
        templatesResponse.setPageSize(payments.getSize());
        templatesResponse.setTotalElements(payments.getTotalElements());
        templatesResponse.setTotalPages(payments.getTotalPages());
        templatesResponse.setLast(payments.isLast());
        Double totalRevenue = paymentRepository.calculateTotalRevenueByShop(shop.getId());
        templatesResponse.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        Double commission = paymentRepository.calculateCommissionByShop(shop.getId());
        templatesResponse.setCommission(commission != null ? commission : 0.0);


        return templatesResponse;
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if(payment.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Payment not found with id: "+ id);

        }
        return modelMapper.map(payment.get(),PaymentDto.class);
    }

    @Override
    public String updateStatusPayment(Long id, String status) {
        Optional<Payment> payment = paymentRepository.findByOrderCode(id);
        if(payment.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Payment not found with orderCode: "+ id);

        }
        Payment payment1 = payment.get();
        payment1.setStatus(status);
        paymentRepository.save(payment1);
        if (status.equalsIgnoreCase("SUCCESS")) {
            if (payment1.getMethod().equalsIgnoreCase("PREMIUM")){
                userService.activatePremium(payment1.getUserId(), 1);
            }else{
                // Tìm Booking liên quan đến Payment
                Optional<Booking> booking = bookingRepository.findByPayments_OrderCode(payment1.getOrderCode());
                if (booking.isEmpty()) {
                    throw new PetApiException(HttpStatus.NOT_FOUND, "Booking not found for orderCode: " + id);
                }

                Booking booking1 = booking.get();
                booking1.setStatus("COMPLETED"); // Hoặc trạng thái thích hợp cho Booking
                bookingRepository.save(booking1);

                // Tìm Room liên quan đến Booking và cập nhật trạng thái Room thành "occupied"
                Room room = booking1.getRoom();
                if (room != null) {
                    room.setStatus("occupied");
                    roomRepository.save(room);
                }
            }
        }


        return "Update payment to "+ status +" successfully.";
    }


}

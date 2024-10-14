package com.bumble.pethotel;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class SignatureGenerator {

    public static void main(String[] args) {
        // Dữ liệu giao dịch
        Map<String, String> data = new TreeMap<>();
        data.put("orderCode", "123");
        data.put("amount", "3000");
        data.put("description", "VQRIO123");
        data.put("accountNumber", "12345678");
        data.put("reference", "TF230204212323");
        data.put("transactionDateTime", "2023-02-04 18:25:00");
        data.put("currency", "VND");
        data.put("paymentLinkId", "124c33293c43417ab7879e14c8d9eb18");
        data.put("code", "00");
        data.put("desc", "Thành công");
        data.put("counterAccountBankId", "");
        data.put("counterAccountBankName", "");
        data.put("counterAccountName", "");
        data.put("counterAccountNumber", "");
        data.put("virtualAccountName", "");
        data.put("virtualAccountNumber", "");

        // Key dùng để tạo chữ ký
        String checksumKey = "142b85085f3639fab02a1f1045b0e52d3de6daf58aabcc1f2a3e857f9efca985";

        try {
            String signature = generateSignature(data, checksumKey);
            System.out.println("Signature: " + signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateSignature(Map<String, String> data, String checksumKey) throws Exception {
        // Tạo chuỗi dữ liệu theo định dạng key=value
        StringBuilder dataStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (dataStringBuilder.length() > 0) {
                dataStringBuilder.append("&");
            }
            dataStringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }

        String dataString = dataStringBuilder.toString();
        System.out.println("Data String: " + dataString);

        // Tạo chữ ký HMAC_SHA256
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hmacResult = mac.doFinal(dataString.getBytes(StandardCharsets.UTF_8));

        // Chuyển đổi byte array thành chuỗi hexa
        StringBuilder hexString = new StringBuilder();
        for (byte b : hmacResult) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

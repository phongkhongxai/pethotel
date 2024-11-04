package com.bumble.pethotel.models.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdated {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String bankName;
    private String accountNumber;
    private List<MultipartFile> files;
}

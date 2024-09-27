package com.bumble.pethotel.models.payload.requestModel;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedRequest {
    private String username;
    private String email;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String address;
    private String gender;
    @Pattern(regexp="(^$|[0-9]{10,11})", message = "Phone number must be 10 or 11 digits!")
    private String phone;

}

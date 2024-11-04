package com.bumble.pethotel.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileDto {
    private Long id;
    private String url;
    private String createdAt;
}

package com.bumble.pethotel.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "image_files")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private Pet pet;
    private LocalDateTime createdAt;

}


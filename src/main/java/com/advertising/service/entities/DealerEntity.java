package com.advertising.service.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LM_Dealer")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealerEntity {
    @Id
    @Column(name="dealerId", unique = true, nullable = false,insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="name", nullable = false)
    String name;

    @Column(name = "dealerPublishedListingsLimit", nullable = false)
    int limitOfPublishedListings;

    @Column(name="email", nullable = false, unique = true)
    String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dealer", cascade = CascadeType.REMOVE)
    List<ListingEntity> listings;

    @CreatedDate
    @Column(name = "createdAt", insertable = true, updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", insertable = false, updatable = true)
    LocalDateTime updatedAt;
}

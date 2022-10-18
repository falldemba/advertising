package com.advertising.service.entities;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.advertising.service.models.State;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LM_Listing")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingEntity {
    @Id
    @Column(name="listingId", unique = true, nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dealer_id", insertable=false, updatable=false)
    @EqualsAndHashCode.Include
    @OnDelete(action = OnDeleteAction.CASCADE)
    DealerEntity dealer;

    int dealer_id;

    @Column(name = "vehicle", nullable = false)
    String vehicle;

    @Column(name = "price", nullable = false)
    Long price;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    State state;

    @CreatedDate
    @Column(name = "createdAt", insertable = true, updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", insertable = false, updatable = true)
    LocalDateTime updatedAt;
}
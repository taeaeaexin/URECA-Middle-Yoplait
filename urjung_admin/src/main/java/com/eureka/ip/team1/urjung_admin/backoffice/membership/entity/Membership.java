package com.eureka.ip.team1.urjung_admin.backoffice.membership.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "membership")
public class Membership {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID membershipId;

    @Column(name = "membership_name", nullable = false)
    private String membershipName;

    @Column(name = "require_amount", nullable = false)
    private int require;

    @Column(name = "gift_discount", nullable = false)
    private Double giftDiscount;

    // user
    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();
}

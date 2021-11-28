package com.fastcampus.housebatch.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AptNotification extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptNotificationId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 5)
    private String guLawdCd;

    @Column(nullable = false)
    private boolean enabled;
}

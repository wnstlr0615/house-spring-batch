package com.fastcampus.housebatch.core.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class AptNotification extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptNotificationId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 5)
    private String guLawdCd;

    @Column(nullable = false)
    private boolean enabled;
}

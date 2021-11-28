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
public class Lawd extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lawdId;

    @Column(nullable = false, unique = true, length = 10)
    private String lawdCd;

    @Column(nullable = false, length = 100)
    private String lawdDong;

    @Column(nullable = false)
    private boolean exist;

}

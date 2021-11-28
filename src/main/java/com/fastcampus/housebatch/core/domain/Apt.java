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
public class Apt extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptId;

    @Column(nullable = false, length = 40)
    private String aptName;

    @Column(nullable = false, length = 20)
    private String jibun;

    @Column(nullable = false, length = 40)
    private String dong;

    @Column(nullable = false, unique = true, length = 5)
    private String guLawdCd;

    @Column(nullable = false)
    private int builtYear;
}

package com.fastcampus.housebatch.core.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Lawd extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lawdId;

    @Column(nullable = false, unique = true, length = 10)
    private String lawdCd;

    @Column(nullable = false, length = 100)
    private String lawdDong;

    @Column(nullable = false)
    private Boolean exist;

    public void update(Lawd lawd) {
        this.lawdDong=lawd.getLawdDong();
        this.exist= lawd.getExist();
    }
}

##  스프링 배치를 활용한 아파트 실거래가 알림

---

지역 코드 아파트 실거래 API를 통해서 해당 지역에 특정 일자에 아파트 거래 내역을 
 사용자 에게 메일을 통해 알림을 보내주는 기능을 수행

※ 해당 프로젝트에서 메일 전송의 기능은 SendService 인터페이스를 구현한 FakeSendService 에서 
로그를 남겨서 처리

## Spring Batch 활용
1. txt 파일에 나열된 지역이름과 지역 코드를 db에 저장(LawdInsertJobConfig)
2. 구코드와 년월을 사용하여 실거래 API로 부터 데이터를 파싱해서 아파트 정보와 실거래 정보를 DB에 저장(AptDealInsertJobConfig)
3. 이메일과 지역코드가 설정 되어 있으면 날짜에 맞춰 각 사용자에게 각 설정한 지역코드 관련한 실거래 API 정보를 메일로 전송

 
##  기술 스택 

spring boot, docker, jpa, mysql, spring batch 

## 관련 API 
[국토 교통부 아파트 매매 실거래 자료](https://www.data.go.kr/iim/api/selectAPIAcountView.do)
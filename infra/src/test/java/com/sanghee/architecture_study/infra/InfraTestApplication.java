package com.sanghee.architecture_study.infra;

import org.springframework.boot.autoconfigure.SpringBootApplication;

// infra 모듈에는 실제 @SpringBootApplication 클래스가 없다(그건 bootstrap 모듈에만 있음).
// @DataJpaTest 같은 슬라이스 테스트가 컨텍스트를 부트스트랩할 때 이 클래스를 찾아 쓴다.
@SpringBootApplication
class InfraTestApplication {
}

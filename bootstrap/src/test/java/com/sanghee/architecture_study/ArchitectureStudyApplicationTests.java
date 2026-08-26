package com.sanghee.architecture_study;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// 로컬에 미리 떠있는 Postgres나 수동 프로필 활성화(SPRING_PROFILES_ACTIVE=local)에
// 의존하지 않도록 Testcontainers로 바꿨다. 이래야 CI처럼 아무 것도 미리 준비되지
// 않은 환경에서도 ./gradlew test 한 번으로 통과한다.
@SpringBootTest
@Testcontainers
class ArchitectureStudyApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	@Test
	void contextLoads() {
	}

}

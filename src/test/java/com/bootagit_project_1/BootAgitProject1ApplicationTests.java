package com.bootagit_project_1;

import com.bootagit_project_1.jwt.JwtToken;
import com.bootagit_project_1.user.controller.UserController;
import com.bootagit_project_1.user.entity.LoginDto;
import com.bootagit_project_1.user.entity.RegisterDto;
import com.bootagit_project_1.user.entity.UserDto;
import com.bootagit_project_1.user.service.UserService;
import com.bootagit_project_1.utils.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class BootAgitProject1ApplicationTests {

    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    UserService userService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;

    private RegisterDto registerDto;
    @Autowired
    private UserController userController;

    // 각 테스트 케이스 전에 실행되어 회원 등록을 위한 registerDto 객체 생성
    @BeforeEach
    void beforeEach(){
        registerDto = RegisterDto.builder()
                .username("test_user")
                .password("12345678")
                .email("test_user@gmail.com")
                .build();
    }

    // 각 테스트 케이스 후에 실행되어 DB를 정리하기 위해 모든 엔티티 삭제
    @AfterEach
    void afterEach(){

//        databaseCleanUp.truncateAllEntity();
    }

    // 회원을 등록하고 저장된 회원 객체의 username, email이 예상값과 일치하는지 확인
    @Test
    public void registerTest(){
        // API 요청 설정
        String url = "http://localhost:" + randomServerPort + "/api/user/register";
        ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity(url, registerDto, UserDto.class);

        // 응답 검증
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto userDto = responseEntity.getBody();
        Assertions.assertThat(userDto.getUsername()).isEqualTo(registerDto.getUsername());
        Assertions.assertThat(userDto.getEmail()).isEqualTo(registerDto.getEmail());
    }

    @Test
    public void loginTest(){
        userService.registerUser(registerDto);
        LoginDto loginDto = LoginDto.builder()
                .username("test_user")
                .password("12345678")
                .build();
        // 로그인 요청
        JwtToken jwtToken = userController.loginUser(loginDto);

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}",httpHeaders);

        // API 요청 설정
        String url = "http://localhost:" + randomServerPort + "/api/user/test";


        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(loginDto.getUsername());
    }




}

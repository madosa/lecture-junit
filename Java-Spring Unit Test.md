## Java/Spring Unit Test

### I. 요구사항 정의

####   1. 회원관리 프로그램

- 회원 등록 / 조회 / 수정 / 삭제 기능 필요
- 회원 정보는 이름, 성별, 생년월일, 이메일, 연락처의 정보가 등록된다.
- 회원 조회는 리스트 조회와 상세 조회가 가능해야 한다.
- 회원 등록과 수정 시 이메일이 중복 되어서는 안된다.
- 회원 삭제는 한번에 한명의 회원만 삭제할 수 있다.



### II. 개발 환경 구축

####   1. OpenJDK, IntelliJ IDEA, Docker, MySQL 설치

- OpenJDK 설치

  - Mac

    ```
    brew tap AdoptOpenJDK/openjdk
    brew cask install adoptopenjdk8
    ```

  - Window : https://adoptopenjdk.net/

- IntelliJ IDEA : https://jwprogramming.tistory.com/99

- Docker 설치

  - Mac : https://madosa.kr/58

    ```
    $ mkdir /Users/majoonchae/mysql
    $ cd /Users/majoonchae/mysql
    $ mkdir /Users/majoonchae/mysql/lecture
    $ cd /Users/majoonchae/mysql/lecture
    $ mkdir conf data
    $ cd conf
    $ vi my.cnf
    [client] 
    default-character-set=utf8mb4 
    
    [mysql] 
    default-character-set=utf8mb4 
    
    [mysqld] 
    collation-server = utf8mb4_unicode_ci 
    init-connect='SET NAMES utf8mb4' 
    character-set-server = utf8mb4
    
    $ sudo docker create --name lecture-mysql -p 43306:3306 -v /Users/majoonchae/mysql/lecture/conf:/etc/mysql/conf.d -v /Users/majoonchae/mysql/lecture/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=admin12# -e MYSQL_DATABASE=lecture -e MYSQL_USER=lecture -e MYSQL_PASSWORD=lecture12# mysql:5.7.19
    
    $ sudo docker start lecture-mysql
    ```

  - Window : https://steemit.com/kr/@mystarlight/docker

  - PowerShell : https://www.tabmode.com/windows10/powershell-to-command-prompt.html

- MySQL 설치 : https://madosa.kr/61

- GIT : https://github.com/madosa/lecture-junit.git

  

####    2. DB Schema

- DB Connection

  ![스크린샷 2019-12-02 오후 11.47.59](/Users/majoonchae/Desktop/스크린샷 2019-12-02 오후 11.47.59.png)



- DB 권한

  ![스크린샷 2019-12-02 오후 11.52.57](/Users/majoonchae/Desktop/스크린샷 2019-12-02 오후 11.52.57.png)



- Create Table

```
# member
CREATE TABLE `member` (
  `memberid` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '회원 ID',
  `membername` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '회원 이름',
  `gender` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '성별',
  `dateofbirth` int(8) NOT NULL COMMENT '생년월일',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '이메일',
  `mobile` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '서비스 이름',
  `createdate` datetime NOT NULL COMMENT '등록일시',
  `modifydate` datetime NOT NULL COMMENT '최종 수정일시',
  PRIMARY KEY (`memberid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 테이블';

# seq_member
CREATE TABLE `seq_member` (
  `seq` bigint(20) NOT NULL COMMENT '이미지 시퀀스 번호',
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='멤버 시퀀스 테이블';

# Init seq_member
INSERT INTO `seq_member` (`seq`)
VALUES (1);
```



####   3. Package 구조

```
// main
com.interpark.lecture.junit
                            common							// 공통 설정 패키지
                                config
                                healthcheck
                                log
                                transaction
                            member							// 회원 관리 패키지
                                MemberController
                                MemberDAO
                                MemberService
```

```
// test
com.interpark.lecture.junit
                            member
                                MemberControllerTest
                                MemberDAOTest
                                MemberServiceTest
```



### III. 어플리케이션 개발

###   1. SpringBoot

![스크린샷 2019-11-29 오전 1.20.43](/Users/majoonchae/Desktop/스크린샷 2019-11-29 오전 1.20.43.png)

- Dependencies

####   1. 소스 코드

```
// Git 주소 
https://github.com/madosa/member.git
```



- 참고. Field injection is not recommended.

  ```
  Field injection is not recommended 
  Inspection info: Spring Team recommends: "Always use constructor based dependency injection in your beans. Always use assertions for mandatory dependencies".
  
  현장 주입은 권장하지 않습니다
  검사(Inspection) 정보 : Spring Team은 다음과 같이 권장합니다. "항상 Bean에서 생성자 기반 종속성 주입을 사용하십시오. 필수 종속성에 대해서는 항상 assertions(단언)을 사용하십시오".
  ```

  

### IV. Test 코드 작성

### @RunWith, @ContextConfiguration

```
토비의 스프링 3.1 Vol1 2장 테스트 p.185, 186

@RunWith는 JUnit 프레임워크의 테스트 실행 방법을 확장할 때 사용하는 애노테이션이다. SpringJUnit4ClassRunner라는 JUnit용 테스트 컨텍스트 프레임워크 확장 클래스를 지정해주면 JUnit이 테스트를 진행하는 중에 테스트가 사용할 애플리케이션 컨텍스트를 만들과 관리하는 작업을 진행해준다.

@ContextConfiguration은 자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일위치를 지정한 것이다.
스프링의 JUnit 확장기능은 테스트가 실행되기 전에 딱 한 번만 애플리케이션 컨텍스트를 만들어두고, 테스트 오브젝트가 만들어질 때마다 특별한 방법을 이용해 애플리케이션 컨텍스트 자신을 테스트 오브젝트의 특정 필드에 주입해주는 것이다.
```

- @RunWith에 Runner클래스를 설정하면 JUnit에 내장된 Runner대신 그 클래스를 실행한다. 여기서는 스프링 테스트를 위해서 SpringJUnit4ClassRunner라는 Runner 클래스를 설정해 준 것이다.

### **@ContextConfiguration**와 **@SpringApplicationConfiguration**를 비교

```
@ContextConfiguration의 locations 속성에는 xml형태의 애플리케이션 컨텍스트만 로딩 가능한 것 같다(class파일을 지정하니 오류 발생)
@ContextConfiguration(locations="applicationContext.xml")

@SpringApplicationConfiguration은 Spring Boot에서 class형태의 애플리케이션 컨텍스트를 로딩 할 수 있는것 같다.
@SpringApplicationConfiguration(classes = App.class)
```



### when thenReturn

메서드 호출을 모의 할 때 반환 값을 알고있을 때 thenReturn 또는 doReturn을 사용해야합니다. 조롱 된 메소드를 호출 할 때이 정의 된 값이 리턴됩니다.

```
@Test
public void test_return() throws Exception {
    Dummy dummy = mock(Dummy.class);
    int returnValue = 5;

    // choose your preferred way
    when(dummy.stringLength("dummy")).thenReturn(returnValue);
    doReturn(returnValue).when(dummy).stringLength("dummy");
}
```



### when thenReturn

조롱 된 메서드가 호출 될 때 추가 작업을 수행해야 할 때 응답이 사용됩니다. 이 메서드 호출의 매개 변수를 기반으로 반환 값을 계산해야하는 경우

```
@Test
public void test_answer() throws Exception {
    Dummy dummy = mock(Dummy.class);
    Answer<Integer> answer = new Answer<Integer>() {
        public Integer answer(InvocationOnMock invocation) throws Throwable {
            String string = invocation.getArgumentAt(0, String.class);
            return string.length() * 2;
        }
    };

    // choose your preferred way
    when(dummy.stringLength("dummy")).thenAnswer(answer);
    doAnswer(answer).when(dummy).stringLength("dummy");
}
```



### **@TransactionConfiguration**

트랜잭션이 적용된 테스트를 설정하는 클래스수준의 메타데이터를 정의한다. 특히, 원하는 PlatformTransactionManager의 빈 이름이 "transactionManager"가 아니라면 트랜잭션을 유도하는데 사용하는 PlatformTransactionManager의 빈 이름을 명시적으로 설정할 수 있다. 추가적으로 defaultRollback 플래그를 false로 변경할 수 있다. 보통 @TransactionConfiguration는 @ContextConfiguration와 결합해서 사용한다.

```
@ContextConfiguration
@TransactionConfiguration(transactionManager="txMgr", defaultRollback=false)
public class CustomConfiguredTransactionalTests {
  // class body...
}
```



### V. Jenkins 연동

####    1. Jenkins 설치

####    2. Jenkins 연동





## 출처

```
Book
JUnit in Action 단위 테스트의 모든 것 
자바와 JUnit을 활용한 실용주의 단위 테스트

Web
https://countryxide.tistory.com/17
https://www.slideshare.net/gyumee/ss-90206560
https://javacan.tistory.com/entry/MocktestUsingMockito
```




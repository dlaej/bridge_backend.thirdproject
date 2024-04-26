# git 정리
+ git config --global [user.name](http://user.name) “” : 현재 사용자의 모든 저장소 범위에서 user.name을 “”으로 설정. 이 명령어를 통해 커밋 이용자를 알 수 있음.
+ git config --global [user.email](http://user.email) “” : 위와 마찬가지
cf) local, system도 있음
+ git init : 현재 폴더를 git이 관리하는 작업 디렉토리로 설정
+ git remote add origin “” : origin이라는 이름으로 “” 주소를 원격 저장소로 지정하고 연결
+ git remote -v : 현재 로컬 저장소에 있는 원격 저장소를 나열해서 보여주는 명령어
+ git remote remove origin: origin이라는 원격저장소와의 연결을 해제하는 명령어
+ rd /s /q .git : .git 디렉터리 삭제. git init 취소하는 명령어
+ git branch : 브랜치 확인하는 명령어. 브랜치를 따로 생성 안 했을 때는 커밋까지 해야 브랜치명이 보임. 기본: master
+ git branch “” : 브랜치 생성하는 명령어
+ git checkout “” : 작업 브랜치를 해당 브랜치로 변경하는 명령어
+ git add : git 인덱스에 추가. 인덱스는 로컬 저장소에 커밋할 준비를 하기 위해 변경 내용을 임시로 저장하는 위치
+ git commit: 인덱스에 추가된 파일을 커밋. 로컬 저장소에 기록하는 방법
+ git log: 커밋 정보를 알려주는 명령어
+ git push origin master: origin이라는 원격저장소의 master라는 브랜치에 푸시하는 명령어
+ git pull: 원격 저장소의 내용을 로컬 브랜치로 가져오는 명령어


# 코드 설명

```java
@Component
@Aspect
@Slf4j
public class LoggerAspect {
	@Pointcut("execution(* bridge..controller.*Controller.*(..)) || execution(* bridge..service.*ServiceImpl.*(..)) || execution(* bridge..mapper.*Mapper.*(..))")
	private void loggerTarget() {}
	
	@Around("loggerTarget()")
	public Object logPrinter(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		log.debug(className + "." + methodName + "()");
		return joinPoint.proceed();
	}
}
```
loggerTarget() 이라는 이름으로 pointcut을 정의하는 메서드

@Around 어노테이션을 이용해 loggerTarget에서 지정한 포인트컷의 시기대로 타켓 메서드가 호출되기 전 후에 aop 즉 어드바이스를 적용. around는 어드바이스 어노테이션

정리

loggerAspect는 log를 기능별로 출력하고 싶은데 그럼 너무 비효율적이니깐 aop를 사용해 만든 클래스이다. loggerAspect 즉 aspect는 부가기능과 포인트컷이 지정된 클래스라고 보면된다. 부가기능 즉 advice는 joinpoint를 이용해 타켓 메서드가 호출될 때 실행될 메서드들을 정의하고 있다. 이때 어드바이스를 정의하는 어노테이션 즉 around는 advice가 타켓 메서드 호출에 따라 언제 실행이 될지를 정의하는 어노테이션인데 구체적으로 around는 타켓 메서드가 호출되기 전 후에 advice를 실행시키는 어노테이션이다. 이 부가기능에서는 주로 joinpoint 인터페이스의 getSignature메서드를 사용하는데 joinpoint를 이용해 타켓 메서드의 정보를 출력한다. 타켓 메서드의 클래스, 메서드 명을 출력한다. 그리고 return문을 이용해proceed 메서드를 호출하면서 다음 어드바이스 또는 타켓 메서드를 호출하는 기능으로 부가기능을 끝낸다.

원래 로그 사용할 때에는 logger 객체를 생성해서 사용해야 하는데 ( 이때 loggerFactory 이용) 그리고 관련 라이브러리를 임포트도 해줘야 함. 아무튼 이 과정을 롬복을 사용하는 경우 @Slf4j 어노테이션으로 생략할 수 있음. 자동으로 로거 생성됨.


@SpringbootApplication 어노테이션: 자동 설정을 해주기 위한 어노테이션. @SpringBootConfiguration @EnableAutoConfiguration @ComponentScan 으로 크게 3가지가 합쳐진 어노테이션이다. 스프링 부트 어플리케이션은 bean을 2번 등록한다. 처음에 componentScan으로 등록하고 EnableAutoConfiguration으로 추가적인 bean들을 읽어서 등록한다. @SpringBootConfiguration은 @Configuration의 하위 어노테이션으로 @Configuration과 동일한 역할을 수행한다. 이 어노테이션은 애플리케이션에 1개만 존재해야 하며 일반적으로 @SpringBootApplication에 포함되어있기 때문에 따로 작성할 필요는 없다. @EnableAutoConfiguration은 우리가 필요할 것 같은 빈들을 자동으로 설정해주는 자동 설정 기능을 활성화하는 어노테이션이다. @ComponentScan은 빈을 등록하기 위한 어노테이션들을 탐색하는 위치를 지정한다. 이 어노테이션이 붙은 클래스의 패키지를 스캔하여 컴포넌트(@Component, @Service, @Controller 등) 을 찾도록 지시한다. 참고로 @Configuration 어노테이션은 스프링 프레임워크에게 해당 클래스가 하나 이상의 빈을 제공하는 구성 클래스임을 나타내는 역할을 한다. 이 어노테이션은 빈을 정의하며 ( bean어노테이션을 사용하여 빈을 정의할 수 있다. bean 어노테이션은 해당 메서드가 생성하고 반환하는 객체를 스프링 컨테이너에 빈으로 등록한다.) 빈 간 의존성을 관리하고 환경 설정 및 외부 프로퍼티에도 사용한다 (애플리케이션의 환경 설정을 담당하는 데에도 사용된다. 프로퍼티 파일에서 값을 읽어오거나, 환경 변수를 통해 설정을 가져오는 등의 작업을 수행할 수 있다.). 


SpringApplication.run: 이 명령어는 많은 동작들을 수행하는데 그 중에서도 기억에 남는 건 내장 서버를 구동시킨다는 것이다. 이 메서드는 springboot의 내장된 웹서버 즉 WAS를 구동시켜 기본적인 설정을 적용하여 애플리케이션을 실행한다. 이때 WAS는 사용자가 요청을 보내면 특정 포트로 오는 요청을 잡아 SpringBoot 어플리케이션으로 연결시켜준다고 한다. 참고로 springboot에는 내장된 웹서버 즉 tomcat, jetty, undertow 중 하나의 서버를 사용할 수 있다. 따라서 개발자가 따로 웹서버?(Tomcat)을 설치할 필요가 없다.



RuleBasedTransactionAttribute.setName: transaction이 적용되는 메서드의 이름을 지정한다. jstg, 강의록에서는 *을 사용해 모든 메서드에 트랜잭션을 적용한다.

MatchAlwaysTransactionAttributeSource: 이 클래스는 항상 동일한 트랜잭션 속성을 반환하도록 구현되어 있습니다

@Autowired 어노테이션 : 필요한 의존 객체의 ‘타입’에 해당하는 빈을 찾아 주입한다. 생성자/setter/필드 이 3가지 경우에 Autowired를 사용한다. ioc 컨테이너에 등록되어 있는 참조할 빈을 찾아 주입한다.

@Bean 어노테이션: 우선 빈이란 스프링 ioc 컨테이너가 관리하는 객체로 의존성 관리를 위해 사용된다. 또한 이런 빈들을 싱글톤의 형태이다. 더 알아봐야겠지만 빈은 @Bean 어노테이션 뿐만 아니라 component 어노테이션 그 안에 controller service 어노테이션들을 통칭하는 것 같다. 그 중에 @Bean 어노테이션은 개발자가 컨트롤 할 수 없는 외부라이브러리를 빈으로 등록하고 싶은 경우에 쓰인다. 메서드로 return 되는 객체를 빈으로 등록한다고 한다.

autowired를 사용하여 appllicationContext를 주입하는 경우와 autowired를 사용하면서 생성자 또는 다른 필드를 사용하여 주입하는 경우는 다르다. 내가 이해한 바로는 전자는 스프링 컨테이너에서 직접 접근하여 필요한 빈을 가져올 때 사용하며 후자는 빈들 간의 의존성을 해결하기 위해 사용된다.



DatabaseConfiguration

applicationContext: ApplicationContext는 스프링 컨테이너라고 한다. Configuration이 붙은 클래스들을 설정 정보로 등록하고 @Bean 어노테이션이 붙은 메서드의 이름으로 빈들을 묶어 빈 목록을 생성한다. 클라이언트가 빈을 요청하면 getBean 함수를 통해 빈 목록에서 해당 빈을 찾고 그 빈을 갖고 있는 설정 클래스로부터 빈 생성을 요청하고 생성된 빈을 반환한다. 이때 빈은 싱글톤 규칙에 의해 생성되고 사용된다. 추가로 applicationContext 인터페이스의 구현체가 여러가지가 있는데 구현체에 따라 스프링 컨테이너를 xml을 기반으로 만들 수도 있고, 자바 클래스로 만들 수도 있다. 아무튼 아까의 설명보다 더 자세히 말하자면 스프링 컨테이너 내부에는 빈 저장소가 존재한다. 빈 저장소는 key로 빈 이름을 가지고 있으며 value로 실제 빈 객체를 가지고 있다. 내 생각인데 이때 key가 빈 어노테이션이 붙은 메서드 이름 같다. 또한 심화로 spring에서는 빈의 생성과 관계설정 같은 제어를 담당하는 ioc 컨테이너인 빈 팩토리가 존재한다. 근데 이 인터페이스 외에도 추가 기능이 필요한데 그 인터페이스가 빈 팩토리 인터페이스를 구현하는 applicationContext 인터페이스라고 한다. 상속 받아 확장한 이라고 설명이 되어 있어서 상속관계일 수도 암튼.

@Configuration: 위에 설명 자세히 되어 있음.

@PropertySource: 소스 코드를 작성하다 보면 정적으로 하드코딩된 내용을 쓸 때가 있다. 대표적으로 db의 설정정보가 그렇다. 하지만 이 방법은 변경사항이 있을 때 공유를 해야 하는 상황일 때 (민감한 정보까지 공유) 단점이 있다. 그래서 이를 해결하기 위해 설정 정보 파일을 따로 분리하고 불러오는 방식을 사용한다. 스프링 프로퍼티가 바로 그것이다. (jstg에서는 application.properties) 같다. 이 때 설정 정보 파일을 불러오는 방법이 propertySource이다. @Configuration 어노테이션과 같이 쓰인다. 내 생각인데 configuration 어노테이션이 빈 관련 역할도 하지만 설정 정보를 불러오는 역할도 하기 때문에 그런 것 같다.

@ConfigurationProperties: 이건 여러 프로퍼티를 가져와(보통 접두어를 사용하는 것 같다.) 하나의 클래스에 매핑 시켜 그 클래스 자체를 빈으로 등록할 때 쓰는 어노테이션이다. 이 프로퍼티들을 이용해 매핑 시켜 빈으로 등록한다는 점에서 PropertySouce 어노테이션과는 다르다.

BoardController

@Controller: 컨트롤러 클래스는 클라이언트와의 요청, 응답을 주고 받는 역할을 한다. 우선 내가 이해한 바로는 @Component 어노테이션은 개발자가 정의한 클래스(메서드가 아니라)를 스프링 빈으로 등록하고자 할 때 쓰는 어노테이션이다. Controller 어노테이션은 이 component 어노테이션의 하위 어노테이션으로 컨트롤러 클래스의 역할을 담은 어노테이션이다. 즉 사용자와의 요청과 응답을 제공하는 어노테이션으로 만약 component 어노테이션을 사용하려면 url을 연결해주는 @RequestMappling 어노테이션을 클래스 위에 추가적으로 써줘야 한다. 그리고 빈을 등록할 때는 key값과 value 값이 있는데 Controller 어노테이션은 key 값에 url 주소를 매핑하고 value에 빈으로 등록될 클래스 객체가 들어가는 것 같다. 그리고 추가적으로 bean 어노테이션은 메서드에 component 어노테이션에는 클래스 자체를 단위로 빈을 등록한다고 한다.

간단하게 말하면, **`@Component`** 어노테이션은 클래스 단위로 빈을 등록하는 데 사용되고, **`@Bean`** 어노테이션은 메서드 단위로 빈을 등록하는 데 사용됩니다. 일반적으로 **`@Component`**는 자동으로 스캔되는 빈을 생성할 때 사용되고, **`@Bean`**은 수동으로 빈을 정의하거나 설정할 때 사용됩니다.

@GetMapping: 우선 RequestMapping 어노테이션에 대해서 알아야 한다. RequestMapping 어노테이션은 http 메서드 요청을 처리할 수 있는 어노테이션이다. 이때 메서드 종류가 get, post, put, delete 등이 있다. 그래서 해당 메서드를 특정지어 쓰기 위해서는  method  속성?을 추가하여 메서드 방식을 지정해주어야 한다. 즉 RequestMapping 어노테이션은 이 모든 메서드를 포괄하는 http 요청을 수행하는 역할이라고 보면된다. 그럼 GetMapping 어노테이션은 자연스럽게 이 http 요청에서 해당 get 메서드 요청이 올 때 mapping 시켜주는 어노테이션이라고 보면 된다. 추가로 클래스 위에 RequestMapping으로 url이 설정되어 있고 메서드 안에 또 RequestMapping이나 GetMapping으로 url이 설정되어 있다면 이 url을 차례로 연결시킨 요청이 해당 메서드를 실행시킨다. 또한 RequestMapping에서 method를 지정하지 않을 수도 있는데 이럴 때는 해당하는 모든 메서드 요청에 그 클래스가 매핑이 된다.

ModelAndView: mvc에서 사용하는 클래스이다. 이 클래스는 사용자의 요청과 응답이 이루어지는 컨트롤러에서 사용되며 Model 클래스와 구분된다. 우선 Model 클래스는 데이터를 뷰에 전달하는 역할을 수행하며 이에 추가로 ModelAndView는 view의 정보까지 뷰에 전달하는 역할을 수행한다. 이때 데이터를 넘길 때는 model은 addAttribute() 메서드를 이용하며 modelandview는 addObject()메서드를 이용한다. 또한 modelandview에서 view를 전달할 때는 setViewName()메서드를 이용한다. 추가로 modelandview 객체를 생성할 때에는 인자로 뷰 경로를 입력해 뷰를 설정한 modelandview 객체를 생성할 수 있다.

return mv;  return “first/noticeWrite”; 쓰임 차이 정리: 이건 그냥 내 생각대로 해도 될 것 같다. 전자는 데이터와 뷰를 전달해 그 뷰에서 응답받아 modelandview 객체를 반환하는 거고 후자는 modelandview를 보통 사용하지 않고(modelandview는 view에 데이터 또는 뷰를 전달할 때 쓰는 것이기 때문에) 정적인 뷰를 반환할 때 사용하는 것 같다.

@RequestMapping: 위에 설명 있음

return “redirect:/openNoticeList.do”;: 내가 이해한 바로는 redirect는 보통 뷰를 반환하지 않을 때 사용하고 해당 메서드만 수행이 되면 redirect url을 다시 요청하여 그 url요청에 맞는 메서드를 다시 수행할 때 사용한다.

@RequestParam: 우선 이 어노테이션은 requestbody와 구분된다. 전자는 url에서 데이터를 받아오는 경우에 사용되고 requestbody는 그 이외에 보통 사용이 된다. requestbody도 url로 받을 수 있지만 변수 명을 구분해서 받을 수는 없다고 한다. 근데 마지막 이 말은 확실하지 않다.

@PostMapping: 위에 설명 있다.

List<>: 자바에서 사용하는 클래스 중 하나로 배열과 구분된다. 배열은 처음에 크기를 정적으로 지정하여 사용해야 하는 반면 list는 크기를 동적으로 생성시킬 수 있다. 즉 메모리가 허용하는 한 필요에 따라 크기를 또는 데이터를 추가할 수 있다.

@PathVariable: RequestParam과 동일한 기능을 하는데 requestParam과 달리 하나의 값만 받아올 수 있다고 한다. 근데 이 설명은 잘 모르겠다. 아닌 것 같은데? 나중에 더 알아보고 암튼 챗 지피티의 설명에 따르면 requestParam은 http 요청 파라미터를 추출하는데 사용되고 pathVariable은 url 템플릿의 경로 변수를 추출하는데 사용된다고 한다. 아직 차이 잘 모르겠다. 내가 이해한 바로는 requestparam은 파라미터로 가져오는 파라미터명 자체를 추출해서 사용하는 거고 pathvariable은 경로에 있는 변수 명을 이용해 변수 안의 실제 값을 추출해서 사용하는 것 같다.

CelebrityController

MultipartFile: multipart는 웹 클라이언트가 요청을 보낼 때, http 프로토콜의 바디 부분에 데이터를 여러 부분으로 나눠서 보내는 것이다. 웹 클라이언트가 서버에게 파일을 업로드할 때, http 프로토콜의 바디 부분에 파일정보를 담아서 전송을 하는데 파일을 한 번에 여러 개 전송을 하면 body 부분에 파일이 여러 개의 부분으로 연결되어 전송된다. 이렇게 여러 부분으로 나뉘어서 전송되는 것을 multipart data라고 한다. 보통 파일을 전송할 때 사용한다. multipartfile이란 사용자가 업로드한 file을 핸들러에서 손쉽게 다룰 수 있게 도와주는 매개변수 중 하나이다. 매개변수를 사용하기 위해서는 multipartresolver bean이 등록되어 있어야 한다. 이는 springboot에서 자동 등록을 지원하지만 springMVC에서는 기본으로 등록해주지 않으므로 꼭 확인해야 한다. multipartfile 인터페이스는 스프링에서 업로드한 파일을 표현할 때 사용되는 인터페이스이다. 이 인터페이스를 이용해서 업로드한 파일의 이름, 실제 데이터, 파일 크기 등을 구할 수 있다. 

HttpServletResponse: was가 웹브라우져로부터 서블렛요청을 받으면 요청을 받을 때 전달 받은 정보를 httpservletrequest 객체를 생성하여 저장.  웹브라우져에게 응답을 돌려줄 httpservletresponse 객체를 생성. 생성된 httpservletrequest(정보가 저장된)와 httpservletresponse(비어 있는)를 servlet에게 전달. httpservletrequest: http프로토콜의 request 정보를 서블릿에게 전달하기 위한 목적으로 사용. header 정보, parameter, cookie, url, uri 등의 정보를 읽어들이는 메서드를 가진 클래스. body의 stream을 읽어들이는 메서드를 가지고 있음. httpservletresponse: servlet은 httpservletresponse객체에 content type, 응답코드, 응답 메세지 등을 담아서 전송함.

다운로드 코드:

*@GetMapping*("/download.do") 

public void downloadFile(*@RequestParam* int fanIdx, HttpServletResponse response) throws Exception {

CelebrityDto fanDto = celebrityService.selectOneFanByFanId(fanIdx);

String fanImage = fanDto.getFimage();

FileInputStream fis = null;  //파일 경로나 file객체를 바이트 단위로 데이터를 읽기 위한 스트림이다.

BufferedInputStream bis = null;  //입력 스트림을 버퍼링하여 성능을 향상시키는 역할을 한다. 버퍼링으로 매핑하는 기능이기 때문에 단독으로 사용한다기보다는 다른 입력 스트림을 감싸서 사용한다. 버퍼는 데이터를 읽어올 때 일정량의 데이터를 한 번에 읽어와 메모리에 보관하고 이후에는 버퍼에서 데이터를 읽어오기 때문에 입출력 성능이 향상된다. 내 생각엔 버퍼가 속도가 빠르고 성능을 향상시켜주는 메모리영역이다. 검색해 봤더니 데이터를 일시적으로 저장하는 메모리 영역을 말한다.

BufferedOutputStream bos = null;   //출력스트림을 버퍼링하여 성능을 향상시키는 역할을 한다. 이것도 위와 마찬가지로 단독으로 사용한다기보다는 다른 출력 스트림을 감싸서 사용한다. 버퍼를 사용하므로 데이터를 출력할 때 일정량의 데이터를 한 번에 출력 버퍼에 담아두고 이후에는 한 번에 출력한다. 이로 인해 입출력 성능이 향상된다.

try {

response.setHeader("Content-Disposition", "inline;");    //http응답 헤더에 content-disposition 헤더를 설정하여 브라우저가 파일을 어떻게 처리해야 하는지 지정한다. 여기서는 inline으로 설정되어 있어 브라우저에서 파일을 바로 표시하도록 지시한다. 구체적으로 content-disposition 헤더는 서버에서 브라우저로 전송되는 콘텐츠의 처리 방식을 지정하는데 사용된다. 만약 inline이 아니라 attachment 값으로 설정하면 브라우저는 해당 콘텐츠를 다운로드하도록 지시하게 된다.

byte[] buf = new byte[1024];

fis = new FileInputStream(fanImage);   //파일 경로가 저장된 데이터를 아까 생성한 fis에 대입한다.

bis = new BufferedInputStream(fis);   // 속도와 성능을 향상시키기 위하여 fis를 bufferdinputstream을 이용해 버퍼로 감싼다.

bos = new BufferedOutputStream(response.getOutputStream()); response 객체의 출력스트림을 반환해 bufferedoutputstream을 이용해 버퍼로 감싸는 코드이다.

int read;

while((read = bis.read(buf, 0, 1024)) != -1) {          //bis 즉 fis에서 바이트?를 읽어와 read에 대입

bos.write(buf, 0, read);  //그 read를 bos 즉 response객체의 출력스트림에 write

}

} finally {

bos.close();

bis.close();

fis.close();

}

}

Dto

@Data 어노테이션: 일반적으로 자바 클래스 내의 일반적인 메소드를 자동으로 생성해주는 편리한 방법을 제공한다. 대표적으로 getter, setter 함수 등.

Mapper

@Mapper: mybatis와 같은 orm 프레임워크에서 주로 사용되는 어노테이션 중 하나이다. 이 어노테이션은 인터페이스에 부여되며 mybatis가 해당 인터페이스를 구현하는 클래스를 자동으로 생성하도록 지시한다. 

MyBatis는 SQL 쿼리와 자바 메소드를 매핑시키는 방식으로 데이터베이스와 상호작용합니다. 이를 위해 개발자는 SQL 쿼리를 포함하는 인터페이스를 작성하고, MyBatis는 이 인터페이스를 구현하는 클래스를 동적으로 생성하여 SQL 쿼리를 실행합니다.

**`@Mapper`** 어노테이션은 MyBatis에서 인터페이스를 매퍼(Mapper)로 표시하는 데 사용됩니다. MyBatis가 이 인터페이스를 읽고, 해당하는 SQL 쿼리를 실행할 수 있는 클래스를 생성합니다. 

MyBatis는 애플리케이션이 시작될 때 이러한 매퍼 인터페이스를 스캔하고, 해당하는 클래스를 동적으로 생성하여 SQL 쿼리를 실행할 준비를 합니다. 그러면 개발자는 이러한 인터페이스를 사용하여 데이터베이스와 상호작용할 수 있습니다.

ServiceImpl

@Service: 이 어노테이션은 해당 클래스가 비즈니스 로직을 담당하는 서비스 클래스임을 나타낸다. 주로 서비스계층에서 사용되며 해당 클래스가 비즈니스 로직을 처리하고 트랜잭션 관리, 예외 처리 등의 역할을 담당한다.

@Override: 이 어노테이션은 자바 프로그래밍에서 메소드를 재정의할 때 사용되는 어노테이션이다. 

@Value(”${application.upload-path}”): 외부 프로퍼티 값을 가져와서 uploadPath라는 변수에 할당하는 코드이다. @Value 어노테이션은 스프링 프레임워크에서 외부 설정 파일의 값을 가져와서 변수에 주입하는데 사용된다. @PrpertySource 어노테이션은 스프링 구성 클래스에서 외부 프로퍼티 파일을 지정하는데 사용된다. 이 어노테이션을 사용하면 스프링 애플리케이션 컨텍스트에 외부 프로퍼티 파일을 로드할 수가 있다. @PropertySource를 사용하여 지정된 파일은 스프링 환경에 추가되고 @Value 어노테이션을 사용하여 프로퍼티 파일에서 값을 주입할 수 있다. 즉 @PropertySource는 외부 프로퍼티 파일을 스프링 환경에 로드하는데 사용되고 @Value는 외부 프로퍼티 파일의 값을 스프링 빈에 주입하는데 사용된다.

https://s.gravatar.com/avatar/f4304bca83085a0e7271bd0afa284bde?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fdl.png

**You**

그럼 @PropertySource("classpath:application.properties") 
이 코드가 있어야
 @Value("${application.upload-path}")
    private String uploadPath;
이 코드를 쓸 수 있는 거야?

ChatGPT

**ChatGPT**

네, 맞습니다. **`@PropertySource`** 어노테이션을 사용하여 외부 프로퍼티 파일을 로드한 후에 **`@Value`** 어노테이션을 사용하여 해당 프로퍼티 파일에서 값을 주입할 수 있습니다.

**`@PropertySource`** 어노테이션은 외부 프로퍼티 파일을 스프링 환경에 로드하는 역할을 합니다. 그리고 **`@Value`** 어노테이션은 프로퍼티 파일에서 특정 값을 가져와서 해당 필드에 주입하는 역할을 합니다.

따라서 **`@PropertySource`** 어노테이션을 사용하여 프로퍼티 파일을 로드한 후에 **`@Value`** 어노테이션을 사용하여 해당 프로퍼티 파일에서 값을 사용할 수 있습니다.

예를 들어:

```java
javaCopy code
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${application.upload-path}")
    private String uploadPath;

    // uploadPath 변수에 외부 프로퍼티 파일의 값을 주입합니다.
}

```

위의 코드에서 **`@PropertySource`** 어노테이션을 사용하여 클래스패스의 **`application.properties`** 파일을 스프링 환경에 로드한 후에, **`@Value`** 어노테이션을 사용하여 해당 파일에서 **`application.upload-path`** 키에 해당하는 값을 가져와서 **`uploadPath`** 변수에 주입합니다.

업로드 코드: 이해완료

sql 쿼리

<!**DOCTYPE** mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"

"https://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace=””> </mapper>: 매퍼 파일의 루트 엘리먼트이다. 이 엘리먼트는 매퍼 파일 전체의 시작과 끝을 나타낸다. 또한 이 엘리먼트의 namespace 속성을 사용하여 해당 매퍼에 대한 namespace를 지정한다. namespace 속성은 해당 매퍼 파일이 연결될 자바 인터페이스의 경로를 지정한다. 이 경로는 mybatis 설정 파일에 등록되어 있는 매퍼 인터페이스와 연결된다. 이를 통해 mybatis는 해당 인터페이스에 정의된 메소드와 매퍼 파일에 정의된 sql 쿼리를 매핑한다.

templates

thymeleaf -#lists

form - action, method, id

form

<form action="요청을 전달할 서버의 페이지" method="요청방식" enctype="전달하는 데이터의 형식">

method - get, post. 두 가지가 있다.

get 방식: 주소창에 파라미터가 노출되어 전달되는 방식. http:// www.naver.com?code=154464. 여기 뒤에 ?파라미터 이름= 파라미터 값(=쿼리스트링).

post 방식: 주소창에 파라미터가 넘어가는 방식이 아니라 http 프로토콜의 body에 파라미터가 넘어가는 방식(확인: 개발자도구 network탭에 headers의 (body 부분?) Form Data 부분). 즉, 눈에 보이지 않아 보안성이 유지되므로 주로 로그인 페이지, 회원가입 페이지, 글 작성페이지 등에 사용된다. 용량 제한이 있는 get방식과 다르게 사용자의 입력을 표준 입력으로 넘겨주기 때문에 입력 내용의 길이의 제한을 받지 않는다.

enctype = multipart/form-data, application/x-www-form-urlcencoded, text/plain. 세 가지가 있다.

multipart/form-data : 파일(<input type=”file”>)이 포함된 폼을 전송할 때 사용. 그렇게 하지 않으면 웹 서버로 데이터를 넘길 때 파일의 경로명만 전송되고 파일 내용이 전송되지 않기 때문이다. 이때 메소드는 post값으로 지정해줘야 한다. 파일 업로드가 없는 폼에도 사용 가능. 다만 multipart/form-data는 파일 전송을 위해 추가적으로 표시되는 요소들이 있기 때문에 application/x-www-form-urlccencoded 보다 기본 데이터가 더 커지기 때문에 짧은 데이터 전송에는 비효율적이다.

application/www-form-urlencoded: 디폴트 값이다. enctype을 따로 설정하지 않으면 이 값이 설정된다. 파일이 없는 폼에 사용. 인코딩한 문자열을 get 방식으로 전송하는 것과 동일한 전송 데이터를 생성하며, post 방식으로 전송하는 차이만 있다. 폼데이터는 서버로 전송되기 이전에 url-encode된다.

text/plain: 인코딩을 하지 않은 문자 상태로 전송한다.
 

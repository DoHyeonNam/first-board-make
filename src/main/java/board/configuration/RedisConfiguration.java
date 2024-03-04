package board.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    // @Value : 주입된 값을 주입받는 데 사용됩니다.
    // 이 어노테이션은 주로 프로퍼티 파일이나 환경 변수 등의 외부 소스에서 값을 주입받아 필드에 할당할 때 사용
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        // lettuceConnectionFactory를 생성해서 반환한다
        // lettuceConnectionFactory : Redis 서버와의 연결을 설정하는 클래스
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> template() {
        // 자동으로 만들어진다(아래에 만들어지는 template의 종류가 설명되어있다)
        // RedisTemplate 는 키와 값이 String, Object 인 레디스 서버랑 상호 작용할 수 있도록 설정된다
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        // connectionFactory 는 레디스 연결을 관리하는 인터페이스

        // 직렬화 객체
        template.setKeySerializer(new StringRedisSerializer()); // 레디스 해시 키에 대한 직렬화 방법 설정, 문자열 직렬화
        template.setHashKeySerializer(new StringRedisSerializer()); // 레디스 해시 값에 대한 직렬화 방법 설정
        template.setHashKeySerializer(new JdkSerializationRedisSerializer()); // 레디스 값에 대한 직렬화 방법 설정
        template.setValueSerializer(new JdkSerializationRedisSerializer()); // RedisTemplate 이 트랜잭션을 지원하도록 설정
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
    // 결론 안해도 된다(자동으로 만들어준다). 내부 구조만 이해하기
    // Redis CLI(Command Line Interface) : Redis 서버에 연결하고 다양한 작업을 수행
}
/*
*spring-boot parent 2.2이상 버전에서는 자동 설정에 의해서 redis template 빈이 4개가 생성됩니다.
*
*@Autowired RedisTemplate redisTemplate;
*
*@Autowired StringRedisTemplate stringRedisTemplate;
*
*@Autowired ReactiveRedisTemplate reactiveRedisTemplate;
*
*@Autowired ReactiveStringRedisTemplate reactiveStringRedisTemplate;

*redisTemplate와 stringRedisTemplate의 차이는 Serializer(직렬화)입니다.
*redisTemplate의 key, value serializer는 JdkSerializationRedisSerializer이고
*stringRedisTemplate의 serializer StringRedisSerializer입니다.
*/
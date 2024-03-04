package board.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("swagger 테스트")
                .version("1.0")
                .description("API에 대한 설명 부분");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
// SpringDoc : API의 명세를 간편하게 작성하고 문서화
// 의존성 관리 도구를 통해 간편하게 프로젝트에 추가가능
// springdoc.api-docs.path=/api-docs (application.properties)
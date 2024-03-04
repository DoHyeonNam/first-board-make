package board.configuration;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

// spring boot 3 이상부터 multipart를 사용할때 이걸 만들어줘야 한다.
// 착각을 한게 앞단에서 Mutipart로 form-data로 넘어온다고 파라미터를 Mutipart로 받으면 읽을 수가 없다.
// 받는건 MultipartHttpServletRequest multipartHttpServletRequest으로 응답을 받아오고 parseFileInfo(multipartHttpServletRequest)로 넘겨줘서
// fileUtils에서 해결하는게 정확하다

// 멀티파트 요청을 처리하기 위한 설정 클래스

@Configuration
public class MultipartConfig {

    // 요건 셋팅값이 없으면 default로 10485760 이만큼 셋팅하겠다는 의미
    @Value("${file.multipart.maxUploadSize:10485760}")
    private long maxUploadSize;

    @Value("${file.multipart.maxUploadSizePerFile:10485760}")
    private long maxUploadSizePerFile;

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofBytes(maxUploadSize)); // 요청의 최대 크기 설정
        factory.setMaxFileSize(DataSize.ofBytes(maxUploadSizePerFile)); // 단일 파일의 최대 크기 설정

        return factory.createMultipartConfig();
    }
}
package com.web.curation.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.sun.istack.Nullable;
import com.web.curation.commons.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Authentication.class)
                .directModelSubstitute(PageRequest.class, Page.class)
                .apiInfo(this.swaggerInfo())
                .select()
                .paths(or(regex("/api/.*")))
                .build();

    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("Sub Project1 API")
                .description("서브 프로젝트 1의 API 문서입니다.")
                .version("1.0.0")
                .build();
    }
    @ApiModel
    @Getter @Setter
    static class Page{
        @ApiParam(value="페이지 번호(0~N)", example = "0")
        private Integer page;


        @ApiParam(value="한 페이지에서 받을 크기(min = 1, max = 500)", example = "10")
        private Integer size;

        @ApiParam(value="정렬")
        private List<String> sort;

    }
}


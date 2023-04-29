package com.kalita.drone.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Getter
    @AllArgsConstructor
    public enum Code {
        DRONE_NOT_FOUND("drone.not.found"),
        MEDICATION_NOT_FOUND("medication.not.found"),
        DRONE_IS_FULL("drone.is.full"),
        DRONE_LOADED("drone.loaded"),
        LOW_BATTERY("drone.low.battery");

        private final String value;
    }
}

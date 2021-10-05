package com.raytotti.convertcurrency.commons.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessagesConfig {

    @Bean
    public MessageSource messageSource() {
        return messageSourceConfig("messages/exception/messages");
    }

    @Bean
    public LocalValidatorFactoryBean getValidatorFactoryBean() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSourceConfig("messages/validation/messages"));
        return factoryBean;
    }

    public MessageSource messageSourceConfig(String path) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(path);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}

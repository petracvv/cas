package org.apereo.cas.config;

import org.apereo.cas.config.support.CasConfigurationEmbeddedValueResolver;
import org.apereo.cas.util.SchedulingUtils;
import org.apereo.cas.util.io.CommunicationsManager;
import org.apereo.cas.util.spring.ApplicationContextProvider;
import org.apereo.cas.util.spring.SpringAwareMessageMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringValueResolver;

import javax.annotation.PostConstruct;
import javax.validation.MessageInterpolator;

/**
 * This is {@link CasCoreUtilConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Configuration("casCoreUtilConfiguration")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableScheduling
public class CasCoreUtilConfiguration {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    @Bean
    public MessageInterpolator messageInterpolator() {
        return new SpringAwareMessageMessageInterpolator();
    }

    @Bean
    public CommunicationsManager communicationsManager() {
        return new CommunicationsManager();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public StringValueResolver durationCapableStringValueResolver() {
        return SchedulingUtils.prepScheduledAnnotationBeanPostProcessor(applicationContext);
    }

    @PostConstruct
    public void init() {
        final ConfigurableApplicationContext ctx = applicationContextProvider().getConfigurableApplicationContext();
        final DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(true);
        conversionService.setEmbeddedValueResolver(new CasConfigurationEmbeddedValueResolver(ctx));
        ctx.getEnvironment().setConversionService(conversionService);
    }
}

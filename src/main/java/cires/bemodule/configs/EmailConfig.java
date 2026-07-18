package cires.bemodule.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Configuration for the Thymeleaf template engine used when rendering emails.
 * <p>
 * Defines a custom {@link ITemplateResolver} that locates email templates on
 * the classpath under the {@code mail-templates/} directory.  This resolver
 * is picked up automatically by the {@link org.thymeleaf.spring6.SpringTemplateEngine}
 * auto‑configured by Spring Boot.
 * </p>
 * <p>
 * <b>Is this file still needed?</b><br>
 * Yes – the {@link cires.bemodule.listeners.EmailQueueConsumer} uses the
 * {@code SpringTemplateEngine} to render HTML emails.  Without this resolver,
 * the engine would not know where to find the template files (they would need
 * to be in the default {@code templates/} folder).  The explicit
 * {@code setCheckExistence(true)} is also useful to fail early if a template
 * is missing.
 * </p>
 */
@Configuration
public class EmailConfig {

    /**
     * Creates a {@link ClassLoaderTemplateResolver} that reads Thymeleaf
     * templates from the classpath folder {@code mail-templates/}.
     *
     * @return the template resolver
     */
    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mail-templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

}
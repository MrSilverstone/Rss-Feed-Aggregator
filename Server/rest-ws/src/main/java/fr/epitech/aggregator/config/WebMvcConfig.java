package fr.epitech.aggregator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages={"fr.epitech.aggregator.web"})
public class WebMvcConfig  extends WebMvcConfigurerAdapter {

}

package com.itsol.mockup.config;

import com.itsol.mockup.utils.ExcelUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelUtilConfig {
    @Bean
    public ExcelUtil excelUtil(){
        return new ExcelUtil();
    }
}

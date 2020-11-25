package com.aconcaguasf.basa.digitalize.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;



@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login")                         .setViewName("login");
        registry.addViewController("/home")                          .setViewName("home");
        registry.addViewController("/requerimientos")                .setViewName("requerimientos");
        registry.addViewController("/hoja_ruta")                     .setViewName("hoja_ruta");
        registry.addViewController("/admin_conceptos")               .setViewName("admin_conceptos");
        registry.addViewController("/remitos")                       .setViewName("pages/remitos");

        //modals
        registry.addViewController("/asignar_tarea")                 .setViewName("asignar_tarea");
        registry.addViewController("/marcar_elementos")              .setViewName("marcar_elementos");
        registry.addViewController("/controlar_remitos")             .setViewName("controlar_remitos");
        registry.addViewController("/controlar_elementos")           .setViewName("controlar_elementos");
        registry.addViewController("/leer_hdr_y_remitos")            .setViewName("leer_hdr_y_remitos");
        registry.addViewController("/mensajes_dialog")               .setViewName("mensajes_dialog");
        registry.addViewController("/edit-modal")                    .setViewName("edit-modal");
        registry.addViewController("/cambiar_tipo")                  .setViewName("cambiar_tipo");
        registry.addViewController("/alerta")                        .setViewName("alerta");
        registry.addViewController("/publisher/browse")              .setViewName("publisher/browse");
        registry.addViewController("/403")                           .setViewName("403");
        registry.addViewController("/App")                           .setViewName("App");
        registry.addViewController("/dialogs/dialog_find_hdr_by_req").setViewName("dialogs/dialog_find_hdr_by_req");
        registry.addViewController("/dialogs/dialog_message")        .setViewName("dialogs/dialog_message");
        registry.addViewController("/requerimientoFiltros")          .setViewName("requerimientoFiltros");
        /* Redirect / to /home */
        registry.addRedirectViewController("/", "/home");
    }

    @Bean(name = "db")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("db") DataSource ds) {
        return new JdbcTemplate(ds);
    }

}


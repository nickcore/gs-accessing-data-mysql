package com.example.accessingdatamysql;

import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.DatabaseStartupValidator;

@SpringBootApplication
public class AccessingDataMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataMysqlApplication.class, args);
	}

	@Bean
	public static BeanFactoryPostProcessor dependsOnPostProcessor() {
		return bf -> {
			// Let beans that need the database depend on the DatabaseStartupValidator
			// like the JPA EntityManagerFactory or Flyway
			String[] jpa = bf.getBeanNamesForType(EntityManagerFactory.class);
			Stream.of(jpa).map(bf::getBeanDefinition).forEach(it -> it.setDependsOn("databaseStartupValidator"));
		};
	}

	@Bean
	public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
		DatabaseStartupValidator dsv = new DatabaseStartupValidator();
		dsv.setDataSource(dataSource);
		//dsv.setInterval(30);
		dsv.setValidationQuery(DatabaseDriver.MYSQL.getValidationQuery());
		return dsv;
	}
}

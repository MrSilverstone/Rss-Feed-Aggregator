package com.epitech.config;


import com.epitech.repository.UserRepository;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableMongoRepositories(basePackages = "com.epitech.repository")
public class DBConfig {

    @Value("${mongo.db.server:localhost}")
    private String host;
    
    @Bean
    public  MongoDbFactory mongoDbFactory() throws Exception {
	System.out.println("lalalala!");
	System.out.println(host);
        MongoClient mongoClient = new MongoClient(host, 27017);
        UserCredentials userCredentials = new UserCredentials("","");
        return new SimpleMongoDbFactory(mongoClient, "aggregator", userCredentials);
    }
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
}

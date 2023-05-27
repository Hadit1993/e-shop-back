package com.hadit1993.eshopback.db

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.client.HttpClientErrorException.UnprocessableEntity
import java.util.*

@Configuration
@EnableMongoRepositories(basePackages = ["com.hadit1993.eshopback.repositories"])
class MongoConfig(
    @Value("\${spring.data.mongodb.uri}")
    private val mongoUri: String,
    @Value("\${spring.data.mongodb.database}")
    private val dbName: String
): AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String {
       return dbName
    }

    override fun mongoClient(): MongoClient {

        val connectionString = ConnectionString(mongoUri)
        val mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build()
        return MongoClients.create(mongoClientSettings)


    }


    override fun getMappingBasePackages(): MutableCollection<String> {
        return Collections.singleton("com.hadit1993.eshopback")
    }


}
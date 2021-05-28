package ru.leroymerlin.random.coffee.core.model

import nonapi.io.github.classgraph.json.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document data class User(
    @Id val id: String,
    val firstName:  String,
    val surName:    String,
    val companyName:    String,
    val companyPosition:    String,
    val email:  String,
    val phone:  String,
    val telegramLogin:  String,
    val meetDate:   LocalDate,
    val meetAddress:    String
    )


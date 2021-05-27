package ru.leroymerlin.random.coffee.core.model

import nonapi.io.github.classgraph.json.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document data class User(
    @Id val id: String,
    val info:   Info,
    val company:    Company,
    val contact:    Contact,
    val meetInfo:   MeetInfo
    )
@Document data class Company(
    @Id val id: String,
    val name:   String,
    val position:   String
)
@Document data class Contact(
    @Id val id: String,
    val email:  String,
    val phone: String
)

@Document data class Info(
    @Id val id: String,
    val firstName:  String,
    val surName: String
)
@Document data class MeetInfo(
    @Id val id: String,
    val date:   LocalDate,
    val name:   String
)


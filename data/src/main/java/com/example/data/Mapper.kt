package com.example.data

import com.example.database.UserEntity
import com.example.domain.model.User
import com.example.network.dtos.UserDto


fun List<UserEntity>.toUsers(): List<User> {
    return this.map { userEntity ->
        userEntity.toDomain()
    }
}

fun UserDto.toEntity(pictureFilePath: String): UserEntity {
    return UserEntity(
        id = 0,
        gender = gender,
        firstName = name.first,
        lastName = name.last,
        title = name.title,
        city = location.city,
        state = location.state,
        country = location.country,
        postcode = location.postcode,
        email = email,
        birthDate = dob.date,
        age = dob.age,
        phone = phone,
        cell = cell,
        nationality = nat,
        pictureFilePath = pictureFilePath
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        gender = gender,
        firstName = firstName,
        lastName = lastName,
        title = title,
        city = city,
        state = state,
        country = country,
        postcode = postcode,
        email = email,
        birthDate = birthDate,
        age = age,
        phone = phone,
        cell = cell,
        nationality = nationality,
        pictureFilePath = pictureFilePath
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        gender = gender,
        firstName = firstName,
        lastName = lastName,
        title = title,
        city = city,
        state = state,
        country = country,
        postcode = postcode,
        email = email,
        birthDate = birthDate,
        age = age,
        phone = phone,
        cell = cell,
        nationality = nationality,
        pictureFilePath = pictureFilePath
    )
}
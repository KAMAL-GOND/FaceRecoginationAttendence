package com.example.facerecoginationattendence.Presentation.navigation

import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
sealed class Routes {
    @kotlinx.serialization.Serializable
    object MarkAttendence : Routes()

    @Serializable
    object AddStudent : Routes()

    @Serializable
    data class StudentProfile(var id :Long) : Routes()

    @Serializable
    object StudentPresent : Routes()


}
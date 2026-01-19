package com.example.facerecoginationattendence.Presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object MarkAttendence : Routes()

    @Serializable
    object AddStudent : Routes()

}
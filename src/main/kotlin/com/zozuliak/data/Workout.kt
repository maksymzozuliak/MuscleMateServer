package com.zozuliak.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Workout(
    @BsonId
    val id: String? = ObjectId().toString(),
    val userId: String,
    var name: String,
    var exercises: List<Exercise>? = null
)
@Serializable
data class Exercise(
    var id: Int,
    var name: String,
    var sets: List<Set>? = null,
    var group: String,
    var personalRecord: Set? = null,
)

@Serializable
data class Set(
    var weight: Float,
    var repetitions: Int
)

val  workout = Workout(
    userId = "1",
    name = "A",
    exercises = listOf(
        Exercise(id = 1, name = "Push", sets = listOf(
            Set(1f,2),
            Set(2f,4)
        ), group = "Chest", personalRecord = Set(3f,1)),
        Exercise(id = 2, name = "Pull", sets = listOf(
            Set(2f,5),
            Set(3f,3)
        ), group = "Back", personalRecord = Set(5f,1)),
    )
)
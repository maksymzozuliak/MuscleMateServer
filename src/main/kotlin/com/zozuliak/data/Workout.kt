package com.zozuliak.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Workout(
    @BsonId
    val id: String = ObjectId().toString(),
    val userId: String,
    var name: String,
    val exercises: List<Exercise>? = null
)
@Serializable
data class Exercise(
    val id: Int,
    var name: String,
)

val  workout = Workout(
    userId = "1",
    name = "A",
    exercises = listOf(
        Exercise(id = 1, name = "Chest"),
        Exercise(id = 2, name = "Legs")
    )
)
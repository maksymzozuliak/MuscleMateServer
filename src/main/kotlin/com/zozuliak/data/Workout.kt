package com.zozuliak.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Workout(
    @BsonId
    val id: String? = ObjectId().toString(),
    val userId: String,
    var name: String
)
@Serializable
data class Exercise(
    @BsonId
    val id: String? = ObjectId().toString(),
    val workoutId: String,
    var position: Int,
    var name: String,
    var sets: List<Set>? = null,
    var group: String,
    var personalRecord: Set? = null,
    var restTime: Int? = null
)

@Serializable
data class Set(
    var weight: Float,
    var repetitions: Int
)

package com.zozuliak.model

import com.zozuliak.data.Workout
import org.litote.kmongo.Id

interface WorkoutService {

    fun addWorkout(workout: Workout) : String

    fun getWorkoutsForUser(userId: String) : List<Workout>

    fun deleteById(id: String): Boolean

    fun findById(id: String): Workout?

    fun updateById(workoutId: String, exerciseId: Int, name: String): Boolean

}
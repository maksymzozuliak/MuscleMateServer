package com.zozuliak.model

import com.zozuliak.data.Workout
import org.litote.kmongo.Id

interface WorkoutService {

    fun addWorkout(workout: Workout) : String

    fun getWorkoutsForUser(userId: String) : List<Workout>

    fun deleteWorkoutById(id: String): Boolean

    fun findWorkoutById(id: String): Workout?

    fun updateWorkout(workout: Workout): Boolean

}
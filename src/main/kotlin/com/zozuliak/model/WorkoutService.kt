package com.zozuliak.model

import com.zozuliak.data.Exercise
import com.zozuliak.data.Workout
import org.litote.kmongo.Id

interface WorkoutService {

    suspend fun addWorkout(workout: Workout) : String

    suspend fun getWorkoutsForUser(userId: String) : List<Workout>

    suspend fun deleteWorkoutById(id: String): Boolean

    suspend fun findWorkoutById(id: String): Workout?

    suspend fun updateWorkout(workout: Workout): Boolean

    suspend fun addExercise(exercise: Exercise) : String

    suspend fun getExercisesForWorkout(workoutId: String) : List<Exercise>

    suspend fun deleteExerciseById(id: String): Boolean

    suspend fun findExerciseById(id: String): Exercise?

    suspend fun updateExercise(exercise: Exercise): Boolean

    suspend fun moveExercise(up: Boolean, id: String) : Boolean

}
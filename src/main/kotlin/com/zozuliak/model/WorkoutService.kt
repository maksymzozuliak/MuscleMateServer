package com.zozuliak.model

import com.zozuliak.data.Exercise
import com.zozuliak.data.Workout
import org.litote.kmongo.Id

interface WorkoutService {

    fun addWorkout(workout: Workout) : String

    fun getWorkoutsForUser(userId: String) : List<Workout>

    fun deleteWorkoutById(id: String): Boolean

    fun findWorkoutById(id: String): Workout?

    fun updateWorkout(workout: Workout): Boolean

    fun addExercise(exercise: Exercise) : String

    fun getExercisesForWorkout(workoutId: String) : List<Exercise>

    fun deleteExerciseById(id: String): Boolean

    fun findExerciseById(id: String): Exercise?

    fun updateExercise(exercise: Exercise): Boolean

    fun moveExercise(up: Boolean, id: String) : Boolean

}
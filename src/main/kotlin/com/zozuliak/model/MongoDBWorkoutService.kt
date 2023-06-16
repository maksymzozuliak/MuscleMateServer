package com.zozuliak.model

import com.zozuliak.data.Exercise
import com.zozuliak.data.Workout
import org.litote.kmongo.*

class MongoDBWorkoutService : WorkoutService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("MuscleMate")
    private val workoutCollection = database.getCollection<Workout>()
    private val exerciseCollection = database.getCollection<Exercise>()

    override fun addWorkout(workout: Workout): String {
        workoutCollection.insertOne(workout)
        return workout.id ?: ""
    }

    override fun getWorkoutsForUser(userId: String): List<Workout> {
        val workoutsForUser = workoutCollection.find(Workout::userId eq userId)
        return workoutsForUser.toList()
    }

    override fun deleteWorkoutById(id: String): Boolean {
        val deleteResult = workoutCollection.deleteOneById(id)
        return deleteResult.deletedCount == 1L
    }

    override fun findWorkoutById(id: String): Workout? {
        return workoutCollection.findOne(Workout::id eq id)
    }

    override fun updateWorkout(workout: Workout): Boolean {
        return if( workout.id != null) {
            val updateResult = workoutCollection.updateOneById(workout.id, workout)
            updateResult.wasAcknowledged()
        } else false
    }

    override fun addExercise(exercise: Exercise): String {
        exerciseCollection.insertOne(exercise)
        return exercise.id ?: ""
    }

    override fun getExercisesForWorkout(workoutId: String): List<Exercise> {
        val exercisesForWorkout = exerciseCollection.find(Exercise::workoutId eq workoutId)
        return exercisesForWorkout.toList().sortedBy { it.position }
    }

    override fun deleteExerciseById(id: String): Boolean {
        val deleteResult = exerciseCollection.deleteOneById(id)
        return deleteResult.deletedCount == 1L
    }

    override fun findExerciseById(id: String): Exercise? {
        return exerciseCollection.findOne(Exercise::id eq id)
    }

    override fun updateExercise(exercise: Exercise): Boolean {
        return if( exercise.id != null) {
            val updateResult = exerciseCollection.updateOneById(exercise.id, exercise)
            updateResult.wasAcknowledged()
        } else false
    }

    override fun moveExercise(up: Boolean, id: String): Boolean {
        val mainExercise = findExerciseById(id)
        val temp : Int
        if (mainExercise != null) {
            if (up) {
                val upperExercise = exerciseCollection.findOne(and(
                    Exercise::position eq mainExercise.position - 1,
                    Exercise::workoutId eq mainExercise.workoutId
                ))
                if (upperExercise != null) {
                    temp = mainExercise.position
                    mainExercise.position = upperExercise.position
                    upperExercise.position = temp
                    return (updateExercise(mainExercise) && updateExercise(upperExercise))
                }
            } else {
                val lowerExercise = exerciseCollection.findOne(and(
                    Exercise::position eq mainExercise.position + 1,
                    Exercise::workoutId eq mainExercise.workoutId
                ))
                if (lowerExercise != null) {
                    temp = mainExercise.position
                    mainExercise.position = lowerExercise.position
                    lowerExercise.position = temp
                    return (updateExercise(mainExercise) && updateExercise(lowerExercise))
                }
            }
        }
        return false
    }
}
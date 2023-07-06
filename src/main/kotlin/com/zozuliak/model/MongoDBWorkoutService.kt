package com.zozuliak.model

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.zozuliak.data.Exercise
import com.zozuliak.data.Workout
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.*

class MongoDBWorkoutService : WorkoutService {

    val uri = System.getenv("CONNECTION_STRING_URI")

    private val client = MongoClient.create(uri)
    private val database = client.getDatabase("MuscleMate")
    private val workoutCollection = database.getCollection<Workout>("Workout")
    private val exerciseCollection = database.getCollection<Exercise>("Exercise")

    override suspend fun addWorkout(workout: Workout): String {
        workoutCollection.insertOne(workout)
        return workout.id ?: ""
    }

    override suspend fun getWorkoutsForUser(userId: String): List<Workout> {
        val workoutsForUser = workoutCollection.find(Workout::userId eq userId)
        return workoutsForUser.toList()
    }

    override suspend fun deleteWorkoutById(id: String): Boolean {
        val deleteResult = workoutCollection.deleteOne(Workout::id eq id)
        exerciseCollection.deleteMany(Exercise::workoutId eq id)
        return deleteResult.deletedCount == 1L
    }

    override suspend fun findWorkoutById(id: String): Workout? {
        return workoutCollection.find(Workout::id eq id).firstOrNull()
    }

    override suspend fun updateWorkout(workout: Workout): Boolean {
        return if( workout.id != null) {
            val updateResult = workoutCollection.replaceOne(Workout::id eq workout.id, workout)
            updateResult.wasAcknowledged()
        } else false
    }

    override suspend fun addExercise(exercise: Exercise): String {
        val _exercise = exercise
        if (exercise.personalRecord == null && exercise.sets != null) {
            _exercise.personalRecord = exercise.sets!!.maxByOrNull { it.weight }!!
        }
        exerciseCollection.insertOne(exercise)
        return exercise.id ?: ""
    }

    override suspend fun getExercisesForWorkout(workoutId: String): List<Exercise> {
        val exercisesForWorkout = exerciseCollection.find(Exercise::workoutId eq workoutId)
        return exercisesForWorkout.toList().sortedBy { it.position }
    }

    override suspend fun deleteExerciseById(id: String): Boolean {
        val deleteResult = exerciseCollection.deleteOne(Workout::id eq id)
        return deleteResult.deletedCount == 1L
    }

    override suspend fun findExerciseById(id: String): Exercise? {
        return exerciseCollection.find(Exercise::id eq id).firstOrNull()
    }

    override suspend fun updateExercise(exercise: Exercise): Boolean {
        return if( exercise.id != null) {
            val _exercise = exercise
            if (exercise.personalRecord != null && exercise.sets != null &&
                exercise.personalRecord!!.weight < (exercise.sets!!.maxByOrNull { it.weight }?.weight ?: 0f)
            ) {
                _exercise.personalRecord = exercise.sets!!.maxByOrNull { it.weight }
            }
            val updateResult = exerciseCollection.replaceOne(Exercise::id eq exercise.id, _exercise)
            updateResult.wasAcknowledged()
        } else false
    }

    override suspend fun moveExercise(up: Boolean, id: String): Boolean {
        val mainExercise = findExerciseById(id)
        val temp : Int
        if (mainExercise != null) {
            if (up) {
                val upperExercise = exerciseCollection.find(and(
                    Exercise::position eq mainExercise.position - 1,
                    Exercise::workoutId eq mainExercise.workoutId
                )).firstOrNull()
                if (upperExercise != null) {
                    temp = mainExercise.position
                    mainExercise.position = upperExercise.position
                    upperExercise.position = temp
                    return (updateExercise(mainExercise) && updateExercise(upperExercise))
                }
            } else {
                val lowerExercise = exerciseCollection.find(and(
                    Exercise::position eq mainExercise.position + 1,
                    Exercise::workoutId eq mainExercise.workoutId
                )).firstOrNull()
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
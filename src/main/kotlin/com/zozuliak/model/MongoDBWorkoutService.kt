package com.zozuliak.model

import com.zozuliak.data.Workout
import org.litote.kmongo.*

class MongoDBWorkoutService : WorkoutService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("workout")
    private val workoutCollection = database.getCollection<Workout>()

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

}
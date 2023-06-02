package com.zozuliak.model

import com.zozuliak.data.Workout
import org.litote.kmongo.*

class MongoDBWorkoutService : WorkoutService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("workout")
    private val workoutCollection = database.getCollection<Workout>()

    override fun addWorkout(workout: Workout): String {
        workoutCollection.insertOne(workout)
        return workout.id
    }

    override fun getWorkoutsForUser(userId: String): List<Workout> {
        val workoutsForUser = workoutCollection.find(Workout::userId eq userId)
        return workoutsForUser.toList()
    }

    override fun deleteById(id: String): Boolean {
        val deleteResult = workoutCollection.deleteOneById(id)
        return deleteResult.deletedCount == 1L
    }

    override fun findById(id: String): Workout? {
        return workoutCollection.findOne(Workout::id eq id)
    }

    override fun updateById(workoutId: String, exerciseId: Int, name: String): Boolean {
        findById(workoutId)
            ?.let { workout ->
                workout.exercises?.find { it.id == exerciseId }?.name = name
                workout.let { workoutCollection.updateOneById(workout.id, workout) }
                return true
            } ?: return false
    }

}
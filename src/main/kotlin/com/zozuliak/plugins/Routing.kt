package com.zozuliak.plugins

import com.zozuliak.data.ErrorResponse
import com.zozuliak.data.Exercise
import com.zozuliak.data.Workout
import com.zozuliak.model.MongoDBWorkoutService
import com.zozuliak.model.WorkoutService
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    val service = MongoDBWorkoutService()

    routing {

        route("/workouts") {

            post {
                val workout = call.receive<Workout>()
                service.addWorkout(workout)
                    .let { id ->
                        call.respond(HttpStatusCode.Created, id)
                    }
            }

            put {
                val workout = call.receive<Workout>()

                val updatedSuccessfully = service.updateWorkout(workout)
                if (updatedSuccessfully) {
                    call.respond(HttpStatusCode.OK, updatedSuccessfully)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: ""
                val deletedSuccessfully = service.deleteWorkoutById(id)
                if (deletedSuccessfully) {
                    call.respond(HttpStatusCode.OK, deletedSuccessfully)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }

            get("/{userId}") {
                val userId = call.parameters["userId"] ?: ""
                val workoutsForUser = service.getWorkoutsForUser(userId = userId)
                call.respond(workoutsForUser)
            }
        }

        route("/exercises") {

            post {
                val exercise = call.receive<Exercise>()
                service.addExercise(exercise)
                    .let { id ->
                        call.respond(HttpStatusCode.Created, id)
                    }
            }

            put {
                val exercise = call.receive<Exercise>()

                val updatedSuccessfully = service.updateExercise(exercise)
                if (updatedSuccessfully) {
                    call.respond(HttpStatusCode.OK, updatedSuccessfully)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: ""
                val deletedSuccessfully = service.deleteExerciseById(id)
                if (deletedSuccessfully) {
                    call.respond(true)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }

            get("/single") {
                val exerciseId = call.parameters["exerciseId"] ?: ""
                val exercise = service.findExerciseById(id = exerciseId)
                if (exercise != null) {
                    call.respond(exercise)
                } else {
                    call.respond(exerciseId)
                }
            }

            get("/{workoutId}") {
                val workoutId = call.parameters["workoutId"] ?: ""
                val exercisesForWorkout = service.getExercisesForWorkout(workoutId = workoutId)
                call.respond(exercisesForWorkout)
            }

            post("/move/{id}") {
                val id = call.parameters["id"] ?: ""
                val up = call.parameters["up"]?.toBoolean()
                val movedSuccessfully = service.moveExercise(up = up ?: true, id = id)
                if (movedSuccessfully) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }

            }
        }
    }
}

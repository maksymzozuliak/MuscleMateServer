package com.zozuliak.plugins

import com.zozuliak.data.ErrorResponse
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

        route("/workout") {

            post {
                val workout = call.receive<Workout>()
                service.addWorkout(workout)
                    .let { id ->
                        call.response.headers.append("Workout-Id-Header", id)
                        call.respond(HttpStatusCode.Created)
                    }
            }

            //http://localhost:8080/workout/6479d2865bca302cef15d021?exerciseId=1&name=test
            put("/{workoutId}") {
                val workoutId = call.parameters["workoutId"]?: ""
                val exerciseId = call.parameters["exerciseId"]?.toInt() ?: 0
                val name = call.parameters["name"]?: ""

                val updatedSuccessfully = service.updateById(workoutId,exerciseId,name)
                if (updatedSuccessfully) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: ""
                val deletedSuccessfully = service.deleteById(id)
                if (deletedSuccessfully) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
                }
            }
        }

        route("/workouts") {
            get("/{userId}") {
                val userId = call.parameters["userId"] ?: ""
                val workoutsForUser = service.getWorkoutsForUser(userId = userId)
                call.respond(workoutsForUser)
            }
        }
    }
}

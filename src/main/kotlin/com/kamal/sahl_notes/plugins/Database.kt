package com.kamal.sahl_notes.plugins
import com.kamal.sahl_notes.models.*
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import java.util.UUID

var database: CoroutineDatabase? = null

fun Application.DbInit() {
    val client = KMongo.createClient().coroutine
     database = client.getDatabase("sahl_backend")
    CoroutineScope(Dispatchers.IO).launch {
        if(database?.listCollectionNames()?.contains("user") == false)
        database?.createCollection("user")
        if(database?.listCollectionNames()?.contains("note") == false)
        database?.createCollection("note")
    }
}
suspend fun register(user: User): ResponseBody<UserResponse> {
    val col = database?.getCollection<User>()
    col?.insertOne(user)
    return ResponseBody(true, user.response())
}

suspend fun login(login: LoginRequest, token : String): ResponseBody<UserResponse> {
    val col = database?.getCollection<User>()
    val user =  col?.findOne(User::email eq login.email ,  User::password eq login.password)
    user?.token = token
    user?.let { col.updateOne(User::id eq it.id, it) }
    return ResponseBody(user!=null, user?.response())
}

suspend fun getNotes(email: String): ResponseBody<List<Note>> {
    val col = database?.getCollection<Note>()
    val colU = database?.getCollection<User>()
    val user = colU?.findOne(User::email eq email)
    return ResponseBody(true, col?.find(filter = Note::userId.eq(user?.id))?.sort(sort =  descending(
        Note::createdAt)
    )?.toList())
}

suspend fun postNote(email: String, text: String): ResponseBody<Boolean> {
    val col = database?.getCollection<Note>()
    val colU = database?.getCollection<User>()
    val user = colU?.findOne(User::email eq email)
    val note= Note("${user?.id}-${UUID.randomUUID()}", text , user?.id.toString(), System.currentTimeMillis())
    return ResponseBody(true,col?.insertOne(note)?.wasAcknowledged())
}
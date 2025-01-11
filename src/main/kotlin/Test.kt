import data.datasources.credentials.CredentialsDataSource
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        println(CredentialsDataSource.getCredentials())
    }

}
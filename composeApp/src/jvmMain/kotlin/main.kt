import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.githubrepos.di.initKoin
import com.githubrepos.ui.UI

const val APP_FOLDER_NAME = "GithubRepos"

fun main() = application {
    initKoin()
    Window(onCloseRequest = ::exitApplication, title = "GithubRepos") {
        UI()
    }
}
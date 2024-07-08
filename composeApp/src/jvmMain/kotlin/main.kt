import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.githubrepos.di.initKoin
import com.githubrepos.ui.UI
import com.githubrepos.flavor.di.jvmAppName

const val APP_FOLDER_NAME = "GithubRepos"

fun main() = application {
    initKoin()
    Window(onCloseRequest = ::exitApplication, title = jvmAppName) {
        UI()
    }
}
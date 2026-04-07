import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.githubrepos.di.JvmKoinManager
import com.githubrepos.di.getJvmModuleFactories
import com.githubrepos.flavor.jvmAppName
import com.githubrepos.ui.UI

fun main() = application {
    JvmKoinManager().run {
        addModuleFactories(getJvmModuleFactories())
        startKoin()
    }
    Window(onCloseRequest = ::exitApplication, title = jvmAppName) {
        UI()
    }
}
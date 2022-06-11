import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("rainrain android library apply")
    }


}
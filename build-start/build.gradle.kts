println("rainrain include root gradle start")

// fix "build - clean project" error
tasks.register("clean", Delete::class) {
    println("rainrain build-start clean")
    delete(rootProject.buildDir)
}
println("rainrain lib ${libs}")
println("rainrain include root gradle end")
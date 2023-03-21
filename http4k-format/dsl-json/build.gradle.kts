description = "Http4k DSL JSON support"

dependencies {
    api(project(":http4k-format-core"))
    api(project(":http4k-realtime-core"))
    api("com.dslplatform:dsl-json-java8:_")
    api("com.thoughtworks.paranamer:paranamer:_")

    testImplementation(project(":http4k-core"))
    testImplementation(project(path = ":http4k-core", configuration = "testArtifacts"))
    testImplementation(project(path = ":http4k-format-core", configuration = "testArtifacts"))
    testImplementation(project(":http4k-testing-approval"))
}

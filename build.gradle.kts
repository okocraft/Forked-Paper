import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.21"
}

paperweight {
    upstreams.register("folia") {
        repo = github("PaperMC", "Folia")
        ref = providers.fileContents(rootProject.layout.projectDirectory.file("paper-ref")).asText

        patchFile {
            path = "folia-server/build.gradle.kts"
            outputFile = file("okocraft-server/build.gradle.kts")
            patchFile = file("okocraft-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "folia-api/build.gradle.kts"
            outputFile = file("okocraft-api/build.gradle.kts")
            patchFile = file("okocraft-api/build.gradle.kts.patch")
        }
        patchFile {
            path = "folia-checkstyle/build.gradle.kts"
            outputFile = file("okocraft-checkstyle/build.gradle.kts")
            patchFile = file("okocraft-checkstyle/build.gradle.kts.patch")
        }
        patchRepo("paperApi") {
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("okocraft-api/paper-patches")
            outputDir = file("paper-api")
        }
        patchDir("foliaApi") {
            upstreamPath = "folia-api"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
            patchesDir = file("okocraft-api/folia-patches")
            outputDir = file("folia-api")
        }
        patchRepo("paperCheckstyle") {
            upstreamPath = "paper-checkstyle"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("okocraft-checkstyle/paper-patches")
            outputDir = file("paper-checkstyle")
        }
        patchDir("foliaCheckstyle") {
            upstreamPath = "folia-checkstyle"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("okocraft-checkstyle/folia-patches")
            outputDir = file("folia-checkstyle")
        }
        patchRepo("paperCheckstyleConfig") {
            upstreamPath = ".checkstyle"
            patchesDir = file("okocraft-checkstyle/config-patches")
            outputDir = file(".checkstyle")
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }

    dependencies {
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            /*
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
             */
        }
    }
}

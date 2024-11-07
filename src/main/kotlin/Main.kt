package io.spektacle

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.zip.ZipFile
import kotlin.io.path.Path

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args: Array<String>) {
    val argParser = ArgParser("kpm")
    val name by argParser.option(ArgType.String, description = "Your name", shortName = "n").default("Jeremy")
    argParser.parse(args)
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, $name!")
    val compiler = K2JVMCompiler()
    val collector = object: MessageCollector {
        override fun clear() {
            println("Clear has been called")
        }

        override fun report(
            severity: CompilerMessageSeverity,
            message: String,
            location: CompilerMessageSourceLocation?
        ) {
            println("message = $message")
        }

        override fun hasErrors(): Boolean {
            println("hasErrors has been called")
            return true
        }
    }
    val arguments = K2JVMCompilerArguments().apply {
        moduleName = "test"
        destination = ".kpm/build/test.jar"
        kotlinHome = "/home/jeremy-jones/Projects/kpm/.kpm/kotlinc/"
        freeArgs = listOf("/home/jeremy-jones/Projects/kpm/src/main/kotlin/Test.kt")
    }
    var request = HttpRequest
        .newBuilder()
        .uri(URI.create("https://github.com/JetBrains/kotlin/releases/download/v2.0.21/kotlin-compiler-2.0.21.zip"))
        .GET()
        .build()
    val client = HttpClient
        .newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofFile(Path("/tmp/kotlin-compiler-2.0.21.zip")))
    unzip("/tmp/kotlin-compiler-2.0.21.zip",".kpm/")
    compiler.exec(collector, Services.Builder().build(), arguments)
}

fun unzip(zipFilePath: String, destDir: String) {
    val zipFile = ZipFile(zipFilePath)
    zipFile.use { zip ->
        zip.entries().asSequence().forEach { entry ->
            val outputFile = File(destDir, entry.name)
            if (entry.isDirectory) {
                outputFile.mkdirs()
            } else {
                outputFile.parentFile.mkdirs()
                zip.getInputStream(entry).use { input ->
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}
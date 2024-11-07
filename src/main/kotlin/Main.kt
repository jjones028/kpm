package io.spektacle

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")
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
        noStdlib = true
        noReflect = true
        classpath = ""
//        kotlinHome = System.getenv("KOTLIN_HOME")
    }
    compiler.exec(collector, Services.Builder().build(), arguments)
}
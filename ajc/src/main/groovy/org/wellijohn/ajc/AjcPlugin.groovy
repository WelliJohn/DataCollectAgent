package org.wellijohn.ajc

import org.gradle.api.GradleException;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile;
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

class AjcPlugin implements Plugin<Project> {
    void apply(Project project) {
        final def variants
        final def log = project.logger
        println("WelliJohn,start")
        log.debug "WelliJohn args: "

        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
        } else {
            throw new GradleException("The 'android' or 'android-library' plugin is required.")
        }
        println("WelliJohn,compile")

        variants.all { variant ->
            if (!variant.buildType.isDebuggable()) {
                log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
                return;
            }
            println("WelliJohn,compile start")

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                log.debug "ajc args: " + Arrays.toString(args)
                println("江伟维,compile")

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
            println("WelliJohn,compile finish")

        }
    }
}
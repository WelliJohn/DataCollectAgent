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

import java.util.jar.JarEntry
import java.util.jar.JarFile

class AjcPlugin implements Plugin<Project> {
    void apply(Project project) {
        def variants
        def log = project.logger

        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
        } else {
            throw new GradleException("The 'android' or 'android-library' plugin is required.")
        }

        variants.all { variant ->
            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String path = javaCompile.destinationDir.toString() + ";" + javaCompile.classpath.asPath
                for (String temp : path.split(";")) {
                    println("路径：" + temp);
                    if (temp.endsWith("jar")) {
                        ExtrtactClassFormJar2Dir(javaCompile.destinationDir.getParentFile().getParentFile().getAbsolutePath() + File.separator + "extract_jar", temp)
                    }
                }




                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString() + ";" + (javaCompile.destinationDir.getParentFile().getParentFile().getAbsolutePath() + File.separator + "extract_jar"),
//                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                log.debug "ajc args: " + Arrays.toString(args)

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

        }
    }

    private void ExtrtactClassFormJar2Dir(String paramDestDir, String paramJarFile) {
        JarFile jar = new JarFile(paramJarFile);
        Enumeration enums = jar.entries();
        while (enums.hasMoreElements()) {
            JarEntry file = (JarEntry) enums.nextElement();
            File f = new File(paramDestDir, file.getName());
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f = new File(paramDestDir, file.getName());
            }
            if (file.isDirectory()) { // if its a directory, create it
                f.mkdir();
                continue;
            }
            InputStream is = jar.getInputStream(file); // get the input stream
            FileOutputStream fos = new FileOutputStream(f);
            while (is.available() > 0) {  // write contents of 'is' to 'fos'
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }
    }
}
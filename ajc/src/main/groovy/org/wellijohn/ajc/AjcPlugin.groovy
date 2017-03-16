package org.wellijohn.ajc;

import org.gradle.api.Plugin
import org.gradle.api.Project;

class AjcPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('hello') {
            doLast {
                println "Hello from the GreetingPlugin"
            }
        }
    }
}
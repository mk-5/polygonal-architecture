package io.polygonal.plugin

import groovy.transform.PackageScope
import groovy.transform.TupleConstructor
import io.polygonal.DiContainer
import io.polygonal.LanguageRecognizer
import io.polygonal.verifytask.VerifyPolygonsDefaultTask
import io.polygonal.verifytask.ports.VerifyPolygonsTask
import org.gradle.api.Project
import org.gradle.api.Task

import java.nio.file.Paths

@PackageScope
@TupleConstructor(includeFields = true)
class ProjectDispatcher {
    private static final String POLYGONAL_ARCHITECTURE = "polygonalArchitecture"
    private static final String VERIFY_POLYGONS = "verifyPolygons"

    private final Project project
    private final PolygonalArchitectureExtension extension
    private final Task task

    ProjectDispatcher(Project project) {
        this.project = project
        this.extension = project.extensions.create(POLYGONAL_ARCHITECTURE, PolygonalArchitectureExtension, project)
        this.task = project.tasks.create(VERIFY_POLYGONS, VerifyPolygonsDefaultTask, project, extension)
    }

    VerifyPolygonsTask dispatch() {
        task.doFirst {
            def language = LanguageRecognizer.recognize(project)
            if (extension.sourcesDir == null) {
                extension.sourcesDir = new File(project.projectDir, "src/main/${language.name().toLowerCase()}")
            }
            if (!extension.sourcesDir.isDirectory()) {
                throw new IllegalStateException("given 'sourcesDir' (${extension.sourcesDir}) is not a directory. Please provide valid source directory for project ${project.name} by using polygonalArchitecture{ sourcesDir = 'xxx' }")
            }
            def defaultPolygonTemplate = Paths.get(extension.sourcesDir.absolutePath, "../resources/polygon.yml").toFile()
            if (extension.polygonTemplate == null && defaultPolygonTemplate.canRead()) {
                extension.polygonTemplate = defaultPolygonTemplate
            }
        }
        project.tasks.build.dependsOn(task)
        return task as VerifyPolygonsTask
    }
}

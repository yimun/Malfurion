package me.yimu.malfurion

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager

public class PluginImpl extends Transform implements Plugin<Project> {

    void apply(Project project) {
        println("=====================")
        println("Hello, Malfurion")
        println("=====================")
        def android = project.extensions.getByType(AppExtension);
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return "Malfurion";
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
    }
}
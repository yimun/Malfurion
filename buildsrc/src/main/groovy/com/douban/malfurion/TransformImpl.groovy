package com.douban.malfurion

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.gradle.api.Project

class TransformImpl extends Transform {

    private final Project project;

    public TransformImpl(Project project) {
        this.project = project;
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
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println("///===========================asm visit start===============================///")
        inputs.each { TransformInput input ->
            /**
             * 遍历目录
             */
            input.directoryInputs.each {
                DirectoryInput directoryInput ->
                    //是否是目录
                    if (directoryInput.file.isDirectory()) {
                        //遍历目录
                        directoryInput.file.eachFileRecurse {
                            File file ->
                                def className = file.name
                                //这里进行我们的处理 TODO
                                if (className.endsWith(".class")
                                        && !className.contains('R$')
                                        && !className.contains('R.class')
                                        && !className.contains("BuildConfig.class")) {
                                    injectClass(file)
                                }
                                println '//PluginImpl find file:' + file.getAbsolutePath()
                                //project.logger.
                        }
                    }
                    //处理完输入文件之后，要把输出给下一个任务
                    def dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.copyDirectory(directoryInput.file, dest)
            }

            /**
             * 遍历jar
             */
            input.jarInputs.each { JarInput jarInput ->
                //TODO 注入代码
                String jarPath = jarInput.file.absolutePath;
                String projectName = project.rootProject.name;
                /*println("--" + jarPath)
                if (jarPath.endsWith("classes.jar")) {
                    File jarFile = new File(jarPath)

                    // jar包解压后的保存路径
                    String jarZipDir = jarFile.getParent() + "/" + jarFile.getName().replace('.jar', '')

                    // 解压jar包, 返回jar包中所有class的完整类名的集合（带.class后缀）
                    List classFileList = JarZipUtil.unzipJar(jarPath, jarZipDir)

                    // 删除原来的jar包
                    jarFile.delete()

                    // 注入代码
                    for (File file: classFileList) {
                        String className = file.name
                        if (className.endsWith(".class")
                                && !className.contains('R$')
                                && !className.contains('R.class')
                                && !className.contains("BuildConfig.class")) {
                            injectClass(file)
                        }
                    }

                    // 从新打包jar
                    JarZipUtil.zipJar(jarZipDir, jarPath)

                    // 删除目录
                    FileUtils.deleteDirectory(new File(jarZipDir))
                }*/

                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(jarName + "_" + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        println '//===============asm visit end===============//'
    }

    protected void injectClass(File file) {
        ClassReader classReader = new ClassReader(file.bytes)
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new ClickEventVisitor(classWriter)
        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
        byte[] code = classWriter.toByteArray()
        FileOutputStream fos = new FileOutputStream(file.absolutePath)
        fos.write(code)
        fos.close()
    }
}
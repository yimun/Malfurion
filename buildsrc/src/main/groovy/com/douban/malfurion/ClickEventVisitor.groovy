package com.douban.malfurion

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
/**
 * Created by linwei on 2017/6/3.
 */

class ClickEventVisitor extends ClassVisitor {

    ClickEventVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        def methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {

            boolean isInject() {
                return name == "onClick"
            }

            @Override
            void visitCode() {
                super.visitCode()
            }

            @Override
            protected void onMethodEnter() {
                super.onMethodEnter()
                if (!isInject()) {
                    return
                }
                // System.out.println("Hacking Start");
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                mv.visitLdcInsn("---Hacking Start!---")
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                        "(Ljava/lang/String;)V", false)

                // System.out.println(view);
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                mv.visitVarInsn(ALOAD, 1)
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                        "(Ljava/lang/Object;)V", false)

                // Monitor.INSTANCE.onViewClick(view)
                mv.visitFieldInsn(GETSTATIC, "com/douban/malfurion/Monitor", "INSTANCE", "Lcom/douban/malfurion/Monitor;")
                mv.visitVarInsn(ALOAD, 1)
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/douban/malfurion/Monitor", "onViewClick",
                         "(Landroid/view/View;)V", false)

                // System.out.println("Hacking Over");
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                mv.visitLdcInsn("---Hacking Over---")
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                        "(Ljava/lang/String;)V", false)
            }

            @Override
            protected void onMethodExit(int opcode) {
                super.onMethodExit(opcode)
            }
        }
        return methodVisitor
    }
}

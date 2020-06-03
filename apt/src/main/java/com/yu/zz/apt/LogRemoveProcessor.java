package com.yu.zz.apt;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * kotlin 无效
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
@SupportedAnnotationTypes({"*"})//注意替换成你自己的注解名
public class LogRemoveProcessor extends AbstractProcessor {
    private Trees mTrees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTrees = Trees.instance(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "RAIN, ====");
            roundEnvironment.getRootElements().stream()
                    .filter(it -> it.getKind() == ElementKind.CLASS)
                    .forEach(it -> ((JCTree) mTrees.getTree(it)).accept(new LogTreeTranslator()));
        }
        return false;
    }

    private static class LogTreeTranslator extends TreeTranslator {
        @Override
        public void visitBlock(JCTree.JCBlock jcBlock) {
            super.visitBlock(jcBlock);
            List<JCTree.JCStatement> jcStatements = jcBlock.getStatements();
            if (jcStatements == null || jcStatements.isEmpty()) {
                return;
            }
            List<JCTree.JCStatement> replace = List.nil();
            for (JCTree.JCStatement block : jcStatements) {
                if (block.toString().startsWith("Log.")) {
                    continue;
                }
                replace = replace.append(block);
            }
            jcBlock.stats = replace;
        }
    }
}

package 

import dev.turtin.turtinlib.client.command.CommandHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

public class CommandAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement requiredAnnotation = processingEnv.getElementUtils().getTypeElement("dev.turtin.turtinlib.client.command.Command");

        if (requiredAnnotation == null) {
            return false;
        }

        roundEnv.getRootElements().forEach((root) -> {
            for (TypeElement type : ElementFilter.typesIn(List.of(root))) {
                if (type.getKind() != ElementKind.CLASS) {
                    continue;
                }

                TypeElement interfaceElement = processingEnv.getElementUtils().getTypeElement(CommandHandler.class.getCanonicalName());

                if (type.getInterfaces().stream().anyMatch((item) -> processingEnv.getTypeUtils().isSameType(interfaceElement.asType(), item))) {
                    List<ExecutableElement> classMethods = ElementFilter.methodsIn(type.getEnclosedElements());
                    List<ExecutableElement> interfaceMethods = ElementFilter.methodsIn(interfaceElement.getEnclosedElements());

                    for (ExecutableElement method : classMethods) {
                        if (interfaceMethods.stream().anyMatch((iMethod) -> processingEnv.getElementUtils().overrides(method, iMethod, type))
                                && method.getAnnotationMirrors().stream().anyMatch((annotation) -> processingEnv.getTypeUtils().isSameType(annotation.getAnnotationType(), requiredAnnotation.asType()))) {
                            System.out.println("Annotation Clear");
                        } else {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Invalid annotation", type); // temp logging
                        }
                    }
                }
            }
        });

        return false;
    }
}

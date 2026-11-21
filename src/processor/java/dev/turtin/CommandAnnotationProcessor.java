package dev.turtin;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_25)
public class CommandAnnotationProcessor extends AbstractProcessor {
    String[] interfaces = {"dev.turtin.turtinlib.client.command.CommandHandler", "dev.turtin.turtinlib.client.command.SubCommand"};

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement requiredAnnotation = processingEnv.getElementUtils().getTypeElement("dev.turtin.turtinlib.client.command.annotations.Command");

        if (requiredAnnotation == null) {
            return false;
        }

        roundEnv.getRootElements().forEach((root) -> {
            for (TypeElement type : ElementFilter.typesIn(List.of(root))) {
                if (type.getKind() != ElementKind.CLASS) {
                    continue;
                }
                checkClass(type, requiredAnnotation);
            }
        });

        return false;
    }

    private void checkClass(TypeElement type, TypeElement requiredAnnotation) {
        List<TypeElement> interfaceElements = new ArrayList<>();
        for (String i : interfaces) {
            interfaceElements.add(processingEnv.getElementUtils().getTypeElement(i));
        }

        if (validateImplementation(type, interfaceElements)) {
            interfaceElements.forEach((interfaceElement) -> {
                List<ExecutableElement> classMethods = ElementFilter.methodsIn(type.getEnclosedElements());
                List<ExecutableElement> interfaceMethods = ElementFilter.methodsIn(interfaceElement.getEnclosedElements());

                for (ExecutableElement method : classMethods) {
                    if (interfaceMethods.stream().anyMatch((iMethod) -> processingEnv.getElementUtils().overrides(method, iMethod, type))) {
                        if (!method.getAnnotationMirrors().stream().anyMatch((annotation) -> processingEnv.getTypeUtils().isSameType(annotation.getAnnotationType(), requiredAnnotation.asType()))) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Missing annotation, try adding \"@Command(name={...})\"", method);
                        }
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotations passing for " + type.getQualifiedName());
                    }
                }
            });
        }

        for (TypeElement element : ElementFilter.typesIn(type.getEnclosedElements())) { // Check inner classes
            if (element.getKind() == ElementKind.CLASS) {
                checkClass(element, requiredAnnotation);
            }
        }
    }

    private boolean validateImplementation(TypeElement type, List<TypeElement> interfaceElements) {
        boolean passing = false;

        for (TypeMirror element : type.getInterfaces()) {
            for (TypeElement i : interfaceElements) {
                passing = passing || processingEnv.getTypeUtils().isSameType(i.asType(), element);
            }
        }
        return passing;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

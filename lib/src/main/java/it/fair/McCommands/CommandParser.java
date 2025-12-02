package it.fair.McCommands;

import io.github.classgraph.ClassGraph;

import java.lang.reflect.Method;
import java.util.*;


public class CommandParser {

    final private String pkgPath;
    private final Map<String, Command> classes;
    private final CommandTree commandTree;
    private final Map<String, Command> subclasses;

    public CommandParser(String pkgPath) {
        this.pkgPath = pkgPath;
        this.classes = new HashMap<>();
        this.subclasses = new HashMap<>();
        this.findClasses();
        this.findCommandCall();

        this.commandTree = new CommandTree(this.pkgPath);
        this.createCommandTree();
    }


    private void createCommandTree() {
        for (var key : this.classes.keySet()) {
            Command command = this.classes.get(key);
            this.commandTree.addCommand(command);
            createCommandTree(command);
        }
    }

    public void createCommandTree(Command command) {
        command.subPackages().forEach(
                subPkg -> this.subclasses
                        .values()
                        .stream()
                        .filter(c -> c.pkg().equals(subPkg))
                        .forEach(c -> {
                            this.commandTree.addSubCommand(command, c);
                            this.createCommandTree(c);
                        }));
    }

    private void findClasses() {
        try (var scan = new ClassGraph().enableAnnotationInfo()
                .enableClassInfo()
                .enableMethodInfo()
                .acceptPackages(this.pkgPath)
                .scan()) {
            scan
                    .getClassesWithAnnotation(CommandClass.class.getName()).
                    forEach(cl -> {
                        var clazz = cl.loadClass();
                        var subCommands = Arrays.stream(clazz.getAnnotation(CommandClass.class).subCommandClass()).toList();
                        var alias = clazz.getAnnotation(CommandClass.class).alias().isEmpty() ? clazz.getSimpleName() : clazz.getAnnotation(CommandClass.class).alias();
                        if (clazz.getPackageName().equals(this.pkgPath)) {
                            this.classes.put(cl.getName(), new Command(clazz.getPackageName(), clazz, new ArrayList<>(), alias, subCommands));
                        } else {
                            this.subclasses.put(cl.getName(), new Command(clazz.getPackageName(), clazz, new ArrayList<>(), alias, subCommands));
                        }
                    });
        }
    }


    private void findCommandCall() {
        findCommandCall(this.classes);
        findCommandCall(this.subclasses);
    }

    private static void findCommandCall(Map<String, Command> clazzes) {
        clazzes.forEach((className, clazz) ->
                Arrays
                        .stream(clazz.clazz().getMethods())
                        .filter(method -> method.getAnnotation(CommandCall.class) != null)
                        .forEach(method -> clazz.method().add(method)));
    }

    public CommandTree getCommandTree() {
        return this.commandTree;
    }
}

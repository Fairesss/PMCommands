package it.fair.McCommands;

import io.github.classgraph.ClassGraph;

import java.lang.reflect.Method;

import java.util.*;


public class CommandRegister {

    final private String pkgPath;
    private final Map<String, Command> clazzes;
    private final Map<String, List<Method>> methods;
    private final CommandTree commandTree;
    private final Map<String, Command> subClazzes;

    public CommandRegister(String pkgPath) {
        this.pkgPath = pkgPath;
        this.clazzes = new HashMap<>();
        this.methods = new HashMap<>();
        this.subClazzes = new HashMap<>();
        this.findClasses();
        this.findCommandCall();

        this.commandTree = new CommandTree(this.pkgPath);
        this.createCommandTree();
    }

    private void createCommandTree() {
        for (var key : this.clazzes.keySet()) {
            Command command = this.clazzes.get(key);
            this.commandTree.addCommand(command);

            command.subPackages().forEach(
                    subPkg -> this.subClazzes.values().stream()
                            .filter(c -> c.pkg().equals(subPkg))
                            .forEach(
                                    c -> this.commandTree.addSubCommand(command, c)
                            )
            );

        }
    }


    private void findClasses() {
        try (var scan = new ClassGraph()
                .enableAnnotationInfo()
                .enableClassInfo()
                .enableMethodInfo()
                .acceptPackages(this.pkgPath)
                .scan()) {
            scan
                    .getClassesWithAnnotation(CommandClass.class.getName())
                    .forEach(cl -> {
                        var clazz = cl.loadClass();
                        var subCommands = Arrays
                                .stream(clazz
                                        .getAnnotation(CommandClass.class)
                                        .subCommandClass()).toList();

                        if (clazz.getPackageName().equals(this.pkgPath)) {
                            this.clazzes.put(cl.getName(), new Command(clazz.getPackageName(), clazz, new ArrayList<>(), subCommands));
                        } else {
                            this.subClazzes.put(cl.getName(), new Command(clazz.getPackageName(), clazz, new ArrayList<>(), subCommands));
                        }
                    });
        }
    }


    private void findCommandCall() {
        findCommandCall(this.clazzes);
        findCommandCall(this.subClazzes);
    }

    private static void findCommandCall(Map<String, Command> clazzes) {
        clazzes.forEach((className, clazz) -> Arrays.stream(clazz.clazz().getMethods())
                .filter(method -> method.getAnnotation(CommandCall.class) != null)
                .forEach(method -> clazz.method().add(method)));
    }

    public Map<String, Command> getClazzes() {
        return this.clazzes;
    }

    public Map<String, List<Method>> getMethods() {
        return this.methods;
    }

    public CommandTree getCommandTree() {
        return this.commandTree;
    }
}

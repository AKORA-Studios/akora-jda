package utilitys;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import static main.Bot.jda;

public class Module {
    private String name;
    private ArrayList<String> command;
    private String syntax;
    private String description;
    private Collection<Permission> permissions;
    private EmbedGenerator embedGenerator;
    private ModuleType type;

    public Module(String name, ArrayList<String>command, String syntax, String description, Collection<Permission> permissions, ModuleType type) {
        this.name = name;
        this.command = command;
        this.syntax = syntax;
        this.description = description;
        this.permissions = permissions;
        this.type = type;
        this.embedGenerator = new EmbedGenerator();
    }

    public void onExecute(Message msg, String[] args) {

    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getCommand() {
        return command;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public boolean checkPermission(EnumSet<Permission>perms) {
        if (Arrays.asList(permissions).containsAll(Arrays.asList(perms))) {
            return true;
        } else {
            return false;
        }
    }

    public ModuleType getType() {
        return type;
    }

    public EmbedGenerator getEmbedGenerator() {
        return embedGenerator;
    }
}
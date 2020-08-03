package main;

import database.Config;
import database.dbHandler;
import main.listener.CommandListener;
import main.listener.GuildEventListener;
import main.listener.ReactionListener;
import modules.economy.Balance;
import modules.economy.Pay;
import modules.economy.Profile;
import modules.fun.Cat;
import modules.fun.Dog;
import modules.fun.Pixabay;
import modules.fun.RandomPixabay;
import modules.info.Info;
import modules.info.Roleinfo;
import modules.info.Serverinfo;
import modules.info.Userinfo;
import modules.misc.*;
import modules.moderation.*;
import modules.music.Join;
import modules.music.Leave;
import modules.music.Play;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import utilitys.EmbedGenerator;
import utilitys.Module;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;

public class Bot extends ListenerAdapter {
    public static boolean test = false;
    public static boolean docker = false;

    private static EmbedGenerator embedGenerator;
    private static JDABuilder builder;
    public static JDA jda;

    public static ArrayList<Module> modules;
    public static Config config;
    public static String prefix;
    public static dbHandler dbHandler;

    //public static ArrayList<String> copyedServer = new ArrayList<String>();
    //public static ArrayList<Long> copier_id = new ArrayList<Long>();

    public static void main(String args[]) throws LoginException {

        embedGenerator = new EmbedGenerator();

        modules = new ArrayList<Module>();
        //Misc
        add(new PingPong());
        add(new Help());
        add(new Performance());
        add(new Color());
        add(new Invite());

        //Info
        add(new Userinfo());
        add(new Roleinfo());
        add(new Serverinfo());
        add(new Info());

        //Economy
        add(new Profile());
        add(new Balance());
        add(new Pay());

        //Music
        add(new Join());
        add(new Play());
        add(new Leave());

        //Moderation
        add(new Kick());
        add(new Ban());
        add(new NSFW());
        add(new Slowmode());
        add(new Mute());
        add(new Deaf());
        add(new Rolecolor());
        //add(new CopyServer());

        //FUN
        add(new Cat());
        add(new Dog());
        add(new Pixabay());
        add(new RandomPixabay());

        config = new Config("config.json");
        if (test) {
            prefix = config.test_prefix;
        } else {
            prefix = config.prefix;
        }
        System.out.println(prefix);
        dbHandler = new dbHandler();

        initializeBot();
    }

    @Override
    public void onReady(ReadyEvent event) {
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, prefix + "help auf " + jda.getGuilds().size() + " Servern", "https://discord.gg/vEkS7Q9"));
        System.out.println("Started Bot successfully!");
    }

    private static void initializeBot() {
        String token = "";
        if (test) {
            token = config.test_token;
        } else {
            token = config.token;
        }

        builder = JDABuilder.createDefault(token);

        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, " booting up", "https://discord.gg/vEkS7Q9"));

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        for (GatewayIntent intent : GatewayIntent.values()) {
            builder.enableIntents(intent);
        }

        builder.addEventListeners(
                new Bot(),
                new CommandListener(),
                new ReactionListener(),
                new GuildEventListener()
        );

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        System.out.println("Starting as " + jda.getSelfUser().getAsTag());
    }

    public static void restartBot(boolean force) {
        System.out.println("Attempting to restart Bot");
        if (force) {
            jda.shutdownNow();
        } else {
            jda.shutdown();
        }

        //initializeBot();
        try {
            processManager.restartApplication(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(Module mod) {
        modules.add(mod);
    }
}
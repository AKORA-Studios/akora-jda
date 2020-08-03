package modules.misc;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import utilitys.Module;
import utilitys.ModuleType;

import java.util.ArrayList;
import java.util.Arrays;

public class PingPong extends Module {

    public PingPong() {
        super(
                "Ping", new ArrayList<String>(Arrays.asList(new String[]{"ping", "pong"})),
                "ping", "Zeigt dir den Ping des Bot's an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.FUN
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        long time = System.currentTimeMillis();
        msg.getChannel().sendMessage("Pong!") /* => RestAction<Message> */
                .queue(response /* => Message */ -> {
                    response.editMessage("> Ping: `" + (System.currentTimeMillis() - time) + " ms`").queue();
                });
    }
}
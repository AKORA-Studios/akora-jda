package modules.misc;

import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import utilitys.Module;
import utilitys.ModuleType;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class Performance extends Module {

    public Performance() {
        super(
                "Performance", new ArrayList<String>(Arrays.asList(new String[]{"performance", "workload"})),
                "performance", "Zeigt dir den Performance des Bot's an",
                new ArrayList<Permission>(Arrays.asList()),
                ModuleType.MISC
        );
    }

    @Override
    public void onExecute(Message msg, String[] args) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuload, com_cpuload, ramuse, total_ram;

        cpuload = round(osBean.getProcessCpuLoad(), 4)*100;
        com_cpuload = round(osBean.getSystemCpuLoad(), 4)*100;

        ramuse = round((osBean.getTotalPhysicalMemorySize()-osBean.getFreePhysicalMemorySize())/(1024*1024*1024), 4);
        total_ram = round((osBean.getTotalPhysicalMemorySize())/(1024*1024*1024), 4);

        EmbedBuilder emb = getEmbedGenerator().newEmbed(msg.getAuthor());
        emb.setTitle("Performance");
        emb.addField("**CPU load(Bot)**", "`"+str(cpuload+"")+" %`", false)
                .addField("**CPU load(Host)**", "`"+str(com_cpuload+"")+" %`", true)
                .addField("**RAM use(Host)**", "`"+str(ramuse+"")+"GB / "+str(total_ram+"")+" GB ("+str(round((ramuse/total_ram),4)*100+"")+" %)`", false);
        msg.getChannel().sendMessage(emb.build()).complete();
    }

    private double round(double a, int b) {
        double c = Math.pow(10, b);
        return Math.floor(a*c)/c;
    }

    private String str(String a) {
        if (a.length() > 4) {
            return a.substring(0, 4);
        } else {
            return a;
        }
    }
}
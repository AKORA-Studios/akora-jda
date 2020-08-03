package database;

public class GuildConfig {
    public int id;
    public String welcome_msg;
    public int welcome_ch_id;
    public String goodbye_msg;
    public int goodbye_ch_id;
    public int autorole;
    public boolean economy;

    public GuildConfig(int id, String welcome_msg, int welcome_ch_id, String goodbye_msg, int goodbye_ch_id, int autorole, boolean economy) {
        this.id = id;
        this.welcome_msg = welcome_msg;
        this.welcome_ch_id = welcome_ch_id;
        this.goodbye_msg = goodbye_msg;
        this.goodbye_ch_id = goodbye_ch_id;
        this.autorole = autorole;
        this.economy = economy;
    }
}
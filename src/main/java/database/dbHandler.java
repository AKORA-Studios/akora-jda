package database;

import main.Bot;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dbHandler {

    static final String address = Bot.config.dbConfig.get("address");
    static final String port = Bot.config.dbConfig.get("port");
    static final String user = Bot.config.dbConfig.get("user");
    static final String password = Bot.config.dbConfig.get("password");

    static final String guilds_table = Bot.config.dbConfig.get("guilds_table");
    static final String economy_table = Bot.config.dbConfig.get("economy_table");
    static final String db = Bot.config.dbConfig.get("name");

    public static Connection conn = null;
    public static Statement stmt = null;

    public dbHandler() {
        openConnection();
        checkDatabaseStructure();
    }//end main

    public GuildConfig getGuildConfig(long long_id) {
        return getGuildConfig((int) long_id);
    }

    public GuildConfig getGuildConfig(String string_id) {
        long id = Long.parseLong(string_id);
        return getGuildConfig((int) id);
    }

    public GuildConfig getGuildConfig(int id) {
        checkConnection();

        String sql = "SELECT * " +
                "FROM `" + guilds_table + "` " +
                "WHERE id=" + id + ";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            List<HashMap> result_list = resultSetToArrayList(rs);
            if (result_list.size() < 1) {
                return null;
            } else {
                HashMap result = result_list.get(0);
                boolean economy = false;
                if (result.get("modules/economy").toString() == "1") {
                    economy = true;
                }

                return new GuildConfig(
                        Integer.parseInt(result.get("id").toString()),
                        result.get("welcome_msg").toString(),
                        Integer.parseInt(result.get("welcome_ch_id").toString()),
                        result.get("goodbye_msg").toString(),
                        Integer.parseInt(result.get("goodbye_ch_id").toString()),
                        Integer.parseInt(result.get("autorole").toString()),
                        economy
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setGuildConfig(GuildConfig conf) {
        checkConnection();

        int eco = 0;
        if (conf.economy) {
            eco = 1;
        }

        String sql = "UPDATE `" + guilds_table + "` SET " +
                "`id`=" + conf.id + "," +
                "`welcome_msg`=\"" + conf.welcome_msg + "\"," +
                "`welcome_ch_id`=" + conf.welcome_ch_id + "," +
                "`goodbye_msg`=\"" + conf.goodbye_msg + "\"," +
                "`goodbye_ch_id`=" + conf.goodbye_ch_id + "," +
                "`autorole`=" + conf.autorole + "," +
                "`economy`=" + conf.economy + " " +
                "WHERE id=" + eco + ";";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGuildConfig(GuildConfig conf) {
        checkConnection();

        String sql = "INSERT INTO `" + guilds_table + "` VALUES (" +
                conf.id + "," +
                "\"" + conf.welcome_msg + "\"," +
                conf.welcome_ch_id + "," +
                "\"" + conf.goodbye_msg + "\"," +
                conf.goodbye_ch_id + "," +
                conf.autorole + "," +
                conf.economy + ");";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkGuildConfig(int id) {
        if (Bot.dbHandler.getGuildConfig(id) == null) {
            GuildConfig conf = new GuildConfig(
                    id,
                    "",
                    0,
                    "",
                    0,
                    0,
                    true
            );
            Bot.dbHandler.addGuildConfig(conf);
        }
    }

    //==================================================================================================================

    public UserEconomy getUserEconomy(long long_id) {
        return getUserEconomy((int) long_id);
    }

    public UserEconomy getUserEconomy(String string_id) {
        long id = Long.parseLong(string_id);
        return getUserEconomy((int) id);
    }

    public UserEconomy getUserEconomy(int id) {
        checkConnection();

        String sql = "SELECT * " +
                "FROM `" + economy_table + "` " +
                "WHERE id=" + id + ";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            List<HashMap> result_list = resultSetToArrayList(rs);
            if (result_list.size() < 1) {
                return null;
            } else {
                HashMap result = result_list.get(0);

                return new UserEconomy(
                        Integer.parseInt(result.get("id").toString()),
                        Integer.parseInt(result.get("xp").toString()),
                        Integer.parseInt(result.get("level").toString()),
                        Integer.parseInt(result.get("balance").toString())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setUserEconomy(UserEconomy eco) {
        checkConnection();

        String sql = "UPDATE `" + economy_table + "` SET " +
                "`id`=" + eco.id + "," +
                "`xp`=" + eco.xp + "," +
                "`level`=" + eco.level + "," +
                "`balance`=" + eco.balance + " " +
                "WHERE id=" + eco.id + ";";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUserEconomy(UserEconomy eco) {
        checkConnection();

        String sql = "INSERT INTO `" + economy_table + "` VALUES (" +
                eco.id + ", " +
                eco.xp + ", " +
                eco.level + ", " +
                eco.balance + ");";

        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkUserEconomy(int id) {
        if (Bot.dbHandler.getUserEconomy(id) == null) {
            UserEconomy eco = new UserEconomy(
                    id,
                    0,
                    0,
                    100
            );
            Bot.dbHandler.addUserEconomy(eco);
        }
    }

    //======================================================

    public List<HashMap> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colums = md.getColumnCount();
        ArrayList<HashMap> list = new ArrayList();
        while (rs.next()) {
            HashMap row = new HashMap(colums);
            for (int i = 1; i <= colums; i++) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    //==========================================================

    public void checkConnection() {
        try {
            if (stmt.isClosed()) {
                if (conn.isClosed()) {
                    closeConnection();
                    openConnection();
                } else {
                    stmt = conn.createStatement();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() {
        try {
            //STEP 1: Register JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to " + address + ":" + port + " as " + user);
            conn = DriverManager.getConnection("jdbc:mariadb://" + address, user, password);
            System.out.println("Connected database successfully...\n");

            stmt = conn.createStatement();

            System.out.println("Preparing Database...");
            System.out.println(" > Using Database `" + db + "`");

            String db_sql = "USE `" + db + "`";
            stmt.executeUpdate(db_sql);
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public void checkDatabaseStructure() {
        try {
            System.out.println(" > Checking `" + economy_table + "` table");
            String economy_sql = "CREATE TABLE IF NOT EXISTS `" + db + "`.`" + economy_table + "` " +
                    "( `id` INT(18) NOT NULL , " +
                    "`xp` INT NOT NULL , " +
                    "`level` INT NOT NULL , " +
                    "`balance` INT NOT NULL , " +
                    "PRIMARY KEY `ID` (`id`)) ENGINE = InnoDB;";
            stmt.executeUpdate(economy_sql);

            System.out.println(" > Checking `" + guilds_table + "` table\n");
            String guild_settings_sql = "CREATE TABLE IF NOT EXISTS `" + db + "`.`" + guilds_table + "` " +
                    "( `id` INT(18) NOT NULL , " +
                    "`welcome_msg` TEXT NOT NULL , " +
                    "`welcome_ch_id` INT(18) NOT NULL , " +
                    "`goodbye_msg` TEXT NOT NULL , " +
                    "`goodbye_ch_id` INT(18) NOT NULL , " +
                    "`autorole` INT(18) NOT NULL , " +
                    "`economy` BOOLEAN NOT NULL , " +
                    "PRIMARY KEY `ID` (`id`)) ENGINE = InnoDB;";
            stmt.executeUpdate(guild_settings_sql);

            System.out.println("Checked tables in database `" + db + "`");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (stmt != null) {
                conn.close();
            }
        } catch (SQLException se) {

        }// do nothing

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }//end finally try
    }
}
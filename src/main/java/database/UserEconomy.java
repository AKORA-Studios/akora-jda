package database;

public class UserEconomy {
    public int id;
    public int xp;
    public int level;
    public int balance;

    public UserEconomy(int id, int xp, int level, int balance) {
        this.id = id;
        this.xp = xp;
        this.level = level;
        this.balance = balance;
    }
}
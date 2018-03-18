package ga.fmohican.sql;


import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


class SQLConfig {
    private final SQLWL plugin;

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    public static CommentedConfigurationNode config;
    public static String sqlhost;
    public static String sqluser;
    public static String sqlpass;
    public static String sqldb;
    public static int sqlport;
    public static String stabname;
    public static String susers;
    public static String swhitelist;

    public SQLConfig(SQLWL main) throws IOException, ObjectMappingException {
        plugin = main;
        loader = HoconConfigurationLoader.builder().setPath(plugin.defaultConf).build();
        config = loader.load();
        configCheck();
    }
    public void configCheck() throws IOException, ObjectMappingException {

        if (!plugin.defaultConfFile.exists()) {
            plugin.defaultConfFile.createNewFile();
        }
        config.setComment("Currently the plugin support only MySQL and MariaDB");
        sqlhost = check(config.getNode("SQL", "host"), "localhost", "The address of SQL Server").getString();
        sqlport = check(config.getNode("SQL", "port"), 3306, "The port of SQL Server, if you don't know leave as is (3306)").getInt();
        sqluser = check(config.getNode("SQL", "user"), "MySQLUSER", "The user of SQL Server").getString();
        sqlpass = check(config.getNode("SQL", "password"), "MySuperSecurePassword", "Password for specify user").getString();
        sqldb = check(config.getNode("SQL", "sqldb"), "MySQLTabel", "Name of the database").getString();
        stabname = check(config.getNode("Structure", "Tabelname"), "users", "Name of the database").getString();
        susers = check(config.getNode("Structure", "usercolumn"), "user", "Name of the database").getString();
        swhitelist = check(config.getNode("Structure", "whitelistcolumn"), "whitelist", "The whitelist column, For user who are allowed the value should be 1").getString();
        loader.save(config);
    }

    private CommentedConfigurationNode check(CommentedConfigurationNode node, Object defaultValue, String comment) {
        if (node.isVirtual()) {
            node.setValue(defaultValue).setComment(comment);
        }
        return node;
    }
    private CommentedConfigurationNode checkList(CommentedConfigurationNode node, Integer[] defaultValue, String comment) {
        if (node.isVirtual()) {
            node.setValue(Arrays.asList(defaultValue)).setComment(comment);
        }
        return node;
    }
    private CommentedConfigurationNode checkList(CommentedConfigurationNode node, String[] defaultValue, String comment) {
        if (node.isVirtual()) {
            node.setValue(Arrays.asList(defaultValue)).setComment(comment);
        }
        return node;
    }
}
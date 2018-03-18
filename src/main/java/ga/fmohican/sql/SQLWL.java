package ga.fmohican.sql;


import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


@Plugin(id = "sqlwhitelist", name = "SQL WhiteList", authors = "Fmohican", description = "Whitelist based on SQL", version = "0.0.2", url = "https://fmohican.ga/")
public class SQLWL
{
    public static SQLWL INSTANCE;
    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path defaultConf;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public File defaultConfFile;

    @Inject
    private Logger logger;
    public Logger getLogger(){
        return logger;
    }
    private SQLConfig config;
    @Listener
    public void Init(GameInitializationEvent event) throws IOException, ObjectMappingException {
        this.config = new SQLConfig(this);
        getLogger().info("PreInitialization SQL WhiteList [Checking config]");
    }

    @Listener
    public void join(ClientConnectionEvent.Login e) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://" + SQLConfig.sqlhost +":"+ SQLConfig.sqlport +"/" + SQLConfig.sqldb + "", SQLConfig.sqluser, SQLConfig.sqlpass);
            Statement stmt = con.createStatement();
            Optional<String> pname = e.getProfile().getName();
            String newname = pname.toString();
            newname = newname.replace("Optional[","").replace("]","");
            ResultSet rs = stmt.executeQuery("select `"+SQLConfig.susers+"` from `"+SQLConfig.stabname+"` where ( `"+SQLConfig.susers+"`='" + newname + "' AND `"+SQLConfig.swhitelist+"` = 1 );");
            if (rs.first()) {
                getLogger().info("Connection accepted from "+newname+" they are on the whitelist");
                }
            else {
                    getLogger().warn("The user "+newname+" was disconnected because are not on whitelist");
                    e.setMessage(TextSerializers.FORMATTING_CODE.deserialize("I can't find you in whitelist"));
                    e.setCancelled(true);
                }
            }
        catch (Exception a) {
            getLogger().error("There is an ERROR!" + a.getMessage());
            e.setMessage(TextSerializers.FORMATTING_CODE.deserialize("SQLWhitelist isn't work well. Check configuration and try again."));
            e.setCancelled(true);
        }
    }
    @Listener
    public void start(GameStartedServerEvent e) throws IOException
    {
        INSTANCE = this;
    }
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        getLogger().info("SQL WhiteList - Enabled");
    }
}


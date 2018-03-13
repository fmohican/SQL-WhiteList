package ga.fmohican.sql;


import org.slf4j.Logger;
import com.google.inject.Inject;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.serializer.TextSerializers;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;


@Plugin(id = "sqlwhitelsit", name = "SQL WhiteList", authors = "Fmohican", description = "SQL Based whitelist", version = "0.0.1")
public class SQLWL
{
    public static SQLWL INSTANCE;
    @Inject
    private Logger logger;
    public Logger getLogger(){
        return logger;
    }

    @Listener
    public void join(ClientConnectionEvent.Login e) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://SQL-SERVER-ADDRESS:SQL-SERVER-PORT/SQL-DATABASE", "SQL-USER", "SQL-PASSWORD");
            Statement stmt = con.createStatement();
            Optional<String> pname = e.getProfile().getName();
            String newname = pname.toString();
            newname = newname.replace("Optional[","").replace("]","");
            ResultSet rs = stmt.executeQuery("select `user` from `beta` where ( `user`='" + newname + "' AND `whitelist` = 1 );");
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
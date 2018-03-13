## How to setup ?
1. Open the file from src/main/ga/fmohican/sql/SQLWL.java
2. Edit 
```
DriverManager.getConnection("jdbc:mysql://SQL-SERVER-ADDRESS:SQL-SERVER-PORT/SQL-DATABASE", "SQL-USER", "SQL-PASSWORD");
```
4. Edit with what you need. The script will count result. So if in database exist any entity the player will be allowed on server
```
ResultSet rs = stmt.executeQuery("select `user` from `beta` where ( `user`='" + newname + "' AND `whitelist` = 1 );");
```
5. Run gradlew build

6. Copy complied file from /build/libs/ the file will be named SQLWhitelist-0.0.1.jar

7. Enjoy

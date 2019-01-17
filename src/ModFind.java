import java.util.*;
import java.io.*;
import java.time.Instant;

public class ModFind {
    // the file control module, singleton
    // singleton
    private static final ModFind instance = new ModFind();
    public static ModFind getInstance(){
        return instance;
    }

    // private constructor
    private ModFind() {}

    private ArrayList<UserInfo> users = new ArrayList<UserInfo>();
    public String nickname = "Anonymous";

    public ArrayList<UserInfo> getAvailableUsers() {
        // Find users who are available in the last five minutes
        // All user info entries expire in 5 minutes

        ArrayList<UserInfo> validUsers = new ArrayList<UserInfo>();
        ListIterator<UserInfo> li = users.listIterator();
        while (li.hasNext()) {
            UserInfo ui = li.next();
            if (ui.LastFind.isBefore(Instant.now()) && ui.LastFind.isAfter(Instant.now().minusSeconds(5 * 60))) {
                // add to list
                validUsers.add(ui);
            }
        }
        return validUsers;
    }

    public boolean isAvailable(String ip) {
        ListIterator<UserInfo> li = users.listIterator();
        while (li.hasNext()) {
            UserInfo ui = li.next();
            if (ui.LastFind.isBefore(Instant.now()) && ui.LastFind.isAfter(Instant.now().minusSeconds(5 * 60))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Public API: send a file to the remote side
     * @param fileAbsPath The ABSOLUTE PATH to the file. 
     * Maybe you should search for the difference between the ABSOLUTE path and the RELATIVE path
     */
    public void sendMyself(String myip, String host) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (!myip.equals("0.0.0.0")) {
                baos.write(Utility.intToByte(nickname.getBytes().length), 0, 4);
                baos.write(nickname.getBytes(), 0, nickname.getBytes().length);

                // Serialize current time
                byte[] b = Utility.serialize(Instant.now());

                baos.write(Utility.intToByte(b.length), 0, 4);
                baos.write(b, 0, b.length);

                /* Send */
                BaseSender.getInstance().sendBytes(host, "BEAT", baos.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Protected API: receive a file from the remote side
     * You should NOT call this method, this method is automatically called whenever a file is received
     * @param byteArr The buffer
     */
    public void receiveHeartbeat(String IP, byte[] byteArr) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);

            byte[] nicknamesizeArr = new byte[4];
            bais.read(nicknamesizeArr, 0, 4);
            int nicknamesize = Utility.byteToInt(nicknamesizeArr);
            byte[] nicknameArr = new byte[nicknamesize];
            bais.read(nicknameArr, 0, nicknamesize);
            String nickname = new String(nicknameArr);

            byte[] lastfindsizeArr = new byte[4];
            bais.read(lastfindsizeArr, 0, 4);
            int lastfindsize = Utility.byteToInt(lastfindsizeArr);
            byte[] lastfindArr = new byte[lastfindsize];
            bais.read(lastfindArr, 0, lastfindsize);
            Instant lastfind = (Instant) Utility.deserialize(lastfindArr);

            // update
            ListIterator<UserInfo> li = users.listIterator();
            while (li.hasNext()) {
                UserInfo ui = li.next();
                if (ui.IP.equals(IP)) {
                    // update
                    ui.Nickname = nickname;
                    ui.LastFind = lastfind;
                    return;
                }
            }
            // not found, add
            UserInfo ui = new UserInfo();
            ui.IP = IP;
            ui.Nickname = nickname;
            ui.LastFind = lastfind;
            users.add(ui);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
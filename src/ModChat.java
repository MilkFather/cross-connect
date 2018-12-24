import java.io.*;

class ModChat {
    // the chat control module, singleton
    // singleton
    private static final ModChat instance = new ModChat();
    public static ModChat getInstance(){
        return instance;
    }

    // private constructor
    private ModChat() {}

    /**
     * Public API: send a message to remote side
     * @param message The message, in a String
     */
    public void sendMsg(String host, String message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            /* Just write the message */
            baos.write(message.getBytes(), 0, message.getBytes().length);

            BaseSender.getInstance().sendBytes(host, "CHAT", baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Protected API: receive a message
     * Please don't call it, it will read automatically
     * However please decide a way to present it whenever it is available
     * Currently it is printed on the console screen
     * @param byteArr The buffer
     */
    public void receiveMsg(String host, byte[] byteArr) {
        try {
            /* Read the message */
            String msg = new String(byteArr);

            System.out.println(host + ": " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
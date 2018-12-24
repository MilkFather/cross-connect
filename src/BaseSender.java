import java.io.*;
import java.net.*;

public class BaseSender {
    // singleton class, server
    // singleton
    private static final BaseSender instance = new BaseSender();
    public static BaseSender getInstance(){
        return instance;
    }
    private BaseSender() {}
    
    private class BaseSenderInternal {
        private Socket socket = null;
        private BufferedInputStream bfin = null;
        private BufferedOutputStream bfout = null;
    
        /**
         * Private API: connect to a remote host.
         * Do not perform any chat, file or control action unless connected.
         * @param host The remote host name, to test on local machine, just leave it "localhost" or "127.0.0.1"
         * @return Whether this action is successful
         */
        private boolean connect(String host) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, 11037), 1000);
                bfin = new BufferedInputStream(socket.getInputStream());
                bfout = new BufferedOutputStream(socket.getOutputStream());
                //System.out.println("Connected");
                return true;
            } catch (IOException e) {
                //e.printStackTrace();
                return false;
            }
        }

        /**
         * Private API: disconnect from a remote host.
         * After this action, do not perform any network action unless successfully connected to another host.
         * 
         * Make sure that there are no actions alive!
         */
        private void disconnect() {
            try {
                bfout.close();
                bfout = null;
                bfin.close();
                bfin = null;
                socket.close();
                socket = null;
                //System.out.println("Disonnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Private API: send data to remote host.
         * Do not call this function, let Mods call them.
         * @param code The action code of this data
         * @param bytes The data content
         */
        public void sendBytes(String host, String code, byte[] bytes) {
            try {
                if (connect(host)) {
                    //System.out.println(host + " -> " + code);
                    String addr = Utility.getInboundAddr();
                    bfout.write(code.getBytes(), 0, 4);
                    bfout.write(Utility.intToByte(addr.getBytes().length), 0, 4);
                    bfout.write(addr.getBytes(), 0, addr.getBytes().length);
                    bfout.write(Utility.intToByte(bytes.length), 0, 4);
                    bfout.write(bytes, 0, bytes.length);
                    bfout.flush();

                    byte[] res = new byte[4];
                    bfin.read(res, 0, 4);
                    disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendBytes(String host, String code, byte[] bytes) {
        new Thread() {
            @Override
            public void run() {
                BaseSenderInternal bsi = new BaseSenderInternal();
                bsi.sendBytes(host, code, bytes);
            }
        }.start();
    }
}


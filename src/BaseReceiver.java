import java.io.*;
import java.nio.*;
import java.net.*;

class BaseReceiver extends Thread {
    // singleton
    private static final BaseReceiver instance = new BaseReceiver();
    public static BaseReceiver getInstance() {
        return instance;
    }

    private BaseReceiver() {}
    
    public class BaseReceiverInternal {
        // the inner code, act as server to receive incoming calls
        private ServerSocket ssocket = null;
        private Socket socket = null;
        private InputStream bfin = null;
        private OutputStream bfout = null;
        
        private BaseReceiverInternal() {
            try {
                ssocket = new ServerSocket(11037);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    
        public void accept() {
            try {
                socket = ssocket.accept();
                bfin = socket.getInputStream();
                bfout = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void getInput() {
            try {
                // read first handshake
                byte[] tp = new byte[4];
                bfin.read(tp, 0, 4);  // read data type
                String cmd = new String(tp);

                String ip = socket.getInetAddress().toString().substring(socket.getInetAddress().toString().lastIndexOf("/") + 1);
                // read data size
                byte[] datasizeArr = new byte[4];
                bfin.read(datasizeArr, 0, 4);
                int dataSize = Utility.byteToInt(datasizeArr);
    
                // read the whole data
                byte[] data = new byte[dataSize];
                for (int i = 0; i < dataSize; i++) {
                    data[i] = (byte)bfin.read();
                }
    
                bfout.write("OKAY".getBytes(), 0, 4);
    
                if (cmd.equals("BEAT")) {
                    ModFind.getInstance().receiveHeartbeat(ip, data);
                } else if (cmd.equals("FILE")) {
                    ModFile.getInstance().receiveFile(ip, data);
                } else if (cmd.equals("CHAT")) {
                    ModChat.getInstance().receiveMsg(ip, data);
                } else if (cmd.equals("EXIT")) {
                    System.exit(0);  // exit
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        BaseReceiverInternal br = new BaseReceiverInternal();

        while (true) {
            br.accept();
            br.getInput();
        }
    }
}
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
        private BufferedInputStream bfin = null;
        private BufferedOutputStream bfout = null;
        
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
                bfin = new BufferedInputStream(socket.getInputStream());
                bfout = new BufferedOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void getInput() {
            //while (socket.isConnected()) {
                try {
                    //System.out.println("received");
                    // read first handshake
                    byte[] tp = new byte[4];
                    bfin.read(tp, 0, 4);  // read data type
                    String cmd = new String(tp);
                    //System.out.println("data type read");
    
                    byte[] ipsizeArr = new byte[4];
                    bfin.read(ipsizeArr, 0, 4);   // read ip data size
                    int ipsize = Utility.byteToInt(ipsizeArr);
                    //System.out.println("IP size read");
    
                    byte[] ipArr = new byte[ipsize];
                    bfin.read(ipArr, 0, ipsize);    // read ip
                    String ip = new String(ipArr);
                    //System.out.println("IP read");
    
                    // read data size
                    byte[] datasizeArr = new byte[4];
                    bfin.read(datasizeArr, 0, 4);
                    int dataSize = Utility.byteToInt(datasizeArr);
                    //System.out.println("Data size read");
    
                    // read the whole data
                    byte[] data = new byte[dataSize];
                    bfin.read(data, 0, dataSize);
                    //System.out.println("Data read");
    
                    bfout.write("OKAY".getBytes(), 0, 4);
                    bfout.flush();
    
                    //socket.close();
    
                    System.out.println(ip + " " + cmd);
    
                    if (cmd.equals("BEAT")) {
                        
                        ModFind.getInstance().receiveHeartbeat(ip, data);
                    } else if (cmd.equals("FILE")) {
                        ModFile.getInstance().receiveFile(ip, data);
                    } else if (cmd.equals("CHAT")) {
                        ModChat.getInstance().receiveMsg(ip, data);
                    } else if (cmd.equals("CTRL")) {
                        //sendFile();  // send file
                        // TODO
                    } else if (cmd.equals("EXIT")) {
                        System.exit(0);  // exit
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
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
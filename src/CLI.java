import java.io.IOException;

class CLI {
    public static void main(String[] args) {
        //BaseReceiverThread brt = new BaseReceiverThread();
        BaseReceiver.getInstance().start();
        ScannerThread st = new ScannerThread();
        //brt.start();
        //st.start();

        
        ModChat c = ModChat.getInstance();
        //c.sendMsg("127.0.0.1", "Test");
        //c.sendMsg("192.168.2.9", "Test2");
        ModFile f = ModFile.getInstance();
        
        System.out.println("Testing...");
        f.sendFile("192.168.2.9", "/Users/kevin/Desktop/yaht.pdf");
        //f.sendFile("192.168.2.9", "/Users/kevin/Desktop/Game.hs");
        //c.sendMsg("172.18.61.210", "Test3");
        //brt.stop();
        
/*
        try {
            while (true) {
                System.in.read();
                ModFind.getInstance().getAvailableUsers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
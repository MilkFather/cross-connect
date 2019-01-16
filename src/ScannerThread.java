import java.io.Console;
import java.util.*;

class ScannerThread extends Thread {

    private class V4InterfaceScannerThread extends Thread {
        private String myIP;
        private short prefixlen;

        V4InterfaceScannerThread(String __myIP, short __prefix) {
            //super();
            myIP = __myIP; prefixlen = __prefix;
        }

        private int getintfromIp(String in) {
            int[] ip = new int[4];
            String[] parts = myIP.split("\\.");

            for (int i = 0; i < 4; i++) {
                ip[i] = Integer.parseInt(parts[i]);
            }
            int ipNumbers = 0;
            for (int i = 0; i < 4; i++) {
                ipNumbers += ip[i] << (24 - (8 * i));
            }
            return ipNumbers;
        }

        private ArrayList<String> enumerateSubnetIP() {
            ArrayList<String> al = new ArrayList<String>();
            int mask = (int)(0xffffffff) << (32 - prefixlen);
            int basenetIp = getintfromIp(myIP) & mask;
            int maxRange = 0x1 << (32 - prefixlen);

            for (int i = 0; i < maxRange; i++) {
                long ip = basenetIp + i;
                String subnetip = String.valueOf((int)(ip & 0xff000000) >>> 24) + "." +
                                  String.valueOf((int)(ip & 0x00ff0000) >>> 16) + "." + 
                                  String.valueOf((int)(ip & 0x0000ff00) >>> 8)  + "." +
                                  String.valueOf((int)(ip & 0x000000ff) >>> 0);
                al.add(subnetip);
            }

            return al;
        }

        @Override
        public void run() {
            try {
                ArrayList<String> subnetIPs = enumerateSubnetIP(); 
                ListIterator<String> li = subnetIPs.listIterator();
                while (li.hasNext()) {
                    String ip = li.next();
                    if (!ip.equals(myIP)) {
                        ModFind.getInstance().sendMyself(myIP, ip);
                        sleep(10);
                    }
                }
            } catch (InterruptedException e) {
                // e.printStackTrace(); // actually does nothing
            }
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                ArrayList<NetInterfaceInfo> interfaces = Utility.getInboundAddr();
                ListIterator<NetInterfaceInfo> it = interfaces.listIterator();
                while(it.hasNext()) {
                    NetInterfaceInfo nii = it.next();
                    V4InterfaceScannerThread newScanner = new V4InterfaceScannerThread(nii.LocalIP, nii.PrefixLen);
                    newScanner.run();
                }

                sleep(90 * 1000);  // scan for every one and a half minute
            }
        } catch (InterruptedException e) {
            // e.printStackTrace(); // do nothing, please
        }
        
    }
}
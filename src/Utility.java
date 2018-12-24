import java.nio.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Utility {
    public static int byteToInt(byte[] b) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        int num = wrapped.getInt(); // 1
        return num;
    }
    
    public static byte[] intToByte(int i) {
        ByteBuffer dbuf = ByteBuffer.allocate(4);
        dbuf.putInt(i);
        byte[] bytes = dbuf.array();
        return bytes;
    }

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

    public static String getInboundAddr() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    String ip = addr.getHostAddress();
                    //System.out.println(iface.getDisplayName() + " " + ip);
                    if (ip.startsWith("192.168")) return ip;
                }
            }
            return "0.0.0.0";
        } catch (SocketException e) {
            return "0.0.0.0";
        }
    }
}
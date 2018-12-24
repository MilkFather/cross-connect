import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class ModFile {
    // the file control module, singleton
    // singleton
    private static final ModFile instance = new ModFile();
    public static ModFile getInstance(){
        return instance;
    }

    // private constructor
    private ModFile() {}

    /**
     * Public API: send a file to the remote side
     * @param fileAbsPath The ABSOLUTE PATH to the file. 
     * Maybe you should search for the difference between the ABSOLUTE path and the RELATIVE path
     */
    public void sendFile(String host, String fileAbsPath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            File f = new File(fileAbsPath);

            /* Write the file name part */
            baos.write(Utility.intToByte(f.getName().getBytes().length), 0, 4);
            baos.write(f.getName().getBytes(), 0, f.getName().getBytes().length);

            /* Write the file content part */
            byte[] filebyte = Files.readAllBytes(f.toPath());
            baos.write(Utility.intToByte(filebyte.length), 0, 4);
            baos.write(filebyte, 0, filebyte.length);

            /* Send */
            BaseSender.getInstance().sendBytes(host, "FILE", baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Protected API: receive a file from the remote side
     * You should NOT call this method, this method is automatically called whenever a file is received
     * @param byteArr The buffer
     */
    public void receiveFile(String host, byte[] byteArr) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);

            /* Read the file name part */
            byte[] fnsizeArr = new byte[4];
            bais.read(fnsizeArr, 0, 4);
            int fnsize = Utility.byteToInt(fnsizeArr);
            byte[] filenameArr = new byte[fnsize];
            bais.read(filenameArr, 0, fnsize);
            String filename = new String(filenameArr);

            File f = new File(System.getProperty("user.home") + "/Downloads/" + filename);  // Default save the file to user's download folder
            OutputStream outStream = new FileOutputStream(f);

            /* Read the file content part */
            byte[] fsizeArr = new byte[4];
            bais.read(fsizeArr, 0, 4);
            int filesize = Utility.byteToInt(fsizeArr);

            int readBytes = 0;
            int thisRead = 0;
            byte[] buf = new byte[8192];
            while (readBytes < filesize && (thisRead = bais.read(buf, 0, Math.min(8192, filesize - readBytes)) ) != -1) {
                outStream.write(buf, 0, Math.min(8192, filesize - readBytes));
                readBytes += thisRead;
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
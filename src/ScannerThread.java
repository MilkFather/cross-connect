class ScannerThread extends Thread {

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i <= 255; i++) {
                    for (int j = 0; j <= 255; j++) {
                        //System.out.println("Detecting 192.168." + String.valueOf(i) + "." + String.valueOf(j));
                        ModFind.getInstance().sendMyself("192.168." + String.valueOf(i) + "." + String.valueOf(j));
                        sleep(10);
                    }
                }
                //System.out.println("complete");
                //sleep(15 * 60 * 1000); // sleep for 90 seconds
                sleep(30 * 1000); // sleep for 30 seconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
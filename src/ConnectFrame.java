import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

public class ConnectFrame extends JFrame{
	private JButton connect;
	private JTextField portInfo;
	private JLabel connectAt;
	private JPanel mainLay;
	private BaseSender bs = BaseSender.getInstance();
	private BaseReceiverThread t1 = new BaseReceiverThread();

	
	
	public ConnectFrame() {
		setTitle("连接服务器");
		mainLay = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		connectAt = new JLabel("连接到",JLabel.LEFT);
		connectAt.setFont(new Font("宋体",0,20));
		
		portInfo = new JTextField(20);
		portInfo.setFont(new Font("宋体",0,20));
		portInfo.setEditable(true);
		connect = new JButton("连接");
		connect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		connect.setBackground(new Color(30,145,255));
        connect.setForeground(Color.WHITE);
        connect.setPreferredSize(new Dimension(100,40));
        connect.setFont(new Font("宋体",0,20));
        
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		top.add(connectAt);
		JPanel down = new JPanel(new FlowLayout(FlowLayout.LEFT,0,20));
		down.add(connect);
		JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
		portInfo.setText("127.0.0.1");
		center.add(portInfo);
		
		mainLay.add(top);
		mainLay.add(center);
		mainLay.add(down);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
		int height = getHeight();
	    int width = getWidth();
	    int screenWidth = screenSize.width / 2;
	    int screenHeight = screenSize.height / 2;
	    setLocation(screenWidth - width / 2, screenHeight - height / 2);
		addEventHandler();
		add(mainLay);
		setVisible(true);
		setSize(350,150);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void addEventHandler() {
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				//t1.start();
				String ip = portInfo.getText();
				bs.connect(ip);
				dispose();
				//LoginUI log = new LoginUI();//test

			}
		});
	}
	
	public static void main(String[] args) {
		ConnectFrame cf = new ConnectFrame();
	}
}

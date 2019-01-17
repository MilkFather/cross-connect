
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;



public class ChatFrame extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L;
    private JTextArea chatTextArea;
    private JButton send;
    private JTextArea inputField;
    private JLabel currentUserNameTitleLabel;
    private JTextArea userInfoListArea;
    private SimpleDateFormat simpleDateFormat;
    private JPanel userInfoPanel;
    private JPanel filePanel;
    private JButton fileBtn;
    private JTextField sendToIP;
    private JFileChooser chooser = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("doc", "docx","txt", "jpg", "gif");
    private ModChat me = ModChat.getInstance();
    private ModFile f = ModFile.getInstance();


    private static final ChatFrame instance = new ChatFrame();
    public static ChatFrame getInstance(){
        return instance;
    }
    
    private ChatFrame(){
        super(" 聊天 ");
        simpleDateFormat = (SimpleDateFormat) DateFormat.getInstance();
        simpleDateFormat.applyPattern("yyyy/MM/dd HH:mm:ss");
        createFrame();
        addEventHandler();
    }
 
    private void createFrame(){
    	setSize(920,700);
    	setVisible(true);
        fileBtn = new JButton("发送文件");
    	fileBtn.setBounds(0, 0, 70, 40);
    	
    	JLabel sendTo = new JLabel("发送到");
    	sendTo.setFont(new Font("微软雅黑", Font.PLAIN, 25));
    	sendToIP = new JTextField();
    	sendToIP.setColumns(18);
    	sendToIP.setFont(new Font("宋体", Font.PLAIN, 20));
    	
    	filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    	filePanel.add(fileBtn,BorderLayout.WEST);
    	filePanel.add(sendTo);
    	filePanel.add(sendToIP);
    	filePanel.setPreferredSize(new Dimension (670,60));
    	chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setPreferredSize(new Dimension (670,400));
        chatTextArea.setFont(new Font("宋体", Font.PLAIN, 20));
        userInfoListArea = new JTextArea();
        userInfoListArea.setEditable(false);
        JScrollPane centerScrollPane = new JScrollPane(chatTextArea);
        send = new JButton("发送");
        send.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        inputField = new JTextArea();
        inputField.setPreferredSize(new Dimension (680,130));
        inputField.setFont(new Font("宋体", Font.PLAIN, 20)); 
        userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel portraItImageLable = new JLabel();
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        northPanel.add(portraItImageLable, BorderLayout.NORTH);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        northPanel.add(userInfoPanel);
        southPanel.add(inputField);
        southPanel.add(send);

        add(northPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.add(centerScrollPane, BorderLayout.NORTH);
        centerPanel.add(filePanel);
        add(centerPanel,BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
		int height = getHeight();
	    int width = getWidth();
	    int screenWidth = screenSize.width / 2;
	    int screenHeight = screenSize.height / 2;
	    setLocation(screenWidth - width / 2, screenHeight - height / 2);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void AddReply(String ip, String msg) {
        chatTextArea.append(ip + "：\n" + msg + "\n");
    }
 
    @Override
    public void actionPerformed(ActionEvent e){
        String message = inputField.getText();
        String to_ip = sendToIP.getText();
        if (message == null || message.trim().equals("")){
            JOptionPane.showMessageDialog(this, "不能发送空消息！");
        }
        else if(to_ip == null || to_ip.trim().equals("")) {
        	JOptionPane.showMessageDialog(this, "接收ip地址不能为空！");
        } else if (!ModFind.getInstance().isAvailable(to_ip)) {
            JOptionPane.showMessageDialog(this, "接收方无法连接！");
        }
        else{
            String time = simpleDateFormat.format(new Date());
            me.sendMsg(to_ip, message);
            chatTextArea.append("\t\t\t" + time + "\n");
            chatTextArea.append("我：\n" + message + "\n");
            inputField.setText("");
            //chatTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    }
    
    
    private void addEventHandler(){
        send.addActionListener(this);

        fileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                String to_ip = sendToIP.getText();
                if(to_ip == null || to_ip.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "接收ip地址不能为空！");
                } else if (!ModFind.getInstance().isAvailable(to_ip)) {
                    JOptionPane.showMessageDialog(null, "接收方无法连接！");
                } else if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					String file_path = chooser.getSelectedFile().getAbsolutePath();
	                f.sendFile(to_ip, file_path);
	            }
			}
		});
    }
 
    public static void main (String[] args) {
    	BaseReceiver.getInstance().start();
        ScannerThread st = new ScannerThread();
        st.start();
    	ChatFrame chat = ChatFrame.getInstance();
    	chat.setVisible(true);
    	try {
            while (true) {
                System.in.read();
                ModFind.getInstance().getAvailableUsers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


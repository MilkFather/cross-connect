import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private JFileChooser chooser = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("doc", "docx","txt", "jpg", "gif");
    private ModFile md = ModFile.getInstance();
	private ModChat mc = ModChat.getInstance();
    
    ChatFrame(String ip){
        super(" 聊天 ");
        simpleDateFormat = (SimpleDateFormat) DateFormat.getInstance();
        simpleDateFormat.applyPattern("yyyy/MM/dd HH:mm:ss");
        createFrame(ip);
        addEventHandler();
    }
 
    private void createFrame(String ip){
    	setSize(920,700);
    	setVisible(true);
    	ImageIcon fileImg = new ImageIcon(getClass().getResource("\\file.jpg"));
    	fileImg.setImage(fileImg.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
    	
    	fileBtn = new JButton(fileImg);
    	fileBtn.setBounds(0, 0, 70, 40);
    	filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    	filePanel.add(fileBtn,BorderLayout.WEST);
    	filePanel.setPreferredSize(new Dimension (700,50));
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
        currentUserNameTitleLabel = new JLabel(" 复读机 ");
        currentUserNameTitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 40));
        userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        userInfoPanel.add(currentUserNameTitleLabel);
        userInfoListArea.setText("IP地址：" + ip);
        userInfoListArea.setPreferredSize(new Dimension (200,400)); 
        userInfoListArea.setFont(new Font("宋体", Font.PLAIN, 20));
        ImageIcon portraItImage = new ImageIcon(getClass().getResource("\\test.jpg"));
        
        portraItImage.setImage(portraItImage.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        JLabel portraItImageLable = new JLabel(portraItImage);
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        northPanel.add(portraItImageLable, BorderLayout.NORTH);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        northPanel.add(userInfoPanel);
        southPanel.add(inputField);
        southPanel.add(send);
        JScrollPane listScrollPane = new JScrollPane(userInfoListArea);
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(new JLabel("信息列表："), BorderLayout.NORTH);
        eastPanel.add(listScrollPane, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.add(centerScrollPane, BorderLayout.NORTH);
        centerPanel.add(filePanel,BorderLayout.SOUTH);
        //add(centerScrollPane, BorderLayout.CENTER);
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
 
    @Override
    public void actionPerformed(ActionEvent e){
        String message = inputField.getText();
        if (message == null || message.trim().equals("")){
            JOptionPane.showMessageDialog(this, "不能发送空消息！");
        } 
        else{
            String time = simpleDateFormat.format(new Date());
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
				if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
	                String file_name = chooser.getSelectedFile().getName();
	                md.sendFile(file_name);
	            }
			}
		});
    }
 
    public static void main (String[] args) {
    	ChatFrame chat = new ChatFrame("127.0.0.1");
    	chat.setVisible(true);
    }
}


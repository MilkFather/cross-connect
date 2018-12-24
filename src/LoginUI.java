import javax.swing.*;
import java.awt.*;


public class LoginUI extends JFrame{
	private String accNum;
	private String passWord;
	private JCheckBox rememberPassword;
	private JCheckBox autoLogin;
	private JTextField accNumField;
	private JPasswordField passField;
	private JButton logButton;
	private JLabel imageLabel;
	private JLabel headimage;
	private JPanel mainLay;
	private JLabel register;
	private JLabel findpassword;
	
	private JPanel account;//�˺�
	private JPanel pass;//����
	private JPanel panel1;
	private JPanel checkpanel;
	private JPanel imagepanel;
	private JPanel loginpanel;
	private JPanel headpanel;
	
	private void createMap() {
		setTitle("��¼");
		ImageIcon imageup = new ImageIcon(getClass().getResource("\\qq.jpg"));
		imageLabel = new JLabel(imageup);//�����Ϸ�ͼƬ
		ImageIcon userImage = new ImageIcon(getClass().getResource("\\test.jpg"));
		userImage.setImage(userImage.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
		headimage = new JLabel(userImage);//����ͷ��
		mainLay = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		imagepanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
		imagepanel.add(imageLabel);
		
		logButton = new JButton("��¼");
		logButton.setBackground(new Color(30,145,255));
		logButton.setForeground(Color.WHITE);
		logButton.setPreferredSize(new Dimension(180,30));//��¼����
		
		accNumField = new JTextField(18);
		passField = new JPasswordField(18);//�����
		
		register = new JLabel("ע���˺�");
		findpassword = new JLabel("�һ�����");
		register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//���ù����״
		findpassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		register.setForeground(Color.BLUE);
		findpassword.setForeground(Color.BLUE);
		
		rememberPassword = new JCheckBox("��ס����");
		autoLogin = new JCheckBox("�Զ���¼");
		
		account = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
		pass = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
		panel1 = new JPanel(new GridLayout(3,15,0,0));
		
		account.add(accNumField);
		account.add(register);
		pass.add(passField);
		pass.add(findpassword);
		
		panel1.add(account);
		panel1.add(pass); //�����
		
		checkpanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,1));
		loginpanel = new JPanel();
		loginpanel.add(logButton);
		checkpanel.add(rememberPassword);
		checkpanel.add(autoLogin);
		panel1.add(checkpanel);
		mainLay.add(imagepanel);
		
		headpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,10));
		headpanel.add(headimage);
		headpanel.add(panel1);
		
		mainLay.add(headpanel);
		mainLay.add(loginpanel);
		
		add(mainLay);
		setVisible(true);
		setSize(540,420);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public LoginUI() {
		createMap();
	}
	
	public static void main(String[] args) {
		LoginUI log = new LoginUI();
	}
}

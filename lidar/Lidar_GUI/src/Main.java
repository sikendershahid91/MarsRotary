import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class Main {

	private JFrame frame;
	private Serial_Port serialPort = new Serial_Port();
	private JComboBox<String> commPorts_ComboBox;
	private Status_Plane statusPlane = new Status_Plane();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 684, 481);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().setLayout(null);
		
		JLabel lblThisProgrmaIs = new JLabel("This Progrma is for communicating with the Lidar Sensor");
		lblThisProgrmaIs.setBounds(20, 11, 638, 14);
		frame.getContentPane().add(lblThisProgrmaIs);
		
		JPanel status_Plane = new JPanel();
		status_Plane.setBackground(Color.LIGHT_GRAY);
		status_Plane.setBounds(20, 407, 585, 24);
		frame.getContentPane().add(status_Plane);
		GridBagLayout gbl_status_Plane = new GridBagLayout();
		gbl_status_Plane.columnWidths = new int[]{67, 0};
		gbl_status_Plane.rowHeights = new int[]{24, 0};
		gbl_status_Plane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_status_Plane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		status_Plane.setLayout(gbl_status_Plane);
		GridBagConstraints gbc_statusPlane = new GridBagConstraints();
		gbc_statusPlane.anchor = GridBagConstraints.WEST;
		gbc_statusPlane.gridx = 0;
		gbc_statusPlane.gridy = 0;
		status_Plane.add(statusPlane, gbc_statusPlane);
		
		JPanel commPorts_Panel = new JPanel();
		commPorts_Panel.setBackground(Color.LIGHT_GRAY);
		commPorts_Panel.setBounds(20, 42, 590, 36);
		frame.getContentPane().add(commPorts_Panel);
		commPorts_Panel.setLayout(null);
		
		JButton commPortConnect_Btn = new JButton("Connect ");
		commPortConnect_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.openPort(commPorts_ComboBox.getSelectedIndex())){
					statusPlane.writeStatus(Status.PORT_OPENED);
				}else{
					statusPlane.writeStatus(Status.PORT_NOT_OPEN);
				}
			}
		});
		commPortConnect_Btn.setBounds(491, 7, 89, 23);
		commPorts_Panel.add(commPortConnect_Btn);
		
		JLabel commPorts_Lbl = new JLabel("Comm Ports");
		commPorts_Lbl.setBounds(10, 11, 75, 14);
		commPorts_Panel.add(commPorts_Lbl);
		
		commPorts_ComboBox = new JComboBox<String>();
		commPorts_ComboBox.setBounds(86, 8, 298, 20);
		String ports[] = serialPort.getPorts();
		for(int i = 0; i < ports.length; i++){
			commPorts_ComboBox.addItem(ports[i]);
		}
		commPorts_Panel.add(commPorts_ComboBox);
		
		JButton commPortsRefresh_Btn = new JButton("Refresh");
		commPortsRefresh_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commPorts_ComboBox.removeAllItems();
				String ports[] = serialPort.getPorts();
				for(int i = 0; i < ports.length; i++){
					commPorts_ComboBox.addItem(ports[i]);
				}
			}
		});
		commPortsRefresh_Btn.setBounds(394, 7, 87, 23);
		commPorts_Panel.add(commPortsRefresh_Btn);
		
		textField = new JTextField();
		textField.setBounds(20, 96, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort(textField.getText())){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		
		btnSend.setBounds(116, 95, 89, 23);
		frame.getContentPane().add(btnSend);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(20, 134, 86, 254);
		frame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton DSButton = new JButton("DS");
		DSButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("DS")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		
		panel.add(DSButton);
		
		JButton DXButton = new JButton("DX");
		DXButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("DX")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(DXButton);
		
		JButton MZButton = new JButton("MZ");
		MZButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("MZ")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(MZButton);
		
		JButton MIButton = new JButton("MI");
		MIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("MI")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(MIButton);
		
		JButton LIButton = new JButton("LI");
		LIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("LI")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(LIButton);
		
		JButton IVButton = new JButton("IV");
		IVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("IV")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(IVButton);
		
		JButton IDButton = new JButton("ID");
		IDButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("ID")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(IDButton);
		
		JButton RRButton = new JButton("RR");
		RRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serialPort.writeToPort("RR")){
					statusPlane.writeStatus(Status.WRITE_SUSSESFUL);
				}else{
					statusPlane.writeStatus(Status.WRITE_FAILED);
				}
			}
		});
		panel.add(RRButton);
		
		
		commPorts_ComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				commPorts_ComboBox.removeAllItems();
				String ports[] = serialPort.getPorts();
				for(int i = 0; i < ports.length; i++){
					commPorts_ComboBox.addItem(ports[i]);
				}
			}
		});
		commPortConnect_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//System.out.println(commPorts_ComboBox.getSelectedIndex());
				
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				statusPlane.clearStatus();
			}
		});
	}
}

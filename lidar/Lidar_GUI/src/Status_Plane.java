import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Status_Plane extends JPanel {
	JLabel title_label;
	JLabel info_label;
	
	public void writeStatus(Status sc){
		info_label.setText(sc.name());
	}
	
	public void clearStatus(){
		info_label.setText("...");
	}
	
	public Status_Plane(){
		this.setBackground(Color.LIGHT_GRAY);
		
		title_label = new JLabel("Status:");
		//this.add(title_label);
		info_label = new JLabel("...");
		//this.add(info_label);
		
		GridBagLayout gbl_status_Plane = new GridBagLayout();
		gbl_status_Plane.columnWidths = new int[]{67, 0, 0};
		gbl_status_Plane.rowHeights = new int[]{24, 0};
		gbl_status_Plane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_status_Plane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gbl_status_Plane);
		
		//JLabel lblNewLabel = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		this.add(title_label, gbc_lblNewLabel);
		
		//JLabel lblNewLabel_1 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		this.add(info_label, gbc_lblNewLabel_1);	
		
	}
}

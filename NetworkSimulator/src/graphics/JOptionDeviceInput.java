package graphics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import networking.Device;
import networking.Host;
import networking.Router;

public class JOptionDeviceInput extends JPanel implements ActionListener {
	// ignore this
	private static final long serialVersionUID = 1L;
	
	String hostStr = "Host", routerStr = "Router";
	int x, y;
	
	JRadioButton hostButton = new JRadioButton(hostStr);
	JRadioButton routerButton = new JRadioButton(routerStr);
	JTextField addr = new JTextField(15);
	JLabel hostnameLabel = new JLabel("Hostname: ");
	JTextField hostname = new JTextField(15);
	JLabel routerLabel = new JLabel("Router Name: ");
	JTextField routername = new JTextField(15);
	
	public JOptionDeviceInput(int x, int y) {
		this.x = x;
		this.y = y;
		
		hostButton.setActionCommand(hostStr);
		hostButton.setSelected(true);

		routerButton.setActionCommand(routerStr);
		
		ButtonGroup group = new ButtonGroup();
		group.add(hostButton);
		group.add(routerButton);
		
		hostButton.addActionListener(this);
		routerButton.addActionListener(this);
		
		this.setLayout(new GridLayout(0,1));
		
		this.add(hostButton);
		this.add(routerButton);
		this.add(new JLabel("Address:"));
		this.add(addr);
//		this.add(Box.createHorizontalStrut(15)); // a spacer
		this.add(hostnameLabel);
		this.add(hostname);
		
		//		int result = JOptionPane.showConfirmDialog(null, myPanel,
//				"New Device", JOptionPane.OK_CANCEL_OPTION);
//		if (result == JOptionPane.OK_OPTION) {
//			System.out.println(addr + ", " + hostname);
//		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// This can be better done with cards/tabs?
		if(e.getActionCommand().equals(hostStr)) {
			this.remove(routerLabel);
			this.remove(routername);
			this.add(hostnameLabel);
			this.add(hostname);
		} else if(e.getActionCommand().equals(routerStr)) {
			this.remove(hostnameLabel);
			this.remove(hostname);
			this.add(routerLabel);
			this.add(routername);
		}
		
		this.revalidate();
		this.repaint();
	}

	public Device getDevice() {
		if(hostButton.isSelected()) {
			return new Host(addr.getText(), x, y, hostname.getText());
		} else if(routerButton.isSelected()) {
			return new Router(addr.getText(), x, y);
		}
		
		return null;
	}
}

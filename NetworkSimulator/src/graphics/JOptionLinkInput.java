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
import networking.Link;
import networking.Router;

public class JOptionLinkInput extends JPanel {
	// ignore this
	private static final long serialVersionUID = 1L;
	
	String hostStr = "Host", routerStr = "Router";
	Device d1, d2;
	
	JTextField rate = new JTextField(15);
	JTextField latency = new JTextField(15);
	
	public JOptionLinkInput(Device d1, Device d2) {
		this.d1 = d1;
		this.d2 = d2;
		
		this.setLayout(new GridLayout(0,1));
		
		this.add(new JLabel("Rate:"));
		this.add(rate);
		this.add(new JLabel("Latency:"));
		this.add(latency);
	}

	public Link getLink() {
		int r, l;
		
		try {
			r = Integer.parseInt(rate.getText());
			l = Integer.parseInt(latency.getText());
		} catch (NumberFormatException e){
			return null;
		}
		
		return new Link(
				d1, 
				d2, 
				r,
				l,
				64
		);
	}
}

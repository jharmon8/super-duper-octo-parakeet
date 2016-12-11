package graphics;

import generator.NetworkGenerator;
import io.NetworkLoader;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import networking.Network;

public class LaunchPanel extends JPanel 
						 implements ActionListener {

	public LaunchPanel() {
		// TODO Auto-generated constructor stub
		this.setPreferredSize(new Dimension(512, 256));
		
		GridLayout launchLayout = new GridLayout(3,3);
		this.setLayout(launchLayout);
		
		JButton b = new JButton("Case 0: Reno");
		b.addActionListener(this);
		b.setActionCommand("0,0");
		this.add(b);	
		b = new JButton("Case 1: Reno");
		b.addActionListener(this);
		b.setActionCommand("0,1");
		this.add(b);	
		b = new JButton("Case 2: Reno");
		b.addActionListener(this);
		b.setActionCommand("0,2");
		this.add(b);	
		b = new JButton("Case 0: Fast");
		b.addActionListener(this);
		b.setActionCommand("1,0");
		this.add(b);	
		b = new JButton("Case 1: Fast");
		b.addActionListener(this);
		b.setActionCommand("1,1");
		this.add(b);	
		b = new JButton("Case 2: Fast");
		b.addActionListener(this);
		b.setActionCommand("1,2");
		this.add(b);	
		b = new JButton("Custom");
		b.addActionListener(this);
		b.setActionCommand("Viewer");
		this.add(b);	
		b = new JButton("Designer");
		b.addActionListener(this);
		b.setActionCommand("Designer");
		this.add(b);	
		b = new JButton("Generator");
		b.addActionListener(this);
		b.setActionCommand("Generator");
		this.add(b);	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if(e.getActionCommand().equals("0,0")) {
				Network n = NetworkLoader.loadNetwork("Test_Case_0_Reno.txt");
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("0,1")) {
				Network n = NetworkLoader.loadNetwork("Test_Case_1_Reno.txt");
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("0,2")) {
				Network n = NetworkLoader.loadNetwork("Test_Case_2_Reno.txt");
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("1,0")) {
				Network n = NetworkLoader.loadNetwork("Test_Case_0_Fast.txt");
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("1,1")) {
				Network n = NetworkLoader.loadNetwork("Test_Case_1_Fast.txt");
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("1,2")) {
//				Network n = NetworkLoader.loadNetwork("Test_Case_2_Fast.txt");
//				NetworkViewer.launchViewer(n); 
//				n.init();
//				n.run();

//				System.out.println("Simulation finished.");
				System.out.println("This button is broken :(");
			} else if(e.getActionCommand().equals("Viewer")) {
				String filename = JOptionPane.showInputDialog("Filename:");
				Network n = NetworkLoader.loadNetwork(filename);
				NetworkViewer.launchViewer(n); 
				n.init();
				n.run();

				System.out.println("Simulation finished.");
			} else if(e.getActionCommand().equals("Designer")) {
				DesignerViewer.launchDesigner(); 
			} else if(e.getActionCommand().equals("Generator")) {
				Network n = NetworkGenerator.genCircularNetwork();
				System.out.println(n);
//				n.saveNetwork("circle_test.txt");
				
//				n = NetworkLoader.loadNetwork("circle_test.txt");
				NetworkViewer.launchViewer(n); 
			}
		} catch(IOException ioe) {
			System.out.println(ioe);
			System.exit(1);
		}
	}
}

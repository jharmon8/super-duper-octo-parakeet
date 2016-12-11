package graphics;

import javax.swing.JFrame;

public class Launcher {

	public static void launch() {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("143 Project - Jake, Annie, and Spencer");
		LaunchPanel p = new LaunchPanel();
		f.add(p);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}

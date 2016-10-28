package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import networking.Network;


public class NetworkPanel extends JPanel implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private int S_WIDTH = 1024;
	private int S_HEIGHT = 1024;
	private Timer timer;
	
	private Network network;
	
	public NetworkPanel(Network network) {
		addKeyListener(this);
		setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		
		this.network = network;
		
		timer = new Timer(1000, this);
		timer.setInitialDelay(100);
		timer.start();
		
		setFocusable(true);
		requestFocus();
		
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		network.draw(g, S_WIDTH, S_HEIGHT);
	}

	public void actionPerformed(ActionEvent ev) {
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			break;
		case KeyEvent.VK_A:
			break;
		case KeyEvent.VK_S:
			break;
		case KeyEvent.VK_D:
			break;
		case KeyEvent.VK_SPACE:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

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
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import networking.Device;
import networking.Flow;
import networking.Link;
import networking.Network;

public class DesignerPanel extends JPanel implements KeyListener, ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private int S_WIDTH = 1024;
	private int S_HEIGHT = 1024;
	private Timer timer;
	
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	private Device selected = null;
	
	private double minClickDistance = 60.0;
	
	public DesignerPanel() {
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		
		devices = new ArrayList<Device>();
		links = new ArrayList<Link>();
		flows = new ArrayList<Flow>();
		
		timer = new Timer(1000, this);
		timer.setInitialDelay(100);
		timer.start();
		
		setFocusable(true);
		requestFocus();
		
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		// clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, S_WIDTH, S_HEIGHT);
		
		// draw everything
		if(selected != null) {
			selected.drawSelection(g);
		}
		
		for(Link l : links) { l.draw(g); }
		for(Device d : devices) { d.draw(g); }
		for(Flow f : flows) { f.draw(g); }
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
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == 1) {
			// find minimum distance
			double minDistance = 100000;
			
			if(devices.size() > 0) {
				minDistance = devices.get(0).getDistance(e.getX(), e.getY());
				for(Device d : devices) {
					double dist = d.getDistance(e.getX(), e.getY());
					if(dist < minDistance) {minDistance = dist;}
				}
			}
				
			if(minDistance < minClickDistance) {
				JOptionPane.showMessageDialog(this, "Devices must be farther apart.");
			} else {
		        JOptionDeviceInput newD = new JOptionDeviceInput(e.getX(), e.getY());
		        JOptionPane.showMessageDialog(
		        						this, 
		        						newD, 
		        						"New Device",
		        						JOptionPane.QUESTION_MESSAGE);
		        
		        Device newDevice = newD.getDevice();
		        if(newDevice != null) {
			        devices.add(newDevice);
		        }
			}
		}
		
		if(e.getButton() == 3) {
			if(devices.size() > 0) {
				// find minimum distance
				double minDistance = devices.get(0).getDistance(e.getX(), e.getY());
				Device closest = devices.get(0);
				for(Device d : devices) {
					double dist = d.getDistance(e.getX(), e.getY());
					if(dist < minDistance) {
						minDistance = dist;
						closest = d;
					}
				}
				
				if(minDistance > minClickDistance) { // too far
					selected = null;
				} else if(selected == null) { // first selection
					selected = closest;
				} else { // second selection
			        JOptionLinkInput newL = new JOptionLinkInput(selected, closest);
			        JOptionPane.showMessageDialog(
			        						this, 
			        						newL, 
			        						"New Link",
			        						JOptionPane.QUESTION_MESSAGE);
			        
			        Link newLink = newL.getLink();
			        if(newLink != null) {
				        links.add(newLink);
			        }
					selected = null;
				}
			} else {
				selected = null;
			}
		}
        repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}

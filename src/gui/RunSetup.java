/**
 * Run the GUI for setting up the SQL database
 */
package gui;

import javax.swing.JFrame;
import gui.DatabaseSetup;

public class RunSetup {
	
	/* main method */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
	
	/* run the GUI method */
	private static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new DatabaseSetup();
	}
}

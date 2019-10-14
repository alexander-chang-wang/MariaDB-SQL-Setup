/**
 * Program for setting up the MariaDB SQL Database
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DatabaseSetup extends JFrame {
	
	private static final long serialVersionUID = 6315626803999116831L;
	
	/* Members */
	private JPanel mainPanel;
	private JButton testConnButton, saveButton, setupButton;
	private JTextField sqlTField;
	private JLabel warningLabel;
	private String SQLURL;
	private GridBagConstraints c;
	
	/* Constructor */
	public DatabaseSetup() {
		
		// get the SQL URL
		SQLURL = getSQLURL();
		
		// initialize JTextField for the SQL connection string
		// set its initial contents to the SQL string
		sqlTField = new JTextField(20);
		sqlTField.setText(SQLURL);
		
		// initialize and configure JButton for testing SQL connection
		testConnButton = new JButton("Test Connection");
		testConnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// get the contents from the JTextField 'sqlTField'
				String contents = sqlTField.getText();
				try {
					// declare and initialize a Connection object
					Connection conn = DriverManager.getConnection(contents);
					// close the Connection object
					conn.close();
					// notify user that the connection to the database was successful
					JOptionPane.showMessageDialog(null, "Connection Successful");
				} catch (SQLException e) {
					// notify user that the connection failed
					JOptionPane.showMessageDialog(null, "Connection Failed");
					e.printStackTrace();
				}
			}
		});
		
		// initialize and configure JButton for saving contents in the JTextField 'sqlTField'
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// get the text from the JTextField 'sqlTField'
				String contents = sqlTField.getText();
				try {
					// declare and initialize a File object to the text file './sql/sql_url.txt'
					File file = new File("./sql/sql_url.txt");
					// declare and initialize a FileWriter object to the text file 'file'
					FileWriter out = new FileWriter(file);
					// declare and initialize a BufferedWriter object to the FileWriter 'out'
					BufferedWriter writer = new BufferedWriter(out);
					// write the contents in the JTextField to the file
					writer.write(contents); writer.newLine();
					// close the BufferedWriter object
					writer.close();
					// notify user that the saving was successful
					JOptionPane.showMessageDialog(null, "Saved");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Unexpected Error: Saving Failed");
				}
			}
		});
		
		// initialize a JLabel for warning user that the setup will only execute using the URL that is saved
		warningLabel = new JLabel("!!! MAKE SURE TO SAVE YOUR CONNECTION URL BEFORE PRESSING THE SETUP BUTTON !!!");
		
		// initialize and configure JButton for setting up the table in the database
		setupButton = new JButton("Setup Table");
		setupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// get the SQL statement
				String sqlStatement = getCreateTableStatement();
				try {
					// declare and initialize a Connection object 
					Connection conn = DriverManager.getConnection(getSQLURL());
					// declare and initialize a Statement object
					Statement st = conn.createStatement();
					// execute the SQL statement
					st.execute(sqlStatement);
					// notify user that setup was successful
					JOptionPane.showMessageDialog(null, "Setup Successful");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Unexpected Error: Setup Unsuccessful");
					e.printStackTrace();
				}
			}
		});
		
		// initialize and configure the main JPanel
		mainPanel  = new JPanel(); mainPanel.setLayout(new GridBagLayout()); c = new GridBagConstraints();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		c.ipadx = 5; c.ipady = 5;
		c.gridwidth = 6; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 0; mainPanel.add(sqlTField, c);
		c.gridwidth = 2; c.fill = GridBagConstraints.NONE;
		c.gridx = 0; c.gridy = 1; mainPanel.add(testConnButton, c);
		c.gridx = 2; c.gridy = 1; mainPanel.add(saveButton, c);
		c.gridx = 4; c.gridy = 1; mainPanel.add(setupButton, c);
		c.gridwidth = 6;
		c.gridx = 0; c.gridy = 2; mainPanel.add(warningLabel, c);
		
		// initialize and configure the main JFrame (this)
		this.setTitle("SQL Setup");
		this.add(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/* method: returns a SQL connection string from sql_url.txt */
	private static String getSQLURL() {
		try {
			File file = new File("./sql/sql_url.txt");
			FileReader in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			String url = reader.readLine();
			reader.close();
			return url;
		}
		catch (FileNotFoundException e) { e.printStackTrace(); return null;}
		catch (IOException e)           { e.printStackTrace(); return null;}
	}
	
	/* method: gets the SQL statement that creates the database table */
	private static String getCreateTableStatement() {
		try {
			File file = new File("./sql/create_table.sql");
			Scanner sc = new Scanner(file);
			sc.useDelimiter("\\Z");
			String statement = sc.next();
			sc.close();
			return statement;
		}
		catch (FileNotFoundException e) { e.printStackTrace(); return null; }
	}
}

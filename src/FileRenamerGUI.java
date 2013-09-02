/**
 * Hi! This in the main GUI for the all the Renamer classes.
 *
 * What this program does is that it renames files for you.
 * More specifically, it renames the files of a certain TV Series to something mmuch more readable.
 *
 * Instead of he filename of an episode being e.g. "Game.Of.Thrones.720P.S1E4.xvid.IMMERSE",
 * this program will rename it to "Game of Thrones - S01E04 - Cripples, Bastards, and Broken Things".
 *
 * At this point in time, it is not possible for the user of the .JAR-file to define how they want the files to be named.
 *
 * @author Marcus Heine
 *
 */

import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class FileRenamerGUI extends JFrame implements ActionListener {

	public static final long serialVersionUID = 42L;

	FileReader reader;

	JPanel mainPanel;

	JLabel textLabel;
	JLabel reqLabel;
	JLabel spaceLabel;
	JLabel firstLabel;
	JLabel secondLabel;
	JLabel thirdLabel;

	JTextField directoryField;

	JButton rename;
	JButton cancel;
	JButton help;

	String introString;
	String reqString;
	String space;
	String firstString;
	String secondString;
	String thirdString;

	String [] options = {"Continue", "Cancel"};

	public static void main(String [] args)  throws IOException, FileNotFoundException {
		FileRenamerGUI renamer = new FileRenamerGUI();
	}

	public FileRenamerGUI() {


		this.setMinimumSize(new Dimension(550, 250));

		introString = "<html>This is a program that renames your TV Series files for you.<br></html>";
		firstString = "<html><br>Copy and paste the directory in which you keep your TV Series.<br>Example: G:\\Media\\Series\\Family Guy\\Season 9\\</html>";

		mainPanel = new JPanel();
		textLabel = new JLabel(introString);
		reqLabel = new JLabel(reqString);
		spaceLabel = new JLabel(" ");
		firstLabel = new JLabel(firstString);
		secondLabel = new JLabel(secondString);
		thirdLabel = new JLabel(thirdString);

		rename = new JButton("RENAME");
		cancel = new JButton("CANCEL");
		help = new JButton("HELP");

		directoryField = new JTextField(30);

		rename.addActionListener(this);
		cancel.addActionListener(this);
		help.addActionListener(this);

		directoryField.addActionListener(this);

		rename.setPreferredSize(new Dimension(408, 25));
		cancel.setPreferredSize(new Dimension(200, 25));
		help.setPreferredSize(new Dimension(200, 25));

		spaceLabel.setPreferredSize(new Dimension(550, 25));

		reqLabel.setForeground(new Color(254, 254, 254));
		textLabel.setForeground(new Color(254, 254, 254));
		firstLabel.setForeground(new Color(254, 254, 254));
		secondLabel.setForeground(new Color(254, 254, 254));
		thirdLabel.setForeground(new Color(254, 254, 254));
		spaceLabel.setForeground(new Color(254, 254, 254));

		mainPanel.setBackground(new Color(45, 45, 45));

		add(mainPanel);

		mainPanel.add(textLabel);
		mainPanel.add(reqLabel);
		mainPanel.add(firstLabel);
		mainPanel.add(directoryField);

		mainPanel.add(spaceLabel);

		mainPanel.add(help);
		mainPanel.add(cancel);
		mainPanel.add(rename);


		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);

		setVisible(true);

		this.setResizable(false);

		setTitle("File Renamer                                                                          \u00A9 Artemis/Marcus");
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == cancel)
		{
			System.out.println("BYELOL");
			System.exit(0);
		}

		if (e.getSource() == help)
		{
			JOptionPane.showMessageDialog(null,
					"This program is designed to help you rename the files of a certain TV Series.\n"
							+ "It will name your files into something like 'Family Guy - S07E14 - The Name of It'\n\n"
							+ "Is the program not working? Make sure that:\n"
							+ "You have specified the correct directory.\n"
							+ "The TV Series exists. To check this, search on www.epguides.com\n"
							+ "\nIf it still does not work, contact me via facebook/Marcus.Heine or at mheine@kth.se\n"
							+ "Some series (such as BBC's Life) aren't named the way they should be.\n"
							+ "If your TV Series has become incorrectly named, I will do my very best to fix it.\n\n", "Help/Troubleshooting", JOptionPane.INFORMATION_MESSAGE);
		}

		if (e.getSource() == rename)
		{
			int response = JOptionPane.showOptionDialog(
					null,
					"WARNING: This program could potentially give your files the wrong names.\nIf you are unsure of what to do, or if you don't trust me, click cancel.",
					"WARNING",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[1]);

			if(response == 0)
			{
				try {
					
					reader = new FileReader();
				} 
				catch (IOException exc) {
					JOptionPane.showMessageDialog(null, "Some sort of fucking error. Not sure how you got this.");
				}
				
				
				try {
					reader.readName(directoryField.getText());
				} 
				catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Some sort of fucking error. How do?");
				}
				
			}

		}

	}


}

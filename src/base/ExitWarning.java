package base;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExitWarning extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6223694993427564940L;

	public ExitWarning(String content) {
		super("WARNING");
		this.setSize(720, 320);
		this.setLayout(new GridLayout(2, 1));
		JLabel label = new JLabel(content, JLabel.CENTER);
		label.setFont(new Font(getName(), Font.BOLD, 24));
		this.add(label);
		JButton button = new JButton("confirm");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		JPanel subPanel = new JPanel();
		subPanel.add(button, BorderLayout.CENTER);
		this.add(subPanel);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}

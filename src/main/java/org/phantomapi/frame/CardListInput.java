package org.phantomapi.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class CardListInput extends JPanel
{
	private static final long serialVersionUID = -1765102384697264044L;
	protected JLabel title;
	protected JLabel text;
	protected JTextArea content;
	
	/**
	 * Create the panel.
	 */
	public CardListInput()
	{
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBackground(Color.WHITE);
		
		title = new JLabel("Card");
		title.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 32));
		
		text = new JLabel("Textual information which is very cool to see");
		text.setForeground(SystemColor.textInactiveText);
		text.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addComponent(title).addComponent(text)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(title).addGap(18).addComponent(text).addGap(18).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE).addContainerGap()));
		
		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(SystemColor.text);
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		content = new JTextArea();
		content.setWrapStyleWord(true);
		content.setLineWrap(true);
		content.setFont(new Font("Monospaced", Font.PLAIN, 16));
		content.setBackground(SystemColor.menu);
		content.setText("Test Example");
		panel.add(content, BorderLayout.CENTER);
		setLayout(groupLayout);
	}
}

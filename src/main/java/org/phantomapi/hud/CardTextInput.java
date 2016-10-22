package org.phantomapi.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class CardTextInput extends JPanel
{
	private static final long serialVersionUID = -1765102384697264044L;
	protected JLabel title;
	protected JLabel text;
	protected JTextField textbox;
	
	/**
	 * Create the panel.
	 */
	public CardTextInput()
	{
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBackground(Color.WHITE);
		
		title = new JLabel("Card");
		title.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 32));
		
		text = new JLabel("Textual information which is very cool to see");
		text.setForeground(SystemColor.textInactiveText);
		text.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		
		textbox = new JTextField();
		textbox.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		textbox.setBackground(SystemColor.menu);
		textbox.setText("Text");
		textbox.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(title).addComponent(text).addComponent(textbox, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(title).addGap(18).addComponent(text).addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE).addComponent(textbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		setLayout(groupLayout);
	}
}

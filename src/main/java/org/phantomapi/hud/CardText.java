package org.phantomapi.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class CardText extends JPanel
{
	private static final long serialVersionUID = -1765102384697264044L;
	protected JLabel title;
	protected JLabel text;
	
	/**
	 * Create the panel.
	 */
	public CardText()
	{
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBackground(Color.WHITE);
		
		title = new JLabel("Card");
		title.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 32));
		
		text = new JLabel("Textual information which is very cool to see");
		text.setForeground(SystemColor.textInactiveText);
		text.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(title).addComponent(text)).addContainerGap(387, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(title).addGap(18).addComponent(text).addContainerGap(197, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}
	
}

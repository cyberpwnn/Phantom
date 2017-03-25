package org.phantomapi.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class CardToggleInput extends JPanel
{
	private static final long serialVersionUID = -1765102384697264044L;
	public JLabel title;
	public JLabel text;
	public JCheckBox toggle;
	
	/**
	 * Create the panel.
	 */
	public CardToggleInput()
	{
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBackground(Color.WHITE);
		
		title = new JLabel("Card");
		title.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 32));
		
		text = new JLabel("Textual information which is very cool to see");
		text.setForeground(SystemColor.textInactiveText);
		text.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		
		toggle = new JCheckBox("Enabled");
		toggle.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		toggle.setBackground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(title).addComponent(text).addComponent(toggle)).addContainerGap(99, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(title).addGap(18).addComponent(text).addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE).addComponent(toggle).addContainerGap()));
		setLayout(groupLayout);
	}
}

package org.phantomapi.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import org.phantomapi.lang.GList;

public class CardSelectInput extends JPanel
{
	private static final long serialVersionUID = -1765102384697264044L;
	protected JLabel title;
	protected JLabel text;
	protected GList<String> selections;
	
	/**
	 * Create the panel.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public CardSelectInput(GList<String> selections)
	{
		this.selections = new GList<String>();
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setBackground(Color.WHITE);
		
		title = new JLabel("Card");
		title.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 32));
		
		text = new JLabel("Textual information which is very cool to see");
		text.setForeground(SystemColor.textInactiveText);
		text.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		
		JComboBox<?> comboBox = new JComboBox();
		comboBox.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 14));
		comboBox.setBackground(SystemColor.inactiveCaptionBorder);
		comboBox.setModel(new DefaultComboBoxModel(selections.toArray(new String[selections.size()])));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(title).addComponent(text, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(comboBox, 0, 424, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(title).addGap(18).addComponent(text).addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE).addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		setLayout(groupLayout);
	}
}

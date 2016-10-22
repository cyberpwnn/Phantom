package org.phantomapi.hud;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataCluster.ClusterDataType;
import org.phantomapi.lang.GList;

public class ConfigurationUI extends JFrame
{
	private static final long serialVersionUID = -6652875257009730072L;
	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public ConfigurationUI(DataCluster cc)
	{
		HudUtil.setSystemUi();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 584, 538);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		for(String i : cc.keys())
		{
			if(cc.getType(i).equals(ClusterDataType.BOOLEAN))
			{
				CardToggleInput toggle = new CardToggleInput();
				toggle.toggle.setSelected(cc.getBoolean(i));
				toggle.toggle.setToolTipText("This field can either be true or false");
				toggle.title.setText(i);
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.STRING))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(cc.getString(i));
				toggle.textbox.setToolTipText("Can be any string of text");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.DOUBLE))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(String.valueOf(cc.getDouble(i)));
				toggle.textbox.setToolTipText("Must be a number with optional decimals. Example 2.12 or 0.004434");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.STRING_LIST))
			{
				CardListInput toggle = new CardListInput();
				toggle.title.setText(i);
				toggle.content.setToolTipText("Each new line (excluding word wrapping) denotes a new line");
				toggle.content.setText(new GList<String>(cc.getStringList(i)).toString("\n"));
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.INTEGER))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(String.valueOf(cc.getInt(i)));
				toggle.textbox.setToolTipText("Must be a number without decimal points.");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.LONG))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setToolTipText("Must be a number without decimal points. (OR REALLY BIG TOO)");
				toggle.textbox.setText(String.valueOf(cc.getInt(i)));
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				panel.add(toggle);
			}
			
			JLabel j = new JLabel("   ");
			panel.add(j);
		}
	}
	
}

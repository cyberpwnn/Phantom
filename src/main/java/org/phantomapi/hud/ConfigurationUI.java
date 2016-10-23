package org.phantomapi.hud;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataCluster.ClusterDataType;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.D;

public class ConfigurationUI extends JFrame
{
	private static final long serialVersionUID = -6652875257009730072L;
	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public ConfigurationUI(DataCluster cc)
	{
		D d = new D("Wormhole");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfigurationUI.class.getResource("/org/phantomapi/phantom.png")));
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
				
				toggle.toggle.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						cc.set(i, toggle.toggle.isSelected());
						d.v(i + " <> " + toggle.toggle.isSelected());
						
						toggle.setBackground(new Color(204, 255, 225));
						
						new TaskLater(1)
						{
							
							@Override
							public void run()
							{
								toggle.setBackground(Color.WHITE);
							}
						};
					}
				});
				
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.STRING))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(cc.getString(i));
				toggle.textbox.setToolTipText("Can be any string of text");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				
				toggle.textbox.addKeyListener(new KeyListener()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						
					}
					
					@Override
					public void keyReleased(KeyEvent e)
					{
						cc.set(i, toggle.textbox.getText());
						d.v(i + " <> " + toggle.textbox.getText());
						
						toggle.setBackground(new Color(204, 255, 225));
						
						new TaskLater(1)
						{
							
							@Override
							public void run()
							{
								toggle.setBackground(Color.WHITE);
							}
						};
					}
					
					@Override
					public void keyPressed(KeyEvent e)
					{
						
					}
				});
				
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.DOUBLE))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(String.valueOf(cc.getDouble(i)));
				toggle.textbox.setToolTipText("Must be a number with optional decimals. Example 2.12 or 0.004434");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				
				toggle.textbox.addKeyListener(new KeyListener()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						
					}
					
					@Override
					public void keyReleased(KeyEvent e)
					{
						try
						{
							cc.set(i, Double.valueOf(toggle.textbox.getText()));
							d.v(i + " <> " + toggle.textbox.getText());
							
							toggle.setBackground(new Color(204, 255, 225));
							
							new TaskLater(1)
							{
								
								@Override
								public void run()
								{
									toggle.setBackground(Color.WHITE);
								}
							};
						}
						
						catch(Exception ex)
						{
							toggle.setBackground(new Color(255, 204, 204));
						}
					}
					
					@Override
					public void keyPressed(KeyEvent e)
					{
						
					}
				});
				
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.STRING_LIST))
			{
				CardListInput toggle = new CardListInput();
				toggle.title.setText(i);
				toggle.content.setToolTipText("Each new line (excluding word wrapping) denotes a new line");
				toggle.content.setText(new GList<String>(cc.getStringList(i)).toString("\n"));
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				
				toggle.content.addKeyListener(new KeyListener()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						
					}
					
					@Override
					public void keyReleased(KeyEvent e)
					{
						GList<String> text = new GList<String>();
						
						for(String j : toggle.content.getText().split("\n"))
						{
							text.add(j.trim());
						}
						
						cc.set(i, text);
						d.v(i + " <> " + text.toString(", "));
						
						toggle.setBackground(new Color(204, 255, 225));
						
						new TaskLater(1)
						{
							
							@Override
							public void run()
							{
								toggle.setBackground(Color.WHITE);
							}
						};
					}
					
					@Override
					public void keyPressed(KeyEvent e)
					{
						
					}
				});
				
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.INTEGER))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setText(String.valueOf(cc.getInt(i)));
				toggle.textbox.setToolTipText("Must be a number without decimal points.");
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				
				toggle.textbox.addKeyListener(new KeyListener()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						
					}
					
					@Override
					public void keyReleased(KeyEvent e)
					{
						try
						{
							cc.set(i, Integer.valueOf(toggle.textbox.getText()));
							d.v(i + " <> " + toggle.textbox.getText());
							toggle.setBackground(new Color(204, 255, 225));
							
							new TaskLater(1)
							{
								
								@Override
								public void run()
								{
									toggle.setBackground(Color.WHITE);
								}
							};
						}
						
						catch(Exception ex)
						{
							toggle.setBackground(new Color(255, 204, 204));
						}
					}
					
					@Override
					public void keyPressed(KeyEvent e)
					{
						
					}
				});
				
				panel.add(toggle);
			}
			
			if(cc.getType(i).equals(ClusterDataType.LONG))
			{
				CardTextInput toggle = new CardTextInput();
				toggle.title.setText(i);
				toggle.textbox.setToolTipText("Must be a number without decimal points. (OR REALLY BIG TOO)");
				toggle.textbox.setText(String.valueOf(cc.getInt(i)));
				toggle.text.setText("[" + cc.getType(i) + "] " + cc.getComment(i));
				
				toggle.textbox.addKeyListener(new KeyListener()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						
					}
					
					@Override
					public void keyReleased(KeyEvent e)
					{
						try
						{
							cc.set(i, Long.valueOf(toggle.textbox.getText()));
							d.v(i + " <> " + toggle.textbox.getText());
							toggle.setBackground(new Color(204, 255, 225));
							
							new TaskLater(1)
							{
								
								@Override
								public void run()
								{
									toggle.setBackground(Color.WHITE);
								}
							};
						}
						
						catch(Exception ex)
						{
							toggle.setBackground(new Color(255, 204, 204));
						}
					}
					
					@Override
					public void keyPressed(KeyEvent e)
					{
						
					}
				});
				
				panel.add(toggle);
			}
			
			JLabel j = new JLabel("   ");
			panel.add(j);
		}
	}
	
}

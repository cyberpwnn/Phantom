package org.phantomapi.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

public class TestFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JLabel blockEntity;
	public JLabel coords;
	public JLabel lblProtector;
	public JLabel lblMeta;
	public JTextPane nestdata;
	
	/**
	 * Launch the application.
	 */
	public static TestFrame begin()
	{
		try
		{
			TestFrame frame = new TestFrame();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(TestFrame.DISPOSE_ON_CLOSE);
			return frame;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Create the frame.
	 */
	public TestFrame()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 613, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBackground(Color.WHITE);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBackground(Color.WHITE);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_3.setBackground(Color.WHITE);
		
		JLabel lblNested = new JLabel("Nested");
		lblNested.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 48));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGap(5).addComponent(lblNested)).addGroup(gl_panel_3.createSequentialGroup().addContainerGap().addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))).addContainerGap()));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGap(5).addComponent(lblNested).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE).addContainerGap()));
		
		nestdata = new JTextPane();
		nestdata.setFont(new Font("Courier New", Font.PLAIN, 18));
		nestdata.setText("None");
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addComponent(nestdata, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addComponent(nestdata, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE));
		panel_4.setLayout(gl_panel_4);
		panel_3.setLayout(gl_panel_3);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(panel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false).addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(Alignment.LEADING, gl_panel.createSequentialGroup().addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))).addContainerGap(106, Short.MAX_VALUE)));
		
		JLabel lblProtected = new JLabel("Protection");
		lblProtected.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 48));
		
		lblProtector = new JLabel("Protector");
		lblProtector.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 24));
		
		lblMeta = new JLabel("Meta");
		lblMeta.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 24));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addGap(9).addComponent(lblProtected)).addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(lblProtector, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(lblMeta, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))).addContainerGap(34, Short.MAX_VALUE)));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addGap(5).addComponent(lblProtected).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblProtector, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblMeta, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addContainerGap(116, Short.MAX_VALUE)));
		panel_2.setLayout(gl_panel_2);
		
		JLabel lookingAt = new JLabel("Looking At ");
		lookingAt.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 48));
		
		blockEntity = new JLabel("Block Entity");
		blockEntity.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		
		coords = new JLabel("Coords");
		coords.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 18));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(5).addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(coords, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE).addComponent(blockEntity).addComponent(lookingAt)).addGap(13)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(5).addComponent(lookingAt).addPreferredGap(ComponentPlacement.RELATED).addComponent(blockEntity).addPreferredGap(ComponentPlacement.RELATED).addComponent(coords, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addContainerGap(32, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
	}
}

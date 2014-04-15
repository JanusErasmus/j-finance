package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import content.Category;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CategoryDialog extends JDialog
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panel;
	
	private JPanel subPanel;
	
	ArrayList<Category> catagories;

	
	private JComboBox catCombo;
	JFrame parentFrm;
	Component thisFrm;
	/**
	 * Create the dialog.
	 */
	public CategoryDialog(ArrayList<Category> cat, JFrame parent)
	{
		
		parentFrm = parent;
		catagories = cat;
		thisFrm = (JDialog)this;
				
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(CategoryDialog.class.getResource("/ico/jFin.png")));
		setTitle("Edit Catagory");
		setBounds(100, 100, 450, 442);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		catCombo = new JComboBox();
		catCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if( arg0.getStateChange() == ItemEvent.SELECTED)
				{
					//System.out.println("Selected " + catCombo.getSelectedItem());
					fillSubCatagories();
				}
			}
		});
		catCombo.setBounds(20, 51, 103, 20);
		contentPanel.add(catCombo);		
		
		JLabel lblCatagory = new JLabel("Catagory");
		lblCatagory.setBounds(20, 26, 46, 14);
		contentPanel.add(lblCatagory);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sub Catagories", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(151, 26, 273, 334);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int idx = catCombo.getSelectedIndex();
				
				String s = (String) JOptionPane.showInputDialog(parentFrm,
						"Catagory:",
						"New Catagory for " + catagories.get(idx).name, JOptionPane.PLAIN_MESSAGE);

				// If a string was returned, say so.
				if ((s != null) && (s.length() > 0))
				{
					if(catagories.get(idx).subCatagories.size() == 10)
					{
						JOptionPane.showMessageDialog(parentFrm,
							    "To much catagories.",
							    "New Catagory error",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					Category newCat = new Category();
					newCat.name = s;
					catagories.get(idx).subCatagories.add(newCat);
					fillSubCatagories();					
					
				}
			}
		});
		btnAdd.setBounds(10, 300, 89, 23);
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int index = catCombo.getSelectedIndex();
				Category cat = catagories.get(index); 
				
				ArrayList<String> catNames = new ArrayList<String>();
				for (int i = 0; i < cat.subCatagories.size(); i++)
				{
					catNames.add(cat.subCatagories.get(i).name);
				}
				
				Object[] possibilities = catNames.toArray();				
				String s = (String)JOptionPane.showInputDialog(
				                    parentFrm,
				                    "Select catagory to remove:\n",				                    
				                    "Remove Catagory",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    "");
				
				if ((s != null) && (s.length() > 0))
				{				
					int idx = catNames.indexOf(s);
					
					Object[] options = { "Yes", "No" };
					int n = JOptionPane.showOptionDialog(parentFrm,
							"Are you sure you want to delete "  + s + " ?",
							"Remove catagory",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
					
					if(n == 0)
					{		
						System.out.println("Removing " + idx);
						cat.subCatagories.remove(idx);
						fillSubCatagories();
					}
				}
			}
		});
		btnRemove.setBounds(107, 300, 89, 23);
		panel.add(btnRemove);
		
		subPanel = new JPanel();
		subPanel.setBounds(10, 24, 253, 265);
		panel.add(subPanel);
		subPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("12px"),
				ColumnSpec.decode("45px"),
				ColumnSpec.decode("12px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog("Rename this catagory",catCombo.getSelectedItem());
				
				if(s != null && s.length() > 0)
				{
					int index = catCombo.getSelectedIndex();										 
					catagories.get(index).name = s;
					
					catCombo.removeAllItems();					
					for (int i = 0; i < catagories.size(); i++)
					{
						catCombo.addItem(catagories.get(i).name);
					}					
				}						
			}
		});
		btnRename.setBounds(20, 82, 89, 23);
		contentPanel.add(btnRename);
		
		JButton btnMoveUp = new JButton("Set top");
		btnMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int index = catCombo.getSelectedIndex();
				
				if(index > 0)
				{
				Category cat = catagories.get(index);
				catagories.remove(index);				
				catagories.add(0, cat);
				}
				
				catCombo.removeAllItems();					
				for (int i = 0; i < catagories.size(); i++)
				{
					catCombo.addItem(catagories.get(i).name);
				}
			}
		});
		btnMoveUp.setBounds(20, 116, 89, 23);
		contentPanel.add(btnMoveUp);
		
		JButton btnMoveDwn = new JButton("Set bottom");
		btnMoveDwn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int index = catCombo.getSelectedIndex();
				int last = catCombo.getItemCount();

				if (index < last)
				{
					Category cat = catagories.get(index);
					catagories.remove(index);
					catagories.add(last - 1, cat);
				}

				catCombo.removeAllItems();
				for (int i = 0; i < catagories.size(); i++)
				{
					catCombo.addItem(catagories.get(i).name);
				}
			}
		});
		btnMoveDwn.setBounds(20, 150, 89, 23);
		contentPanel.add(btnMoveDwn);
		for (int i = 0; i < cat.size(); i++)
		{
			catCombo.addItem(cat.get(i).name);	
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		fillSubCatagories();
	}
	
	private void fillSubCatagories()
	{
		int idx = catCombo.getSelectedIndex();
		JLabel label = null;
		JTextField field = null;
		String arg = null;
		
		//System.out.println("Catagory " + catagories.get(idx).name + " has " + catagories.get(idx).subCatagories.size());
		subPanel.removeAll();
		
		int subSize = catagories.get(idx).subCatagories.size();
		if(subSize > 0)
		{
			for (int i = 0; i < subSize; i++)
			{
				Category cat = catagories.get(idx);
				//System.out.println(cat.subCatagories.get(i).name);
				label = new JLabel(cat.subCatagories.get(i).name);			
				arg = "2, " + (i*2 + 2) + ", 1, 1";			
				subPanel.add(label, arg);

				field = new JTextField(parseUtils.amount(cat.subCatagories.get(i).amount));
				field.setColumns(10);		
				field.setName("subCat " + i);
				field.setAlignmentY(RIGHT_ALIGNMENT);
				field.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						((JTextField)e.getSource()).selectAll();
					}
					@Override
					public void focusLost(FocusEvent e) {

						int idx = catCombo.getSelectedIndex();					
						JTextField f = ((JTextField)e.getSource());
						f.setText(parseUtils.amount(f.getText()));
						int index = f.getName().indexOf(" ") + 1;
						catagories.get(idx).subCatagories.get(Integer.parseInt(f.getName().substring(index))).amount = parseUtils.getAmount(f.getText());
					}
				});

				arg = "5, " + (i*2 + 2) + ", 1, 1";
				subPanel.add(field, arg);
			}
						
			if(field != null)
				field.requestFocus();
		}

		subPanel.revalidate();
		
		
		
	}	
}

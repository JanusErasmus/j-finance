package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import content.Category;

public class SummaryDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panel;
	
	
	public static void show(Category cat)
	{
		SummaryDialog dlg = new SummaryDialog(cat);
		dlg.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		dlg.setVisible(true);
	}
	
	
	
	public SummaryDialog(Category val)
	{		
		setIconImage(Toolkit.getDefaultToolkit().getImage(SummaryDialog.class.getResource("/ico/jFin.png")));
		setTitle("Sub catagory summary");
		setBounds(100, 100, 206, 278);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
		panel = new JPanel();
		panel.setBounds(20, 11, 161, 196);
		contentPanel.add(panel);		
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(55dlu;default)"),
				ColumnSpec.decode("max(10dlu;default)"),
				ColumnSpec.decode("max(42dlu;default)"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC,}));		
		}		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		

		String format = "";		
		String arg = "";
		JLabel label;
		
		for (int i = 0; i < val.subCatagories.size(); i++)
		{
			label = new JLabel(val.subCatagories.get(i).name);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			arg = "1, " + (i +2) + ", 1, 1";
			panel.add(label,arg);
			
			label = new JLabel(" ...");
			panel.add(label, "2, " + (i +2));
		}
		
		for (int i = 0; i < val.subCatagories.size(); i++)
		{
			double value = val.subCatagories.get(i).amount;
			format = String.format("%12s\n",parseUtils.amount(value) );
			label = new JLabel(format);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			
			if(value == 0)
				label.setForeground(Color.LIGHT_GRAY);
						
			if(value < 0)
				label.setForeground(Color.RED);
			
			arg = "1, " + (i +2) + ", 3, 1";
			panel.add(label,arg);
		}
	}

}

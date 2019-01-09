package engine;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

class ExitDialog 
{	
	
	private static ExitDialog sExitDialog;
	
	private static String[] sLabelsText = new String[] {"Exit game?", "Yes", "No"};
	
	private static boolean sReady = false;
	
	private static void init(Runnable yes, Runnable no)
	{
		sExitDialog = new ExitDialog(yes, no);	
		sReady=true;
	}
	
	static void setLabels(String content, String btnYesLabel, String btnNoLabel)
	{
		sLabelsText = new String[] {content, btnYesLabel, btnNoLabel};
	}
	
	static void invoke(Runnable yes, Runnable no)
	{
		if(!sReady)
			init(yes, no);
		sExitDialog.vDialog.setVisible(true);
	}
	
	static void hide()
	{
		sExitDialog.vDialog.dispose();
	}
	
	private ExitDialog(Runnable yes, Runnable no)
	{
		vDialog = new JDialog();
		vDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		vDialog.setAlwaysOnTop(true);
		
		
		Container container = vDialog.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		initLabel();
		initBtnYes(yes);
		initBtnNo(no);
		
		container.add(vLabel);
		
		Container tmp = new Container();
		
		container.add(tmp);
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.X_AXIS));
		
		tmp.add(vBtnNo);
		tmp.add(vBtnYes);
		
		
		vDialog.pack();
		vDialog.setResizable(false);
		vDialog.setLocationRelativeTo(null);
	}
	
	private void initBtnYes(Runnable yes)
	{
		vBtnYes = new JButton(sLabelsText[1]);
		vBtnYes.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnYes.addActionListener(createBtnListener(yes));
		
	}	
	
	private void initBtnNo(Runnable no)
	{
		vBtnNo = new JButton(sLabelsText[2]);
		vBtnNo.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnNo.addActionListener(createBtnListener(no));
	}
		
	private ActionListener createBtnListener(Runnable action)
	{
		ActionListener listener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				action.run();
			}
		};
		
		return listener;
	}
	
	private void initLabel()
	{
		vLabel = new JLabel(sLabelsText[0]);
		vLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		vLabel.setBorder(new EmptyBorder(10,10,10,10));
	}
	
	
	
	private JDialog vDialog;
	
	private JButton vBtnYes;
	private JButton vBtnNo;
	private JLabel vLabel;
}

package engine;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

class MultiDialog
{	
	
	private static MultiDialog sMultiDialog;
	
	private static String[] sLabelsText = new String[] {"What do you want?", "Save", "Exit", "Back to game"};
	
	private static boolean sReady = false;
	
	private static void init(Runnable save, Runnable exit, Runnable back)
	{
		sMultiDialog = new MultiDialog(save, exit, back);
		sReady=true;
	}
	
	static void setLabels(String content, String btnSaveLabel, String btnExitLabel, String btnBackLabel)
	{
		sLabelsText = new String[] {content, btnSaveLabel, btnExitLabel, btnBackLabel};
	}
	
	static void invoke(Runnable save, Runnable exit, Runnable back)
	{
		if(!sReady)
			init(save, exit, back);
		sMultiDialog.vDialog.setVisible(true);
	}
	
	static void hide()
	{
		sMultiDialog.vDialog.dispose();
	}
	
	private MultiDialog(Runnable save, Runnable exit, Runnable back)
	{
		vDialog = new JDialog();
		vDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		vDialog.setAlwaysOnTop(true);
		
		
		Container container = vDialog.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		initLabel();
		initBtnSave(save);
		initBtnExit(exit);
		initBtnBack(back);
		
		container.add(vLabel);
		
		Container tmp = new Container();
		
		container.add(tmp);
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.X_AXIS));
		
		tmp.add(vBtnSave);
		tmp.add(vBtnExit);		
		tmp.add(vBtnBack);
		
		vDialog.pack();
		vDialog.setResizable(false);
		vDialog.setLocationRelativeTo(null);
	}
	
	private void initBtnSave(Runnable save)
	{
		vBtnSave = new JButton(sLabelsText[1]);
		vBtnSave.setFocusPainted(false);
		vBtnSave.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnSave.addActionListener(createBtnListener(save));
		
	}	
	
	private void initBtnExit(Runnable exit)
	{
		vBtnExit = new JButton(sLabelsText[2]);
		vBtnExit.setFocusPainted(false);
		vBtnExit.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnExit.addActionListener(createBtnListener(exit));
	}
	
	private void initBtnBack(Runnable back)
	{
		vBtnBack = new JButton(sLabelsText[3]);
		vBtnBack.setFocusPainted(false);
		vBtnBack.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnBack.addActionListener(createBtnListener(back));
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
	
	private JButton vBtnSave;
	private JButton vBtnExit;
	private JButton vBtnBack;
	private JLabel vLabel;
}


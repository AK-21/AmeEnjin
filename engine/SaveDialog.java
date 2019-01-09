package engine;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class SaveDialog
{	
	private static SaveDialog sSaveDialog;
	
	private static String[] sLabelsText = new String[] {"Save as:", "Save", "Cancel", "File exists! Overwrite?", "Yes", "No", "OK", "Game saved!"};
	
	private static boolean sReady = false;
	
	private static String sName="save";
	
	static String getSaveName()
	{
		return sName;
	}	
	
	private static void init(int current, Runnable save, Runnable cancel)
	{
		sSaveDialog = new SaveDialog(current, save, cancel);
		sSaveDialog.saveDir = new File(SaveItem.SAVE_DIR);
		sReady=true;
	}
	
	static void setLabels(String content, String btnSaveLabel, String btnCancelLabel, String overwriteLabel, String btnYesLabel, String btnNoLabel, String btnOKLabel, String successLabel)
	{
		sLabelsText = new String[] {content, btnSaveLabel, btnCancelLabel, overwriteLabel, btnYesLabel, btnNoLabel, btnOKLabel, successLabel};
	}
	
	static void invoke(int current, Runnable save, Runnable cancel)
	{
		if(!sReady)
			init(current, save, cancel);
		sSaveDialog.vDialog.setVisible(true);
	}
	
	static void hide()
	{
		sSaveDialog.vDialog.dispose();
	}
	
	private static int sCurrent=0;
	
	private SaveDialog(int current, Runnable save, Runnable cancel)
	{
		vDialog = new JDialog();
		vDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		vDialog.setAlwaysOnTop(true);
		
		sCurrent=current;
		
		Container container = vDialog.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		
		initLabel();
		initTextField();
		initBtnSave(save);
		initBtnCancel(cancel);
		
		container.add(vLabel);
		
		Container tmp = new Container();
		
		vField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		JPanel txt=new JPanel();
		txt.setLayout(new BoxLayout(txt, BoxLayout.X_AXIS));
		txt.setBorder(new EmptyBorder(2,5,8,5));
		txt.add(vField);
		
		container.add(txt);
		
		container.add(tmp);
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.X_AXIS));
		
		tmp.add(vBtnSave);
		tmp.add(vBtnCancel);
		
		
		vDialog.pack();
		vDialog.setResizable(false);
		vDialog.setLocationRelativeTo(null);
	}
	
	private void initBtnSave(Runnable save)
	{
		vBtnSave = new JButton(sLabelsText[1]);
		vBtnSave.setFocusPainted(false);
		vBtnSave.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnSave.addActionListener(createSaveListener(save));
		
	}
	
	private void initBtnCancel(Runnable cancel)
	{
		vBtnCancel = new JButton(sLabelsText[2]);
		vBtnCancel.setFocusPainted(false);
		vBtnCancel.setAlignmentY(Container.CENTER_ALIGNMENT);
		vBtnCancel.addActionListener(createBtnListener(cancel));
		
	}
	
	private File saveDir;
			
	private ActionListener createSaveListener(Runnable action)
	{
		ActionListener listener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sName=vField.getText();
				vField.setText("");
				SaveItem save = new SaveItem(sCurrent, sName);
				try
				{
					if(!saveDir.exists())
						saveDir.mkdirs();
					
					if(new File(SaveItem.SAVE_DIR+File.separator+sName.toLowerCase()+SaveItem.FILE_EXT).exists())
					{
						initOverwriteDia(save, action);
					}
					else
					{
						save.save();
						Logger.log("Game saved as: \""+save.getName()+"\"");
						initSuccessDia(action);
					}
				}
				catch(GameException ge)
				{
					initInfoDia(ge.getMessage());
				}
			}
		};
		
		return listener;
	}
	
	private JDialog vInfoDia;
	
	private void initSuccessDia(Runnable evt)
	{
		initInfoDia(sLabelsText[7], evt);
	}
	
	private void initInfoDia(String info)
	{
		initInfoDia(info, ()->{});
	}
	
	private void initInfoDia(String info, Runnable evt)
	{
		vInfoDia = new JDialog();
		vInfoDia.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		vInfoDia.setAlwaysOnTop(true);
		Container container = vInfoDia.getContentPane();		
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));	
		
		JPanel tmp=new JPanel();
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.Y_AXIS));
		tmp.setAlignmentX(Container.CENTER_ALIGNMENT);
		tmp.setBorder(new EmptyBorder(10,10,10,10));		
		JLabel l= new JLabel(info);		
		tmp.add(l);
		container.add(tmp);
		
		JPanel tmp2=new JPanel();
		tmp2.setLayout(new BoxLayout(tmp2, BoxLayout.Y_AXIS));
		tmp2.setAlignmentX(Container.CENTER_ALIGNMENT);
		tmp2.setBorder(new EmptyBorder(10,10,10,10));
		JButton b=new JButton(sLabelsText[6]);
		b.setFocusPainted(false);
		b.addActionListener( createBtnListener(()->{vInfoDia.dispose(); evt.run();}));		
		tmp2.add(b);		
		container.add(tmp2);
		
		vInfoDia.pack();
		vInfoDia.setResizable(false);
		vInfoDia.setLocationRelativeTo(null);
		vInfoDia.setVisible(true);
	}
	
	private JDialog vOverwrite;	
	private void initOverwriteDia(SaveItem save, Runnable action)
	{
		vOverwrite = new JDialog();
		vOverwrite.setAlwaysOnTop(true);
		vOverwrite.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		vOverwrite.setAlwaysOnTop(true);
		Container container = vOverwrite.getContentPane();		
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));	
		
		JPanel tmp=new JPanel();
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.Y_AXIS));
		tmp.setAlignmentX(Container.CENTER_ALIGNMENT);
		tmp.setBorder(new EmptyBorder(10,10,10,10));		
		JLabel l= new JLabel(sLabelsText[3]);		
		tmp.add(l);
		container.add(tmp);
		
		JPanel tmp2=new JPanel();
		tmp2.setLayout(new BoxLayout(tmp2, BoxLayout.X_AXIS));
		tmp2.setAlignmentX(Container.CENTER_ALIGNMENT);
		tmp2.setBorder(new EmptyBorder(10,10,10,10));
		
		JButton a=new JButton(sLabelsText[4]);
		a.addActionListener( createBtnListener(()->{
			try
			{
				vOverwrite.dispose();
				save.save();
				Logger.log("Game saved as: \""+save.getName()+"\"");
				initSuccessDia(action);
			}
			catch (GameException e)
			{
				initInfoDia(e.getMessage());
			}			
		}));		
		a.setFocusPainted(false);
		
		
		JButton b=new JButton(sLabelsText[5]);
		b.addActionListener( createBtnListener(()->vOverwrite.dispose()));
		b.setFocusPainted(false);
		
		tmp2.add(b);
		tmp2.add(a);
		container.add(tmp2);
		
		vOverwrite.pack();
		vOverwrite.setResizable(false);
		vOverwrite.setLocationRelativeTo(null);
		vOverwrite.setVisible(true);		
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
	
	private void initTextField()
	{
		vField = new JTextField();
		vField.setAlignmentX(Container.CENTER_ALIGNMENT);
		vField.setBorder(new EmptyBorder(10,10,10,10));
	}
	
	private JDialog vDialog;
	
	private JButton vBtnSave;
	private JButton vBtnCancel;
	private JLabel vLabel;
	private JTextField vField;
	
	
}

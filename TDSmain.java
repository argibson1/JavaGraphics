import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.List;
import java.awt.color.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.lang.*;
import java.lang.Integer.*;

public class TDSmain {
	
	public static void main(String[]args){
		ThreeDimensionalSpace TDS;
                if(args.length ==0){
			TDS = new ThreeDimensionalSpace();
		}else{
			TDS = new ThreeDimensionalSpace(args[0],args[1].charAt(0),args[2].charAt(0));
		}
		JFrame frame = new JFrame("TDS");
		JFrame newS = new JFrame("Build");
		
		frame.add(TDS);
		frame.setVisible(true);
		frame.addKeyListener(TDS);
		frame.setFocusable(true);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(TDS.screenX,TDS.screenY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
}


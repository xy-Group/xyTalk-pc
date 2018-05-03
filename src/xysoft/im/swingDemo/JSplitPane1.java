package xysoft.im.swingDemo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JSplitPane1{
public JSplitPane1(){
     JFrame f=new JFrame("JSplitPaneDemo");
     Container contentPane=f.getContentPane();
     JLabel label1=new JLabel("Label 1",JLabel.CENTER);                            
     label1.setBackground(Color.green);
     label1.setOpaque(true);//setOpaque(ture)方法的目的是让组件变成不透明，这样我们在JLabel上所设置的颜色显示出来。

     JLabel label2=new JLabel("Label 2",JLabel.CENTER);                            
     label2.setBackground(Color.pink);
     label2.setOpaque(true);                           

     JLabel label3=new JLabel("Label 3",JLabel.CENTER);                            
     label3.setBackground(Color.yellow);
     label3.setOpaque(true);     
     /*加入label1,label2到splitPane1中，并设置此splitPane1为水平分割且具有Continuous Layout的
      *功能。
      */
     JSplitPane splitPane1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,label1,label2);
     /*设置splitPane1的分隔线位置，0.3是相对于splitPane1的大小而定，因此这个值的范围在0.0～1.0
      *中。若你使用整数值来设置splitPane的分隔线位置，如第34行所示，则所定义的值以pixel为计算单位
      */
     	splitPane1.setDividerLocation(0.5);

	splitPane1.setResizeWeight(0.5);
     /*设置JSplitPane是否可以展开或收起(如同文件总管一般)，设为true表示打开此功能。
      */
     splitPane1.setOneTouchExpandable(true);
     splitPane1.setDividerSize(10);//设置分隔线宽度的大小，以pixel为计算单位。
     
     JSplitPane splitPane2=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,splitPane1,label3);
     //splitPane2.setDividerLocation(135);
     //设置JSplitPane是否可以展开或收起(如同文件总管一般),设为true表示打开此功能.
     splitPane2.setOneTouchExpandable(false);
     splitPane2.setDividerSize(2);
     
     
     contentPane.add(splitPane2);
     
      f.setSize(250,200);
      f.show();
      f.addWindowListener(
           new WindowAdapter(){
               public void windowClosing(WindowEvent e){
                  System.exit(0);        
               }        
           }        
      );     
}        
public static void main(String[] args){
     new JSplitPane1();
}
}

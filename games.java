import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.io.FileReader;
import java.lang.String;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import javax.swing.text.BadLocationException;

public class games extends Thread implements ActionListener{
    private JPanel jp1,jp2,jp3;
    private JMenuItem menuopen,menuexit,menuabout;
    private JTextField speed_jtf, remainT_error_jtf;
    private JTextPane jta1;
    private JTextArea jta2;
    //private JTextPane jtp1,jtp2;
    private JTextPane jta3;

    private JButton jb_start_test, jb_end_test, jb_addfile;
    private CardLayout card1,card2,card3;
    private File getPath;
    private StringBuffer strbuffer_file;
    private String strtTime;
    private String state="";//addFile/start/over
    private boolean fileover;
    private boolean lock=true;
    private double starttime;
    private int count_word,count_errorword;

    private int []error;
    private int []error1;
    private StringBuffer str_all;

    private int count=0;

    private int count1=0;
    //大体界面的设计
    public void setwindow(){
        JFrame jf=new JFrame("word_exercise");

        Container cont=jf.getContentPane();
        cont.setLayout(new BorderLayout());

        Font ft=new Font("宋体",Font.PLAIN,22);

        //菜单设计
        JMenuBar jmb=new JMenuBar();
        JMenu jm_file,jm_help;
        jm_file=new JMenu("文件");
        jm_help=new JMenu("帮助");
        //菜单选项内容的设计
        menuopen=new JMenuItem("打开");
        menuopen.setActionCommand("打开");

        menuexit=new JMenuItem("退出");
        menuexit.setActionCommand("退出");

        menuabout=new JMenuItem("关于");
        menuabout.setActionCommand("关于");
        jm_file.add(menuopen);
        jm_file.addSeparator();//下划线隔开
        jm_file.add(menuexit);

        jm_help.add(menuabout);
        jmb.add(jm_file);
        jmb.add(jm_help);

        //显示数量，耗时和错误率框
        speed_jtf =new JTextField();
        speed_jtf.setFont(ft);
        speed_jtf.setEditable(false);

        remainT_error_jtf =new JTextField();
        remainT_error_jtf.setFont(ft);
        remainT_error_jtf.setEditable(false);

        JPanel jptf=new JPanel(new GridLayout(0,2));
        jptf.add(speed_jtf);
        jptf.add(remainT_error_jtf);
        cont.add(BorderLayout.NORTH,jptf);
        //大的容器
        JPanel jp_text;
        jp_text=new JPanel();
        jp_text.setLayout(new GridLayout(3,0,0,30));

        //两个文本框的
        jta1=new JTextPane();

        jta3=new JTextPane();
        jta2=new JTextArea(){
            private static final long serialVersionUID = 1L;

            @Override
            public void paste(){

            }
        };
        jta1.setFont(ft);
        //jta1.setLineWrap(true);//自动换行
        jta1.setEditable(false);
        jta1.setBackground(new Color(200,255,200));//绿色的背景颜色

        Style def=jta1.getStyledDocument().addStyle(null,null);
        StyleConstants.setFontFamily(def,"宋体");
        StyleConstants.setFontSize(def,22);
        Style normal=jta1.addStyle("normal",def);
        Style s=jta1.addStyle("red",normal);
        StyleConstants.setForeground(s,Color.RED);
        jta1.setParagraphAttributes(normal,true);
        //jta1.setSize(300,40);

        jta2.setFont(ft);
        //Style j2=jta2.getStyledDocument().addStyle(null,null);
        //StyleConstants.setFontFamily(j2,"宋体");
        //StyleConstants.setFontSize(j2,22);
       // jta2.setLineWrap(true);
        jta2.setEditable(false);
        jta2.setBackground(new Color(255,255,255));//白色


       // jta3.setFont(ft);
        jta3.setEditable(false);
        jta3.setBackground(new Color(255,255,255));//绿色的背景颜色

        Style a=jta3.getStyledDocument().addStyle(null,null);
        StyleConstants.setFontFamily(a,"宋体");
        StyleConstants.setFontSize(a,22);
        Style nor=jta3.addStyle("nor",a);
        Style r=jta3.addStyle("r",nor);
        StyleConstants.setForeground(r,Color.RED);
        jta3.setParagraphAttributes(nor,true);

/*
        //jpanel
        jtp1=new JTextPane();
        jtp2=new JTextPane(){
            private static final long serialVersionUID=1L;

            public void paste(){}
        };//序列化实体类

       jta1.setFont(ft);
       jta1.setEditable(false);
       jta1.setBackground(new Color(200,255,200));
       jta2.setFont(ft);
       jta2.setEditable(false);
       jta2.setBackground(new Color(255,255,255));
*/
        JScrollPane scrollPane=new JScrollPane(jta1);

        //第一个小容器的设计
        jb_addfile =new JButton("添加文件");
        jb_addfile.setActionCommand("打开");

        JPanel jpb;
        jpb=new JPanel(new CardLayout(300,20));
        jpb.add("add_file", jb_addfile);


        JScrollPane scroll1,scroll2,scroll3;
        scroll1=new JScrollPane(jta1);
        scroll2=new JScrollPane(jta2);
        scroll3=new JScrollPane(jta3);

        card1=new CardLayout();
        jp1=new JPanel(card1);
        jp1.add("card1",jpb);
        jp1.add("card2",scroll1);

       card3=new CardLayout();
        jp3=new JPanel(card3);
        jp3.add("card3",scroll3);

        //第二个小容器的设计
        card2=new CardLayout();
        jp2=new JPanel(card2);
        jp2.add("card2",scroll2);
        //
        jp_text.add(jp1);
        jp_text.add(jp3);
        jp_text.add(jp2);
        cont.add(BorderLayout.CENTER,jp_text);

        JPanel jpf;
        jpf=new JPanel();
        jpf.setLayout(new FlowLayout(FlowLayout.CENTER,50,0));
        jb_start_test =new JButton("开始测试");
        jb_start_test.setActionCommand("开始");
        jb_end_test =new JButton("结束测试");
        jb_end_test.setActionCommand("结束");
        jpf.add(jb_start_test);
        jpf.add(jb_end_test);
        cont.add(BorderLayout.SOUTH,jpf);

        jf.setBounds(250,50,810,630);
        jf.setJMenuBar(jmb);
        jf.setVisible(true);//窗口可见
        jb_addfile.requestFocus();//获取焦点
        jf.setResizable(false);//窗口不可拉伸
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //判断弹出框输入是否为数字

    public boolean is_Num(String str){
        for(int i=str.length();--i>0;){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    //读取文件
    public void ready_File(){
        String strFile="";
        try{
            String strLine = "";
            FileReader in = new FileReader(String.valueOf(getPath));
            BufferedReader buf = new BufferedReader(in);
            strLine = buf.readLine();
            while (strLine != null) {
                strFile += strLine;
                strLine = buf.readLine();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        strbuffer_file=new StringBuffer(strFile);
        str_all=new StringBuffer(strFile);
        fileover=false;
    }

    //显示文本文件
    public void read_File(){
        String strpart="";
        int n=320;
        if(strbuffer_file.length()<=n){
            jta1.setText(strbuffer_file+"\n");
            fileover=true;
            return;
        }
        strpart=strbuffer_file.substring(0,n);
        strbuffer_file=strbuffer_file.delete(0,n);
        jta1.setText(strpart+"\n");
    }

    public void readyFile() {

        String strFile = "";

        try {
            String strLine = "";
            FileReader in = new FileReader(String.valueOf(getPath));
            BufferedReader buf = new BufferedReader(in);
            strLine = buf.readLine();
            while (strLine != null) {
                strFile += strLine;
                strLine = buf.readLine();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        strbuffer_file = new StringBuffer(strFile);
        fileover = false;
    }

    //读取文件并显示
    public void readFile(){
        String strPart="";
        int n=320;
        if(strbuffer_file.length()<=n){
            jta1.setText(strbuffer_file+"\n");
            fileover=true;
            return;
        }
        strPart=strbuffer_file.substring(0,n);
        strbuffer_file=strbuffer_file.delete(0,n);
        jta1.setText(strPart+"\n");
    }

    //剩余时间and错误率 速率 顶上两个框的显示内容
    private class Timer implements Runnable {
        public void run() {
            double nowTime, spendTime, setTime, speed = 0, remainTime;
            int count, remainMin, remainSec;
            nowTime = System.currentTimeMillis();//获取当前的时间
            spendTime = (nowTime - starttime) / 60000;
            setTime = Double.parseDouble(strtTime);
            while (spendTime < setTime && !state.equals("over")) {
                count = jta2.getText().length();//输入字符的数量
                speed = (count_word + count) / spendTime;//计算速率
                remainTime = setTime - spendTime;//剩余的时间
                remainMin = (int) remainTime;
                remainSec = (int) Math.round((remainTime - remainMin) * 60);//剩余的秒数
                speed_jtf.setText("当前的速度：" + Math.round(speed) + "字/分");
                remainT_error_jtf.setText("剩余时间：" + remainMin + "分" + remainSec + "秒");
                try {
                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                nowTime = System.currentTimeMillis();
                spendTime = (nowTime - starttime) / 60000;
            }
            speed_jtf.setText("速度" + Math.round(speed) + "字/分");
            putScore();//
        }

        public void putScore() {
            jta2.setEditable(false);
            String str1, str2;
            int length1, length2;
            str1 = jta1.getText();
            length1 = str1.length();
            str2 = jta2.getText();
            length2 = str2.length();

            error=new int[length1];
           for(int i=0;i<length1;i++)
           { error[i]=1;}

            //计算错误率
            double count_error;
            int k = length1 < length2 ? length2 - length1 : 0;
            int length = length1 < length2 ? length1 : length2;

            for (int i = 0; i < length; i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    k++;
                   error[i] = 0;
                    // i是错误的位置
                }
            }
            count_error = (count_word + length) == 0 ? 0 : ((count_errorword + k) * 1.0 / (count_word + length)) * 100;//计算错误率
            //防止错误率大于100%

            if ((Math.ceil(count_error * 100) / 100.0 < 100) && (Math.ceil(count_error * 100) / 100.0 > 0))
                remainT_error_jtf.setText("错误率:" + Math.ceil(count_error * 100) / 100.0 + "%");
            else
                remainT_error_jtf.setText("错误率：" + 100.0 + "%");

            for(int i=0;i<str2.length();i++)
            {
                if(str2.charAt(i)=='a')
                    count1=count1+1;
            }
            System.out.print(count1);


            count_errorword = 0;
            count_word = 0;
            state = "addFile";//设置标志为addfile
            lock = true;//lock为true
            jb_start_test.requestFocus();

            //显示错误 不过是结束后显示
            //push_error();

    }
    public void push_error(){
        String str1,str2;
        //int length1,length2;
        str1=jta1.getText();
        //length1=str1.length();
        str2=jta2.getText();
       // length2=str2.length();

        String error_word = "";


        for (int b = 0; b < str2.length(); b++) {
            String test = "";
            test = String.valueOf(str1.charAt(b));
            error_word += test;

            try {
                jta1.getDocument().insertString(jta1.getDocument().getLength(), String.valueOf(str1.charAt(b)), jta1.getStyle(error[b] == 0 ? "red" : "normal"));
            } catch (BadLocationException e1) { }
         }
        }

    }
    //点击开始测试后设置时间
    public void set_start(){
        Font ft=new Font("宋体",Font.PLAIN,20);
        JPanel jpanel=new JPanel(new GridLayout(2,0));
        JLabel jlael=new JLabel("设置测试时间");
        jlael.setFont(ft);

        JTextField jtf_time=new JTextField();
        jpanel.add(jlael);
        jpanel.add(jtf_time);

        int select=JOptionPane.showConfirmDialog(null,jpanel,"设置",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(select!=JOptionPane.OK_OPTION)
            return;
        strtTime=jtf_time.getText();

        if(is_Num(strtTime)){
                if (jta1.getText()!=null){
                    //读取文件
                    readyFile();
                    readFile();
                }
                card2.show(jp2,"card2");

                jta2.setText(null);
                jta2.setEditable(true);
                jta2.requestFocus();
                state="start";
                lock=false;
                starttime=System.currentTimeMillis();
                Timer ti=new Timer();
                Thread t=new Thread(ti);
                t.start();
            }
            else{//不然弹出消息框表示重新输入
                JOptionPane.showMessageDialog(null,"请输入数字","提示",JOptionPane.INFORMATION_MESSAGE);
            }
        }

    /*
    public void show_error() {
        String str1, str2;
        //int length1, length2;
        str1 = jta1.getText();
        //length1 = str1.length();
        str2 = jta2.getText();
        //length2 = str2.length();

        String error_word = "";


        for (int b = 0; b < str2.length(); b++) {
            String test = "";
            test = String.valueOf(str1.charAt(b));
            error_word += test;

            try {
                jta1.getDocument().insertString(jta1.getDocument().getLength(), String.valueOf(str1.charAt(b)), jta1.getStyle(error[b] == 0 ? "red" : "normal"));

            } catch (BadLocationException e1) { }
        }
    }
*/
    //添加文件
    public void add_file(){
        JFileChooser filechoose=new JFileChooser();
        filechoose.setCurrentDirectory(new File("files"));
        //过滤器 过滤除txt以外的文件
        filechoose.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return
                        f.getName().toLowerCase().endsWith(".txt")||f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "文本文档*.txt*";
            }
        });
        if(filechoose.showOpenDialog(null)==JFileChooser.CANCEL_OPTION)return;
        getPath=filechoose.getSelectedFile();
        if(!getPath.getPath().toLowerCase().endsWith(".txt"))
        {
            JOptionPane.showMessageDialog(null,"请选择文本文档","提示",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        readyFile();
        readFile();
        state="addFile";
        card1.show(jp1,"card2");
        jb_start_test.requestFocus();
    }

    public void setListerner(){
        menuopen.addActionListener(this);
        menuexit.addActionListener(this);
        menuabout.addActionListener(this);

        jb_addfile.addActionListener(this);
        jb_start_test.addActionListener(this);
        jb_end_test.addActionListener(this);


        jb_addfile.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e3) {

            }

            @Override
            public void keyPressed(KeyEvent e2) {
                if(e2.getKeyCode()==KeyEvent.VK_ENTER)
                    add_file();
            }

            @Override
            public void keyReleased(KeyEvent e1) {

            }
        });
        jta2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e1) {
                String str1=jta1.getText();
                String str2=jta2.getText();
                if(str2.length()<=str1.length()) {
                    int length1 = str1.length();
                    error1 = new int[length1];
                    for (int i = 0; i < length1; i++) {
                        error1[i] = 1;
                    }

                    String error_words = "";
                    for (int i = 0; i < str2.length(); i++) {
                        if (str1.charAt(i) != str2.charAt(i)) {
                            error1[i] = 0;
                            //error_words=String.valueOf(str2.charAt(i));
                        }

                    }
                    count = count + 1;
                    jta3.setText(null);
                    for (int i = 0; i < str2.length(); i++) {
                        try {
                            jta3.getDocument().insertString(jta3.getDocument().getLength(), String.valueOf(str2.charAt(i)), jta3.getStyle(error1[i] == 0 ? "r" : "nor"));
                        } catch (BadLocationException e) {
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"已经输入完文字,请结束测试","信息",JOptionPane.PLAIN_MESSAGE);

                }
                if (str1.length()==str2.length()&&str2.endsWith("\n")){
                    if(fileover){
                        state="over";

                    }
                    else{
                        count_word+=str2.length();
                        for (int i=0;i<str2.length();i++){
                            if(str1.charAt(i)!=str2.charAt(i))
                            {
                                count_errorword++;
                            }

                            //show_error();
                        }
                            System.out.println(count1);
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                jta2.setText(null);
                            }
                        });
                        readFile();
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e2) {

            }

            @Override
            public void changedUpdate(DocumentEvent e3) {

            }
        });
    }

    public void actionPerformed(ActionEvent e){

        String strAction=e.getActionCommand();
        if(strAction=="打开"&&lock){
            jta1.setText(null);
            jta2.setText(null);
            jta3.setText(null);
            add_file();
        }
        else if(strAction=="退出"){
            System.exit(1);
        }
        else if(strAction=="关于"){
            String str="作者：luojiebin20162180099";
            JOptionPane.showMessageDialog(null,str,"信息",JOptionPane.PLAIN_MESSAGE);
        }
        else if (strAction=="开始"&&lock){
            if(!state.equals("addFile"))
            {
                JOptionPane.showMessageDialog(null,"无有效文档","消息",JOptionPane.INFORMATION_MESSAGE);
            }
            else set_start();
        }
        else if(strAction=="结束"){
            if(!state.equals("start"))
            {
                JOptionPane.showMessageDialog(null,"还未测试","消息",JOptionPane.INFORMATION_MESSAGE);
            }
            else state="over";
        }
    }
    public void run(){
        setwindow();
        setListerner();
    }

}

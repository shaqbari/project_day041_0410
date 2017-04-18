/*join ���̶�?
 * -����ȭ�� ���� ���������� �и��� ���̺��� ��ġ �ϳ��� ���̺�ó��
 * ������ �� �ִ� ���� 
 * 
 * ���ݱ��� ��°��� inner join�̾���.
 * outer join�� �ִ�.
 * 
 * inner join : ���δ���� �Ǵ� ���̺� �������� ���ڵ常 �����´�.
 * 		��������: �������� ���ڵ尡 �ƴ� ��� ������Ų��.!!	
 * 
 *tip:�����ڵ��� outer join�� ���� �����.
 * outer join : ���δ���� �Ǵ� ���̺� ����� ���ڵ�Ӹ� �ƴ϶�,
 * 					������ ���̺��� ���ڵ�� ��� �����´�. 
 * 
 * */

package com.ss.bread;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements ItemListener, ActionListener{
	JPanel p_west, p_center, p_east;
	JPanel p_center_up, p_center_down;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;
	
	//���ʿ���
	Choice ch_top, ch_sub;
	JTextField t_name_west, t_price_west;
	Canvas can_west;
	BufferedImage image=null;
	JButton bt_regist;
	
	//���ʿ���
	Canvas can_east;
	
	JTextField t_id_east, t_name_east, t_price_east;
	JButton bt_update, bt_delete;
	
	DBManager manager;
	Connection con;
	ArrayList<Top_category> topList=new ArrayList<Top_category>();
	ArrayList<Sub_category> subList=new ArrayList<Sub_category>();
	
	//Tablemodel ��ü��
	UpModel upModel;
	DownModel downModel;
	
	JFileChooser chooser;
	File file;
	
	
	public MainWindow() {
		p_west=new JPanel();
		p_center=new JPanel();
		p_east=new JPanel();
		p_center_up=new JPanel();
		p_center_down=new JPanel();
		table_up=new JTable();
		table_down=new JTable();
		scroll_up=new JScrollPane(table_up);
		scroll_down=new JScrollPane(table_down);
				
		//���ʿ���
		ch_top=new Choice();
		ch_sub=new Choice();
		t_name_west=new JTextField(10);
		t_price_west=new JTextField(10);
		
		try {
			URL url=this.getClass().getResource("/noimg.png");
			image=ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		can_west=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage((Image)image, 0, 0, 135, 135, this); //�����͸�Ŭ������ final�� ����� ���������� ������ �����Ƿ� ��������� ������
			}
		};
		can_west.setPreferredSize(new Dimension(135, 135));
		
		
		bt_regist=new JButton("���");
					
		//���ʿ���
		can_east=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135, 135, this);
			}		
		};
		can_east.setPreferredSize(new Dimension(135, 135));
		
		t_id_east=new JTextField(10);
		t_name_east=new JTextField(10);
		t_price_east=new JTextField(10);
		bt_update=new JButton("����");
		bt_delete=new JButton("����");
				
		//�� �г��� ���� ����
		p_west.setBackground(Color.CYAN);
		p_center.setBackground(Color.RED);
		p_east.setBackground(Color.YELLOW);
		p_center_up.setBackground(Color.ORANGE);
		p_center_down.setBackground(Color.GREEN);
		
		//�� �гε��� ũ������
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		ch_top.setPreferredSize(new Dimension(135, 40));
		ch_sub.setPreferredSize(new Dimension(135, 40));
		ch_top.add("������� ���á�");
		ch_sub.add("������� ���á�");
		
		chooser=new JFileChooser("E:/git/java_workspace3/project_day041_0410/res/");
		
		//���� ����
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name_west);
		p_west.add(t_price_west);
		p_west.add(can_west);
		p_west.add(bt_regist);

		//����� ��ũ�� ����
		p_center_up.setLayout(new BorderLayout()); //���޶�ٰ� border��
		p_center_down.setLayout(new BorderLayout());		
		p_center_up.add(scroll_up);
		p_center_down.add(scroll_down);
		
		//���� ����
		p_east.add(can_east);
		t_id_east.setEditable(false);//�������ϰ�
		p_east.add(t_id_east);		
		p_east.add(t_name_east);
		p_east.add(t_price_east);
		p_east.add(bt_update);
		p_east.add(bt_delete);		
		
		//������ �׸��� �����ϰ� �� �Ʒ� ����
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_center_up);
		p_center.add(p_center_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east, BorderLayout.EAST);
				
		//���̽��� ������ ����
		ch_top.addItemListener(this);
		bt_regist.addActionListener(this);	
		
		//ĵ������ ���콺 ������ ����
		can_west.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				preView();
			}			
		});
		
		//�ٿ����̺�� ������ ����
		table_up.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row=table_up.getSelectedRow();
				int col=0;
				String sub_category_id=(String) table_up.getValueAt(row, col);				
				System.out.println(sub_category_id);
				
				//������ id�� �Ʒ��� �𵨿� ��������.
				downModel.getList(Integer.parseInt(sub_category_id));			
				table_down.updateUI();
				
			}
		});
		
		table_down.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row=table_down.getSelectedRow();
				
				//������ ���Ϳ� ��� �ִ� ���͸� ������.
				Vector vec=downModel.data.get(row);
				//System.out.println(vec.get(2));
				getDetail(vec);
			}			
		});
				
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		init();
		getTop();
		getUpList(); //���� ���̺� ó��
		getDownList();
	}
	
	//�����ͺ��̽� Ŀ�ؼ� ���
	public void init(){
		manager=DBManager.getInstance();
		con=manager.getConnection();
		System.out.println(con);
		
	}
	
	//�ֻ��� ī�װ� ���
	public void getTop(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
				
		String sql="select * from top_category order by top_category_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				//ch_top.add(rs.getString("top_category_name")); dto�� �Ẹ��
				Top_category dto=new Top_category();
				dto.setTop_category_id(rs.getInt("top_category_id"));
				dto.setTop_category_name(rs.getString("top_category_name"));
			
				topList.add(dto);//����Ʈ�� ž��
				ch_top.add(dto.getTop_category_name());
			}
			
			/*for (int i = 0; i < topList.size(); i++) {
				ch_top.add(topList.get(i).getTop_category_name());
			}*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}			
		}		
	}
	
	//���� ���̺� ������ ó��
	public void getUpList(){
		table_up.setModel(upModel=new UpModel(con));
		table_up.updateUI();		
	}
	
	//�Ʒ��� ���̺� ������ ó��
	public void getDownList(){
		table_down.setModel(downModel=new DownModel(con));
		table_down.updateUI();
	}
	
	//���ε� ����
	public void getSub(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from sub_category where top_category_id=?"; //bind���� �̿�����
		try {
			pstmt=con.prepareStatement(sql);
			//���ε� ������ ����
			int index=ch_top.getSelectedIndex();
			if(index-1>=0){
				Top_category dto=topList.get(index-1);
				pstmt.setInt(1, dto.getTop_category_id());//ù��° �߰ߵ� ���ε庯����, ������ ������ ���Ѵ�. 
				rs=pstmt.executeQuery();
								
				//������� �����
				subList.removeAll(subList);
				ch_sub.removeAll();
				
				while (rs.next()) {
					Sub_category subDto=new Sub_category();
					subDto.setSub_category_id(rs.getInt("sub_category_id"));
					subDto.setTop_category_id(rs.getInt("top_category_id"));
					subDto.setSub_cateogry_name(rs.getString("sub_category_name"));
					
					subList.add(subDto);
					ch_sub.add(subDto.getSub_cateogry_name());
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		//����ī�װ� ���ϱ�
		getSub();		
		
	}
	
	/*---------------------------------------------------------
	 * ��ǰ���
	 * ----------------------------------------------------------*/	
	public void regist(){
		PreparedStatement pstmt=null;		
				
		String sql="insert into product(product_id, sub_category_id, product_name, product_price, product_img)";
		sql+="values(seq_product.nextVal, ?, ?, ? ,?)";
		
		try {
			pstmt=con.prepareStatement(sql);
			
			//arraylist�ȿ� ����ִ� subcategory dto�� �����Ͽ� fk���� �־�����
			/*int index=ch_sub.getSelectedIndex();
			Sub_category vo=subList.get(index);
			int sub_id=vo.getSub_category_id();*/
			
			//���ε� ������ �� �� ����
			pstmt.setInt(1, subList.get(ch_sub.getSelectedIndex()).getSub_category_id());
			pstmt.setString(2, t_name_west.getText());
			pstmt.setInt(3, Integer.parseInt(t_price_west.getText()));
			pstmt.setString(4, file.getName());
			
			System.out.println(sql);
			//executeUpdate�޼ҵ�� ������ ���� �� �ݿ��� ���ڵ��� ������ ��ȯ���ش�.
			//���� insert���� ��� ������ �����ߴٸ� 1��, update�� 1�� �̻�, delete 1��
			//���) insert�� ��ȯ���� 0�̶�� insert ����
			int result=pstmt.executeUpdate();
			if (result!=0) {
				JOptionPane.showMessageDialog(this, "��ϼ���");
								
				upModel.getList(); //db�� ���Ӱ� ������ ������ ���� ����
				table_up.updateUI();
				
				copy();
				
			}else{
				JOptionPane.showMessageDialog(this, "��Ͻ���");								
			}			
						
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//ĵ������ �̹��� �ݿ��ϱ�
	public void preView(){
		int result=chooser.showOpenDialog(this);
		if (result==JFileChooser.APPROVE_OPTION) {
			//ĵ������ �̹��� �׸���!
			file=chooser.getSelectedFile(); //�����Ҷ� ���� ���������
			
			//����� ������ ������ �̹����� ��ü�Ѵ�.
			//image=���ϰ� ������ ���ο��̹���;
			try {
				image=ImageIO.read(file);
				can_west.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//���� �޼ҵ� ����
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream("E:/git/java_workspace3/project_day041_0410/data/"+file.getName());			
			
			byte[] b=new byte[1024];			
			int flag; //-1���� �Ǵ�
			while (true) {
				flag=fis.read(b);
				if (flag == -1) break;
				fos.write(b);
			}
			System.out.println("�̹��� ���� �Ϸ�");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//������ �����ֱ�
	public void getDetail(Vector vec){
		t_id_east.setText(vec.get(0).toString());
		t_name_east.setText(vec.get(2).toString());
		t_price_east.setText(vec.get(3).toString());
		try {
			image=ImageIO.read(new File("E:/git/java_workspace3/project_day041_0410/data/"+vec.get(4)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		can_east.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		regist();
	}
		
	public static void main(String[] args) {
		new MainWindow();
		
	}



}

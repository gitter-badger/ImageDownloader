package gui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;

import app.Applikation;

import javax.swing.DropMode;

import parsers.*;

import gui.BackgroundArea;

import java.io.IOException;
import java.util.Random;




public class GUI {

	private JFrame frmPahealImageDownloader;
	private JTextField txtInsertTagHere;
	private JTextArea textArea;
	private JSpinner PageSpinner;
	private JComboBox<Object> comboBox;
	private ImageParser parser;
	private JButton Parsebtn;
	private JTextField DelayField;
	private BufferedImage bgImage;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblTag;
	
	private final String FOURCHAN_THREAD = "4chan-Thread - http://boards.4chan.org/x/res/123123";
	private final String FOURCHAN_BOARD = "4chan-Board - http://boards.4chan.org/x/catalog";
	
	private final String INFINITYCHAN_THREAD = "Infinity Chan Thread - https://8chan.co/BOARD/res/THREADNR.html";
	private final String INFINITYCHAN_BOARD = "Infinity Chan Board - https://8chan.co/BOARDNR/";
	
	private final String PAHEAL = "http://rule34.paheal.net/";
	private final String XXX = "http://rule34.xxx/";
	private final String GELBOORU = "http://gelbooru.com/";
	
	private final String TUMBLR = "Tumblr Artist - http://XxXxXxX.tumblr.com";
	private final String IMGUR = "Imgur-Album - http://imgur.com/a/xXxXx";
	
	private final String GE_HENTAI_SINGLE = "http://g.e-hentai.org/ - Single Album Page";
	private final String GE_HENTAI_MORE = "http://g.e-hentai.org/ - >=1 Pages";
	
	private final String ARCHIVE_MOE_THREAD = "archive.moe/fgts.jp (Thread) - https://archive.moe/BOARD/thread/THREADNR/";
	private final String ARCHIVE_MOE_BOARD = "archive-moe (Board) - https://archive.moe/BOARD/";
	
	private final String FGTS_JP_BOARD = "fgts.jp (Board) - http://fgts.jp/BOARD/";
	

	
	
	private boolean parsing;

	public GUI() {
		
		System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0"); 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frmPahealImageDownloader = new JFrame();
		frmPahealImageDownloader.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/images/icon.png")));
		frmPahealImageDownloader.getContentPane().setBackground(
				UIManager.getColor("Button.background"));
		frmPahealImageDownloader.setResizable(false);
		frmPahealImageDownloader.setTitle("Image Downloader v1.5");
		frmPahealImageDownloader.setBounds(100, 100, 761, 558);
		frmPahealImageDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPahealImageDownloader.getContentPane().setLayout(null);

		JLabel lblMadeByLars = new JLabel("@ berserkingyadis");
		lblMadeByLars.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMadeByLars.setBounds(14, 468, 157, 40);
		frmPahealImageDownloader.getContentPane().add(lblMadeByLars);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		scrollPane.setBounds(205, 185, 542, 331);
		scrollPane.setAutoscrolls(true);
 		frmPahealImageDownloader.getContentPane().add(scrollPane);
 		 
 		try {
			bgImage = ImageIO.read(Applikation.class.getResource("/images/bg"+(new Random().nextInt(4)+1)+".png"));
		} catch (IOException e) {
			appendLog("Could not get Background Image", true);
		}
		textArea = new BackgroundArea(bgImage);
		textArea.setEditable(false);
		textArea.setDropMode(DropMode.INSERT);
		textArea.setColumns(2);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		txtInsertTagHere = new JTextField();
		txtInsertTagHere.setText("insert tag here");
		txtInsertTagHere.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtInsertTagHere.setBounds(14, 208, 181, 30);
		frmPahealImageDownloader.getContentPane().add(txtInsertTagHere);
		txtInsertTagHere.setColumns(10);

		lblTag = new JLabel("Tag:");
		lblTag.setFont(new Font("Dialog", Font.BOLD, 12));
		lblTag.setBounds(12, 183, 175, 24);
		frmPahealImageDownloader.getContentPane().add(lblTag);

		Parsebtn = new JButton("start parsing");
		Parsebtn.setBounds(12, 372, 181, 85);
		frmPahealImageDownloader.getContentPane().add(Parsebtn);

		PageSpinner = new JSpinner();
		PageSpinner.setModel(new SpinnerNumberModel(new Integer(1),
				new Integer(1), null, new Integer(1)));
		PageSpinner.setBounds(155, 249, 40, 30);
		frmPahealImageDownloader.getContentPane().add(PageSpinner);

		lblNewLabel = new JLabel("Pages to parse:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 11));
		lblNewLabel.setBounds(12, 249, 123, 30);
		frmPahealImageDownloader.getContentPane().add(lblNewLabel);

		lblNewLabel_1 = new JLabel("1 Page = 50-60 Pictures");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 290, 175, 30);
		frmPahealImageDownloader.getContentPane().add(lblNewLabel_1);

		comboBox = new JComboBox<Object>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				switch (comboBox.getSelectedItem().toString()) {
				
				case XXX:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("100");
					lblNewLabel.setEnabled(true);
					lblNewLabel_1.setEnabled(true);
					PageSpinner.setEnabled(true);
					lblTag.setText("Tag:");
					txtInsertTagHere.setText("insert tag here");
					break;
				case GE_HENTAI_SINGLE:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("100");
					lblNewLabel_1.setEnabled(true);
					PageSpinner.setEnabled(false);
					lblTag.setText("Page URL:");
					txtInsertTagHere.setText("insert page URL here");
					lblNewLabel.setEnabled(false);
					break;
				case GE_HENTAI_MORE:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("100");
					lblNewLabel.setEnabled(true);
					lblNewLabel_1.setEnabled(true);
					PageSpinner.setEnabled(true);
					lblTag.setText("Album URL:");
					txtInsertTagHere.setText("insert album URL here");
					
					break;
				case PAHEAL:
				case GELBOORU:

					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("100");
					lblNewLabel.setEnabled(true);
					lblNewLabel_1.setEnabled(true);
					PageSpinner.setEnabled(true);
					lblTag.setText("Tag:");
					txtInsertTagHere.setText("insert tag here");
					lblNewLabel.setText("Pages to parse:");
					break;
				case FOURCHAN_THREAD:
				case INFINITYCHAN_THREAD:
				case ARCHIVE_MOE_THREAD:

					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("10");
					lblNewLabel.setText("Pages to parse:");
					lblNewLabel.setEnabled(false);
					lblNewLabel_1.setEnabled(false);
					PageSpinner.setEnabled(false);
					lblTag.setText("Thread URL:");
					txtInsertTagHere.setText("insert link here");
					break;

				case FOURCHAN_BOARD:
				case INFINITYCHAN_BOARD:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("10");
					lblNewLabel.setEnabled(true);
					lblNewLabel.setText("Threads to parse:");
					lblNewLabel_1.setEnabled(false);
					PageSpinner.setEnabled(true);
					lblTag.setText("Board(eg: v or e):");
					txtInsertTagHere.setText("insert Board Letter here");
					break;
				case ARCHIVE_MOE_BOARD:
				case FGTS_JP_BOARD:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("10");
					lblNewLabel.setEnabled(true);
					lblNewLabel.setText("Sites to parse(1 Site = 100 Threads):");
					lblNewLabel_1.setEnabled(false);
					PageSpinner.setEnabled(true);
					lblTag.setText("Board(eg: v or e):");
					txtInsertTagHere.setText("insert Board Letter here");
					break;
				case TUMBLR:
					Parsebtn.setEnabled(true);
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setText("start parsing");
					DelayField.setText("100");
					lblNewLabel.setEnabled(true);
					lblNewLabel_1.setEnabled(true);
					PageSpinner.setEnabled(true);
					lblTag.setText("Artist name:");
					txtInsertTagHere.setText("insert artist name here");
					lblNewLabel.setText("Pages to parse:");
					break;

				case IMGUR:

					lblNewLabel.setEnabled(false);
					lblNewLabel_1.setEnabled(false);
					PageSpinner.setEnabled(false);
					DelayField.setText("100");
					lblTag.setText("Album Letters: (eg: \"Bl1QP\")");
					txtInsertTagHere.setText("insert the letters here");
					Parsebtn.setText("start parsing");
					Parsebtn.setFont(new Font("Dialog", Font.BOLD, 12));
					Parsebtn.setEnabled(true);
					break;
				}
			}
		});

		comboBox.setModel(new DefaultComboBoxModel<Object>(
				new String[] {
						PAHEAL,
						XXX,
						GELBOORU,
						GE_HENTAI_SINGLE,
						GE_HENTAI_MORE,
						FOURCHAN_THREAD,
						FOURCHAN_BOARD,
						ARCHIVE_MOE_THREAD,
						ARCHIVE_MOE_BOARD,
						FGTS_JP_BOARD,
						INFINITYCHAN_THREAD,
						INFINITYCHAN_BOARD,
						TUMBLR,
						IMGUR
						}));
		comboBox.setBounds(434, 35, 313, 24);

		comboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		frmPahealImageDownloader.getContentPane().add(comboBox);

		JLabel lblNewLabel_2 = new JLabel("choose the Site to parse:");
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_2.setBounds(434, 5, 268, 30);
		frmPahealImageDownloader.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel(
				"<html>\nWelcome to the Image Downloader. <br>\nYour can enter the Tag of the images you are looking for below. <br>\nImgur is not supported yet. <br>\nWhen you click parse a folder with the name of the tag will be <br>\ncreated and the images will be downloaded into it. <br><br>\nI strongly advise you to not set the delay under 100 ms, <br>\nif you abuse this you will get banned from the site <br><br>\n\nhave fun :)\n\n\n</html>");
		lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(12, 12, 404, 195);
		frmPahealImageDownloader.getContentPane().add(lblNewLabel_3);

		JLabel lblDelay = new JLabel("Delay(ms):");
		lblDelay.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDelay.setBounds(12, 331, 85, 30);
		frmPahealImageDownloader.getContentPane().add(lblDelay);

		DelayField = new JTextField();
		DelayField.setText("100");
		DelayField.setBounds(140, 331, 55, 30);
		frmPahealImageDownloader.getContentPane().add(DelayField);
		DelayField.setColumns(10);

		JLabel lblPahealSearchFor = new JLabel(
				"<html>\r\npaheal: Search for Characters or Artists\r\n\t\t eg: Tifa Lockhart, Fugtrup<br>\r\nrule34.xxx: Search for things\r\n\t\teg: long hair, hand on hip<br>\r\ngelbooru: Search for what you want<br>\r\n4chan/8chan: paste the Thread  URL/Boardletter in the\r\n\t\tTextfield <br>\r\ntumblr: enter the artist's name<br>imgur: enter the album id</html>");
		lblPahealSearchFor.setVerticalAlignment(SwingConstants.TOP);
		lblPahealSearchFor.setFont(new Font("Dialog", Font.PLAIN, 11));
		lblPahealSearchFor.setBounds(434, 71, 313, 109);
		frmPahealImageDownloader.getContentPane().add(lblPahealSearchFor);

		Parsebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (parsing) {
					parser.diePlease();
					parser = null;

				} else {

					GUI g = getGUI();
					textArea.setText("");
					frmPahealImageDownloader
							.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

					String tag = txtInsertTagHere.getText().replace(' ', '_');
					int pages = (int) PageSpinner.getValue();
					String site = comboBox.getSelectedItem().toString();
					int delay = Integer.parseInt(DelayField.getText());

					switch (site) {
					case PAHEAL:

						parser = new PahealParser();
						parser.setup(tag, pages, g, delay);
						break;
					case XXX:
						parser = new XXXParser();
						parser.setup(tag, pages, g, delay);
						break;
					case GELBOORU:
						parser = new GelBooruParser();
						parser.setup(tag, pages, g, delay);
						break;
					case FOURCHAN_THREAD:
						parser = new FourChanParser(tag, delay, g, 0);
						break;
					case FOURCHAN_BOARD:
						parser = new FourChanParser(tag, delay, g, 1, pages);
						break;
					case INFINITYCHAN_THREAD:
						parser = new InfinityChanParser(tag, delay, g, 0);
						break;
					case INFINITYCHAN_BOARD:
						parser = new InfinityChanParser(tag, delay, g, 1, pages);
						break;
					case ARCHIVE_MOE_THREAD:
						parser = new ArchiveMoeParser(tag, delay, g, 0);
						break;
					case ARCHIVE_MOE_BOARD:
						parser = new ArchiveMoeParser(tag, delay, g, 1, pages);
						break;
					case FGTS_JP_BOARD:
						parser = new ArchiveMoeParser(tag, delay, g, 2, pages);
						break;
					case TUMBLR:
						parser = new TumblrParser();
						parser.setup(tag, pages, g, delay);
						break;
					case IMGUR:
						parser = new ImgurParser(tag,delay,g);
						break;
					case GE_HENTAI_SINGLE:
						parser = new GEHentaiParser(tag,delay,g,false,pages);
						break;
					case GE_HENTAI_MORE:
						parser = new GEHentaiParser(tag,delay,g,true,pages);

					}
					parser.start();
					parsing = true;
					Parsebtn.setText("stop parsing");
				}
			}
		});
		frmPahealImageDownloader.setVisible(true);
		parsing = false;

	}

	public void appendLog(String txt, boolean brk) {
		textArea.append(txt);
		if (brk)
			textArea.append("\n");
	}

	private GUI getGUI() {
		return this;
	}

	public void reportback() {
		Parsebtn.setEnabled(true);
		frmPahealImageDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Parsebtn.setText("start parsing");
		parsing = false;
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class NewSheet {

	public JFrame mainWindow;
	public JPanel contentPane;
	public JPanel menuBar;
	public JScrollPane scrollPane;
	public JButton refresh;
	public JSlider slider;
	public JTextField textField;
	public double scale;
	public JPanel scrollPanel;
	public JPanel spellSlots;
	public JPanel key;
	public int[] maxSlots;
	public TreeMap<String, ArrayList<String>> spells;
	public JComboBox<String> characterList;
	public JButton plusButton;
	public JButton minusButton;
	public ArrayList<SpellCard> cards;
	public ArrayList<String> cardTitles;
	public JPanel spellAdd;
	public JScrollPane spellPane;
	public JList<String> spellList;
	public JButton addButton;

	public NewSheet() throws IOException {

		new File("character").mkdirs();

		spells = new TreeMap<String, ArrayList<String>>();
		FileReader databaseInit = null;
		try {
			databaseInit = new FileReader(new File("newSpellDatabase.txt"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"No database found. Try the readme.", "",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		BufferedReader database = new BufferedReader(databaseInit);

		String read = database.readLine();
		String current = new String();
		while (read != null) {
			if (read.equals("******")) {
				read = database.readLine();
				if (read != null) {
					current = read;
					if (!spells.containsKey(current)) {
						spells.put(current, new ArrayList<String>());
					}
				}
			} else {
				ArrayList<String> temp = spells.get(current);
				if (!temp.contains(read)) {
					temp.add(read);
					spells.put(current, temp);
				}
			}
			read = database.readLine();
		}
		database.close();
		databaseInit.close();

		mainWindow = new JFrame("Spell Sheet");
		mainWindow.setSize(1280, 720);
		mainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.addComponentListener(new ResizeListener(this));

		contentPane = new JPanel(new BorderLayout());
		contentPane.setSize(mainWindow.getWidth(), mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top);
		contentPane.setPreferredSize(new Dimension(mainWindow.getWidth(),
				mainWindow.getHeight() - mainWindow.getInsets().bottom
						- mainWindow.getInsets().top));
		contentPane.setLocation(0, 0);
		// contentPane.setBackground(Color.RED);

		menuBar = new JPanel(null);
		menuBar.setSize(mainWindow.getWidth(), 50);
		menuBar.setPreferredSize(new Dimension(mainWindow.getWidth(), 50));
		menuBar.setLocation(0, 0);
		// menuBar.setBackground(Color.GREEN);

		scale = 0.5;

		key = new JPanel(new GridLayout(3, 1));
		key.setSize(75, 50);
		key.setLocation(200, 0);

		String[] words = { "Level", "Max", "Used" };

		for (String a : words) {
			JLabel temp = new JLabel(a);
			temp.setHorizontalAlignment(JLabel.CENTER);
			key.add(temp);
		}

		menuBar.add(key);

		maxSlots = new int[9];

		spellSlots = new JPanel(new GridLayout(3, 9));
		spellSlots.setSize(425, 50);
		spellSlots.setLocation(275, 0);
		// spellSlots.setBackground(Color.MAGENTA);

		for (int a = 0; a < 9; a++) {
			JLabel temp = new JLabel(a + 1 + "");
			temp.setHorizontalAlignment(JLabel.CENTER);
			spellSlots.add(temp);
		}
		for (int a = 0; a < 9; a++) {
			JLabel temp = new JLabel("0");
			temp.setHorizontalAlignment(JLabel.CENTER);
			temp.addMouseListener(new SpellSlotListener(temp, a, this));
			spellSlots.add(temp);
		}
		for (int a = 0; a < 9; a++) {
			JLabel temp = new JLabel("0");
			temp.setHorizontalAlignment(JLabel.CENTER);
			temp.addMouseListener(new UsedSpellSlotListener(temp, a, this));
			spellSlots.add(temp);
		}

		menuBar.add(spellSlots);

		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		slider.setSize(200, 50);
		slider.setLocation(0, 0);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new SpellSliderListener(this));

		menuBar.add(slider);

		characterList = new JComboBox<String>();
		characterList.setSize(200, 25);
		characterList.setPreferredSize(new Dimension(200, 25));
		characterList.setLocation(700, 12);

		for (File a : new File("character/").listFiles())
			characterList.addItem(a.getName().replaceAll(".txt", ""));

		menuBar.add(characterList);

		minusButton = new JButton("-");
		minusButton.setSize(40, 40);
		minusButton.setSize(new Dimension(40, 40));
		minusButton.setLocation(900, 5);
		minusButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		minusButton.addActionListener(new CharacterListListener(characterList,
				true));

		menuBar.add(minusButton);

		plusButton = new JButton("+");
		plusButton.setSize(40, 40);
		plusButton.setSize(new Dimension(40, 40));
		plusButton.setLocation(940, 5);
		plusButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		plusButton.addActionListener(new CharacterListListener(characterList,
				false));

		menuBar.add(plusButton);

		scrollPanel = new JPanel(new WrapLayout());
		scrollPanel.setMinimumSize(new Dimension(800, 600));

		scrollPane = new JScrollPane(scrollPanel);
		scrollPane.setSize(mainWindow.getWidth(), mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top
				- 50);
		scrollPane.setPreferredSize(new Dimension(mainWindow.getWidth(),
				mainWindow.getHeight() - mainWindow.getInsets().bottom
						- mainWindow.getInsets().top - 50));
		scrollPane.setLocation(0, 0);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		contentPane.add(menuBar, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		spellAdd = new JPanel(null);
		spellAdd.setSize(300, mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top);
		spellAdd.setPreferredSize(new Dimension(300, mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top));
		spellAdd.setLocation(0, 0);

		spellList = new JList<String>();
		spellList.setMinimumSize(new Dimension(300, 300));
		spellList.setLayoutOrientation(JList.VERTICAL);
		spellList.setVisibleRowCount(-1);
		spellList.setLocation(0, 0);

		spellPane = new JScrollPane(spellList);
		spellPane.setSize(300, mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top
				- 50);
		spellPane.setPreferredSize(new Dimension(300, mainWindow.getHeight()
				- mainWindow.getInsets().bottom - mainWindow.getInsets().top
				- 50));
		spellPane.setLocation(0, 0);
		spellPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spellPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		DefaultListModel<String> lm = new DefaultListModel<String>();
		spellList.setModel(lm);

		for (String a : spells.keySet())
			lm.addElement(a);

		addButton = new JButton("Add Spells");
		addButton.setSize(280, 50);
		addButton.setPreferredSize(new Dimension(300, 50));
		addButton.setLocation(980, 0);
		addButton.addActionListener(new ListListener(this));

		menuBar.add(addButton);

		spellAdd.add(spellPane);

		contentPane.add(spellAdd, BorderLayout.EAST);

		scrollPane.revalidate();
		scrollPane.repaint();
		scrollPanel.revalidate();
		scrollPanel.repaint();

		characterList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					updateMaxes();
					addSpells();
					writeSpells(cards);
					clearUsed();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		mainWindow.setContentPane(contentPane);
		updateMaxes();
		addSpells();
	}

	public void resize() {
		if (spellAdd != null) {
			spellAdd.setSize(300,
					mainWindow.getHeight() - mainWindow.getInsets().bottom
							- mainWindow.getInsets().top);
			spellAdd.setPreferredSize(new Dimension(300, mainWindow.getHeight()
					- mainWindow.getInsets().bottom
					- mainWindow.getInsets().top));
			spellPane.setSize(300,
					mainWindow.getHeight() - mainWindow.getInsets().bottom
							- mainWindow.getInsets().top - 50);
			spellPane.setPreferredSize(new Dimension(300, mainWindow
					.getHeight()
					- mainWindow.getInsets().bottom
					- mainWindow.getInsets().top - 50));
		}
	}

	public void addSpells() throws IOException {
		scrollPanel.removeAll();
		FileReader listReader = new FileReader(new File("character/"
				+ characterList.getSelectedItem() + ".txt"));
		BufferedReader list = new BufferedReader(listReader);

		cards = new ArrayList<SpellCard>();
		list.readLine();
		String r = list.readLine();
		while (r != null) {
			cards.add(new SpellCard(this, r));
			r = list.readLine();
		}
		list.close();
		listReader.close();

		Collections.sort(cards);

		for (SpellCard a : cards) {
			scrollPanel.add(a);
		}

		scrollPane.revalidate();
		scrollPane.repaint();
		scrollPanel.revalidate();
		scrollPanel.repaint();
		writeSpells(cards);
	}

	public void addSpellsToList(List<String> list2) throws IOException {
		FileReader fr = new FileReader(new File("character/"
				+ characterList.getSelectedItem() + ".txt"));
		BufferedReader read = new BufferedReader(fr);
		ArrayList<String> list = new ArrayList<String>();
		String q = read.readLine();
		while (q != null) {
			list.add(q);
			q = read.readLine();
		}
		read.close();
		fr.close();
		for (String a : list2) {
			cardTitles = new ArrayList<String>();
			for (SpellCard b : cards) {
				cardTitles.add(b.title);
			}
			if (!cardTitles.contains(a))
				list.add(a);
		}
		FileWriter fw = new FileWriter(new File("character/"
				+ characterList.getSelectedItem() + ".txt"));
		PrintWriter pw = new PrintWriter(fw);
		for (String a : list)
			pw.println(a);
		pw.close();
		fw.close();
	}

	public void updateMaxes() throws IOException {
		FileReader fr = new FileReader(new File("character/"
				+ characterList.getSelectedItem() + ".txt"));
		BufferedReader read = new BufferedReader(fr);
		String d = read.readLine();
		if (d != null) {
			Scanner spells = new Scanner(d);
			for (int a = 0; a < 9; a++) {
				int b = spells.nextInt();
				maxSlots[a] = b;
				((JLabel) spellSlots.getComponent(a + 9)).setText(b + "");
			}
			spells.close();
		} else {
		}
		read.close();
		fr.close();
	}

	public void writeSpells(ArrayList<SpellCard> sC) throws IOException {
		FileWriter fw = new FileWriter(new File("character/"
				+ characterList.getSelectedItem() + ".txt"));
		PrintWriter writer = new PrintWriter(fw);
		for (int a : maxSlots) {
			writer.print(a + " ");
		}
		writer.println();
		for (SpellCard a : sC) {
			writer.println(a.title);
		}
		writer.close();
		fw.close();

	}

	public void clearUsed() {
		for (int a = 0; a < 9; a++) {
			((JLabel) spellSlots.getComponent(a + 18)).setText("0");
		}
	}

	public ImageIcon scale(String s, double scale) {
		ImageIcon temp = null;
		try {
			temp = new ImageIcon(NewSheet.class.getResource(s));
			if (scale == 0.0)
				scale = 0.01;
			temp.setImage(temp.getImage().getScaledInstance(
					(int) (temp.getIconWidth() * scale),
					(int) (temp.getIconHeight() * scale), Image.SCALE_DEFAULT));
		} catch (NullPointerException e) {

		}
		return temp;
	}

	public static void main(String[] args) throws IOException {
		new NewSheet();
	}
}
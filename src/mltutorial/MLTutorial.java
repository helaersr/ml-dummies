package mltutorial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JCheckBox;

import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import mltutorial.Dataset.DNA;
import mltutorial.Node.Neighbor;
import mltutorial.exceptions.NullAncestorException;
import mltutorial.exceptions.TooManyNeighborsException;

import com.swtdesigner.SwingResourceManager;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.distributions.GammaDistribution;

public class MLTutorial extends JFrame {
	private final Color selectionColor = new Color(0,191,255);
	private final Color childrenColor = new Color(131,111,255);

  public enum EvaluationModel {
  	JC("Jukes Cantor 1969",0,true),
  	K2P("Kimura's 2 Parameter",1,true), 
  	HKY85("Hasegewa-Kishino-Yano 1985",1,false), 
  	GTR("General-Time-Reversible",5,false); 
  private final String name;
  private final int rateParameters;
  private final boolean hasEqualBaseFrequencies;
  EvaluationModel(String name, int rateParam, boolean hasEBF){
  	this.name = name;
  	this.rateParameters = rateParam;
  	this.hasEqualBaseFrequencies = hasEBF;
  	}
  public int getNumRateParameters(){return rateParameters;}
  public boolean hasEqualBaseFrequencies(){return hasEqualBaseFrequencies;}
  public String verbose(){return name;}}
  
  public enum RateParameter {
  	A("Alpha"), 
  	B("Beta"), 
  	C("Gamma"), 
  	D("Delta"), 
  	E("Epsilon"), 
  	K("Kappa");
  private final String name;
  RateParameter(String name){this.name = name;}
  public String verbose(){return name;}}  
  
  public enum EvaluationDistribution {NONE, GAMMA}

	private JPanel panel_28;
	private JPanel panel_12;
	private JTextField lambaV_T_PIi_3;
	private JTextField lambaV_T_CL_3;
	private JTextField lambaV_G_PIi_3;
	private JTextField lambaV_G_CL_3;
	private JTextField lambaV_C_PIi_3;
	private JTextField lambaV_C_CL_3;
	private JTextField lambaV_A_PIi_3;
	private JTextField lambaV_A_CL_3;
	private JLabel label_12;
	private JPanel lambda_V_panel;
	private JPanel lambda_I_panel;
	private JPanel lambda_panel;
	private JPanel panel_28_1;
	private JPanel panel_28_2;
	private JTextField lambaV_T_PIi_2;
	private JTextField lambaV_T_CL_2;
	private JTextField lambaV_G_PIi_2;
	private JTextField lambaV_G_CL_2;
	private JTextField lambaV_C_PIi_2;
	private JTextField lambaV_C_CL_2;
	private JTextField lambaV_A_PIi_2;
	private JTextField lambaV_A_CL_2;
	private JTextField lambaV_T_PIi_1;
	private JTextField lambaV_T_K_1;
	private JTextField lambaV_T_CL_1;
	private JTextField lambaV_G_PIi_1;
	private JTextField lambaV_G_K_1;
	private JTextField lambaV_G_CL_1;
	private JTextField lambaV_C_PIi_1;
	private JTextField lambaV_C_K_1;
	private JTextField lambaV_C_CL_1;
	private JTextField lambaV_A_PIi_1;
	private JTextField lambaV_A_K_1;
	private JTextField lambaV_A_CL_1;
	private JLabel lambda;
	private JLabel label_9;
	private JCheckBox pinvCheckbox;
	private JCheckBox gammaCheckBox;
	private JLabel label_7;
	private JPanel gammaMainPanel;
	private JPanel gammaCutPanel;
	private JPanel gammaRatesPanel;
	private JPanel gammaGraphPanel;
	private JPanel pinvPanel;
	private JPanel rateParamPanel;
	private JPanel rateKappaPanel;
	private JTextField kappaField;
	private JTextField mL_lambda_Invar;
	private JTextField mL_invar;
	private JTextField mL_lambda_Var;
	private JTextField mL_Var;
	private JTextField ML_site;
	private JTextField lambaV_T_PIi;
	private JTextField lambaV_T_K;
	private JTextField lambaV_T_CL;
	private JTextField lambaV_G_PIi;
	private JTextField lambaV_G_K;
	private JTextField lambaV_G_CL;
	private JTextField lambaV_C_PIi;
	private JTextField lambaV_C_K;
	private JTextField lambaV_C_CL;
	private JTextField lambaV_A_PIi;
	private JTextField lambaV_A_K;
	private JTextField lambaV_A_CL;
	private JLabel lambda_variant;
	private JLabel lamba_invariant = new JLabel("");
	private JLabel likelihoodLabel;
	private JLabel PPic;
	private JLabel QPic;
	private JLabel xAsLabel;
	private JTextField condLikNodeCT = new JTextField();
	private JTextField condLikNodeCG = new JTextField();
	private JTextField condLikNodeCC = new JTextField();
	private JTextField condLikNodeCA = new JTextField();
	private JTextField condLikNodeBT = new JTextField();
	private JTextField condLikNodeBG = new JTextField();
	private JTextField condLikNodeBC = new JTextField();
	private JTextField condLikNodeBA = new JTextField();
	private JTextField condLikNodeA8 = new JTextField();
	private JTextField condLikNodeA7 = new JTextField();
	private JTextField condLikNodeA6 = new JTextField();
	private JTextField condLikNodeA5 = new JTextField();
	private JTextField condLikNodeA4 = new JTextField();
	private JTextField condLikNodeA3 = new JTextField();
	private JTextField condLikNodeA2 = new JTextField();
	private JTextField condLikNodeA1 = new JTextField();
	private JTextField condLikNodeAT = new JTextField();
	private JTextField condLikNodeAG = new JTextField();
	private JTextField condLikNodeAC = new JTextField();
	private JTextField condLikNodeAA = new JTextField();
	private JTextField vABField;
	private JTextField vACField;
	private JLabel categoryLabel;
	private JLabel siteLabel = new JLabel();
	private JLabel nodeCLabel;
	private JLabel nodeBLabel;
	private JLabel nodeALabel;
	private JLabel selectedSiteLabel = new JLabel();
	private JTextField gammaShapeTextField;
	private JLabel gammaRateslabel;
	private JLabel cutpointsLabel;
	private JTextField piTextField;
	private JTextField scalingField;
	private JTextField freqTField;
	private JTextField freqGField;
	private JTextField freqCField;
	private JTextField freqAField;
	private JTextField rateEField;
	private JTextField rateDField;
	private JTextField rateCField;
	private JTextField rateBField;
	private JTextField rateAField;
	private JSpinner kSpinner;
	private JLabel vACMatrixLabel;
	private JLabel vABMatrixLabel;
	private JTable matrixP_AC;
	private JTable matrixP_AB;
	private JTable matrixQ;
	private JTable sequencesTable;
	private JSpinner pinvSpinner;
	private JTextField textField_7;
	private JLabel titvLabel;
	private JSpinner ratioTvSpinner;
	private JSpinner ratioTiSpinner;
	private JPanel titvRatioPanel;
	private GraphPanel graphPanel;
	private AggregateLayout<Node,Edge> layout;
	private DefaultModalGraphMouse<Node,Edge> graphMouse;
	private PickedState<Node> psV;
	private PickedState<Edge> psE;
	private GraphZoomScrollPane graphScrollPane;
	private VisualizationViewer<Node,Edge> vv;
	private VertexShapeFactory<String> shapeFactory;
	
	private boolean isAdjustingModel = false;
	private boolean isAdjustingRatio = false;
	private boolean isPicking = false;
	private final Map<String, Integer> categories = new TreeMap<String, Integer>();
	private final Map<String, Integer> sites = new TreeMap<String, Integer>();
	
	private Dataset dataset = new Dataset(new String[]{"AGCCTA","AGCCTA","AGCAGN","CGCATN","ATCCTA","ATCGTA"});
	private int s = 0;
	private double pi = 0.0;
	private EvaluationModel model = EvaluationModel.JC;
	private Map<RateParameter,Double> rateParam = new HashMap<RateParameter, Double>();
	private EvaluationDistribution distribution = EvaluationDistribution.GAMMA;
	private int k = 1;
	private int c = 0;
	private double alpha = 1.0;
	private int selectedBase = Dataset.A;
	private int selectedConditionalLikelihood = 1;
	private List<Node> tree = new ArrayList<Node>();
	private Node root, A, B, C;
	private Graph<Node, Edge> treeGraph;
	private Likelihood likelihood;
	
	class SequencesTableModel extends AbstractTableModel {
		private final String[][] CELLS = new String[][] {
			{"Taxa", "1", "2", "3", "4", "5", "6"},
			{"1", "A", "G", "C", "C", "T", "A"},
			{"2", "A", "G", "C", "C", "T", "A"},
			{"3", "A", "G", "C", "A", "G", "N"},
			{"4", "C", "G", "C", "A", "T", "N"},
			{"5", "A", "T", "C", "C", "T", "A"},
			{"6", "A", "T", "C", "G", "T", "A"}
		};
		public int getRowCount() {
			return CELLS.length;
		}
		public int getColumnCount() {
			return CELLS[0].length;
		}
		public String getColumnName(int column) {
			return CELLS[0][column];
		}
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
			return String.class;
		}
		public boolean isCellEditable(int row, int col) {
			return (col!=0 && row!=0);
		}
		public Object getValueAt(int row, int column) {
			return CELLS[row].length > column ? CELLS[row][column] : (column + " - " + row);
		}
		public void setValueAt(Object o, int row, int col) {
			try {
				DNA d = DNA.valueOf(o.toString().toUpperCase());
				CELLS[row][col] = d.toString();
				dataset.changeNucleotide(row-1, col-1, d.toString());
				adjustFrequencies();
				sequencesTable.repaint();
				parametersHaveChanged();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "Sequence edition error", JOptionPane.ERROR_MESSAGE);
			}
		}		
	}

	private class SequenceTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			JLabel label = (JLabel) comp;
			label.setHorizontalTextPosition(JLabel.CENTER);
			if (value == null) value = "";
			label.setText(value.toString());
			if (column == 0 || row == 0){
				label.setBackground(Color.BLACK);
				label.setForeground(Color.WHITE);				
			}else if (!table.isCellEditable(row, column)){
				label.setBackground(Color.LIGHT_GRAY);
				label.setForeground(Color.BLACK);
			}else{
				label.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
			}
			if (isSelected) {
				label.setBackground(selectionColor);
				label.setForeground(Color.BLACK);					
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}

	class MatrixTableModel extends AbstractTableModel {
		private final String[][] CELLS = new String[4][4];
		private int highlightedRow = -1;
		private int highlightedColumn = -1;
		
		public int getRowCount() {
			return CELLS.length;
		}
		public int getColumnCount() {
			return CELLS[0].length;
		}
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
			return String.class;
		}
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		public Object getValueAt(int row, int column) {
			return CELLS[row].length > column ? CELLS[row][column] : (column + " - " + row);
		}
		public void setValueAt(Object o, int row, int col) {
			CELLS[row][col] = o.toString();
		}		
		public void setHighlightedCell(int row, int column){
			highlightedRow = row;
			highlightedColumn = column;
		}
		public void unsetHighlightedCell(){
			highlightedRow = -1;
			highlightedColumn = -1;
		}
		public boolean isHighlighted(int row, int column){
			return (highlightedRow == row && highlightedColumn == column);
		}
	}
	
	private class MatrixTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			JLabel label = (JLabel) comp;
			label.setHorizontalTextPosition(JLabel.CENTER);
			if (value == null) value = "";
			label.setText(value.toString());
			label.setBackground(Color.WHITE);
			label.setForeground(Color.BLACK);
			MatrixTableModel model = (MatrixTableModel)table.getModel();
			if (model.isHighlighted(row, column)) {
				label.setBackground(childrenColor);
				label.setForeground(Color.BLACK);					
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}

	public class SelectionListener implements ListSelectionListener {
		JTable table;
		SelectionListener(JTable table) {
			this.table = table;
		}
		public void valueChanged(ListSelectionEvent e) {
			// If cell selection is enabled, both row and column change events are fired
			if (e.getSource() == table.getSelectionModel()
					&& table.getRowSelectionAllowed()) {
			} else if (e.getSource() == table.getColumnModel().getSelectionModel()
					&& table.getColumnSelectionAllowed() ){
				int[] selection = table.getSelectedColumns();
				if (selection[0]==0){
					table.setColumnSelectionInterval(1, 1);
				}else{
					setSelectedSite(selection[0]);
				}
			}

			if (e.getValueIsAdjusting()) {
				// The mouse button has not yet been released
			}
		}
	}

	public class Edge {
		private double weight;
		
		public Edge(double weight){
			this.weight = weight;
		}
		
		public double getWeight(){
			return weight;
		}
		
		public String toString(){
			return Tools.doubletoString(weight,2);
		}
	}
	
	public MLTutorial() {
		super();
		init();
		
		final JPanel northPanel = new JPanel();
		final GridBagLayout gridBagLayout_8 = new GridBagLayout();
		gridBagLayout_8.columnWidths = new int[] {0,0,0,0};
		northPanel.setLayout(gridBagLayout_8);
		getContentPane().add(northPanel, BorderLayout.NORTH);

		final JPanel panel_6 = new JPanel();
		panel_6.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_52 = new GridBagConstraints();
		gridBagConstraints_52.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints_52.anchor = GridBagConstraints.NORTH;
		gridBagConstraints_52.insets = new Insets(5, 5, 5, 5);
		northPanel.add(panel_6, gridBagConstraints_52);

		final JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout());
		final GridBagConstraints gridBagConstraints_20 = new GridBagConstraints();
		gridBagConstraints_20.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_20.gridy = 1;
		gridBagConstraints_20.gridx = 0;
		panel_6.add(panel_5, gridBagConstraints_20);
		panel_5.setBorder(new TitledBorder(null, "Sequences", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

		selectedSiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectedSiteLabel.setToolTipText("Active site displayed in calculations");
		selectedSiteLabel.setText("Selected site = 1");
		panel_5.add(selectedSiteLabel, BorderLayout.SOUTH);

		sequencesTable = new JTable();
		sequencesTable.setModel(new SequencesTableModel());
		sequencesTable.setDefaultRenderer(String.class, new SequenceTableCellRenderer());
		sequencesTable.setShowVerticalLines(false);
		sequencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sequencesTable.setColumnSelectionAllowed(true);
		sequencesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		sequencesTable.setRowSelectionAllowed(false);
		sequencesTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		for (int i=1 ; i < sequencesTable.getModel().getColumnCount() ; i++){
			sequencesTable.getColumnModel().getColumn(i).setPreferredWidth(25);
		}
		SelectionListener listener = new SelectionListener(sequencesTable);
		sequencesTable.getSelectionModel().addListSelectionListener(listener);
		sequencesTable.getColumnModel().getSelectionModel()
        .addListSelectionListener(listener);
		sequencesTable.setColumnSelectionInterval(1, 1);
		panel_5.add(sequencesTable);

		final JPanel panel_4 = new JPanel();
		panel_4.setLayout(new GridLayout(2, 0));
		final GridBagConstraints gridBagConstraints_19 = new GridBagConstraints();
		gridBagConstraints_19.weightx = 1;
		gridBagConstraints_19.gridy = 2;
		gridBagConstraints_19.gridx = 0;
		panel_6.add(panel_4, gridBagConstraints_19);
		panel_4.setBorder(new TitledBorder(null, "Proportion of invariable sites", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

		final JPanel panel_30 = new JPanel();
		panel_30.setLayout(new BorderLayout());
		panel_4.add(panel_30);

		pinvCheckbox = new JCheckBox();
		pinvCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent arg0) {
				addGammaPinv();
			}
		});
		pinvCheckbox.setHorizontalAlignment(SwingConstants.LEFT);
		pinvCheckbox.setText("Add a proportion of invariant sites");
		panel_30.add(pinvCheckbox);

		pinvPanel = new JPanel();
		pinvPanel.setVisible(false);					
		panel_4.add(pinvPanel);

		pinvSpinner = new JSpinner(new SpinnerNumberModel(pi,0,100,1));
		pinvPanel.add(pinvSpinner);
		pinvSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent arg0) {
				pi = Double.parseDouble(pinvSpinner.getValue().toString()) / 100.0;
				piTextField.setText(Tools.doubletoString(pi, 4));
				mL_invar.setText(Tools.doubletoString(pi, 4));
				mL_Var.setText(Tools.doubletoString(1.0-pi, 4));
				parametersHaveChanged();
			}
		});
		pinvSpinner.setPreferredSize(new Dimension(60, 18));

		final JLabel label_6 = new JLabel();
		pinvPanel.add(label_6);
		label_6.setMinimumSize(new Dimension(40, 0));
		label_6.setPreferredSize(new Dimension(60, 20));
		label_6.setText("%");

		final JLabel proportionOfInvariableLabel = new JLabel();
		pinvPanel.add(proportionOfInvariableLabel);
		proportionOfInvariableLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Pi.png"));
		proportionOfInvariableLabel.setText("=");

		piTextField = new JTextField("0");
		pinvPanel.add(piTextField);
		piTextField.setPreferredSize(new Dimension(60, 20));
		piTextField.setMinimumSize(new Dimension(40, 0));
		piTextField.setEditable(false);

		final JLabel label_15 = new JLabel();
		label_15.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/ML_Dummies.png"));
		final GridBagConstraints gridBagConstraints_63 = new GridBagConstraints();
		gridBagConstraints_63.gridy = 0;
		gridBagConstraints_63.gridx = 0;
		panel_6.add(label_15, gridBagConstraints_63);

		final JPanel panel_7 = new JPanel();
		panel_7.setLayout(new GridBagLayout());
		panel_7.setBorder(new TitledBorder(null, "Nucleotide subtitution model", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_53 = new GridBagConstraints();
		gridBagConstraints_53.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints_53.insets = new Insets(5, 5, 5, 5);
		northPanel.add(panel_7, gridBagConstraints_53);

		final JPanel modelPanel = new JPanel();
		final GridBagConstraints gridBagConstraints_68 = new GridBagConstraints();
		gridBagConstraints_68.anchor = GridBagConstraints.NORTH;
		panel_7.add(modelPanel, gridBagConstraints_68);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] {0,0,0,0,0};
		gridBagLayout.columnWidths = new int[] {0};
		modelPanel.setLayout(gridBagLayout);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_6.gridy = 0;
		gridBagConstraints_6.gridx = 0;
		modelPanel.add(panel, gridBagConstraints_6);

		final JLabel modelLabel = new JLabel();
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.insets = new Insets(5, 5, 5, 0);
		panel.add(modelLabel, gridBagConstraints_7);
		modelLabel.setText("Model");

		final JComboBox comboBox = new JComboBox(EvaluationModel.values());
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent arg0) {
				textField_7.setText(EvaluationModel.valueOf(comboBox.getSelectedItem().toString()).verbose());
				model = EvaluationModel.valueOf(comboBox.getSelectedItem().toString());
				switch(model){
				case JC:
					rateAField.setText("1.0");
					rateBField.setText("1.0");
					rateCField.setText("1.0");
					rateDField.setText("1.0");
					rateEField.setText("1.0");
					kappaField.setText("1.0");
					rateAField.setEditable(false);
					rateBField.setEditable(false);
					rateCField.setEditable(false);
					rateDField.setEditable(false);
					rateEField.setEditable(false);
					kappaField.setEditable(false);
					ratioTiSpinner.setEnabled(false);
					ratioTvSpinner.setEnabled(false);  	
					QPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Q_JC.png"));
					PPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/P_JC.png"));		
					rateKappaPanel.setVisible(false);
					rateParamPanel.setVisible(false);
					break;
				case K2P:
					rateAField.setText("1.0");
					rateBField.setText(""+rateParam.get(RateParameter.K));
					rateBField.setCaretPosition(0);
					rateCField.setText("1.0");
					rateDField.setText("1.0");
					rateEField.setText(""+rateParam.get(RateParameter.K));
					rateEField.setCaretPosition(0);
					kappaField.setText(""+rateParam.get(RateParameter.K));
					kappaField.setCaretPosition(0);
					rateAField.setEditable(false);
					rateBField.setEditable(true);
					rateCField.setEditable(false);
					rateDField.setEditable(false);
					rateEField.setEditable(false);
					kappaField.setEditable(true);
					ratioTiSpinner.setEnabled(true);
					ratioTvSpinner.setEnabled(true);  		
					QPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Q_K2P.png"));
					PPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/P_K2P.png"));
					rateKappaPanel.setVisible(true);
					rateParamPanel.setVisible(false);
					break;
				case HKY85:
					rateAField.setText("1.0");
					rateBField.setText(""+rateParam.get(RateParameter.K));
					rateBField.setCaretPosition(0);
					rateCField.setText("1.0");
					rateDField.setText("1.0");
					rateEField.setText(""+rateParam.get(RateParameter.K));
					rateEField.setCaretPosition(0);
					kappaField.setText(""+rateParam.get(RateParameter.K));
					kappaField.setCaretPosition(0);
					rateAField.setEditable(false);
					rateBField.setEditable(true);
					rateCField.setEditable(false);
					rateDField.setEditable(false);
					rateEField.setEditable(false);
					kappaField.setEditable(true);
					ratioTiSpinner.setEnabled(true);
					ratioTvSpinner.setEnabled(true);  		
					QPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Q_HKY.png"));
					PPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/P_HKY.png"));
					rateKappaPanel.setVisible(true);
					rateParamPanel.setVisible(false);
					break;
				case GTR:
					rateAField.setText(""+rateParam.get(RateParameter.A));
					rateAField.setCaretPosition(0);
					rateBField.setText(""+rateParam.get(RateParameter.B));
					rateBField.setCaretPosition(0);
					rateCField.setText(""+rateParam.get(RateParameter.C));
					rateCField.setCaretPosition(0);
					rateDField.setText(""+rateParam.get(RateParameter.D));
					rateDField.setCaretPosition(0);
					rateEField.setText(""+rateParam.get(RateParameter.E));
					rateEField.setCaretPosition(0);
					rateAField.setEditable(true);
					rateBField.setEditable(true);
					rateCField.setEditable(true);
					rateDField.setEditable(true);
					rateEField.setEditable(true);
					kappaField.setEditable(false);
					ratioTiSpinner.setEnabled(false);
					ratioTvSpinner.setEnabled(false);  		
					QPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Q_GTR.png"));
					PPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/P_GTR.png"));
					rateKappaPanel.setVisible(false);
					rateParamPanel.setVisible(true);
					break;
				}
				adjustRatio(true);
				adjustFrequencies();
				parametersHaveChanged();
			}
		});
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.insets = new Insets(5, 5, 5, 0);
		panel.add(comboBox, gridBagConstraints_8);

		textField_7 = new JTextField(model.verbose());
		textField_7.setEditable(false);
		final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
		gridBagConstraints_9.weightx = 1;
		gridBagConstraints_9.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_9.insets = new Insets(5, 5, 5, 0);
		panel.add(textField_7, gridBagConstraints_9);

		titvRatioPanel = new JPanel();
		titvRatioPanel.setBorder(new TitledBorder(null, "Ratio transition:transversion", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		titvRatioPanel.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_5.gridy = 1;
		gridBagConstraints_5.gridx = 0;
		modelPanel.add(titvRatioPanel, gridBagConstraints_5);

		ratioTiSpinner = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 100.0, 0.1));
		ratioTiSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent arg0) {
				adjustRatio(false);
			}
		});
		ratioTiSpinner.setToolTipText("Transitions (A-G and C-T)");
		ratioTiSpinner.setPreferredSize(new Dimension(62, 20));
		ratioTiSpinner.setEnabled(false);
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_1.anchor = GridBagConstraints.WEST;
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 0;
		titvRatioPanel.add(ratioTiSpinner, gridBagConstraints_1);

		ratioTvSpinner = new JSpinner(new SpinnerNumberModel(2.0, 1.0, 100.0, 0.1));
		ratioTvSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				adjustRatio(false);
			}
		});
		ratioTvSpinner.setToolTipText("Transversions (A-C, A-T, C-G, and G-T)");
		ratioTvSpinner.setPreferredSize(new Dimension(62, 20));
		ratioTvSpinner.setEnabled(false);
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 2;
		titvRatioPanel.add(ratioTvSpinner, gridBagConstraints_2);

		titvLabel = new JLabel();
		titvLabel.setText(":");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridx = 1;
		titvRatioPanel.add(titvLabel, gridBagConstraints_3);

		rateKappaPanel = new JPanel();
		rateKappaPanel.setBorder(new TitledBorder(null, "Relative rate parameter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_67 = new GridBagConstraints();
		gridBagConstraints_67.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_67.gridy = 2;
		rateKappaPanel.setVisible(false);
		modelPanel.add(rateKappaPanel, gridBagConstraints_67);

		final JLabel agLabel = new JLabel();
		agLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/kappa.png"));
		agLabel.setText("= A-G = C-T =");
		rateKappaPanel.add(agLabel);

		kappaField = new JTextField();
		kappaField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateKappa();
			}
		});
		kappaField.setEditable(false);
		kappaField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateKappa();
			}
		});
		kappaField.setToolTipText("Relative rate parameter kappa (substitution A-G and C-T)");
		kappaField.setPreferredSize(new Dimension(60, 20));
		rateKappaPanel.add(kappaField);

		rateParamPanel = new JPanel();
		final GridLayout gridLayout = new GridLayout(2, 6);
		gridLayout.setHgap(3);
		gridLayout.setVgap(5);
		rateParamPanel.setLayout(gridLayout);
		rateParamPanel.setBorder(new TitledBorder(null, "Relative rate parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridx = 0;
		rateParamPanel.setVisible(false);
		modelPanel.add(rateParamPanel, gridBagConstraints);

		final JLabel aacLabel = new JLabel();
		aacLabel.setToolTipText("Relative rate parameter a (substitution A-C)");
		aacLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aacLabel.setText("A-C (a)");
		rateParamPanel.add(aacLabel);

		rateAField = new JTextField("1.0");
		rateAField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateA();
			}
		});
		rateAField.setEditable(false);
		rateAField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateA();
			}
		});
		rateAField.setToolTipText("Relative rate parameter a (substitution A-C)");
		rateAField.setPreferredSize(new Dimension(60, 20));
		rateAField.setMinimumSize(new Dimension(40, 0));
		rateParamPanel.add(rateAField);

		final JLabel agbLabel = new JLabel();
		agbLabel.setToolTipText("Relative rate parameter b (substitution A-G)");
		agbLabel.setHorizontalAlignment(SwingConstants.CENTER);
		agbLabel.setText("A-G (b)");
		rateParamPanel.add(agbLabel);

		rateBField = new JTextField("1.0");
		rateBField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateB();
			}
		});
		rateBField.setEditable(false);
		rateBField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateB();
			}
		});
		rateBField.setToolTipText("Relative rate parameter b (substitution A-G)");
		rateBField.setMinimumSize(new Dimension(40, 0));
		rateBField.setPreferredSize(new Dimension(60, 20));
		rateParamPanel.add(rateBField);

		final JLabel cLabel = new JLabel();
		cLabel.setToolTipText("Relative rate parameter c (substitution A-T)");
		cLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cLabel.setText("A-T (c)");
		rateParamPanel.add(cLabel);

		rateCField = new JTextField("1.0");
		rateCField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateC();
			}
		});
		rateCField.setEditable(false);
		rateCField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateC();
			}
		});
		rateCField.setToolTipText("Relative rate parameter c (substitution A-T)");
		rateCField.setPreferredSize(new Dimension(60, 20));
		rateCField.setMinimumSize(new Dimension(40, 0));
		rateParamPanel.add(rateCField);

		final JLabel dLabel = new JLabel();
		dLabel.setToolTipText("Relative rate parameter d (substitution C-G)");
		dLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dLabel.setText("C-G (d)");
		rateParamPanel.add(dLabel);

		rateDField = new JTextField("1.0");
		rateDField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateD();
			}
		});
		rateDField.setEditable(false);
		rateDField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateD();
			}
		});
		rateDField.setToolTipText("Relative rate parameter d (substitution C-G)");
		rateDField.setMinimumSize(new Dimension(40, 0));
		rateDField.setPreferredSize(new Dimension(60, 20));
		rateParamPanel.add(rateDField);

		final JLabel cteLabel = new JLabel();
		cteLabel.setToolTipText("Relative rate parameter e (substitution C-T)");
		cteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cteLabel.setText("C-T (e)");
		rateParamPanel.add(cteLabel);

		rateEField = new JTextField("1.0");
		rateEField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRateE();
			}
		});
		rateEField.setEditable(false);
		rateEField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setRateE();
			}
		});
		rateEField.setToolTipText("Relative rate parameter e (substitution C-T)");
		rateEField.setPreferredSize(new Dimension(60, 20));
		rateEField.setMinimumSize(new Dimension(40, 0));
		rateParamPanel.add(rateEField);

		final JLabel fLabel = new JLabel();
		fLabel.setToolTipText("Relative rate parameter f (substitution G-T)");
		fLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fLabel.setText("G-T (f)");
		rateParamPanel.add(fLabel);

		final JTextField rateFField = new JTextField("1.0");
		rateFField.setEditable(false);
		rateFField.setToolTipText("Relative rate parameter f (substitution G-T)");
		rateFField.setMinimumSize(new Dimension(40, 0));
		rateFField.setPreferredSize(new Dimension(60, 20));
		rateParamPanel.add(rateFField);

		final JPanel panel_2 = new JPanel();
		final GridBagLayout gridLayout_1 = new GridBagLayout();
		gridLayout_1.rowHeights = new int[] {0,0};
		panel_2.setLayout(gridLayout_1);
		panel_2.setBorder(new TitledBorder(null, "Equilibrium base frequencies", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_4.gridy = 4;
		gridBagConstraints_4.gridx = 0;
		modelPanel.add(panel_2, gridBagConstraints_4);

		final JLabel label = new JLabel();
		label.setText("=");
		label.setToolTipText("Equilibrium frequency of A");
		label.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/PI_A.png"));
		final GridBagConstraints gridBagConstraints_14 = new GridBagConstraints();
		gridBagConstraints_14.insets = new Insets(2, 2, 2, 2);
		panel_2.add(label, gridBagConstraints_14);

		freqAField = new JTextField();
		freqAField.setText("0.25");
		freqAField.setToolTipText("Equilibrium frequency of A");
		freqAField.setPreferredSize(new Dimension(60, 20));
		freqAField.setMinimumSize(new Dimension(40, 0));
		freqAField.setEditable(false);
		final GridBagConstraints gridBagConstraints_15 = new GridBagConstraints();
		gridBagConstraints_15.insets = new Insets(2, 2, 2, 10);
		panel_2.add(freqAField, gridBagConstraints_15);

		final JLabel label_1 = new JLabel();
		label_1.setText("=");
		label_1.setToolTipText("Equilibrium frequency of C");
		label_1.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/PI_C.png"));
		final GridBagConstraints gridBagConstraints_16 = new GridBagConstraints();
		gridBagConstraints_16.insets = new Insets(2, 2, 2, 2);
		panel_2.add(label_1, gridBagConstraints_16);

		freqCField = new JTextField();
		freqCField.setText("0.25");
		freqCField.setToolTipText("Equilibrium frequency of C");
		freqCField.setPreferredSize(new Dimension(60, 20));
		freqCField.setMinimumSize(new Dimension(40, 0));
		freqCField.setEditable(false);
		final GridBagConstraints gridBagConstraints_17 = new GridBagConstraints();
		gridBagConstraints_17.insets = new Insets(2, 2, 2, 2);
		panel_2.add(freqCField, gridBagConstraints_17);

		final JLabel label_2 = new JLabel();
		label_2.setText("=");
		label_2.setToolTipText("Equilibrium frequency of G");
		label_2.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/PI_G.png"));
		final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
		gridBagConstraints_10.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints_10.gridy = 1;
		gridBagConstraints_10.gridx = 0;
		panel_2.add(label_2, gridBagConstraints_10);

		freqGField = new JTextField();
		freqGField.setText("0.25");
		freqGField.setToolTipText("Equilibrium frequency of G");
		freqGField.setPreferredSize(new Dimension(60, 20));
		freqGField.setMinimumSize(new Dimension(40, 0));
		freqGField.setEditable(false);
		final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
		gridBagConstraints_11.insets = new Insets(2, 2, 2, 10);
		gridBagConstraints_11.gridy = 1;
		gridBagConstraints_11.gridx = 1;
		panel_2.add(freqGField, gridBagConstraints_11);

		final JLabel label_3 = new JLabel();
		label_3.setText("=");
		label_3.setToolTipText("Equilibrium frequency of T");
		label_3.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/PI_T.png"));
		final GridBagConstraints gridBagConstraints_12 = new GridBagConstraints();
		gridBagConstraints_12.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints_12.gridy = 1;
		gridBagConstraints_12.gridx = 2;
		panel_2.add(label_3, gridBagConstraints_12);

		freqTField = new JTextField();
		freqTField.setText("0.25");
		freqTField.setToolTipText("Equilibrium frequency of T");
		freqTField.setPreferredSize(new Dimension(60, 20));
		freqTField.setMinimumSize(new Dimension(40, 0));
		freqTField.setEditable(false);
		final GridBagConstraints gridBagConstraints_13 = new GridBagConstraints();
		gridBagConstraints_13.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints_13.gridy = 1;
		gridBagConstraints_13.gridx = 3;
		panel_2.add(freqTField, gridBagConstraints_13);

		final JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Mean instantaneous substitution rate", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_18 = new GridBagConstraints();
		gridBagConstraints_18.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_18.gridy = 5;
		gridBagConstraints_18.gridx = 0;
		modelPanel.add(panel_3, gridBagConstraints_18);

		final JLabel label_4 = new JLabel();
		label_4.setText("=");
		label_4.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/MU.png"));
		panel_3.add(label_4);

		final JLabel label_8 = new JLabel();
		label_8.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/mu_formula.png"));
		label_8.setText("=");
		panel_3.add(label_8);

		scalingField = new JTextField();
		scalingField.setEditable(false);
		scalingField.setPreferredSize(new Dimension(70, 20));
		panel_3.add(scalingField);

		final JPanel panel_8 = new JPanel();
		panel_8.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_72 = new GridBagConstraints();
		gridBagConstraints_72.anchor = GridBagConstraints.NORTH;
		panel_7.add(panel_8, gridBagConstraints_72);

		final JPanel panel_9 = new JPanel();
		final GridLayout gridLayout_7 = new GridLayout(1, 0);
		gridLayout_7.setHgap(4);
		panel_9.setLayout(gridLayout_7);
		panel_9.setBorder(new TitledBorder(null, "Instantaneous rate matrix Q", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_51 = new GridBagConstraints();
		gridBagConstraints_51.fill = GridBagConstraints.HORIZONTAL;
		panel_8.add(panel_9, gridBagConstraints_51);

		QPic = new JLabel();
		QPic.setText("=");
		QPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/Q_JC.png"));
		panel_9.add(QPic);

		matrixQ = new JTable();
		matrixQ.setShowHorizontalLines(false);
		matrixQ.setRowHeight(20);
		matrixQ.setCellSelectionEnabled(true);
		matrixQ.setModel(new MatrixTableModel());
		matrixQ.setDefaultRenderer(String.class, new MatrixTableCellRenderer());
		matrixQ.setShowVerticalLines(false);
		matrixQ.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matrixQ.setRowSelectionAllowed(false);
		matrixQ.setColumnSelectionAllowed(false);
		matrixQ.setRowHeight(25);
		matrixQ.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i=0 ; i < matrixQ.getModel().getColumnCount() ; i++){
			matrixQ.getColumnModel().getColumn(i).setPreferredWidth(40);
		}
		panel_9.add(matrixQ);

		final JPanel panel_10 = new JPanel();
		panel_10.setLayout(new BorderLayout());
		panel_10.setBorder(new TitledBorder(null, "Transition probability matrix P", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_50 = new GridBagConstraints();
		gridBagConstraints_50.gridy = 1;
		panel_8.add(panel_10, gridBagConstraints_50);

		final JPanel panel_24 = new JPanel();
		final GridLayout gridLayout_6 = new GridLayout(0, 2);
		gridLayout_6.setVgap(5);
		gridLayout_6.setHgap(5);
		panel_24.setLayout(gridLayout_6);
		panel_10.add(panel_24);

		final JPanel panel_22 = new JPanel();
		panel_24.add(panel_22);
		panel_22.setLayout(new BorderLayout());

		matrixP_AB = new JTable();
		matrixP_AB.setShowHorizontalLines(false);
		matrixP_AB.setRowHeight(20);
		matrixP_AB.setCellSelectionEnabled(true);
		matrixP_AB.setModel(new MatrixTableModel());
		matrixP_AB.setDefaultRenderer(String.class, new MatrixTableCellRenderer());
		matrixP_AB.setShowVerticalLines(false);
		matrixP_AB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matrixP_AB.setRowSelectionAllowed(false);
		matrixP_AB.setColumnSelectionAllowed(false);
		matrixP_AB.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i=0 ; i < matrixP_AB.getModel().getColumnCount() ; i++){
			matrixP_AB.getColumnModel().getColumn(i).setPreferredWidth(40);
		}
		panel_22.add(matrixP_AB, BorderLayout.CENTER);

		vABMatrixLabel = new JLabel();
		panel_22.add(vABMatrixLabel, BorderLayout.NORTH);
		vABMatrixLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/vAB.png"));
		vABMatrixLabel.setText("Branch between node 7 and node 1");

		final JPanel panel_23 = new JPanel();
		panel_24.add(panel_23);
		panel_23.setLayout(new BorderLayout());

		matrixP_AC = new JTable();
		matrixP_AC.setShowHorizontalLines(false);
		matrixP_AC.setRowHeight(20);
		matrixP_AC.setCellSelectionEnabled(true);
		matrixP_AC.setModel(new MatrixTableModel());
		matrixP_AC.setDefaultRenderer(String.class, new MatrixTableCellRenderer());
		matrixP_AC.setShowVerticalLines(false);
		matrixP_AC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matrixP_AC.setRowSelectionAllowed(false);
		matrixP_AC.setColumnSelectionAllowed(false);
		matrixP_AC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i=0 ; i < matrixP_AC.getModel().getColumnCount() ; i++){
			matrixP_AC.getColumnModel().getColumn(i).setPreferredWidth(40);
		}
		panel_23.add(matrixP_AC);

		vACMatrixLabel = new JLabel();
		panel_23.add(vACMatrixLabel, BorderLayout.NORTH);
		vACMatrixLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/vAC.png"));
		vACMatrixLabel.setText("Branch between node 7 and node 2");

		PPic = new JLabel();
		PPic.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/P_JC.png"));
		panel_10.add(PPic, BorderLayout.NORTH);
		PPic.setText("=");

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		panel_1.setBorder(new TitledBorder(null, "Rate heterogeneity", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_74 = new GridBagConstraints();
		gridBagConstraints_74.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_74.weightx = 1.0;
		gridBagConstraints_74.fill = GridBagConstraints.BOTH;
		gridBagConstraints_74.gridy = 0;
		gridBagConstraints_74.gridx = 2;
		northPanel.add(panel_1, gridBagConstraints_74);

		final JPanel panel_11 = new JPanel();
		panel_1.add(panel_11);
		final GridBagLayout gridBagLayout_7 = new GridBagLayout();
		gridBagLayout_7.rowHeights = new int[] {0,0,0,0};
		panel_11.setLayout(gridBagLayout_7);

		gammaMainPanel = new JPanel();
		final GridBagLayout gridBagLayout_1 = new GridBagLayout();
		gridBagLayout_1.rowHeights = new int[] {0,0};
		gammaMainPanel.setLayout(gridBagLayout_1);
		final GridBagConstraints gridBagConstraints_21 = new GridBagConstraints();
		gridBagConstraints_21.anchor = GridBagConstraints.WEST;
		gridBagConstraints_21.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints_21.gridy = 0;
		gridBagConstraints_21.gridx = 0;
		panel_11.add(gammaMainPanel, gridBagConstraints_21);

		final JLabel numberOfCategoriesLabel = new JLabel();
		numberOfCategoriesLabel.setToolTipText("Number of categories");
		numberOfCategoriesLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/k.png"));
		numberOfCategoriesLabel.setText("number of categories");
		final GridBagConstraints gridBagConstraints_28 = new GridBagConstraints();
		gridBagConstraints_28.insets = new Insets(5, 0, 0, 5);
		gammaMainPanel.add(numberOfCategoriesLabel, gridBagConstraints_28);

		gammaShapeTextField = new JTextField();
		gammaShapeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setGammaShape();
			}
		});
		gammaShapeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setGammaShape();
			}
		});
		gammaShapeTextField.setText(alpha+"");
		gammaShapeTextField.setToolTipText("Shape parameter of the gamma distribution");
		gammaShapeTextField.setMinimumSize(new Dimension(40, 0));
		gammaShapeTextField.setPreferredSize(new Dimension(80, 20));
		final GridBagConstraints gridBagConstraints_26 = new GridBagConstraints();
		gridBagConstraints_26.gridy = 1;
		gridBagConstraints_26.gridx = 1;
		gammaMainPanel.add(gammaShapeTextField, gridBagConstraints_26);

		final JComboBox categoryBox = new JComboBox(new String[]{"Category 1","Category 2"});
		categoryBox.setSelectedIndex(c);
		categoryBox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent arg0) {
				if (!isAdjustingModel){
					setSelectedCategory(categories.get(categoryBox.getSelectedItem()));
					graphPanel.repaint();
				}
			}
		});
		categoryBox.setPreferredSize(new Dimension(160, 50));
		categoryBox.setMinimumSize(new Dimension(100, 0));
		categoryBox.setBorder(new TitledBorder(null, "Selection", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		categoryBox.setToolTipText("Active category displayed in calculations");
		final GridBagConstraints gridBagConstraints_27 = new GridBagConstraints();
		gridBagConstraints_27.gridy = 0;
		gridBagConstraints_27.gridx = 2;
		gridBagConstraints_27.fill = GridBagConstraints.BOTH;
		gridBagConstraints_27.insets = new Insets(5, 10, 5, 5);
		gridBagConstraints_27.gridheight = 2;
		gammaMainPanel.add(categoryBox, gridBagConstraints_27);

		kSpinner = new JSpinner(new SpinnerNumberModel(k,1,6,1));
		kSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent arg0) {
				isAdjustingModel = true;
				k = Integer.parseInt(kSpinner.getValue().toString());
				categoryBox.removeAllItems();
				Iterator<String> cats = categories.keySet().iterator();
				for (int i=0 ; i < k ; i++){
					categoryBox.addItem(cats.next());
				}				
				setSelectedCategory(0);
				if (k==1){
					distribution = EvaluationDistribution.NONE;
					gammaShapeTextField.setEnabled(false);
				}else{
					distribution = EvaluationDistribution.GAMMA;
					gammaShapeTextField.setEnabled(true);					
				}
				lambaV_A_K.setText(""+k);	
				lambaV_C_K.setText(""+k);	
				lambaV_G_K.setText(""+k);	
				lambaV_T_K.setText(""+k);	
				lambaV_A_K_1.setText(""+k);	
				lambaV_C_K_1.setText(""+k);	
				lambaV_G_K_1.setText(""+k);	
				lambaV_T_K_1.setText(""+k);	
				parametersHaveChanged();
				isAdjustingModel = false;
			}
		});
		kSpinner.setToolTipText("Number of categories");
		kSpinner.setPreferredSize(new Dimension(80, 20));
		final GridBagConstraints gridBagConstraints_29 = new GridBagConstraints();
		gridBagConstraints_29.gridy = 0;
		gridBagConstraints_29.gridx = 1;
		gridBagConstraints_29.insets = new Insets(5, 0, 0, 0);
		gammaMainPanel.add(kSpinner, gridBagConstraints_29);

		final JLabel shapeLabel = new JLabel();
		shapeLabel.setToolTipText("Shape parameter of the gamma distribution");
		shapeLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/alpha.png"));
		shapeLabel.setText("shape");
		final GridBagConstraints gridBagConstraints_25 = new GridBagConstraints();
		gridBagConstraints_25.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_25.gridy = 1;
		gridBagConstraints_25.gridx = 0;
		gammaMainPanel.add(shapeLabel, gridBagConstraints_25);

		gammaCutPanel = new JPanel();
		gammaCutPanel.setLayout(new BorderLayout());
		final GridBagConstraints gridBagConstraints_22 = new GridBagConstraints();
		gridBagConstraints_22.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_22.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints_22.gridy = 1;
		gridBagConstraints_22.gridx = 0;
		panel_11.add(gammaCutPanel, gridBagConstraints_22);

		cutpointsLabel = new JLabel();
		cutpointsLabel.setToolTipText("Percentage points (cutting points) of the gamma distribution");
		cutpointsLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/z_gamma.png"));
		cutpointsLabel.setText("=");
		gammaCutPanel.add(cutpointsLabel);

		gammaRatesPanel = new JPanel();
		gammaRatesPanel.setLayout(new BorderLayout());
		final GridBagConstraints gridBagConstraints_23 = new GridBagConstraints();
		gridBagConstraints_23.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_23.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints_23.gridx = 0;
		gridBagConstraints_23.gridy = 2;
		panel_11.add(gammaRatesPanel, gridBagConstraints_23);

		gammaRateslabel = new JLabel();
		gammaRateslabel.setToolTipText("Rate of each category of the gamma distribution");
		gammaRateslabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/r_c.png"));
		gammaRateslabel.setText("=");
		gammaRatesPanel.add(gammaRateslabel);

		gammaGraphPanel = new JPanel();
		gammaGraphPanel.setBorder(new TitledBorder(null, "Gamma distribution", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		gammaGraphPanel.setPreferredSize(new Dimension(0, 200));
		gammaGraphPanel.setLayout(new BorderLayout());
		final GridBagConstraints gridBagConstraints_24 = new GridBagConstraints();
		gridBagConstraints_24.insets = new Insets(0, 2, 2, 2);
		gridBagConstraints_24.weighty = 1;
		gridBagConstraints_24.weightx = 1;
		gridBagConstraints_24.fill = GridBagConstraints.BOTH;
		gridBagConstraints_24.gridx = 0;
		gridBagConstraints_24.gridy = 3;
		panel_11.add(gammaGraphPanel, gridBagConstraints_24);

		gammaMainPanel.setVisible(false);
		gammaCutPanel.setVisible(false);
		gammaRatesPanel.setVisible(false);
		gammaGraphPanel.setVisible(false);

		graphPanel = new GraphPanel(categoryBox);
		getContentPane().add(graphPanel);		
		gammaGraphPanel.add(graphPanel, BorderLayout.CENTER);

		final JPanel panel_31 = new JPanel();
		panel_1.add(panel_31, BorderLayout.NORTH);
		panel_31.setLayout(new BorderLayout());

		gammaCheckBox = new JCheckBox();
		gammaCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				addGammaPinv();
			}
		});
		gammaCheckBox.setText("Add rate heterogeneity");
		panel_31.add(gammaCheckBox, BorderLayout.NORTH);

		final JPanel southPanel = new JPanel();
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		final JPanel westPanel = new JPanel();
		final GridBagLayout gridBagLayout_2 = new GridBagLayout();
		gridBagLayout_2.rowHeights = new int[] {0,0,0,0};
		westPanel.setLayout(gridBagLayout_2);
		getContentPane().add(westPanel, BorderLayout.WEST);

		final JPanel panel_15 = new JPanel();
		final GridLayout gridLayout_2 = new GridLayout(0, 4);
		gridLayout_2.setVgap(2);
		panel_15.setLayout(gridLayout_2);
		panel_15.setBorder(new TitledBorder(null, "Selection", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_30 = new GridBagConstraints();
		gridBagConstraints_30.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_30.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_30.gridx = 0;
		westPanel.add(panel_15, gridBagConstraints_30);

		nodeALabel = new JLabel();
		nodeALabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/mA.png"));
		nodeALabel.setText("= Node 7");
		panel_15.add(nodeALabel);

		nodeBLabel = new JLabel();
		nodeBLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/mB.png"));
		nodeBLabel.setText("= Node 1");
		panel_15.add(nodeBLabel);

		final JPanel panel_20 = new JPanel();
		panel_15.add(panel_20);

		final JLabel vABLabel = new JLabel();
		panel_20.add(vABLabel);
		vABLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/vAB.png"));
		vABLabel.setText("=");

		try {
			vABField = new JTextField(Tools.doubletoString(B.getAncestorBranchLength(), 4));
		} catch (NullAncestorException e2) {
			e2.printStackTrace();
		}
		vABField.setPreferredSize(new Dimension(60, 20));
		vABField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setBranchLength(B, vABField);
			}
		});
		vABField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setBranchLength(B, vABField);
			}
		});
		panel_20.add(vABField);

		siteLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/S.png"));
		siteLabel.setText("= Site 1");
		panel_15.add(siteLabel);

		xAsLabel = new JLabel();
		xAsLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/xAs.png"));
		xAsLabel.setText("= A");
		panel_15.add(xAsLabel);

		nodeCLabel = new JLabel();
		nodeCLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/mC.png"));
		nodeCLabel.setText("= Node 2");
		panel_15.add(nodeCLabel);

		final JPanel panel_21 = new JPanel();
		panel_15.add(panel_21);

		final JLabel vACLabel = new JLabel();
		panel_21.add(vACLabel);
		vACLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/vAC.png"));
		vACLabel.setText("=");

		try {
			vACField = new JTextField(Tools.doubletoString(B.getAncestorBranchLength(), 4));
		} catch (NullAncestorException e1) {
			e1.printStackTrace();
		}
		vACField.setPreferredSize(new Dimension(60, 20));
		vACField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setBranchLength(C, vACField);
			}
		});
		vACField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				setBranchLength(C, vACField);
			}
		});
		panel_21.add(vACField);

		categoryLabel = new JLabel();
		categoryLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/c.png"));
		categoryLabel.setText("= Category 1");
		panel_15.add(categoryLabel);

		final JPanel panel_16 = new JPanel();
		panel_16.setLayout(new BorderLayout());
		panel_16.setBorder(new TitledBorder(null, "Conditional likelihood at node A", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_31 = new GridBagConstraints();
		gridBagConstraints_31.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_31.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_31.gridy = 1;
		gridBagConstraints_31.gridx = 0;
		westPanel.add(panel_16, gridBagConstraints_31);

		final JPanel panel_17 = new JPanel();
		final GridBagLayout gridBagLayout_3 = new GridBagLayout();
		gridBagLayout_3.rowHeights = new int[] {0,0};
		panel_17.setLayout(gridBagLayout_3);
		panel_16.add(panel_17);

		label_7 = new JLabel();
		label_7.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_nogamma.png"));
		label_7.setText("=");
		final GridBagConstraints gridBagConstraints_33 = new GridBagConstraints();
		gridBagConstraints_33.insets = new Insets(4, 0, 4, 0);
		panel_17.add(label_7, gridBagConstraints_33);

		label_9 = new JLabel();
		label_9.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_formula_nogamma_nopinv.png"));
		final GridBagConstraints gridBagConstraints_32 = new GridBagConstraints();
		gridBagConstraints_32.gridy = 1;
		gridBagConstraints_32.gridx = 0;
		panel_17.add(label_9, gridBagConstraints_32);

		final JPanel panel_19 = new JPanel();
		panel_19.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_34 = new GridBagConstraints();
		gridBagConstraints_34.weightx = 1;
		gridBagConstraints_34.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_34.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints_34.gridx = 0;
		gridBagConstraints_34.gridy = 2;
		panel_17.add(panel_19, gridBagConstraints_34);

		final JLabel label_13 = new JLabel();
		label_13.setHorizontalAlignment(SwingConstants.CENTER);
		label_13.setText("=");
		final GridBagConstraints gridBagConstraints_47 = new GridBagConstraints();
		gridBagConstraints_47.insets = new Insets(0, 5, 0, 2);
		panel_19.add(label_13, gridBagConstraints_47);

		condLikNodeA1 = new JTextField();
		condLikNodeA1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(1);
			}
		});
		condLikNodeA1.setPreferredSize(new Dimension(60, 20));
		condLikNodeA1.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA1.setEditable(false);
		final GridBagConstraints gridBagConstraints_43 = new GridBagConstraints();
		gridBagConstraints_43.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints_43.weightx = 1;
		gridBagConstraints_43.fill = GridBagConstraints.HORIZONTAL;
		panel_19.add(condLikNodeA1, gridBagConstraints_43);

		final JLabel label_13_1_6 = new JLabel();
		label_13_1_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1_6.setText("+");
		panel_19.add(label_13_1_6, new GridBagConstraints());

		condLikNodeA2 = new JTextField();
		condLikNodeA2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(2);
			}
		});
		condLikNodeA2.setPreferredSize(new Dimension(60, 20));
		condLikNodeA2.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA2.setEditable(false);
		final GridBagConstraints gridBagConstraints_44 = new GridBagConstraints();
		gridBagConstraints_44.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints_44.weightx = 1;
		gridBagConstraints_44.fill = GridBagConstraints.HORIZONTAL;
		panel_19.add(condLikNodeA2, gridBagConstraints_44);

		final JLabel label_13_1_5 = new JLabel();
		label_13_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1_5.setText("+");
		panel_19.add(label_13_1_5, new GridBagConstraints());

		condLikNodeA3 = new JTextField();
		condLikNodeA3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(3);
			}
		});
		condLikNodeA3.setPreferredSize(new Dimension(60, 20));
		condLikNodeA3.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA3.setEditable(false);
		final GridBagConstraints gridBagConstraints_46 = new GridBagConstraints();
		gridBagConstraints_46.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints_46.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_46.weightx = 1;
		panel_19.add(condLikNodeA3, gridBagConstraints_46);

		final JLabel label_13_1_4 = new JLabel();
		label_13_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1_4.setText("+");
		panel_19.add(label_13_1_4, new GridBagConstraints());

		condLikNodeA4 = new JTextField();
		condLikNodeA4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(4);
			}
		});
		condLikNodeA4.setPreferredSize(new Dimension(60, 20));
		condLikNodeA4.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA4.setEditable(false);
		final GridBagConstraints gridBagConstraints_45 = new GridBagConstraints();
		gridBagConstraints_45.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints_45.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_45.weightx = 1;
		panel_19.add(condLikNodeA4, gridBagConstraints_45);

		final JLabel xLabel = new JLabel();
		xLabel.setHorizontalAlignment(SwingConstants.CENTER);
		xLabel.setText("x");
		final GridBagConstraints gridBagConstraints_35 = new GridBagConstraints();
		gridBagConstraints_35.insets = new Insets(3, 5, 0, 2);
		gridBagConstraints_35.gridy = 1;
		panel_19.add(xLabel, gridBagConstraints_35);

		condLikNodeA5 = new JTextField();
		condLikNodeA5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(5);
			}
		});
		condLikNodeA5.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA5.setEditable(false);
		condLikNodeA5.setPreferredSize(new Dimension(60, 20));
		final GridBagConstraints gridBagConstraints_36 = new GridBagConstraints();
		gridBagConstraints_36.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_36.weightx = 1;
		gridBagConstraints_36.insets = new Insets(3, 2, 0, 2);
		gridBagConstraints_36.gridy = 1;
		panel_19.add(condLikNodeA5, gridBagConstraints_36);

		final JLabel label_13_1 = new JLabel();
		final GridBagConstraints gridBagConstraints_37 = new GridBagConstraints();
		gridBagConstraints_37.gridy = 1;
		panel_19.add(label_13_1, gridBagConstraints_37);
		label_13_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1.setText("+");

		condLikNodeA6 = new JTextField();
		condLikNodeA6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(6);
			}
		});
		condLikNodeA6.setPreferredSize(new Dimension(60, 20));
		condLikNodeA6.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA6.setEditable(false);
		final GridBagConstraints gridBagConstraints_38 = new GridBagConstraints();
		gridBagConstraints_38.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_38.weightx = 1;
		gridBagConstraints_38.insets = new Insets(3, 2, 0, 2);
		gridBagConstraints_38.gridy = 1;
		panel_19.add(condLikNodeA6, gridBagConstraints_38);

		final JLabel label_13_1_1 = new JLabel();
		label_13_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1_1.setText("+");
		final GridBagConstraints gridBagConstraints_39 = new GridBagConstraints();
		gridBagConstraints_39.gridy = 1;
		panel_19.add(label_13_1_1, gridBagConstraints_39);

		condLikNodeA7 = new JTextField();
		condLikNodeA7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(7);
			}
		});
		condLikNodeA7.setPreferredSize(new Dimension(60, 20));
		condLikNodeA7.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA7.setEditable(false);
		final GridBagConstraints gridBagConstraints_40 = new GridBagConstraints();
		gridBagConstraints_40.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_40.weightx = 1;
		gridBagConstraints_40.insets = new Insets(3, 2, 0, 2);
		gridBagConstraints_40.gridy = 1;
		panel_19.add(condLikNodeA7, gridBagConstraints_40);

		final JLabel label_13_1_2 = new JLabel();
		label_13_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_13_1_2.setText("+");
		final GridBagConstraints gridBagConstraints_41 = new GridBagConstraints();
		gridBagConstraints_41.gridy = 1;
		panel_19.add(label_13_1_2, gridBagConstraints_41);

		condLikNodeA8 = new JTextField();
		condLikNodeA8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				showConditionalLikelihoodDetail(8);
			}
		});
		condLikNodeA8.setPreferredSize(new Dimension(60, 20));
		condLikNodeA8.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeA8.setEditable(false);
		final GridBagConstraints gridBagConstraints_42 = new GridBagConstraints();
		gridBagConstraints_42.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_42.weightx = 1;
		gridBagConstraints_42.insets = new Insets(3, 2, 0, 2);
		gridBagConstraints_42.gridy = 1;
		panel_19.add(condLikNodeA8, gridBagConstraints_42);

		final JPanel panel_18 = new JPanel();
		final GridLayout gridLayout_3 = new GridLayout(2, 0);
		gridLayout_3.setHgap(2);
		panel_18.setLayout(gridLayout_3);
		panel_16.add(panel_18, BorderLayout.NORTH);

		final JLabel label_5 = new JLabel();
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setText("A");
		panel_18.add(label_5);

		final JLabel cLabel_1 = new JLabel();
		cLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		cLabel_1.setText("C");
		panel_18.add(cLabel_1);

		final JLabel gLabel = new JLabel();
		gLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gLabel.setText("G");
		panel_18.add(gLabel);

		final JLabel tLabel = new JLabel();
		tLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tLabel.setText("T");
		panel_18.add(tLabel);

		condLikNodeAA = new JTextField();
		condLikNodeAA.setBorder(new LineBorder(selectionColor, 4, false));
		condLikNodeAA.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent arg0) {
				setSelectedBase(Dataset.A);
			}
		});
		condLikNodeAA.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeAA.setEditable(false);
		condLikNodeAA.setPreferredSize(new Dimension(60, 20));
		panel_18.add(condLikNodeAA);

		condLikNodeAC = new JTextField();
		condLikNodeAC.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAC.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				setSelectedBase(Dataset.C);
			}
		});
		condLikNodeAC.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeAC.setEditable(false);
		condLikNodeAC.setPreferredSize(new Dimension(60, 20));
		panel_18.add(condLikNodeAC);

		condLikNodeAG = new JTextField();
		condLikNodeAG.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAG.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				setSelectedBase(Dataset.G);
			}
		});
		condLikNodeAG.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeAG.setEditable(false);
		condLikNodeAG.setPreferredSize(new Dimension(60, 20));
		panel_18.add(condLikNodeAG);

		condLikNodeAT = new JTextField();
		condLikNodeAT.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAT.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				setSelectedBase(Dataset.T);
			}
		});
		condLikNodeAT.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeAT.setEditable(false);
		condLikNodeAT.setPreferredSize(new Dimension(60, 20));
		panel_18.add(condLikNodeAT);

		final JPanel panel_18_1 = new JPanel();
		panel_18_1.setBorder(new TitledBorder(null, "Conditional likelihood at node B", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridLayout gridLayout_4 = new GridLayout(2, 0);
		gridLayout_4.setHgap(2);
		panel_18_1.setLayout(gridLayout_4);
		final GridBagConstraints gridBagConstraints_49 = new GridBagConstraints();
		gridBagConstraints_49.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_49.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_49.gridy = 2;
		gridBagConstraints_49.gridx = 0;
		westPanel.add(panel_18_1, gridBagConstraints_49);

		final JLabel label_5_1 = new JLabel();
		label_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_5_1.setText("A");
		panel_18_1.add(label_5_1);

		final JLabel cLabel_1_1 = new JLabel();
		cLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		cLabel_1_1.setText("C");
		panel_18_1.add(cLabel_1_1);

		final JLabel gLabel_1 = new JLabel();
		gLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		gLabel_1.setText("G");
		panel_18_1.add(gLabel_1);

		final JLabel tLabel_1 = new JLabel();
		tLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		tLabel_1.setText("T");
		panel_18_1.add(tLabel_1);

		condLikNodeBA = new JTextField();
		condLikNodeBA.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeBA.setPreferredSize(new Dimension(60, 20));
		condLikNodeBA.setEditable(false);
		panel_18_1.add(condLikNodeBA);

		condLikNodeBC = new JTextField();
		condLikNodeBC.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeBC.setPreferredSize(new Dimension(60, 20));
		condLikNodeBC.setEditable(false);
		panel_18_1.add(condLikNodeBC);

		condLikNodeBG = new JTextField();
		condLikNodeBG.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeBG.setPreferredSize(new Dimension(60, 20));
		condLikNodeBG.setEditable(false);
		panel_18_1.add(condLikNodeBG);

		condLikNodeBT = new JTextField();
		condLikNodeBT.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeBT.setPreferredSize(new Dimension(60, 20));
		condLikNodeBT.setEditable(false);
		panel_18_1.add(condLikNodeBT);

		final JPanel panel_18_1_1 = new JPanel();
		panel_18_1_1.setBorder(new TitledBorder(null, "Conditional likelihood at node C", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridLayout gridLayout_5 = new GridLayout(2, 0);
		gridLayout_5.setHgap(2);
		panel_18_1_1.setLayout(gridLayout_5);
		final GridBagConstraints gridBagConstraints_48 = new GridBagConstraints();
		gridBagConstraints_48.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_48.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_48.gridy = 3;
		gridBagConstraints_48.gridx = 0;
		westPanel.add(panel_18_1_1, gridBagConstraints_48);

		final JLabel label_5_1_1 = new JLabel();
		label_5_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_5_1_1.setText("A");
		panel_18_1_1.add(label_5_1_1);

		final JLabel cLabel_1_1_1 = new JLabel();
		cLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		cLabel_1_1_1.setText("C");
		panel_18_1_1.add(cLabel_1_1_1);

		final JLabel gLabel_1_1 = new JLabel();
		gLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		gLabel_1_1.setText("G");
		panel_18_1_1.add(gLabel_1_1);

		final JLabel tLabel_1_1 = new JLabel();
		tLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		tLabel_1_1.setText("T");
		panel_18_1_1.add(tLabel_1_1);

		condLikNodeCA = new JTextField();
		condLikNodeCA.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeCA.setPreferredSize(new Dimension(60, 20));
		condLikNodeCA.setEditable(false);
		panel_18_1_1.add(condLikNodeCA);

		condLikNodeCC = new JTextField();
		condLikNodeCC.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeCC.setPreferredSize(new Dimension(60, 20));
		condLikNodeCC.setEditable(false);
		panel_18_1_1.add(condLikNodeCC);

		condLikNodeCG = new JTextField();
		condLikNodeCG.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeCG.setPreferredSize(new Dimension(60, 20));
		condLikNodeCG.setEditable(false);
		panel_18_1_1.add(condLikNodeCG);

		condLikNodeCT = new JTextField();
		condLikNodeCT.setHorizontalAlignment(SwingConstants.CENTER);
		condLikNodeCT.setPreferredSize(new Dimension(60, 20));
		condLikNodeCT.setEditable(false);
		panel_18_1_1.add(condLikNodeCT);

		final JPanel eastPanel = new JPanel();
		final GridBagLayout gridBagLayout_5 = new GridBagLayout();
		gridBagLayout_5.rowHeights = new int[] {0,0,0};
		eastPanel.setLayout(gridBagLayout_5);
		getContentPane().add(eastPanel, BorderLayout.EAST);

		final JPanel panel_25 = new JPanel();
		final GridBagLayout gridBagLayout_4 = new GridBagLayout();
		gridBagLayout_4.rowHeights = new int[] {0,0,0};
		panel_25.setLayout(gridBagLayout_4);
		panel_25.setBorder(new TitledBorder(null, "Likelihood of the tree", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_58 = new GridBagConstraints();
		gridBagConstraints_58.fill = GridBagConstraints.HORIZONTAL;
		eastPanel.add(panel_25, gridBagConstraints_58);

		final JLabel label_10 = new JLabel();
		label_10.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/ML_tree.png"));
		label_10.setText("=");
		panel_25.add(label_10, new GridBagConstraints());

		likelihoodLabel = new JLabel();
		likelihoodLabel.setFont(new Font("", Font.BOLD, 16));
		likelihoodLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/ML_tree_formula_nopinv.png"));
		likelihoodLabel.setText("=");
		final GridBagConstraints gridBagConstraints_55 = new GridBagConstraints();
		gridBagConstraints_55.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints_55.anchor = GridBagConstraints.WEST;
		gridBagConstraints_55.gridy = 0;
		gridBagConstraints_55.gridx = 1;
		panel_25.add(likelihoodLabel, gridBagConstraints_55);

		final JPanel panel_29 = new JPanel();
		final GridBagConstraints gridBagConstraints_66 = new GridBagConstraints();
		gridBagConstraints_66.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_66.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints_66.gridwidth = 2;
		gridBagConstraints_66.gridy = 2;
		gridBagConstraints_66.gridx = 0;
		panel_25.add(panel_29, gridBagConstraints_66);

		final JLabel selectedSiteLabel_1 = new JLabel();
		panel_29.add(selectedSiteLabel_1);
		selectedSiteLabel_1.setText("Selected site =");

		ML_site = new JTextField();
		ML_site.setPreferredSize(new Dimension(70, 20));
		ML_site.setHorizontalAlignment(SwingConstants.CENTER);
		ML_site.setEditable(false);
		panel_29.add(ML_site);

		final JLabel lnLabel = new JLabel();
		lnLabel.setText("= ln (");
		panel_29.add(lnLabel);

		mL_Var = new JTextField();
		mL_Var.setText("1.0");
		mL_Var.setPreferredSize(new Dimension(60, 20));
		mL_Var.setHorizontalAlignment(SwingConstants.CENTER);
		mL_Var.setEditable(false);
		panel_29.add(mL_Var);

		mL_lambda_Var = new JTextField();
		mL_lambda_Var.setPreferredSize(new Dimension(60, 20));
		mL_lambda_Var.setHorizontalAlignment(SwingConstants.CENTER);
		mL_lambda_Var.setEditable(false);
		panel_29.add(mL_lambda_Var);

		label_12 = new JLabel();
		label_12.setText("+");
		panel_29.add(label_12);

		mL_invar = new JTextField();
		mL_invar.setText("0");
		mL_invar.setPreferredSize(new Dimension(60, 20));
		mL_invar.setHorizontalAlignment(SwingConstants.CENTER);
		mL_invar.setEditable(false);
		panel_29.add(mL_invar);

		mL_lambda_Invar = new JTextField();
		mL_lambda_Invar.setPreferredSize(new Dimension(60, 20));
		mL_lambda_Invar.setHorizontalAlignment(SwingConstants.CENTER);
		mL_lambda_Invar.setEditable(false);
		panel_29.add(mL_lambda_Invar);

		final JLabel label_14 = new JLabel();
		label_14.setText(")");
		panel_29.add(label_14);

		lambda_I_panel = new JPanel();
		lambda_I_panel.setBorder(new TitledBorder(null, "Invariant likelihood for selected site", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_56 = new GridBagConstraints();
		gridBagConstraints_56.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_56.gridy = 2;
		gridBagConstraints_56.gridx = 0;
		lambda_I_panel.setVisible(false);
		eastPanel.add(lambda_I_panel, gridBagConstraints_56);

		lamba_invariant = new JLabel();
		lamba_invariant.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_invariant.png"));
		lamba_invariant.setText("=");
		lambda_I_panel.add(lamba_invariant);

		lambda_V_panel = new JPanel();
		final GridBagLayout gridBagLayout_10 = new GridBagLayout();
		gridBagLayout_10.rowHeights = new int[] {0,0,0};
		lambda_V_panel.setLayout(gridBagLayout_10);
		lambda_V_panel.setBorder(new TitledBorder(null, "Variable likelihood for selected site", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_57 = new GridBagConstraints();
		gridBagConstraints_57.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_57.gridy = 3;
		gridBagConstraints_57.gridx = 0;
		lambda_V_panel.setVisible(false);
		eastPanel.add(lambda_V_panel, gridBagConstraints_57);

		lambda_variant = new JLabel();
		lambda_variant.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_variant_nogamma.png"));
		lambda_variant.setText("=");
		final GridBagConstraints gridBagConstraints_62 = new GridBagConstraints();
		lambda_V_panel.add(lambda_variant, gridBagConstraints_62);

		panel_28 = new JPanel();
		panel_28.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_158 = new GridBagConstraints();
		gridBagConstraints_158.gridy = 1;
		gridBagConstraints_158.gridx = 0;
		lambda_V_panel.add(panel_28, gridBagConstraints_158);

		final JLabel label_11_3 = new JLabel();
		label_11_3.setText("= ");
		final GridBagConstraints gridBagConstraints_119 = new GridBagConstraints();
		gridBagConstraints_119.gridy = 0;
		gridBagConstraints_119.gridx = 0;
		panel_28.add(label_11_3, gridBagConstraints_119);

		lambaV_A_CL_3 = new JTextField();
		lambaV_A_CL_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_CL_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_120 = new GridBagConstraints();
		gridBagConstraints_120.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_120.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_120.gridx = 2;
		panel_28.add(lambaV_A_CL_3, gridBagConstraints_120);

		final JLabel xLabel_1_3 = new JLabel();
		xLabel_1_3.setText("x");
		final GridBagConstraints gridBagConstraints_131 = new GridBagConstraints();
		gridBagConstraints_131.gridy = 0;
		gridBagConstraints_131.gridx = 3;
		panel_28.add(xLabel_1_3, gridBagConstraints_131);

		lambaV_A_PIi_3 = new JTextField();
		lambaV_A_PIi_3.setText("0.25");
		lambaV_A_PIi_3.setPreferredSize(new Dimension(60, 20));
		lambaV_A_PIi_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_PIi_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_132 = new GridBagConstraints();
		gridBagConstraints_132.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_132.gridy = 0;
		gridBagConstraints_132.gridx = 4;
		panel_28.add(lambaV_A_PIi_3, gridBagConstraints_132);

		final JLabel label_14_4_5 = new JLabel();
		label_14_4_5.setText("+ ");
		final GridBagConstraints gridBagConstraints_137 = new GridBagConstraints();
		gridBagConstraints_137.gridy = 1;
		gridBagConstraints_137.gridx = 0;
		panel_28.add(label_14_4_5, gridBagConstraints_137);

		lambaV_C_CL_3 = new JTextField();
		lambaV_C_CL_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_CL_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_138 = new GridBagConstraints();
		gridBagConstraints_138.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_138.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_138.gridy = 1;
		gridBagConstraints_138.gridx = 2;
		panel_28.add(lambaV_C_CL_3, gridBagConstraints_138);

		final JLabel xLabel_4_3 = new JLabel();
		xLabel_4_3.setText("x");
		final GridBagConstraints gridBagConstraints_144 = new GridBagConstraints();
		gridBagConstraints_144.gridy = 1;
		gridBagConstraints_144.gridx = 3;
		panel_28.add(xLabel_4_3, gridBagConstraints_144);

		lambaV_C_PIi_3 = new JTextField();
		lambaV_C_PIi_3.setText("0.25");
		lambaV_C_PIi_3.setPreferredSize(new Dimension(60, 20));
		lambaV_C_PIi_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_PIi_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_145 = new GridBagConstraints();
		gridBagConstraints_145.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_145.gridy = 1;
		gridBagConstraints_145.gridx = 4;
		panel_28.add(lambaV_C_PIi_3, gridBagConstraints_145);

		final JLabel label_14_4_1_3 = new JLabel();
		label_14_4_1_3.setText("+ ");
		final GridBagConstraints gridBagConstraints_146 = new GridBagConstraints();
		gridBagConstraints_146.gridy = 2;
		gridBagConstraints_146.gridx = 0;
		panel_28.add(label_14_4_1_3, gridBagConstraints_146);

		lambaV_G_CL_3 = new JTextField();
		lambaV_G_CL_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_CL_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_147 = new GridBagConstraints();
		gridBagConstraints_147.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_147.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_147.gridy = 2;
		gridBagConstraints_147.gridx = 2;
		panel_28.add(lambaV_G_CL_3, gridBagConstraints_147);

		final JLabel xLabel_3_3 = new JLabel();
		xLabel_3_3.setText("x");
		final GridBagConstraints gridBagConstraints_150 = new GridBagConstraints();
		gridBagConstraints_150.gridy = 2;
		gridBagConstraints_150.gridx = 3;
		panel_28.add(xLabel_3_3, gridBagConstraints_150);

		lambaV_G_PIi_3 = new JTextField();
		lambaV_G_PIi_3.setText("0.25");
		lambaV_G_PIi_3.setPreferredSize(new Dimension(60, 20));
		lambaV_G_PIi_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_PIi_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_151 = new GridBagConstraints();
		gridBagConstraints_151.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_151.gridy = 2;
		gridBagConstraints_151.gridx = 4;
		panel_28.add(lambaV_G_PIi_3, gridBagConstraints_151);

		final JLabel label_14_4_2_3 = new JLabel();
		label_14_4_2_3.setText("+ ");
		final GridBagConstraints gridBagConstraints_152 = new GridBagConstraints();
		gridBagConstraints_152.gridy = 3;
		gridBagConstraints_152.gridx = 0;
		panel_28.add(label_14_4_2_3, gridBagConstraints_152);

		lambaV_T_CL_3 = new JTextField();
		lambaV_T_CL_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_CL_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_153 = new GridBagConstraints();
		gridBagConstraints_153.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_153.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_153.gridy = 3;
		gridBagConstraints_153.gridx = 2;
		panel_28.add(lambaV_T_CL_3, gridBagConstraints_153);

		final JLabel xLabel_2_3 = new JLabel();
		xLabel_2_3.setText("x");
		final GridBagConstraints gridBagConstraints_156 = new GridBagConstraints();
		gridBagConstraints_156.gridy = 3;
		gridBagConstraints_156.gridx = 3;
		panel_28.add(xLabel_2_3, gridBagConstraints_156);

		lambaV_T_PIi_3 = new JTextField();
		lambaV_T_PIi_3.setText("0.25");
		lambaV_T_PIi_3.setPreferredSize(new Dimension(60, 20));
		lambaV_T_PIi_3.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_PIi_3.setEditable(false);
		final GridBagConstraints gridBagConstraints_157 = new GridBagConstraints();
		gridBagConstraints_157.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_157.gridy = 3;
		gridBagConstraints_157.gridx = 4;
		panel_28.add(lambaV_T_PIi_3, gridBagConstraints_157);

		panel_12 = new JPanel();
		final GridBagLayout gridBagLayout_6 = new GridBagLayout();
		gridBagLayout_6.rowHeights = new int[] {0,0,0,0};
		gridBagLayout_6.columnWidths = new int[] {0,0,0,0,0,0,0};
		panel_12.setLayout(gridBagLayout_6);
		final GridBagConstraints gridBagConstraints_61 = new GridBagConstraints();
		gridBagConstraints_61.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_61.gridy = 2;
		panel_12.setVisible(false);
		lambda_V_panel.add(panel_12, gridBagConstraints_61);

		final JLabel label_11 = new JLabel();
		label_11.setText("= (");
		final GridBagConstraints gridBagConstraints_65 = new GridBagConstraints();
		gridBagConstraints_65.gridy = 0;
		gridBagConstraints_65.gridx = 0;
		panel_12.add(label_11, gridBagConstraints_65);

		lambaV_A_CL = new JTextField();
		lambaV_A_CL.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_CL.setEditable(false);
		final GridBagConstraints gridBagConstraints_59 = new GridBagConstraints();
		gridBagConstraints_59.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_59.gridx = 2;
		gridBagConstraints_59.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_A_CL, gridBagConstraints_59);

		final JLabel label_16 = new JLabel();
		label_16.setText(")  /");
		final GridBagConstraints gridBagConstraints_69 = new GridBagConstraints();
		gridBagConstraints_69.gridy = 0;
		gridBagConstraints_69.gridx = 3;
		panel_12.add(label_16, gridBagConstraints_69);

		lambaV_A_K = new JTextField(""+k);
		lambaV_A_K.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_K.setPreferredSize(new Dimension(60, 20));
		lambaV_A_K.setEditable(false);
		final GridBagConstraints gridBagConstraints_64 = new GridBagConstraints();
		gridBagConstraints_64.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_64.gridy = 0;
		gridBagConstraints_64.gridx = 4;
		panel_12.add(lambaV_A_K, gridBagConstraints_64);

		final JLabel xLabel_1 = new JLabel();
		xLabel_1.setText("x");
		final GridBagConstraints gridBagConstraints_70 = new GridBagConstraints();
		gridBagConstraints_70.gridy = 0;
		gridBagConstraints_70.gridx = 5;
		panel_12.add(xLabel_1, gridBagConstraints_70);

		lambaV_A_PIi = new JTextField("0.25");
		lambaV_A_PIi.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_PIi.setPreferredSize(new Dimension(60, 20));
		lambaV_A_PIi.setEditable(false);
		final GridBagConstraints gridBagConstraints_60 = new GridBagConstraints();
		gridBagConstraints_60.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_60.gridy = 0;
		gridBagConstraints_60.gridx = 6;
		panel_12.add(lambaV_A_PIi, gridBagConstraints_60);

		final JLabel label_14_4 = new JLabel();
		label_14_4.setText("+ (");
		final GridBagConstraints gridBagConstraints_95 = new GridBagConstraints();
		gridBagConstraints_95.gridy = 1;
		gridBagConstraints_95.gridx = 0;
		panel_12.add(label_14_4, gridBagConstraints_95);

		lambaV_C_CL = new JTextField();
		lambaV_C_CL.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_CL.setEditable(false);
		final GridBagConstraints gridBagConstraints_71 = new GridBagConstraints();
		gridBagConstraints_71.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_71.gridy = 1;
		gridBagConstraints_71.gridx = 2;
		gridBagConstraints_71.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_C_CL, gridBagConstraints_71);

		final JLabel label_16_1 = new JLabel();
		label_16_1.setText(")  /");
		final GridBagConstraints gridBagConstraints_89 = new GridBagConstraints();
		gridBagConstraints_89.gridy = 1;
		gridBagConstraints_89.gridx = 3;
		panel_12.add(label_16_1, gridBagConstraints_89);

		lambaV_C_K = new JTextField(""+k);
		lambaV_C_K.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_K.setPreferredSize(new Dimension(60, 20));
		lambaV_C_K.setEditable(false);
		final GridBagConstraints gridBagConstraints_75 = new GridBagConstraints();
		gridBagConstraints_75.gridy = 1;
		gridBagConstraints_75.gridx = 4;
		gridBagConstraints_75.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_C_K, gridBagConstraints_75);

		final JLabel xLabel_4 = new JLabel();
		xLabel_4.setText("x");
		final GridBagConstraints gridBagConstraints_102 = new GridBagConstraints();
		gridBagConstraints_102.gridy = 1;
		gridBagConstraints_102.gridx = 5;
		panel_12.add(xLabel_4, gridBagConstraints_102);

		lambaV_C_PIi = new JTextField("0.25");
		lambaV_C_PIi.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_PIi.setPreferredSize(new Dimension(60, 20));
		lambaV_C_PIi.setEditable(false);
		final GridBagConstraints gridBagConstraints_76 = new GridBagConstraints();
		gridBagConstraints_76.gridy = 1;
		gridBagConstraints_76.gridx = 6;
		gridBagConstraints_76.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_C_PIi, gridBagConstraints_76);

		final JLabel label_14_4_1 = new JLabel();
		label_14_4_1.setText("+ (");
		final GridBagConstraints gridBagConstraints_105 = new GridBagConstraints();
		gridBagConstraints_105.gridy = 2;
		gridBagConstraints_105.gridx = 0;
		panel_12.add(label_14_4_1, gridBagConstraints_105);

		lambaV_G_CL = new JTextField();
		lambaV_G_CL.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_CL.setEditable(false);
		final GridBagConstraints gridBagConstraints_77 = new GridBagConstraints();
		gridBagConstraints_77.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_77.gridy = 2;
		gridBagConstraints_77.gridx = 2;
		gridBagConstraints_77.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_G_CL, gridBagConstraints_77);

		final JLabel label_16_2 = new JLabel();
		label_16_2.setText(")  /");
		final GridBagConstraints gridBagConstraints_90 = new GridBagConstraints();
		gridBagConstraints_90.gridy = 2;
		gridBagConstraints_90.gridx = 3;
		panel_12.add(label_16_2, gridBagConstraints_90);

		lambaV_G_K = new JTextField(""+k);
		lambaV_G_K.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_K.setPreferredSize(new Dimension(60, 20));
		lambaV_G_K.setEditable(false);
		final GridBagConstraints gridBagConstraints_81 = new GridBagConstraints();
		gridBagConstraints_81.gridy = 2;
		gridBagConstraints_81.gridx = 4;
		gridBagConstraints_81.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_G_K, gridBagConstraints_81);

		final JLabel xLabel_3 = new JLabel();
		xLabel_3.setText("x");
		final GridBagConstraints gridBagConstraints_103 = new GridBagConstraints();
		gridBagConstraints_103.gridy = 2;
		gridBagConstraints_103.gridx = 5;
		panel_12.add(xLabel_3, gridBagConstraints_103);

		lambaV_G_PIi = new JTextField("0.25");
		lambaV_G_PIi.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_PIi.setPreferredSize(new Dimension(60, 20));
		lambaV_G_PIi.setEditable(false);
		final GridBagConstraints gridBagConstraints_82 = new GridBagConstraints();
		gridBagConstraints_82.gridy = 2;
		gridBagConstraints_82.gridx = 6;
		gridBagConstraints_82.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_G_PIi, gridBagConstraints_82);

		final JLabel label_14_4_2 = new JLabel();
		label_14_4_2.setText("+ (");
		final GridBagConstraints gridBagConstraints_106 = new GridBagConstraints();
		gridBagConstraints_106.gridy = 3;
		gridBagConstraints_106.gridx = 0;
		panel_12.add(label_14_4_2, gridBagConstraints_106);

		lambaV_T_CL = new JTextField();
		lambaV_T_CL.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_CL.setEditable(false);
		final GridBagConstraints gridBagConstraints_83 = new GridBagConstraints();
		gridBagConstraints_83.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_83.gridy = 3;
		gridBagConstraints_83.gridx = 2;
		gridBagConstraints_83.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_T_CL, gridBagConstraints_83);

		final JLabel label_16_3 = new JLabel();
		label_16_3.setText(")  /");
		final GridBagConstraints gridBagConstraints_91 = new GridBagConstraints();
		gridBagConstraints_91.gridy = 3;
		gridBagConstraints_91.gridx = 3;
		panel_12.add(label_16_3, gridBagConstraints_91);

		lambaV_T_K = new JTextField(""+k);
		lambaV_T_K.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_K.setPreferredSize(new Dimension(60, 20));
		lambaV_T_K.setEditable(false);
		final GridBagConstraints gridBagConstraints_87 = new GridBagConstraints();
		gridBagConstraints_87.gridy = 3;
		gridBagConstraints_87.gridx = 4;
		gridBagConstraints_87.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_T_K, gridBagConstraints_87);

		final JLabel xLabel_2 = new JLabel();
		xLabel_2.setText("x");
		final GridBagConstraints gridBagConstraints_104 = new GridBagConstraints();
		gridBagConstraints_104.gridy = 3;
		gridBagConstraints_104.gridx = 5;
		panel_12.add(xLabel_2, gridBagConstraints_104);

		lambaV_T_PIi = new JTextField("0.25");
		lambaV_T_PIi.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_PIi.setPreferredSize(new Dimension(60, 20));
		lambaV_T_PIi.setEditable(false);
		final GridBagConstraints gridBagConstraints_88 = new GridBagConstraints();
		gridBagConstraints_88.gridy = 3;
		gridBagConstraints_88.gridx = 6;
		gridBagConstraints_88.insets = new Insets(5, 5, 5, 5);
		panel_12.add(lambaV_T_PIi, gridBagConstraints_88);

		lambda_panel = new JPanel();
		final GridBagLayout gridBagLayout_9 = new GridBagLayout();
		gridBagLayout_9.rowHeights = new int[] {0,0,0};
		lambda_panel.setLayout(gridBagLayout_9);
		lambda_panel.setBorder(new TitledBorder(null, "Likelihood for selected site", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final GridBagConstraints gridBagConstraints_54 = new GridBagConstraints();
		gridBagConstraints_54.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_54.gridy = 1;
		gridBagConstraints_54.gridx = 0;
		eastPanel.add(lambda_panel, gridBagConstraints_54);

		lambda = new JLabel();
		lambda.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_nogamma.png"));
		lambda.setText("=");
		final GridBagConstraints gridBagConstraints_73 = new GridBagConstraints();
		gridBagConstraints_73.gridy = 0;
		gridBagConstraints_73.gridx = 0;
		lambda_panel.add(lambda, gridBagConstraints_73);

		panel_28_2 = new JPanel();
		panel_28_2.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_141 = new GridBagConstraints();
		gridBagConstraints_141.gridy = 1;
		gridBagConstraints_141.gridx = 0;
		lambda_panel.add(panel_28_2, gridBagConstraints_141);

		final JLabel label_11_2 = new JLabel();
		label_11_2.setText("= ");
		final GridBagConstraints gridBagConstraints_117 = new GridBagConstraints();
		gridBagConstraints_117.gridy = 0;
		gridBagConstraints_117.gridx = 0;
		panel_28_2.add(label_11_2, gridBagConstraints_117);

		lambaV_A_CL_2 = new JTextField();
		lambaV_A_CL_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_CL_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_118 = new GridBagConstraints();
		gridBagConstraints_118.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_118.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_118.gridx = 2;
		panel_28_2.add(lambaV_A_CL_2, gridBagConstraints_118);

		final JLabel xLabel_1_2 = new JLabel();
		xLabel_1_2.setText("x");
		final GridBagConstraints gridBagConstraints_121 = new GridBagConstraints();
		gridBagConstraints_121.gridy = 0;
		gridBagConstraints_121.gridx = 3;
		panel_28_2.add(xLabel_1_2, gridBagConstraints_121);

		lambaV_A_PIi_2 = new JTextField();
		lambaV_A_PIi_2.setText("0.25");
		lambaV_A_PIi_2.setPreferredSize(new Dimension(60, 20));
		lambaV_A_PIi_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_PIi_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_122 = new GridBagConstraints();
		gridBagConstraints_122.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_122.gridy = 0;
		gridBagConstraints_122.gridx = 4;
		panel_28_2.add(lambaV_A_PIi_2, gridBagConstraints_122);

		final JLabel label_14_4_4 = new JLabel();
		label_14_4_4.setText("+ ");
		final GridBagConstraints gridBagConstraints_123 = new GridBagConstraints();
		gridBagConstraints_123.gridy = 1;
		gridBagConstraints_123.gridx = 0;
		panel_28_2.add(label_14_4_4, gridBagConstraints_123);

		lambaV_C_CL_2 = new JTextField();
		lambaV_C_CL_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_CL_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_124 = new GridBagConstraints();
		gridBagConstraints_124.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_124.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_124.gridy = 1;
		gridBagConstraints_124.gridx = 2;
		panel_28_2.add(lambaV_C_CL_2, gridBagConstraints_124);

		final JLabel xLabel_4_2 = new JLabel();
		xLabel_4_2.setText("x");
		final GridBagConstraints gridBagConstraints_127 = new GridBagConstraints();
		gridBagConstraints_127.gridy = 1;
		gridBagConstraints_127.gridx = 3;
		panel_28_2.add(xLabel_4_2, gridBagConstraints_127);

		lambaV_C_PIi_2 = new JTextField();
		lambaV_C_PIi_2.setText("0.25");
		lambaV_C_PIi_2.setPreferredSize(new Dimension(60, 20));
		lambaV_C_PIi_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_PIi_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_128 = new GridBagConstraints();
		gridBagConstraints_128.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_128.gridy = 1;
		gridBagConstraints_128.gridx = 4;
		panel_28_2.add(lambaV_C_PIi_2, gridBagConstraints_128);

		final JLabel label_14_4_1_2 = new JLabel();
		label_14_4_1_2.setText("+ ");
		final GridBagConstraints gridBagConstraints_129 = new GridBagConstraints();
		gridBagConstraints_129.gridy = 2;
		gridBagConstraints_129.gridx = 0;
		panel_28_2.add(label_14_4_1_2, gridBagConstraints_129);

		lambaV_G_CL_2 = new JTextField();
		lambaV_G_CL_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_CL_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_130 = new GridBagConstraints();
		gridBagConstraints_130.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_130.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_130.gridy = 2;
		gridBagConstraints_130.gridx = 2;
		panel_28_2.add(lambaV_G_CL_2, gridBagConstraints_130);

		final JLabel xLabel_3_2 = new JLabel();
		xLabel_3_2.setText("x");
		final GridBagConstraints gridBagConstraints_133 = new GridBagConstraints();
		gridBagConstraints_133.gridy = 2;
		gridBagConstraints_133.gridx = 3;
		panel_28_2.add(xLabel_3_2, gridBagConstraints_133);

		lambaV_G_PIi_2 = new JTextField();
		lambaV_G_PIi_2.setText("0.25");
		lambaV_G_PIi_2.setPreferredSize(new Dimension(60, 20));
		lambaV_G_PIi_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_PIi_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_134 = new GridBagConstraints();
		gridBagConstraints_134.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_134.gridy = 2;
		gridBagConstraints_134.gridx = 4;
		panel_28_2.add(lambaV_G_PIi_2, gridBagConstraints_134);

		final JLabel label_14_4_2_2 = new JLabel();
		label_14_4_2_2.setText("+ ");
		final GridBagConstraints gridBagConstraints_135 = new GridBagConstraints();
		gridBagConstraints_135.gridy = 3;
		gridBagConstraints_135.gridx = 0;
		panel_28_2.add(label_14_4_2_2, gridBagConstraints_135);

		lambaV_T_CL_2 = new JTextField();
		lambaV_T_CL_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_CL_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_136 = new GridBagConstraints();
		gridBagConstraints_136.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_136.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_136.gridy = 3;
		gridBagConstraints_136.gridx = 2;
		panel_28_2.add(lambaV_T_CL_2, gridBagConstraints_136);

		final JLabel xLabel_2_2 = new JLabel();
		xLabel_2_2.setText("x");
		final GridBagConstraints gridBagConstraints_139 = new GridBagConstraints();
		gridBagConstraints_139.gridy = 3;
		gridBagConstraints_139.gridx = 3;
		panel_28_2.add(xLabel_2_2, gridBagConstraints_139);

		lambaV_T_PIi_2 = new JTextField();
		lambaV_T_PIi_2.setText("0.25");
		lambaV_T_PIi_2.setPreferredSize(new Dimension(60, 20));
		lambaV_T_PIi_2.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_PIi_2.setEditable(false);
		final GridBagConstraints gridBagConstraints_140 = new GridBagConstraints();
		gridBagConstraints_140.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_140.gridy = 3;
		gridBagConstraints_140.gridx = 4;
		panel_28_2.add(lambaV_T_PIi_2, gridBagConstraints_140);

		panel_28_1 = new JPanel();
		panel_28_1.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_116 = new GridBagConstraints();
		gridBagConstraints_116.gridy = 2;
		gridBagConstraints_116.gridx = 0;
		panel_28_1.setVisible(false);
		lambda_panel.add(panel_28_1, gridBagConstraints_116);

		final JLabel label_11_1 = new JLabel();
		label_11_1.setText("= (");
		final GridBagConstraints gridBagConstraints_78 = new GridBagConstraints();
		gridBagConstraints_78.gridy = 0;
		gridBagConstraints_78.gridx = 0;
		panel_28_1.add(label_11_1, gridBagConstraints_78);

		lambaV_A_CL_1 = new JTextField();
		lambaV_A_CL_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_CL_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_79 = new GridBagConstraints();
		gridBagConstraints_79.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_79.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_79.gridx = 2;
		panel_28_1.add(lambaV_A_CL_1, gridBagConstraints_79);

		final JLabel label_16_4 = new JLabel();
		label_16_4.setText(")  /");
		final GridBagConstraints gridBagConstraints_80 = new GridBagConstraints();
		gridBagConstraints_80.gridy = 0;
		gridBagConstraints_80.gridx = 3;
		panel_28_1.add(label_16_4, gridBagConstraints_80);

		lambaV_A_K_1 = new JTextField(""+k);
		lambaV_A_K_1.setPreferredSize(new Dimension(60, 20));
		lambaV_A_K_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_K_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_84 = new GridBagConstraints();
		gridBagConstraints_84.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_84.gridy = 0;
		gridBagConstraints_84.gridx = 4;
		panel_28_1.add(lambaV_A_K_1, gridBagConstraints_84);

		final JLabel xLabel_1_1 = new JLabel();
		xLabel_1_1.setText("x");
		final GridBagConstraints gridBagConstraints_85 = new GridBagConstraints();
		gridBagConstraints_85.gridy = 0;
		gridBagConstraints_85.gridx = 5;
		panel_28_1.add(xLabel_1_1, gridBagConstraints_85);

		lambaV_A_PIi_1 = new JTextField();
		lambaV_A_PIi_1.setText("0.25");
		lambaV_A_PIi_1.setPreferredSize(new Dimension(60, 20));
		lambaV_A_PIi_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_A_PIi_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_86 = new GridBagConstraints();
		gridBagConstraints_86.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_86.gridy = 0;
		gridBagConstraints_86.gridx = 6;
		panel_28_1.add(lambaV_A_PIi_1, gridBagConstraints_86);

		final JLabel label_14_4_3 = new JLabel();
		label_14_4_3.setText("+ (");
		final GridBagConstraints gridBagConstraints_92 = new GridBagConstraints();
		gridBagConstraints_92.gridy = 1;
		gridBagConstraints_92.gridx = 0;
		panel_28_1.add(label_14_4_3, gridBagConstraints_92);

		lambaV_C_CL_1 = new JTextField();
		lambaV_C_CL_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_CL_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_93 = new GridBagConstraints();
		gridBagConstraints_93.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_93.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_93.gridy = 1;
		gridBagConstraints_93.gridx = 2;
		panel_28_1.add(lambaV_C_CL_1, gridBagConstraints_93);

		final JLabel label_16_1_1 = new JLabel();
		label_16_1_1.setText(")  /");
		final GridBagConstraints gridBagConstraints_94 = new GridBagConstraints();
		gridBagConstraints_94.gridy = 1;
		gridBagConstraints_94.gridx = 3;
		panel_28_1.add(label_16_1_1, gridBagConstraints_94);

		lambaV_C_K_1 = new JTextField(""+k);
		lambaV_C_K_1.setPreferredSize(new Dimension(60, 20));
		lambaV_C_K_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_K_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_96 = new GridBagConstraints();
		gridBagConstraints_96.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_96.gridy = 1;
		gridBagConstraints_96.gridx = 4;
		panel_28_1.add(lambaV_C_K_1, gridBagConstraints_96);

		final JLabel xLabel_4_1 = new JLabel();
		xLabel_4_1.setText("x");
		final GridBagConstraints gridBagConstraints_97 = new GridBagConstraints();
		gridBagConstraints_97.gridy = 1;
		gridBagConstraints_97.gridx = 5;
		panel_28_1.add(xLabel_4_1, gridBagConstraints_97);

		lambaV_C_PIi_1 = new JTextField();
		lambaV_C_PIi_1.setText("0.25");
		lambaV_C_PIi_1.setPreferredSize(new Dimension(60, 20));
		lambaV_C_PIi_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_C_PIi_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_98 = new GridBagConstraints();
		gridBagConstraints_98.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_98.gridy = 1;
		gridBagConstraints_98.gridx = 6;
		panel_28_1.add(lambaV_C_PIi_1, gridBagConstraints_98);

		final JLabel label_14_4_1_1 = new JLabel();
		label_14_4_1_1.setText("+ (");
		final GridBagConstraints gridBagConstraints_99 = new GridBagConstraints();
		gridBagConstraints_99.gridy = 2;
		gridBagConstraints_99.gridx = 0;
		panel_28_1.add(label_14_4_1_1, gridBagConstraints_99);

		lambaV_G_CL_1 = new JTextField();
		lambaV_G_CL_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_CL_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_100 = new GridBagConstraints();
		gridBagConstraints_100.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_100.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_100.gridy = 2;
		gridBagConstraints_100.gridx = 2;
		panel_28_1.add(lambaV_G_CL_1, gridBagConstraints_100);

		final JLabel label_16_2_1 = new JLabel();
		label_16_2_1.setText(")  /");
		final GridBagConstraints gridBagConstraints_101 = new GridBagConstraints();
		gridBagConstraints_101.gridy = 2;
		gridBagConstraints_101.gridx = 3;
		panel_28_1.add(label_16_2_1, gridBagConstraints_101);

		lambaV_G_K_1 = new JTextField(""+k);
		lambaV_G_K_1.setPreferredSize(new Dimension(60, 20));
		lambaV_G_K_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_K_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_107 = new GridBagConstraints();
		gridBagConstraints_107.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_107.gridy = 2;
		gridBagConstraints_107.gridx = 4;
		panel_28_1.add(lambaV_G_K_1, gridBagConstraints_107);

		final JLabel xLabel_3_1 = new JLabel();
		xLabel_3_1.setText("x");
		final GridBagConstraints gridBagConstraints_108 = new GridBagConstraints();
		gridBagConstraints_108.gridy = 2;
		gridBagConstraints_108.gridx = 5;
		panel_28_1.add(xLabel_3_1, gridBagConstraints_108);

		lambaV_G_PIi_1 = new JTextField();
		lambaV_G_PIi_1.setText("0.25");
		lambaV_G_PIi_1.setPreferredSize(new Dimension(60, 20));
		lambaV_G_PIi_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_G_PIi_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_109 = new GridBagConstraints();
		gridBagConstraints_109.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_109.gridy = 2;
		gridBagConstraints_109.gridx = 6;
		panel_28_1.add(lambaV_G_PIi_1, gridBagConstraints_109);

		final JLabel label_14_4_2_1 = new JLabel();
		label_14_4_2_1.setText("+ (");
		final GridBagConstraints gridBagConstraints_110 = new GridBagConstraints();
		gridBagConstraints_110.gridy = 3;
		gridBagConstraints_110.gridx = 0;
		panel_28_1.add(label_14_4_2_1, gridBagConstraints_110);

		lambaV_T_CL_1 = new JTextField();
		lambaV_T_CL_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_CL_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_111 = new GridBagConstraints();
		gridBagConstraints_111.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_111.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_111.gridy = 3;
		gridBagConstraints_111.gridx = 2;
		panel_28_1.add(lambaV_T_CL_1, gridBagConstraints_111);

		final JLabel label_16_3_1 = new JLabel();
		label_16_3_1.setText(")  /");
		final GridBagConstraints gridBagConstraints_112 = new GridBagConstraints();
		gridBagConstraints_112.gridy = 3;
		gridBagConstraints_112.gridx = 3;
		panel_28_1.add(label_16_3_1, gridBagConstraints_112);

		lambaV_T_K_1 = new JTextField(""+k);
		lambaV_T_K_1.setPreferredSize(new Dimension(60, 20));
		lambaV_T_K_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_K_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_113 = new GridBagConstraints();
		gridBagConstraints_113.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_113.gridy = 3;
		gridBagConstraints_113.gridx = 4;
		panel_28_1.add(lambaV_T_K_1, gridBagConstraints_113);

		final JLabel xLabel_2_1 = new JLabel();
		xLabel_2_1.setText("x");
		final GridBagConstraints gridBagConstraints_114 = new GridBagConstraints();
		gridBagConstraints_114.gridy = 3;
		gridBagConstraints_114.gridx = 5;
		panel_28_1.add(xLabel_2_1, gridBagConstraints_114);

		lambaV_T_PIi_1 = new JTextField();
		lambaV_T_PIi_1.setText("0.25");
		lambaV_T_PIi_1.setPreferredSize(new Dimension(60, 20));
		lambaV_T_PIi_1.setHorizontalAlignment(SwingConstants.CENTER);
		lambaV_T_PIi_1.setEditable(false);
		final GridBagConstraints gridBagConstraints_115 = new GridBagConstraints();
		gridBagConstraints_115.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints_115.gridy = 3;
		gridBagConstraints_115.gridx = 6;
		panel_28_1.add(lambaV_T_PIi_1, gridBagConstraints_115);
		Distribution dist = new GammaDistribution(alpha,(1.0/alpha));
		dist.initialize();
		graphPanel.setDistribution(dist);
		
		getContentPane().add(graphScrollPane, BorderLayout.CENTER);
		
		mL_Var.setVisible(false);
		mL_invar.setVisible(false);
		mL_lambda_Invar.setVisible(false);
		label_12.setVisible(false);
		parametersHaveChanged();
		showConditionalLikelihoodDetail(selectedConditionalLikelihood);
	}
	
	private void init(){
		categories.put("Category 1", 0);
		categories.put("Category 2", 1);
		categories.put("Category 3", 2);
		categories.put("Category 4", 3);
		categories.put("Category 5", 4);
		categories.put("Category 6", 5);
		sites.put("1", 0);
		sites.put("2", 1);
		sites.put("3", 2);
		sites.put("4", 3);
		sites.put("5", 4);
		sites.put("6", 5);
		rateParam.put(RateParameter.A, 1.0);
		rateParam.put(RateParameter.B, 1.0);
		rateParam.put(RateParameter.C, 1.0);
		rateParam.put(RateParameter.D, 1.0);
		rateParam.put(RateParameter.E, 1.0);
		rateParam.put(RateParameter.K, 1.0);
		try{
		Node node10 = new Node("10");
		Node node9 = new Node("9");
		Node node8 = new Node("8");
		Node node7 = new Node("7");
		Node node6 = new Node("6");
		Node node5 = new Node("5");
		Node node4 = new Node("4");
		Node node3 = new Node("3");
		Node node2 = new Node("2");
		Node node1 = new Node("1");
		Neighbor n = node10.addNeighbor(node8);
		node10.setBranchLength(n, 0.4);
		n= node10.addNeighbor(node9);
		node10.setBranchLength(n, 0.8);
		n= node10.addNeighbor(node6);
		node10.setBranchLength(n, 0.7);
		n= node9.addNeighbor(node4);
		node9.setBranchLength(n, 0.4);
		n= node9.addNeighbor(node5);
		node9.setBranchLength(n, 0.2);
		n= node8.addNeighbor(node3);
		node8.setBranchLength(n, 0.6);
		n= node8.addNeighbor(node7);
		node8.setBranchLength(n, 0.5);
		n= node7.addNeighbor(node1);
		node7.setBranchLength(n, 0.5);
		n= node7.addNeighbor(node2);
		node7.setBranchLength(n, 0.5);
		node10.setToRoot();
		tree.add(node1);
		tree.add(node2);
		tree.add(node3);
		tree.add(node4);
		tree.add(node5);
		tree.add(node6);
		tree.add(node7);
		tree.add(node8);
		tree.add(node9);
		tree.add(node10);
		root = node10;
		A = node7;
		B = node1;
		C = node2;
		treeGraph = new UndirectedSparseGraph<Node, Edge>();
		treeGraph.addVertex(node1);
		treeGraph.addVertex(node2);
		treeGraph.addVertex(node3);
		treeGraph.addVertex(node4);
		treeGraph.addVertex(node5);
		treeGraph.addVertex(node6);
		treeGraph.addVertex(node7);
		treeGraph.addVertex(node8);
		treeGraph.addVertex(node9);
		treeGraph.addVertex(node10);
		treeGraph.addEdge(new Edge(node1.getAncestorBranchLength()), node1, node1.getAncestorNode());
		treeGraph.addEdge(new Edge(node2.getAncestorBranchLength()), node2, node2.getAncestorNode());
		treeGraph.addEdge(new Edge(node3.getAncestorBranchLength()), node3, node3.getAncestorNode());
		treeGraph.addEdge(new Edge(node4.getAncestorBranchLength()), node4, node4.getAncestorNode());
		treeGraph.addEdge(new Edge(node5.getAncestorBranchLength()), node5, node5.getAncestorNode());
		treeGraph.addEdge(new Edge(node6.getAncestorBranchLength()), node6, node6.getAncestorNode());
		treeGraph.addEdge(new Edge(node7.getAncestorBranchLength()), node7, node7.getAncestorNode());
		treeGraph.addEdge(new Edge(node8.getAncestorBranchLength()), node8, node8.getAncestorNode());
		treeGraph.addEdge(new Edge(node9.getAncestorBranchLength()), node9, node9.getAncestorNode());
  	layout = new AggregateLayout<Node,Edge>(new KKLayout<Node,Edge>(treeGraph));
  	layout.setSize(new Dimension(400, 400));
  	psV = new MultiPickedState<Node>();
  	psV.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e){
        if (!isPicking){
          isPicking = true;
          for (Node node : psV.getPicked()){
          	if (node.isInode()) {
          		setSelectedNode(node);
          		break;
          	}
          }
          psV.clear();
          psV.pick(A, true);
          vv.validate();
          vv.repaint();          
          isPicking = false;
        }
      }
    });
  	psE = new MultiPickedState<Edge>();
  	vv = new VisualizationViewer<Node,Edge>(layout);
  	vv.setBackground(Color.WHITE);
  	vv.setPickSupport(new ShapePickSupport<Node,Edge>(vv));
  	vv.setPickedEdgeState(psE);
  	graphScrollPane = new GraphZoomScrollPane(vv);
  	graphMouse = new DefaultModalGraphMouse<Node,Edge>();
  	graphMouse.setZoomAtMouse(false);
  	graphMouse.setMode(Mode.PICKING);
  	vv.setGraphMouse(graphMouse);
		vv.addKeyListener(graphMouse.getModeKeyListener()); //hit 't' or 'p' to change mode
    Transformer<String, Integer> vertexSizeTransformer = new Transformer<String, Integer>(){
    	public Integer transform(String v){
    		return 20;
    	}
    };
    Transformer<String, Float> vertexAspectRatioTransformer = new Transformer<String, Float>(){
    	public Float transform(String v){
    		return 1.0f;
    	}
    };    
    shapeFactory = new VertexShapeFactory<String>(vertexSizeTransformer, vertexAspectRatioTransformer);
    Transformer<Node, Shape> vertexShapeTransformer = new Transformer<Node, Shape>(){
    	public Shape transform(Node n){
        if (n == root){
        	return shapeFactory.getRegularPolygon(n.getLabel(),6);
        }else if (n.isInode()){
          return shapeFactory.getRectangle(n.getLabel());
        }else{
          return shapeFactory.getEllipse(n.getLabel());
        }    		
    	}
    };		
    vv.getRenderContext().setVertexShapeTransformer(vertexShapeTransformer);
  	vv.setPickedVertexState(psV);
    Transformer<Edge,String> stringerE = new Transformer<Edge,String>(){
      public String transform(Edge e) {
          return e.toString();
      }
    };
    vv.setEdgeToolTipTransformer(stringerE);
    vv.getRenderContext().setEdgeLabelTransformer(stringerE);
    Transformer<Node,String> stringerV = new Transformer<Node,String>(){
    	public String transform(Node n) {
    		if (n.isLeaf()){
    			String seq = "";
    			for (DNA dna : dataset.getAllDNA(n.getLabel())){
    				seq += dna.toString();
    			}
    			return seq + "<p><p>" + n.getLabel() + "<p><p><p>";
    		}else{
    			return n.getLabel();
    		}
    	}
    };
  	vv.setVertexToolTipTransformer(stringerV);
    vv.getRenderContext().setVertexLabelTransformer(
    		// this chains together Transformers so that the html tags
    		// are prepended to the toString method output
    		new ChainedTransformer<Node,String>(new Transformer[]{
    				stringerV,	new Transformer<String,String>() {
			public String transform(String input) {
				return "<html><center>"+input;
			}}}));
    //vv.getRenderContext().setVertexLabelTransformer(stringerV2);
    vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Node,Paint>() {
			public Paint transform(Node v) {
				if (v == A){
					return selectionColor;
				}else if (v == B || v == C){
					return childrenColor;
        }else{
        	return new Color(205, 51, 51);
        }				
			}
		});    
		}catch (TooManyNeighborsException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error when building the tree", JOptionPane.ERROR_MESSAGE);
		}catch (NullAncestorException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error when building the graph tree", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addGammaPinv(){
		if (pinvCheckbox.isSelected()){
			pinvPanel.setVisible(true);
			likelihoodLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/ML_tree_formula.png"));
			mL_Var.setVisible(true);
			mL_invar.setVisible(true);
			mL_lambda_Invar.setVisible(true);
			label_12.setVisible(true);
		}else{
			pinvSpinner.setValue(0);
			pinvPanel.setVisible(false);		
			likelihoodLabel.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/ML_tree_formula_nopinv.png"));
			mL_Var.setVisible(false);
			mL_invar.setVisible(false);
			mL_lambda_Invar.setVisible(false);
			label_12.setVisible(false);
		}	
		if (gammaCheckBox.isSelected()){
			gammaMainPanel.setVisible(true);
			gammaCutPanel.setVisible(true);
			gammaRatesPanel.setVisible(true);
			gammaGraphPanel.setVisible(true);
			label_7.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik.png"));
			lambda_variant.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_variant.png"));
			lambda.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda.png"));
		}else{
			kSpinner.setValue(1);
			gammaMainPanel.setVisible(false);
			gammaCutPanel.setVisible(false);
			gammaRatesPanel.setVisible(false);
			gammaGraphPanel.setVisible(false);	
			label_7.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_nogamma.png"));
			lambda_variant.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_variant_nogamma.png"));
			lambda.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/lambda_nogamma.png"));
		}
		if (gammaCheckBox.isSelected() && pinvCheckbox.isSelected()){
			label_9.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_formula.png"));
			lambda_panel.setVisible(false);
			lambda_V_panel.setVisible(true);
			lambda_I_panel.setVisible(true);
			panel_12.setVisible(true);
			panel_28.setVisible(false);
		}else if (!gammaCheckBox.isSelected() && !pinvCheckbox.isSelected()){
			label_9.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_formula_nogamma_nopinv.png"));
			lambda_panel.setVisible(true);
			lambda_V_panel.setVisible(false);
			lambda_I_panel.setVisible(false);
			panel_28_2.setVisible(true);
			panel_28_1.setVisible(false);
		}else if (gammaCheckBox.isSelected() && !pinvCheckbox.isSelected()){
			label_9.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_formula_nopinv.png"));
			lambda_panel.setVisible(true);
			lambda_V_panel.setVisible(false);
			lambda_I_panel.setVisible(false);
			panel_28_2.setVisible(false);
			panel_28_1.setVisible(true);
		}else if (!gammaCheckBox.isSelected() && pinvCheckbox.isSelected()){
			label_9.setIcon(SwingResourceManager.getIcon(MLTutorial.class, "resources/cond_Lik_formula_nogamma.png"));
			lambda_panel.setVisible(false);
			lambda_V_panel.setVisible(true);
			lambda_I_panel.setVisible(true);
			panel_12.setVisible(false);
			panel_28.setVisible(true);
		}
	}
	
	private void setSelectedNode(Node node){
		A = node;
		B = node.getChildren().get(0);
		C = node.getChildren().get(1);
		nodeALabel.setText("= Node "+A.getLabel());
		nodeBLabel.setText("= Node "+B.getLabel());
		nodeCLabel.setText("= Node "+C.getLabel());
		vABMatrixLabel.setText("Branch between node "+A.getLabel()+" and node "+B.getLabel());
		vACMatrixLabel.setText("Branch between node "+A.getLabel()+" and node "+C.getLabel());
		try {
			vABField.setText(Tools.doubletoString(B.getAncestorBranchLength(), 4));
		} catch (NullAncestorException e) {
			vABField.setText("???");
			e.printStackTrace();
		}
		try {
			vACField.setText(Tools.doubletoString(C.getAncestorBranchLength(), 4));
		} catch (NullAncestorException e) {
			vACField.setText("???");
			e.printStackTrace();
		}
		setConditionalLikelihood();
	}
	
	private void setSelectedSite(int site){
		s = site;
		selectedSiteLabel.setText("Selected site = "+ s);
		siteLabel.setText("= Site " + s);
		setConditionalLikelihood();
		if (likelihood != null) {
			ML_site.setText(Tools.doubletoString(likelihood.getLikelihood(s-1), 4));
			ML_site.setCaretPosition(0);
			mL_lambda_Invar.setText(Tools.doubletoString(likelihood.getInvariantSite(s-1),4));
			mL_lambda_Var.setText(Tools.doubletoString(likelihood.getVariableLikelihood(s-1),4));
			lamba_invariant.setText("= " + Tools.doubletoString(likelihood.getInvariantSite(s-1), 2));
			lambaV_A_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
			lambaV_C_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
			lambaV_G_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
			lambaV_T_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
			lambaV_A_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
			lambaV_C_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
			lambaV_G_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
			lambaV_T_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
			lambaV_A_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
			lambaV_C_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
			lambaV_G_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
			lambaV_T_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
			lambaV_A_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
			lambaV_C_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
			lambaV_G_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
			lambaV_T_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
			lambda_variant.setText("= " + Tools.doubletoString(likelihood.getVariableLikelihood(s-1), 4));
			lambda.setText("= " + Tools.doubletoString(likelihood.getVariableLikelihood(s-1), 4));
		}
	}
	
	private void setSelectedCategory(int category){
		c = category;
		categoryLabel.setText("= Category " + (c+1));
		setConditionalLikelihood();
	}
	
	private void setSelectedBase(int base){
		selectedBase = base;		
		condLikNodeAA.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAC.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAG.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeAT.setBorder(new LineBorder(Color.GRAY, 1, false));
		switch (base){
		case Dataset.A :
			xAsLabel.setText("= A");
			condLikNodeAA.setBorder(new LineBorder(selectionColor, 4, false));
			break;
		case Dataset.C :
			xAsLabel.setText("= C");
			condLikNodeAC.setBorder(new LineBorder(selectionColor, 4, false));
			break;
		case Dataset.G :
			xAsLabel.setText("= G");
			condLikNodeAG.setBorder(new LineBorder(selectionColor, 4, false));
			break;
		case Dataset.T :
			xAsLabel.setText("= T");
			condLikNodeAT.setBorder(new LineBorder(selectionColor, 4, false));
			break;
		}
		setConditionalLikelihood();
		showConditionalLikelihoodDetail(selectedConditionalLikelihood);
	}
	
	private void showConditionalLikelihoodDetail(int part){
		selectedConditionalLikelihood = part;
		condLikNodeA1.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA2.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA3.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA4.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA5.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA6.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA7.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeA8.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeBA.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeBC.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeBG.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeBT.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeCA.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeCC.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeCG.setBorder(new LineBorder(Color.GRAY, 1, false));
		condLikNodeCT.setBorder(new LineBorder(Color.GRAY, 1, false));
		switch(part){
		case 1:
			condLikNodeA1.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeBA.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AB.getModel()).setHighlightedCell(selectedBase, Dataset.A);
			((MatrixTableModel)matrixP_AC.getModel()).unsetHighlightedCell();
			break;
		case 2:
			condLikNodeA2.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeBC.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AB.getModel()).setHighlightedCell(selectedBase, Dataset.C);
			((MatrixTableModel)matrixP_AC.getModel()).unsetHighlightedCell();
			break;
		case 3:
			condLikNodeA3.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeBG.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AB.getModel()).setHighlightedCell(selectedBase, Dataset.G);
			((MatrixTableModel)matrixP_AC.getModel()).unsetHighlightedCell();
			break;
		case 4:
			condLikNodeA4.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeBT.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AB.getModel()).setHighlightedCell(selectedBase, Dataset.T);
			((MatrixTableModel)matrixP_AC.getModel()).unsetHighlightedCell();
			break;
		case 5:
			condLikNodeA5.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeCA.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AC.getModel()).setHighlightedCell(selectedBase, Dataset.A);
			((MatrixTableModel)matrixP_AB.getModel()).unsetHighlightedCell();
			break;
		case 6:
			condLikNodeA6.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeCC.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AC.getModel()).setHighlightedCell(selectedBase, Dataset.C);
			((MatrixTableModel)matrixP_AB.getModel()).unsetHighlightedCell();
			break;
		case 7:
			condLikNodeA7.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeCG.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AC.getModel()).setHighlightedCell(selectedBase, Dataset.G);
			((MatrixTableModel)matrixP_AB.getModel()).unsetHighlightedCell();
			break;
		case 8:
			condLikNodeA8.setBorder(new LineBorder(childrenColor, 4, false));
			condLikNodeCT.setBorder(new LineBorder(childrenColor, 4, false));
			((MatrixTableModel)matrixP_AC.getModel()).setHighlightedCell(selectedBase, Dataset.T);
			((MatrixTableModel)matrixP_AB.getModel()).unsetHighlightedCell();
			break;
		}
		matrixP_AB.repaint();
		matrixP_AC.repaint();
	}
	
	private void setGammaShape(){
		try{
			Double d = Double.parseDouble(gammaShapeTextField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
			alpha = d;
			Distribution dist = new GammaDistribution(alpha,(1.0/alpha));
			dist.initialize();
			graphPanel.setDistribution(dist);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + gammaShapeTextField.getText() + " is not a valid positive number. \nShape parameter is reset to 1.0.", "Gamma shape parameter", JOptionPane.ERROR_MESSAGE);
			gammaShapeTextField.setText(alpha+"");
		}		
	}
	
	private void setBranchLength(Node node, JTextField field){
		try{
			Double d = Double.parseDouble(field.getText());
			if (d < 0) throw new NumberFormatException(d + " is a not a positive number");
			node.setBranchLength(node.getAncestorKey(), d);
			for (Edge e : treeGraph.getInEdges(node)){
				if (treeGraph.getEndpoints(e).contains(A)){
					e.weight = d;
				}
			}
      vv.validate();
      vv.repaint();          
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + field.getText() + " is not a valid positive number.", "Branch legnth", JOptionPane.ERROR_MESSAGE);
			try {
				field.setText(Tools.doubletoString(node.getAncestorBranchLength(), 4));
			} catch (NullAncestorException e) {
				e.printStackTrace();
			}
		}catch (NullAncestorException ex){
			ex.printStackTrace();
		}		
	}
	
	private void setConditionalLikelihood(){
		if (likelihood != null){
			if (A == root){
				double[] rootCL = new double[4];
				for (int b=0 ; b < 4 ; b++){
					try {
						rootCL[b] = (likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.A) * likelihood.getPij(b, Dataset.A, B.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.C) * likelihood.getPij(b, Dataset.C, B.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.G) * likelihood.getPij(b, Dataset.T, B.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.T) * likelihood.getPij(b, Dataset.G, B.getAncestorBranchLength(), c)
								)*(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.A) * likelihood.getPij(b, Dataset.A, C.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.C) * likelihood.getPij(b, Dataset.C, C.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.G) * likelihood.getPij(b, Dataset.T, C.getAncestorBranchLength(), c)
								+likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.T) * likelihood.getPij(b, Dataset.G, C.getAncestorBranchLength(), c));
					} catch (NullAncestorException e) {
						e.printStackTrace();
					}					
				}
				condLikNodeAA.setText(Tools.doubletoString(rootCL[0], 4));
				condLikNodeAC.setText(Tools.doubletoString(rootCL[1], 4));
				condLikNodeAG.setText(Tools.doubletoString(rootCL[2], 4));
				condLikNodeAT.setText(Tools.doubletoString(rootCL[3], 4));				
			}else{
				condLikNodeAA.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(A, c, s-1, Dataset.A), 4));
				condLikNodeAC.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(A, c, s-1, Dataset.C), 4));
				condLikNodeAG.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(A, c, s-1, Dataset.G), 4));
				condLikNodeAT.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(A, c, s-1, Dataset.T), 4));
			}
			condLikNodeBA.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.A), 4));
			condLikNodeBC.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.C), 4));
			condLikNodeBG.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.G), 4));
			condLikNodeBT.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.T), 4));
			condLikNodeCA.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.A), 4));
			condLikNodeCC.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.C), 4));
			condLikNodeCG.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.G), 4));
			condLikNodeCT.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.T), 4));
			try {
				for (int i=0 ; i < 4 ; i++){
					for (int j=0 ; j < 4 ; j++){
						matrixP_AB.setValueAt(Tools.doubletoString(likelihood.getPij(i, j, B.getAncestorBranchLength(), c), 4), i, j);
						matrixP_AC.setValueAt(Tools.doubletoString(likelihood.getPij(i, j, C.getAncestorBranchLength(), c), 4), i, j);
					}
				}
				matrixP_AB.repaint();
				matrixP_AC.repaint();
				condLikNodeA1.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.A) * likelihood.getPij(selectedBase, Dataset.A, B.getAncestorBranchLength(), c), 4));
				condLikNodeA2.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.C) * likelihood.getPij(selectedBase, Dataset.C, B.getAncestorBranchLength(), c), 4));
				condLikNodeA3.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.G) * likelihood.getPij(selectedBase, Dataset.T, B.getAncestorBranchLength(), c), 4));
				condLikNodeA4.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(B, c, s-1, Dataset.T) * likelihood.getPij(selectedBase, Dataset.G, B.getAncestorBranchLength(), c), 4));
				condLikNodeA5.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.A) * likelihood.getPij(selectedBase, Dataset.A, C.getAncestorBranchLength(), c), 4));
				condLikNodeA6.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.C) * likelihood.getPij(selectedBase, Dataset.C, C.getAncestorBranchLength(), c), 4));
				condLikNodeA7.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.G) * likelihood.getPij(selectedBase, Dataset.T, C.getAncestorBranchLength(), c), 4));
				condLikNodeA8.setText(Tools.doubletoString(likelihood.getConditionnalLikelihood(C, c, s-1, Dataset.T) * likelihood.getPij(selectedBase, Dataset.G, C.getAncestorBranchLength(), c), 4));
			} catch (NullAncestorException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setRateKappa(){
		try{
			Double d = Double.parseDouble(kappaField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
			rateParam.put(RateParameter.K, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + kappaField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
			kappaField.setText(rateParam.get(RateParameter.K)+"");
		}		
	}
	
	private void setRateA(){
		try{
			Double d = Double.parseDouble(rateAField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
			rateParam.put(RateParameter.A, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + rateAField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
			rateAField.setText(rateParam.get(RateParameter.A)+"");
		}		
	}
	
	private void setRateB(){
		try{
			Double d = Double.parseDouble(rateBField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
			rateParam.put(RateParameter.B, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + rateBField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
				rateBField.setText(rateParam.get(RateParameter.B)+"");
		}		
	}
	
	private void setRateC(){
		try{
			Double d = Double.parseDouble(rateCField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
				rateParam.put(RateParameter.C, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + rateCField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
				rateCField.setText(rateParam.get(RateParameter.C)+"");
		}		
	}
	
	private void setRateD(){
		try{
			Double d = Double.parseDouble(rateDField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
				rateParam.put(RateParameter.D, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + rateDField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
				rateDField.setText(rateParam.get(RateParameter.D)+"");
		}		
	}
	
	private void setRateE(){
		try{
			Double d = Double.parseDouble(rateEField.getText());
			if (d <= 0) throw new NumberFormatException(d + " is a not a non-zero positive number");
				rateParam.put(RateParameter.E, d);
			adjustRatio(true);
			parametersHaveChanged();
		}catch (NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + rateEField.getText() + " is not a valid positive number. \nRate parameter is reset to 1.0.", "Rate parameter", JOptionPane.ERROR_MESSAGE);
				rateEField.setText(rateParam.get(RateParameter.E)+"");
		}		
	}
	
	private void adjustRatio(boolean fromTable){
		if (!isAdjustingRatio){
			isAdjustingRatio = true;
			if (fromTable){
				double ti,tv;
				switch(model){
				case K2P:
				case HKY85:
					ti = 2 * rateParam.get(RateParameter.K);
					tv = 4.0;
					break;
				case GTR:
					ti = rateParam.get(RateParameter.B) + rateParam.get(RateParameter.E);
					tv = rateParam.get(RateParameter.A) + rateParam.get(RateParameter.C) +
							 rateParam.get(RateParameter.D) + 1.0;
					break;
				case JC:
				default:
					ti = 2.0;
					tv = 4.0;
					break;
				}
				if (ti > tv){
					ratioTiSpinner.setValue(new Double(ti/tv));
					ratioTvSpinner.setValue(new Double(1));
				}else{
					ratioTiSpinner.setValue(new Double(1));					
					ratioTvSpinner.setValue(new Double(1.0/(ti/tv)));
				}
			}else{
				double k = Double.parseDouble(ratioTiSpinner.getValue().toString()) / Double.parseDouble(ratioTvSpinner.getValue().toString()) * 2.0; 
				kappaField.setText(""+k);
				kappaField.setCaretPosition(0);
				rateParam.put(RateParameter.K, k);
				parametersHaveChanged();
			}
			isAdjustingRatio = false;
		}
	}

	private void adjustFrequencies(){
		switch(model){
		case JC:	
		case K2P:		
			freqAField.setText("0.25");
			freqCField.setText("0.25");
			freqGField.setText("0.25");
			freqTField.setText("0.25");
			lambaV_A_PIi.setText("0.25");
			lambaV_C_PIi.setText("0.25");
			lambaV_G_PIi.setText("0.25");
			lambaV_T_PIi.setText("0.25");
			lambaV_A_PIi_1.setText("0.25");
			lambaV_C_PIi_1.setText("0.25");
			lambaV_G_PIi_1.setText("0.25");
			lambaV_T_PIi_1.setText("0.25");
			lambaV_A_PIi_2.setText("0.25");
			lambaV_C_PIi_2.setText("0.25");
			lambaV_G_PIi_2.setText("0.25");
			lambaV_T_PIi_2.setText("0.25");
			lambaV_A_PIi_3.setText("0.25");
			lambaV_C_PIi_3.setText("0.25");
			lambaV_G_PIi_3.setText("0.25");
			lambaV_T_PIi_3.setText("0.25");
			break;
		case HKY85:
		case GTR:
			freqAField.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.A], 4));
			freqCField.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.C], 4));
			freqGField.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.G], 4));
			freqTField.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.T], 4));
			lambaV_A_PIi.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.A], 4));
			lambaV_C_PIi.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.C], 4));
			lambaV_G_PIi.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.G], 4));
			lambaV_T_PIi.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.T], 4));
			lambaV_A_PIi_1.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.A], 4));
			lambaV_C_PIi_1.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.C], 4));
			lambaV_G_PIi_1.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.G], 4));
			lambaV_T_PIi_1.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.T], 4));
			lambaV_A_PIi_2.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.A], 4));
			lambaV_C_PIi_2.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.C], 4));
			lambaV_G_PIi_2.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.G], 4));
			lambaV_T_PIi_2.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.T], 4));
			lambaV_A_PIi_3.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.A], 4));
			lambaV_C_PIi_3.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.C], 4));
			lambaV_G_PIi_3.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.G], 4));
			lambaV_T_PIi_3.setText(Tools.doubletoString(dataset.getNucleotideFrequencies()[Dataset.T], 4));
			break;
		}		
	}
	
	private void parametersHaveChanged(){
		likelihood = new Likelihood(dataset, model, distribution, alpha, pi, rateParam, tree, root, k);
		for (int i=0 ; i < 4 ; i++){
			for (int j=0 ; j < 4 ; j++){
				matrixQ.setValueAt(Tools.doubletoString(likelihood.getQ(i, j), 4), i, j);
			}
		}
		matrixQ.repaint();
		scalingField.setText(Tools.doubletoString(likelihood.getScaling(), 4));
		String cuts = "= ";
		double[] cutPoints = new double[k-1];
		for (int c=0 ; c < k-1 ; c++){
			cutPoints[c] = likelihood.getGammaCutPoints(c, c+1);
			cuts += Tools.doubletoString(cutPoints[c], 4);
			if (c < k-2) cuts += ", ";
		}
		if (k == 1) cuts = "= no gamma distribution used";
		cutpointsLabel.setText(cuts);
		String rates = "= ";
		for (int c=0 ; c < k ; c++){
			rates += Tools.doubletoString(likelihood.getGammaRate(c), 4);
			if (c < k-1) rates += ", ";
		}		
		gammaRateslabel.setText(rates);
		graphPanel.setCutPoints(cutPoints);
		likelihoodLabel.setText(" = " + Tools.doubletoString(likelihood.getLikelihoodValue(), 4));
		ML_site.setText(Tools.doubletoString(likelihood.getLikelihood(s-1), 4));
		ML_site.setCaretPosition(0);
		mL_lambda_Invar.setText(Tools.doubletoString(likelihood.getInvariantSite(s-1),4));
		mL_lambda_Var.setText(Tools.doubletoString(likelihood.getVariableLikelihood(s-1),4));
		lamba_invariant.setText("= " + Tools.doubletoString(likelihood.getInvariantSite(s-1), 2));
		lambaV_A_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
		lambaV_C_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
		lambaV_G_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
		lambaV_T_CL.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
		lambaV_A_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
		lambaV_C_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
		lambaV_G_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
		lambaV_T_CL_1.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
		lambaV_A_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
		lambaV_C_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
		lambaV_G_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
		lambaV_T_CL_2.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
		lambaV_A_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.A));
		lambaV_C_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.C));
		lambaV_G_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.G));
		lambaV_T_CL_3.setText(likelihood.getLambaVConditionalLikelihood(s-1, Dataset.T));
		lambda_variant.setText("= " + Tools.doubletoString(likelihood.getVariableLikelihood(s-1), 4));
		lambda.setText("= " + Tools.doubletoString(likelihood.getVariableLikelihood(s-1), 4));
		setConditionalLikelihood();
	}
	
	public void exit(){
		System.exit(0);
	}
	
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      exit();
    }
  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    try {
    	/*
    	boolean updateCheck = true;
    	for (String arg : args){
    		if (arg.equals("noupdate")) updateCheck = false;
    	}
    	if (updateCheck) ApplicationLauncher.launchApplication("170", null, true, null);
    	*/
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  		SwingUtilities.invokeLater(new Runnable() {
  			public void run() {
  				MLTutorial mlTutorial = new MLTutorial();
  				mlTutorial.setTitle("Likelihood computation of a tree for dummies");
  				mlTutorial.setIconImage(Toolkit.getDefaultToolkit().getImage(mltutorial.MLTutorial.class.getResource("resources/ML_Dummies.png")));
  				mlTutorial.validate();
  				//Center the window
  				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  				Dimension frameSize = mlTutorial.getSize();
  				if (frameSize.height > screenSize.height) {
  					frameSize.height = screenSize.height;
  				}
  				if (frameSize.width > screenSize.width) {
  					frameSize.width = screenSize.width;
  				}
  				mlTutorial.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
  				mlTutorial.setExtendedState(JFrame.MAXIMIZED_BOTH);
  				mlTutorial.setVisible(true);
        }
      });
    }
    catch(Exception e) {
      e.printStackTrace();
    }
	}

}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class Main extends JFrame {

    private final Map<String, BigDecimal> sampleData;
    private final JList<String> sampleList;
    private final DefaultListModel<String> sampleListModel;

    private final JList<String> cartList;
    private final DefaultListModel<String> cartListModel;

    private final JTextField searchField;
    private final JScrollPane sampleScrollPane;
    private final JButton calculateButton;

    private final JButton resetButton; // new reset button



    public Main(Map<String, BigDecimal> sampleData) {
        super("Sample Calculator");
        this.sampleData = sampleData;

        // create search bar
        searchField = new JTextField();
        searchField.addActionListener(new SearchActionListener());

        // create sample list
        sampleListModel = new DefaultListModel<>();
        sampleList = new JList<>(sampleListModel);
        sampleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        sampleList.addMouseListener(new MouseAdapter() { // add MouseListener to sampleList
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // check for double click event
                    int index = sampleList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String sampleName = sampleListModel.get(index);
                        cartListModel.addElement(sampleName);
                    }
                }
            }
        });

        sampleScrollPane = new JScrollPane(sampleList); // initialize instance variable

        // create cart list
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);


        // create buttons
        JButton addToCartButton = new JButton("Lägg i varukorg");
        addToCartButton.addActionListener(new AddToCartActionListener());

        calculateButton = new JButton("Beräkna kostnad"); // initialize instance variable
        calculateButton.addActionListener(new CalculateActionListener());

        //reset button
        resetButton = new JButton("Återställ"); // initialize instance variable
        resetButton.addActionListener(new ResetActionListener());


        // add components to the frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchField, BorderLayout.NORTH);
        panel.add(sampleScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addToCartButton);
        panel.add(buttonPanel, BorderLayout.EAST);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(new JLabel("Valda provtagningar"), BorderLayout.NORTH);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        panel.add(cartPanel, BorderLayout.WEST);

        JPanel bottomPanel = new JPanel(new FlowLayout()); // new panel for reset button
        bottomPanel.add(resetButton);
        bottomPanel.add(calculateButton); // moved calculate button to new panel
        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(800, 500)); // set preferred size of panel
        add(panel);

        // populate sample list with all sample names
        sampleListModel.addAll(sampleData.keySet());

        // set size of the frame
        setSize(800, 500);

        //colours to the panels
    }

    private class ResetActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset the sample list model to show all sample names
            sampleListModel.clear();
            sampleListModel.addAll(sampleData.keySet());

            // Clear the cart list model
            cartListModel.clear();

            // Clear the search field
            searchField.setText("");

            // Clear the selected items in the sample and cart lists
            sampleList.clearSelection();
            cartList.clearSelection();

            // Enable the calculate button
            calculateButton.setEnabled(true);

        }
    }

    private class SearchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchText = searchField.getText().trim().toLowerCase();
            List<String> matchingSampleNames = sampleData.keySet().stream()
                    .filter(sampleName -> sampleName.toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            sampleListModel.clear();
            sampleListModel.addAll(matchingSampleNames);
        }
    }

    private class AddToCartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedSampleNames = sampleList.getSelectedValuesList();
            for (String sampleName : selectedSampleNames) {
                cartListModel.addElement(sampleName);
            }
        }
    }

    private class CalculateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedSampleNames = new ArrayList<>();
            for (int i = 0; i < cartListModel.size(); i++) {
                selectedSampleNames.add(cartListModel.getElementAt(i));
            }
            BigDecimal sampleCost = BigDecimal.ZERO;
            for (String sampleName : selectedSampleNames) {
                BigDecimal price = sampleData.get(sampleName);
                if (price != null) {
                    sampleCost = sampleCost.add(price);
                }
            }
            BigDecimal totalPrice;
            if (sampleCost.compareTo(new BigDecimal(400)) > 0) {
                totalPrice = new BigDecimal(50).add(sampleCost).multiply(new BigDecimal(2.5));
            } else {
                totalPrice = new BigDecimal(50).add(sampleCost).multiply(new BigDecimal(3));
            }

            // Create a new list model for selected samples
            DefaultListModel<String> selectedListModel = new DefaultListModel<>();

            // Add selected sample names and their cost to the new list model
            for (String sampleName : selectedSampleNames) {
                BigDecimal price = sampleData.get(sampleName);
                if (price != null) {
                    String sampleString = String.format(sampleName);
                    selectedListModel.addElement(sampleString);
                }
            }

            // Display total price and selected samples to user
           JOptionPane.showMessageDialog(Main.this, String.format("Total kostnad: %s SEK", totalPrice ));

            calculateButton.setEnabled(true);

        }
    }

}






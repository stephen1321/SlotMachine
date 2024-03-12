//This is a working slot machine
//Start off with 100 credits, select an amount to pay (1, 5, 10)
//Match 3 of any symbol and win 10x paid amount

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Interface extends JFrame {
    private JLabel resultLabel;
    private JLabel creditsLabel;
    private JLabel[] slotLabels;  // Array to store references to slot labels
    private int credits = 100;  // Initial credits
    private int selectedPayAmount = 1;  // Default pay amount
    private Timer timer;
    private int spinDuration = 500; // in milliseconds


    public Interface() {
        // Set up the JFrame
        super("Slot Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);  // Adjusted height based on the design
        setLayout(new BorderLayout());

        // Create components for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        resultLabel = new JLabel("Welcome to the Slot Machine", SwingConstants.CENTER);
        creditsLabel = new JLabel("Credits: " + credits, SwingConstants.CENTER);

        // Styling resultLabel and topPanel
        resultLabel.setForeground(new Color(0xFFD700));
        Font resultFont = new Font("Copperplate Gothic Bold", Font.BOLD, 20);
        resultLabel.setFont(resultFont);
        creditsLabel.setForeground(new Color(0xFFD700));
        topPanel.setBackground(new Color(0x4B0082));
        topPanel.setOpaque(true);

        topPanel.add(resultLabel, BorderLayout.CENTER);
        topPanel.add(creditsLabel, BorderLayout.NORTH);

        // Create components for the middle section
        JPanel slotPanel = createSlotPanel();

        // Create components for the bottom section
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton spinButton = new JButton("Spin");
        spinButton.setBackground(new Color(0xd4793d));
        Font buttonFont = new Font("Copperplate Gothic Bold", Font.BOLD, 20);
        spinButton.setFont(buttonFont);

        // Attach ActionListener to the Spin button
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spin();
            }
        });

        // Create toggle buttons for additional credits
        JToggleButton pay1Button = createPayButton("Pay 1", 1);
        JToggleButton pay5Button = createPayButton("Pay 5", 5);
        JToggleButton pay10Button = createPayButton("Pay 10", 10);

        // Add buttons to a ButtonGroup so that only one can be selected at a time
        ButtonGroup payButtonGroup = new ButtonGroup();
        payButtonGroup.add(pay1Button);
        payButtonGroup.add(pay5Button);
        payButtonGroup.add(pay10Button);

        // Add buttons to the bottom panel
        bottomPanel.add(pay1Button);
        bottomPanel.add(pay5Button);
        bottomPanel.add(pay10Button);
        bottomPanel.add(spinButton);

        bottomPanel.setBackground(new Color(0x4B0082)); // Set a purple color

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(slotPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Creates each pay Button (1, 5, 10)
    private JToggleButton createPayButton(String text, final int payAmount) {
        JToggleButton payButton = new JToggleButton(text);
        payButton.setBackground(new Color(0xFFD700));

        // Attach ActionListener to the Pay button
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPayAmount = payAmount;
            }
        });

        return payButton;
    }

    //adds entire slot panel
    private JPanel createSlotPanel() {
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(1, 3));

        // Set initial symbols, gradient colors, center the text, and change the font using a for loop
        Font font = new Font("Copperplate Gothic Bold", Font.BOLD, 46);
        slotLabels = new JLabel[3];  // Initialize the array
        for (int i = 0; i < 3; i++) {
            JLabel slotLabel = new JLabel();
            slotLabel.setText("7");
            slotLabel.setForeground( new Color(0xf0cc18));
            slotLabel.setFont(font);
            // Create a gradient paint from top to bottom
            GradientPaint gradient = new GradientPaint(0, 0, Color.black, 0, 250, Color.GRAY, true);

            // Create a JPanel to act as the background with the gradient
            JPanel gradientPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            // Set the layout to BorderLayout and add the label to the center
            gradientPanel.setLayout(new BorderLayout());
            gradientPanel.add(slotLabel, BorderLayout.CENTER);

            // Set the alignment for the label (center of the BorderLayout)
            slotLabel.setHorizontalAlignment(SwingConstants.CENTER);

            gradientPanel.setFont(font);
            gradientPanel.setBorder(new LineBorder(new Color(0xa321cf), 8));  // Increase border width

            // Add the panel to the slotPanel
            slotPanel.add(gradientPanel);

            // Save reference to the label
            slotLabels[i] = slotLabel;
        }

        return slotPanel;
    }
    //sets each slot to new item
    private void updateSlotSymbols() {
        // Simulate spinning by generating random symbols
        String[] symbols = {"Cherry", "Bar", "7", "Orange", "Lemon"};
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            slotLabels[i].setText(symbols[random.nextInt(symbols.length)]);
        }
    }

    // Class to represent slot information
    private class SlotInfo {
        String symbol;
        Color color;

        SlotInfo(String symbol, Color color) {
            this.symbol = symbol;
            this.color = color;
        }
    }

    //spins slot machine
    private void spin() {
        if (timer != null && timer.isRunning()) {
            // Avoid starting a new spin if the previous one is still running
            return;
        }

        // Deduct the selected pay amount
        if (credits >= selectedPayAmount) {
            credits -= selectedPayAmount;
            creditsLabel.setText("Credits: " + credits);
        } else {
            resultLabel.setText("Not enough credits to spin.");
            return;  // Do not proceed with the spin if not enough credits
        }

        /*
        //slot spinning animation
        timer = new Timer(100, new ActionListener() {
            private long startTime = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime == -1) {
                    startTime = System.currentTimeMillis();
                }

                long elapsedTime = System.currentTimeMillis() - startTime;

                if (elapsedTime < spinDuration) {
                    // Simulate spinning by updating symbols at a fast rate
                    updateSlotSymbols();
                } else {
                    // Stop the timer after the spinDuration
                    timer.stop();
                    // Final update to display the result of the spin
                    updateSlotSymbols();
                    // Check for a win or loss here if needed
                }
            }
        });

        // Start the timer to simulate spinning
        timer.start();
        */
        // Simulate spinning by generating random symbols
        String[] symbols = {"Cherry", "Bar", "7", "Orange", "Lemon"};
        Random random = new Random();

        String symbol1 = symbols[random.nextInt(symbols.length)];
        String symbol2 = symbols[random.nextInt(symbols.length)];
        String symbol3 = symbols[random.nextInt(symbols.length)];

        // Update the labels with the new symbols
        for (int i = 0; i < 3; i++) {
            slotLabels[i].setText(symbol(i + 1, symbol1, symbol2, symbol3));
        }
        // Check for a win
        //if (symbol1.equals(symbol2) && symbol2.equals(symbol3)) {
        if (symbol1 == symbol2 && symbol2 == symbol3) {
            resultLabel.setText("Congratulations! You won!");
            credits += selectedPayAmount * 10;  // Adjust credits on a win (you can modify as needed)
        } else {
            resultLabel.setText("Try again. Good luck!");
        }

        // Update credits label
        creditsLabel.setText("Credits: " + credits);
    }

    // Function to determine the symbol based on the slot number
    private String symbol(int slotNumber, String symbol1, String symbol2, String symbol3) {
        switch (slotNumber) {
            case 1:
                return symbol1;
            case 2:
                return symbol2;
            case 3:
                return symbol3;
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }
}

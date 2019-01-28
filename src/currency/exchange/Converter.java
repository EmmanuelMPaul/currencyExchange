package currency.exchange;

import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;
import javax.swing.JFrame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import javax.swing.*;

public class Converter {

    //initialize variables
    protected static int i, j, k, color, loading = 0;
    protected static String countryfrom[] = {"Select Country", "USD", "EUR", "GBP", "INR", "AUD", "CAD", "SGD"};
    protected static String[][] USD, EUR, GBP, INR, AUD, CAD, SGD;
    protected static java.util.Timer timer;
    protected static String timeUpadte = "";

    //initialize GUI component
    protected JFrame mainFrame;
    protected JPanel controlPanel;
    protected static JLabel headerLabel, statusLabel;

    protected JLabel from, to, amount;
    protected static JComboBox fromCountry, toCountry;

    protected static JTextField amountTxt;
    protected static JButton submit;
    protected static JButton reset;

    //contructor to initilalize declared variables
    public Converter() {
        from = new JLabel("  Convert From:   ");
        to = new JLabel("  To:   ");
        amount = new JLabel("  Amount:  ");
        fromCountry = new JComboBox(countryfrom);
        fromCountry.setBackground(Color.decode("#d2c5a1"));

        toCountry = new JComboBox();
        toCountry.addItem("select country");
        toCountry.setBackground(Color.decode("#d2c5a1"));

        amountTxt = new JTextField(25);

        reset = new JButton("Reset");
        reset.setBackground(Color.orange);

        submit = new JButton("Submit");
        submit.setBackground(Color.GREEN);
        prepareGUI();
    }

    //create GUI Jframe and add component
    private void prepareGUI() {
        mainFrame = new JFrame("World Currency Exchange ");
        mainFrame.setSize(600, 680);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new GridLayout(3, 1));

        headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(34f));
        headerLabel.setForeground(Color.DARK_GRAY);
        statusLabel = new JLabel("<html><span style=\"color:red\">Please wait.... :</span>  <br><span style=\"color:green\">database loading real time records</span</html>", JLabel.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(18f));
        statusLabel.setForeground(Color.blue);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(Color.LIGHT_GRAY);
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    //layout swing components using GridBagLayout
    private void showGridBagLayoutDemo() {
        headerLabel.setText("CURRENCY CONVERTER");

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();

        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        //from
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(4, 3, 4, 3);
        panel.add(from, gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.ipady = 12;
        panel.add(fromCountry, gbc);

        //to
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(to, gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.ipady = 12;
        panel.add(toCountry, gbc);

        //amount
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.ipady = 12;
        panel.add(amount, gbc);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.ipady = 12;
        panel.add(amountTxt, gbc);

        //action
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.ipady = 2;
        gbc.gridwidth = 2;
        panel.add(submit, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.ipady = 2;

        panel.add(reset, gbc);

        controlPanel.add(panel);
        mainFrame.setVisible(true);
    }

    //load data from remote source
    public static void remoteFatch() {
        String country = "";
        for (String from1 : countryfrom) {
            if (!"Select Country".equalsIgnoreCase(from1)) {
                i = 0;
                j = 0;
                k = 0;
                country = from1;
                try {
                    UserAgent userAgent = new UserAgent();
                    userAgent.visit("https://www.x-rates.com/table/?from=" + country + "&amount=1");
                    com.jaunt.Element elements = userAgent.doc.findFirst("<span class=\"ratesTimestamp\">");//find every element who's tagname is div or span.
                    timeUpadte = elements.getChildText(); //set time when database updated results
                    com.jaunt.Element tableElement = userAgent.doc.findFirst("<table class=\"tablesorter ratesTable\">");   //find table Element
                    Table table = new Table((com.jaunt.Element) tableElement);                   //create Table component

                    java.util.List<String> names = table.getTextFromColumn(0);
                    java.util.List<String> numbers = table.getTextFromColumn(1);
                    java.util.List<String> sales = table.getTextFromColumn(2);//get text from first column
                    if (names.size() == numbers.size() && names.size() == sales.size()) {
                        if (null != country) {
                            switch (country) {
                                case "USD":
                                    USD = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        USD[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        USD[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        USD[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "EUR":
                                    EUR = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        EUR[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        EUR[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        EUR[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "GBP":
                                    GBP = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        GBP[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        GBP[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        GBP[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "INR":
                                    INR = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        INR[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        INR[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        INR[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "AUD":
                                    AUD = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        AUD[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        AUD[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        AUD[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "CAD":
                                    CAD = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        CAD[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        CAD[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        CAD[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                case "SGD":
                                    SGD = new String[names.size() - 1][3];

                                    names.subList(1, names.size()).forEach((String text) -> {
                                        SGD[i][0] = text;
                                        i++;
                                    });
                                    numbers.subList(1, numbers.size()).forEach((text1) -> {
                                        SGD[j][1] = text1;
                                        j++;
                                    });
                                    sales.subList(1, sales.size()).forEach((text2) -> {
                                        SGD[k][2] = text2;
                                        k++;
                                    });
                                    break;
                                default:
                                    break;

                            }
                        }
                    }

                } catch (JauntException e) {
                    System.out.println(e);
                }
            }
        }

    }

    public static void main(String[] args) {
        
        //colors to change when database is upadated
        String activecolor[] = {"#ff0000", "#008000", "#666666", "#009900", "#0000ff", "#660066", "#ff0000", "#800080"};

        //timer to loop after every one minute
        timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {              // do your work      
                remoteFatch();
                if (color > 7) {
                    color = 0;
                } else {
                    headerLabel.setForeground(Color.decode(activecolor[color]));
                    color = color + 1;
                    if (loading == 0) {
                        statusLabel.setText("<html>START :  <br>Enter Correct Inputs to start</html>");
                        loading = loading + 1;
                    }
                }

            }
        }, 0, 60 * 1000);

        Converter swingLayoutDemo = new Converter();
        swingLayoutDemo.showGridBagLayoutDemo();
        
        //attach event listener to submit button
        submit.addActionListener((ActionEvent e) -> {
            double amount = 0;
            double num1 = 0;
            double num2 = 0;
            
            String s = (String) fromCountry.getSelectedItem();//get the selected item
            int toIndex = (int) toCountry.getSelectedIndex();//get the selected index
            try {
                amount = Double.parseDouble(amountTxt.getText());

                if ("Select Country".equals(s)) {
                    // this gets caught in the catch block
                    statusLabel.setText("<html>Error : <br>Select Country to convert from!<html>");
                    statusLabel.setForeground(Color.red);
                    throw new IllegalArgumentException("");
                } else if ("Select Country".equals(toCountry.getSelectedItem())) {
                    statusLabel.setText("<html>Error : <br>Select Country to convert to !<html>");
                    statusLabel.setForeground(Color.red);
                    throw new IllegalArgumentException("");
                } else if (amount < 0) {
                    // this gets caught in the catch block
                    statusLabel.setText("<html>Error : <br>Only Positive Numbers & no Letters Please!<html>");
                    statusLabel.setForeground(Color.red);
                    throw new IllegalArgumentException("");
                }

                switch (s) {//check for a match
                    case "USD":
                        num1 = amount * (Double.parseDouble(USD[toIndex][1]));
                        num2 = (Double.parseDouble(USD[toIndex][2]));
                        break;
                    case "EUR":
                        num1 = amount * (Double.parseDouble(EUR[toIndex][1]));
                        num2 = (Double.parseDouble(EUR[toIndex][2]));
                        break;
                    case "GBP":
                        num1 = amount * (Double.parseDouble(GBP[toIndex][1]));
                        num2 = (Double.parseDouble(GBP[toIndex][2]));
                        break;
                    case "INR":
                        num1 = amount * (Double.parseDouble(INR[toIndex][1]));
                        num2 = (Double.parseDouble(INR[toIndex][2]));
                        break;
                    case "AUD":
                        num1 = amount * (Double.parseDouble(AUD[toIndex][1]));
                        num2 = (Double.parseDouble(AUD[toIndex][2]));
                        break;
                    case "CAD":
                        num1 = amount * (Double.parseDouble(CAD[toIndex][1]));
                        num2 = (Double.parseDouble(CAD[toIndex][2]));
                        break;
                    case "SGD":
                        num1 = amount * (Double.parseDouble(SGD[toIndex][1]));
                        num2 = (Double.parseDouble(SGD[toIndex][2]));
                        break;
                    default:
                        break;
                }

                String space = "&nbsp;&nbsp;&nbsp;&nbsp;";
                //output formatted results

                statusLabel.setText("<html><span style=\"color:Red\">RESULT:</span><br> Converted from <br>"
                        + space + "<span style=\"color:green\">" + String.format("%,.2f", amount) + "  " + fromCountry.getSelectedItem()
                        + "</span><br> <span style=\"color:blue\">To</span><br>"
                        + space + "<span style=\"color:green\">" + String.format("%,.2f", num1) + "  " + toCountry.getSelectedItem() + ",<br>"
                        + space + "INV. " + String.format("%,.2f", num2) + "  " + toCountry.getSelectedItem() + "</span><br>"
                        + "<html><span style=\"color:blue\"><i>DB update Time: " + timeUpadte + "</i> :</span><html>");
            } catch (NumberFormatException ex) {
                statusLabel.setText("<html>Error : <br>amount not Number<html>");
                statusLabel.setForeground(Color.red);
            }

        });

        reset.addActionListener((ActionEvent e) -> {
            fromCountry.setSelectedIndex(0);
            toCountry.removeAllItems();
            toCountry.addItem("");
            statusLabel.setText("<html>START :  <br>Enter Correct Inputs to start</html>");
            statusLabel.setForeground(Color.blue);
            amountTxt.setText("");
        });
        fromCountry.addActionListener((ActionEvent e) -> {
            try {
                String s = (String) fromCountry.getSelectedItem();//get the selected item
                toCountry.removeAllItems();
                switch (s) {//check for a match
                    case "USD":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(USD[l][0]);
                        }
                        toCountry.setSelectedIndex(0);

                        break;
                    case "EUR":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(EUR[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    case "GBP":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(GBP[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    case "INR":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(INR[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    case "AUD":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(AUD[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    case "CAD":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(CAD[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    case "SGD":
                        for (int l = 0; l < 53; l++) {
                            toCountry.addItem(SGD[l][0]);
                        }
                        toCountry.setSelectedIndex(0);
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException ext) {
                statusLabel.setText("<html>Oops! :  <br>wait DB to load</html>");
            }

        });

    }
}

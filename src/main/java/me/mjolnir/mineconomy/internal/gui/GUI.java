package me.mjolnir.mineconomy.internal.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bukkit.Bukkit;

import me.mjolnir.mineconomy.MineConomy;
import me.mjolnir.mineconomy.internal.Currency;
import me.mjolnir.mineconomy.internal.MCCom;
import me.mjolnir.mineconomy.internal.MCLang;
import me.mjolnir.mineconomy.internal.Settings;
import me.mjolnir.mineconomy.internal.gui.graphics.ImagePanel;
import me.mjolnir.mineconomy.internal.gui.listeners.AccountListListener;
import me.mjolnir.mineconomy.internal.gui.listeners.CFUListener;
import me.mjolnir.mineconomy.internal.gui.listeners.CreateListener;
import me.mjolnir.mineconomy.internal.gui.listeners.DeleteListener;
import me.mjolnir.mineconomy.internal.gui.listeners.EmptyListener;
import me.mjolnir.mineconomy.internal.gui.listeners.GiveListener;
import me.mjolnir.mineconomy.internal.gui.listeners.HelpListener;
import me.mjolnir.mineconomy.internal.gui.listeners.QuitListener;
import me.mjolnir.mineconomy.internal.gui.listeners.Refresh2Listener;
import me.mjolnir.mineconomy.internal.gui.listeners.RefreshListener;
import me.mjolnir.mineconomy.internal.gui.listeners.SetListener;
import me.mjolnir.mineconomy.internal.gui.listeners.TakeListener;
import me.mjolnir.mineconomy.internal.util.IOH;
import me.mjolnir.mineconomy.internal.util.Update;

/**
 * Handles MineConomy GUI.
 * 
 * @author MjolnirCommando
 */
@SuppressWarnings("javadoc")
public class GUI
{
    public static JFrame                    window;
    public static JPanel                    accounts;
    public static JPanel                    settings;
    public static JPanel                    banks;
    public static JTabbedPane               tabs;
    public static JPanel                    currencies;
    public static JPanel                    content;
    private static JPanel                   pane1;
    public static JLabel                    title;
    public static JComboBox                 accountList;
    public static ArrayList<String>         accountNames;
    public static Hashtable<String, Double> accountBalances;
    public static JLabel                    balance;
    public static boolean                   accountSelected;
    public static String                    selectedAccount;
    public static JButton                   cfubutton;
    public static JButton                   quitbutton;
    public static JButton                   refreshbutton;
    public static JTextField                amount;
    public static JTextField                newaccount;
    public static JButton                   givebutton;
    public static JButton                   takebutton;
    public static JButton                   helpbutton;
    public static JButton                   createbutton;
    public static JButton                   setbutton;
    public static JButton                   emptybutton;
    public static JButton                   deletebutton;
    public static JButton                   refreshbutton2;
    public static JTextArea                 logtext;
    
    private static final String             consoleversion = "4.0";

    /**
     * Creates new GUI object.
     */
    public GUI()
    {
        create();
    }

    private static void create()
    {
        accountSelected = false;

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addComponentListener(new ComponentListener()
        {
            public void componentResized(ComponentEvent e)
            {
                int width = window.getWidth();
                int height = window.getHeight();
                boolean resize = false;
                if (width < 850)
                {
                    resize = true;
                    width = 850;
                }
                if (height < 500)
                {
                    resize = true;
                    height = 500;
                }
                if (resize)
                {
                    window.setSize(width, height);
                }
            }

            public void componentMoved(ComponentEvent e)
            {
            }

            public void componentShown(ComponentEvent e)
            {
            }

            public void componentHidden(ComponentEvent e)
            {
            }
        });
        window.setTitle("MineConomy - GUI");

        tabs = new JTabbedPane();
        
        content = new JPanel();
        content.setLayout(new BorderLayout());
        
        window.setContentPane(content);
        
        createAccounts();
        createCurrencies();
        createBanks();
        createSettings();
        createLang();
        createLog();
        createInfo();

        JPanel pane3 = new JPanel();
        pane3.setLayout(new BorderLayout());

        JPanel pane3EastFlow = new JPanel();
        pane3EastFlow.setLayout(new FlowLayout());

        quitbutton = new JButton("Stop Server");
        quitbutton.addActionListener(new QuitListener());
        pane3EastFlow.add(quitbutton);

        refreshbutton = new JButton("Reload Server");
        refreshbutton.addActionListener(new RefreshListener());
        pane3EastFlow.add(refreshbutton);
        
        refreshbutton2 = new JButton("Reload MineConomy");
        refreshbutton2.addActionListener(new Refresh2Listener());
        pane3EastFlow.add(refreshbutton2);

        helpbutton = new JButton("Help");
        helpbutton.addActionListener(new HelpListener());
        pane3EastFlow.add(helpbutton);

        cfubutton = new JButton("Check For Updates...");
        cfubutton.addActionListener(new CFUListener());
        pane3EastFlow.add(cfubutton);

        pane3.add(pane3EastFlow, BorderLayout.EAST);

        JLabel label2 = new JLabel(
                "<html><span style=\"font-size:8px;\">&nbsp;[Console Version " + consoleversion + "]</span></html>");
        pane3.add(label2, BorderLayout.WEST);

        content.add(tabs, BorderLayout.CENTER);
        content.add(pane3, BorderLayout.SOUTH);

        // window.setSize(700, 400);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
    private static void createAccounts()
    {
        accounts = new JPanel();
        accounts.setLayout(new BorderLayout());
        JScrollPane accountsScroll = new JScrollPane(accounts);
        tabs.addTab("Accounts", accountsScroll);

        JPanel titleFlow = new JPanel();
        titleFlow.setLayout(new FlowLayout());

        title = new JLabel();

        if (Update.check())
        {
            title.setText("<html><center>Control Panel<br><span style=\"color:blue;\">Updates Available!</span><br><br></center></html>");
        }
        else
        {
            title.setText("<html><center>Control Panel<br><br><br></center></html>");
        }

        titleFlow.add(title);
        accounts.add(titleFlow, BorderLayout.NORTH);

        JPanel pane1Flow = new JPanel();
        pane1Flow.setLayout(new FlowLayout());

        pane1 = new JPanel();
        pane1.setLayout(new GridLayout(2, 1));

        pane1Flow.add(pane1);
        accounts.add(pane1Flow, BorderLayout.WEST);

        JLabel label1 = new JLabel(
                "<html><center>Please choose an account.</center></html>");
        pane1.add(label1);

        reloadAccounts(false);
        accountList.insertItemAt("Accounts ---", 0);
        accountList.setSelectedIndex(0);

        JPanel pane2Flow = new JPanel();
        pane2Flow.setLayout(new FlowLayout());

        JPanel pane2 = new JPanel();
        pane2.setLayout(new GridLayout(7, 1));

        balance = new JLabel("<html><center>Balance: -.-- ---"
                + "</center></html>");

        pane2.add(balance);
        pane2Flow.add(pane2);
        accounts.add(pane2Flow, BorderLayout.CENTER);

        amount = new JTextField(10);
        amount.setEnabled(accountSelected);
        pane2.add(amount);

        JPanel buttonFlow = new JPanel();
        buttonFlow.setLayout(new FlowLayout());
        pane2.add(buttonFlow);

        givebutton = new JButton("Give");
        givebutton.addActionListener(new GiveListener());
        givebutton.setEnabled(accountSelected);
        buttonFlow.add(givebutton);

        takebutton = new JButton("Take");
        takebutton.addActionListener(new TakeListener());
        takebutton.setEnabled(accountSelected);
        buttonFlow.add(takebutton);

        setbutton = new JButton("Set");
        setbutton.addActionListener(new SetListener());
        setbutton.setEnabled(accountSelected);
        buttonFlow.add(setbutton);

        emptybutton = new JButton("Empty");
        emptybutton.addActionListener(new EmptyListener());
        emptybutton.setEnabled(accountSelected);
        buttonFlow.add(emptybutton);

        deletebutton = new JButton("Delete");
        deletebutton.addActionListener(new DeleteListener());
        deletebutton.setEnabled(accountSelected);
        buttonFlow.add(deletebutton);

        JLabel label3 = new JLabel("");
        pane2.add(label3);

        newaccount = new JTextField(10);
        newaccount.setEnabled(true);
        pane2.add(newaccount);

        JPanel createpane = new JPanel();
        createpane.setLayout(new FlowLayout());
        createbutton = new JButton("Create Account");
        createbutton.setEnabled(true);
        createbutton.addActionListener(new CreateListener());
        createpane.add(createbutton);
        pane2.add(createpane);

        JPanel logopane = new JPanel();
        logopane.setLayout(new FlowLayout());
        ImagePanel logo = new ImagePanel("internal/gui/graphics/mineconomy.png", 225, 45);
        logo.setPreferredSize(new Dimension(225, 45));
        logopane.add(logo);
        pane2.add(logopane);
    }
    
    private static void createCurrencies()
    {
        currencies = new JPanel();

        currencies.setLayout(new BorderLayout());

        StringBuffer sb = new StringBuffer();

        try
        {
            Scanner in = new Scanner(new File(MineConomy.maindir
                    + "currencies.yml"));

            while (in.hasNextLine())
            {
                sb.append(in.nextLine() + "\n");
            }
            sb.replace(sb.length() - 1, sb.length(), "");

            in.close();
        }
        catch (FileNotFoundException e)
        {
            IOH.error("FileNotFoundException", e);
        }

        final JTextArea currencyText = new JTextArea(sb.toString());

        JScrollPane currencyScroll = new JScrollPane(currencyText);
        currencyScroll.setPreferredSize(new Dimension(750, 500));

        currencies.add(currencyScroll, BorderLayout.CENTER);

        JButton button = new JButton("Save");

        button.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                PrintWriter out;
                try
                {
                    out = new PrintWriter(new File(MineConomy.maindir
                            + "currencies.yml"));

                    out.print(currencyText.getText());

                    out.close();
                }
                catch (FileNotFoundException e)
                {
                    IOH.error("FileNotFoundException", e);
                }

                Currency.reload();
                
                JOptionPane.showMessageDialog(window, "Currency File has been saved.", "MineConomy - Save Complete", JOptionPane.PLAIN_MESSAGE);
            }

        });

        JPanel buttonflow = new JPanel();
        buttonflow.setLayout(new FlowLayout());
        buttonflow.add(button);

        currencies.add(buttonflow, BorderLayout.SOUTH);
        
        tabs.addTab("Currencies", currencies);
    }
    
    private static void createBanks()
    {
        banks = new JPanel();
        
        banks.setLayout(new BoxLayout(banks, BoxLayout.Y_AXIS));
        
        JPanel labelflow = new JPanel();
        labelflow.setLayout(new FlowLayout());
        labelflow.add(new JLabel("<html><span style=\"color:red;\">BANK GUI NOT YET IMPLEMENTED. TRY USING THE IN-GAME COMMANDS. SORRY FOR THE INCONVIENCE</span></html>"));
        
        banks.add(labelflow);
        
//        StringBuffer sb = new StringBuffer();
//        
//        try
//        {
//            Scanner in = new Scanner(new File(MineConomy.maindir + "banks.yml"));
//            
//            while(in.hasNextLine())
//            {
//                sb.append(in.nextLine() + "\n");
//            }
//            sb.replace(sb.length() - 1, sb.length(), "");
//            
//            in.close();
//        }
//        catch (FileNotFoundException e)
//        {
//            IOH.error("", e);
//        }
//        
//        final JTextArea bankEdit = new JTextArea(sb.toString());
//        
//        JScrollPane bankEditScroll = new JScrollPane(bankEdit);
//        bankEditScroll.setPreferredSize(new Dimension(750, 500));
//        
//        banks.add(bankEditScroll);
//        
//        JButton button = new JButton("Save");
//        
//        button.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent arg0)
//            {
//                PrintWriter out;
//                try
//                {
//                    out = new PrintWriter(new File(MineConomy.maindir + "banks.yml"));
//                    
//                    out.print(bankEdit.getText());
//                    
//                    out.close();
//                }
//                catch (FileNotFoundException e)
//                {
//                    IOH.error("", e);
//                }
//                
//                Banking.reload();
//            }
//            
//        });
//        
//        JPanel buttonflow = new JPanel();
//        buttonflow.setLayout(new FlowLayout());
//        buttonflow.add(button);
//        
//        banks.add(buttonflow);
//        
//        JScrollPane banksScroll = new JScrollPane(banks);
        
//        tabs.addTab("Banks", banksScroll); TODO
    }
    
    private static void createSettings()
    {
        settings = new JPanel();
        
        settings.setLayout(new FlowLayout());
        
        JPanel settingscontent = new JPanel();
        
        settingscontent.setLayout(new BoxLayout(settingscontent, BoxLayout.Y_AXIS));
        
        JPanel titleflow = new JPanel();
        titleflow.setLayout(new FlowLayout());
        
        titleflow.add(new JLabel("Settings"));
        
        settingscontent.add(titleflow);
        settingscontent.add(new JLabel(" "));
        
        final JPanel settings1 = new JPanel();
        settings1.setLayout(new BoxLayout(settings1, BoxLayout.X_AXIS));
        
        settings1.add(new JLabel("Starting Balance: "));
        
        double starting = 9999999.99D;
        
        if (Settings.startingBalance > starting)
        {
            starting = Settings.startingBalance;
        }
        
        final JSpinner startingField = new JSpinner(new SpinnerNumberModel(Settings.startingBalance,
                0.00,
                starting,
                1));
        
        settings1.add(startingField);
        
        settingscontent.add(settings1);
        
        JPanel settings2 = new JPanel();
        settings2.setLayout(new BoxLayout(settings2, BoxLayout.X_AXIS));
        
        settings2.add(new JLabel("Max Debt: "));
        
        double maxdebt = 9999999.99D;
        
        if (Settings.maxDebt > maxdebt)
        {
            maxdebt = Settings.maxDebt;
        }
        
        final JSpinner maxDebtField = new JSpinner(new SpinnerNumberModel(Settings.maxDebt,
                0.00,
                maxdebt,
                1));
        
        settings2.add(maxDebtField);
        
        settingscontent.add(settings2);
        
        settingscontent.add(new JLabel(" "));
        
        JPanel settings19 = new JPanel();
        settings19.setLayout(new BoxLayout(settings19, BoxLayout.X_AXIS));
        
        settings19.add(new JLabel("Display GUI: "));
        
        final JComboBox guiBox = new JComboBox(new String[] {"true", "false"});
        
        guiBox.setSelectedItem(Settings.gui + "");
        
        settings19.add(guiBox);
        
        settingscontent.add(settings19);
        
        JPanel settings20 = new JPanel();
        settings20.setLayout(new BoxLayout(settings20, BoxLayout.X_AXIS));
        
        settings20.add(new JLabel("Warn Ops: "));
        
        final JComboBox opsBox = new JComboBox(new String[] {"true", "false"});
        
        opsBox.setSelectedItem(Settings.ops + "");
        
        settings20.add(opsBox);
        
        settingscontent.add(settings20);
        
        JPanel settings10 = new JPanel();
        settings10.setLayout(new BoxLayout(settings10, BoxLayout.X_AXIS));
        
        settings10.add(new JLabel("Log Priority: "));
        
        final JLabel prilabel = new JLabel(Settings.logPriority + "");
        
        final JSlider prislide = new JSlider(0, 5, Settings.logPriority);
        prislide.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0)
            {
                prilabel.setText(prislide.getValue() + "");
            }
            
        });
        
        settings10.add(prislide);
        
        settings10.add(prilabel);
        
        settingscontent.add(settings10);
        
        settingscontent.add(new JLabel(" "));
        
        JPanel settings3 = new JPanel();
        settings3.setLayout(new BoxLayout(settings3, BoxLayout.X_AXIS));
        
        settings3.add(new JLabel("Interest Amount: "));
        
        double iamount = 9999999.99D;
        
        if (Settings.interestAmount > iamount)
        {
            iamount = Settings.interestAmount;
        }
        
        final JSpinner interestAmount = new JSpinner(new SpinnerNumberModel(Settings.interestAmount,
                0.00,
                iamount,
                1));
        
        settings3.add(interestAmount);
        
        settingscontent.add(settings3);
        
        JPanel settings4 = new JPanel();
        settings4.setLayout(new BoxLayout(settings4, BoxLayout.X_AXIS));
        
        settings4.add(new JLabel("Interest Interval (Seconds): "));
        
        double iinterval = 100000D;
        
        if (Settings.interestInterval > iinterval)
        {
            iinterval = Settings.interestInterval;
        }
        
        final JSpinner interestInterval = new JSpinner(new SpinnerNumberModel(Settings.interestInterval,
                0,
                iinterval,
                1));
        
        settings4.add(interestInterval);
        
        settingscontent.add(settings4);
        
        JPanel settings5 = new JPanel();
        
        settings5.setLayout(new BoxLayout(settings5, BoxLayout.X_AXIS));
        
        settings5.add(new JLabel("Interest Mode: "));
        
        String[] comboModel = {"None", "Fixed", "Percent"};
        
        final JComboBox interestMode = new JComboBox(comboModel);
        
        String mode = Settings.interestMode;
        
        if (mode.equalsIgnoreCase("Fixed"))
        {
            interestMode.setSelectedIndex(1);
        }
        else if (mode.equalsIgnoreCase("Percent"))
        {
            interestMode.setSelectedIndex(2);
        }
        else
        {
            interestMode.setSelectedIndex(0);
        }
        
        settings5.add(interestMode);
        
        settingscontent.add(settings5);
        
        settingscontent.add(new JLabel(" "));
        
        JPanel settings6 = new JPanel();
        settings6.setLayout(new BoxLayout(settings6, BoxLayout.X_AXIS));
        
        settings6.add(new JLabel("Tax Amount: "));
        
        double tamount = 9999999.99D;
        
        if (Settings.taxAmount > tamount)
        {
            tamount = Settings.taxAmount;
        }
        
        final JSpinner taxAmount = new JSpinner(new SpinnerNumberModel(Settings.taxAmount,
                0.00,
                tamount,
                1));
        
        settings6.add(taxAmount);
        
        settingscontent.add(settings6);
        
        JPanel settings7 = new JPanel();
        settings7.setLayout(new BoxLayout(settings7, BoxLayout.X_AXIS));
        
        settings7.add(new JLabel("Tax Interval (Seconds): "));
        
        double tinterval = 100000D;
        
        if (Settings.taxInterval > tinterval)
        {
            tinterval = Settings.taxInterval;
        }
        
        final JSpinner taxInterval = new JSpinner(new SpinnerNumberModel(Settings.taxInterval,
                0,
                tinterval,
                1));
        
        settings7.add(taxInterval);
        
        settingscontent.add(settings7);
        
        JPanel settings8 = new JPanel();
        
        settings8.setLayout(new BoxLayout(settings8, BoxLayout.X_AXIS));
        
        settings8.add(new JLabel("Tax Mode: "));
        
        final JComboBox taxMode = new JComboBox(comboModel);
        
        mode = Settings.taxMode;
        
        if (mode.equalsIgnoreCase("Fixed"))
        {
            taxMode.setSelectedIndex(1);
        }
        else if (mode.equalsIgnoreCase("Percent"))
        {
            taxMode.setSelectedIndex(2);
        }
        else
        {
            taxMode.setSelectedIndex(0);
        }
        
        settings8.add(taxMode);
        
        settingscontent.add(settings8);
        
        settingscontent.add(new JLabel(" "));
        
        JPanel settings9 = new JPanel();
        
        settings9.setLayout(new BoxLayout(settings9, BoxLayout.X_AXIS));
        
        settings9.add(new JLabel("Language: "));
        
        final JTextField lang = new JTextField(Settings.lang, 10);
        
        settings9.add(lang);
        
        settingscontent.add(settings9);
        
        JPanel settings11 = new JPanel();
        
        settings11.setLayout(new BoxLayout(settings11, BoxLayout.X_AXIS));
        
        settings11.add(new JLabel("Auto-Save Interval (Seconds): "));
        
        double ainterval = 100000D;
        
        if (Settings.autosaveInterval > ainterval)
        {
            ainterval = Settings.autosaveInterval;
        }
        
        final JSpinner autosaveInterval = new JSpinner(new SpinnerNumberModel(Settings.autosaveInterval,
                0,
                ainterval,
                1));
        
        settings11.add(autosaveInterval);
        
        settingscontent.add(settings11);
        
        JPanel settings12 = new JPanel();
        
        settings12.add(new JLabel("iConomy Compatibility Mode: "));
        
        settings12.setLayout(new BoxLayout(settings12, BoxLayout.X_AXIS));
        
        final JComboBox compatBox = new JComboBox(new String[] {"true", "false"});
        
        compatBox.setSelectedItem(Settings.iconomy + "");
        
        settings12.add(compatBox);
        
        settingscontent.add(settings12);
        
        JPanel settings13 = new JPanel();
        
        settings13.add(new JLabel("Migration Mode: "));
        
        settings13.setLayout(new BoxLayout(settings13, BoxLayout.X_AXIS));
        
        final JComboBox migrateBox = new JComboBox(new String[] {"none", "iconomy", "mysql"});
        
        migrateBox.setSelectedItem(Settings.migrate + "");
        
        settings13.add(migrateBox);
        
        settingscontent.add(settings13);
        
        settingscontent.add(new JLabel(" "));
        
        settingscontent.add(new JLabel("Database:"));
        
        JPanel settings14 = new JPanel();
        
        settings14.setLayout(new BoxLayout(settings14, BoxLayout.X_AXIS));
        
        settings14.add(new JLabel("URL: "));
        
        final JTextField urlField = new JTextField(Settings.dburl, 10);
        
        settings14.add(urlField);
        
        settingscontent.add(settings14);
        
        JPanel settings15 = new JPanel();
        
        settings15.setLayout(new BoxLayout(settings15, BoxLayout.X_AXIS));
        
        settings15.add(new JLabel("Name: "));
        
        final JTextField nameField = new JTextField(Settings.dbname, 10);
        
        settings15.add(nameField);
        
        settingscontent.add(settings15);
        
        JPanel settings16 = new JPanel();
        
        settings16.setLayout(new BoxLayout(settings16, BoxLayout.X_AXIS));
        
        settings16.add(new JLabel("Username: "));
        
        final JTextField userField = new JTextField(Settings.dbuser, 10);
        
        settings16.add(userField);
        
        settingscontent.add(settings16);
        
        JPanel settings17 = new JPanel();
        
        settings17.setLayout(new BoxLayout(settings17, BoxLayout.X_AXIS));
        
        settings17.add(new JLabel("Password: "));
        
        final JTextField passField = new JTextField(Settings.dbpass, 10);
        
        settings17.add(passField);
        
        settingscontent.add(settings17);
        
        JPanel settings18 = new JPanel();
        
        settings18.setLayout(new BoxLayout(settings18, BoxLayout.X_AXIS));
        
        settings18.add(new JLabel("Type: "));
        
        final JComboBox typeField = new JComboBox(new String[] {"none", "mysql"});
        
        typeField.setSelectedItem(Settings.dbtype);
        
        settings18.add(typeField);
        
        settingscontent.add(settings18);
        
        settingscontent.add(new JLabel(" "));
        settingscontent.add(new JLabel(" "));
        
        JPanel buttonflow = new JPanel();
        buttonflow.setLayout(new FlowLayout());
        
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                Settings.startingBalance = Double.parseDouble(startingField.getValue() + "");
                
                Settings.maxDebt = Double.parseDouble(maxDebtField.getValue() + "");
                
                Settings.interestAmount = Double.parseDouble(interestAmount.getValue() + "");
                Settings.interestInterval = Integer.parseInt((interestInterval.getValue() + "").replace(".", "-").split("-")[0]);
                Settings.interestMode = interestMode.getSelectedItem().toString();
                
                Settings.taxAmount = Double.parseDouble(taxAmount.getValue() + "");
                Settings.taxInterval = Integer.parseInt((taxInterval.getValue() + "").replace(".", "-").split("-")[0]);
                Settings.taxMode = taxMode.getSelectedItem().toString();
                
                Settings.lang = lang.getText();
                
                Settings.logPriority = prislide.getValue();
                Settings.gui = Boolean.parseBoolean((String) guiBox.getSelectedItem());
                Settings.ops = Boolean.parseBoolean((String) opsBox.getSelectedItem());
                
                Settings.autosaveInterval = Integer.parseInt((autosaveInterval.getValue() + "").replace(".", "-").split("-")[0]);
                Settings.iconomy = Boolean.parseBoolean((String) compatBox.getSelectedItem());
                Settings.migrate = (String) migrateBox.getSelectedItem();
                
                Settings.dburl = urlField.getText();
                Settings.dbname = nameField.getText();
                Settings.dbuser = userField.getText();
                Settings.dbpass = passField.getText();
                Settings.dbtype = (String) typeField.getSelectedItem();
                
                Settings.save();
                
                JOptionPane.showMessageDialog(window, "Configuration File has been saved.", "MineConomy - Save Complete", JOptionPane.PLAIN_MESSAGE);
            }
            
        });
        buttonflow.add(save);
        
        settingscontent.add(buttonflow);
        
        settingscontent.add(new JLabel(" "));
        
        JPanel labelflow = new JPanel();
        labelflow.setLayout(new FlowLayout());
        labelflow.add(new JLabel("<html><span style=\"color:red;\">Some Changes May Require Server Reload.</span></html>"));
        
        settingscontent.add(labelflow);
        
        settings.add(settingscontent);
        
        JScrollPane settingsScroll = new JScrollPane(settings);
        
        settingsScroll.setPreferredSize(new Dimension(750, 500));
        
        tabs.addTab("Settings", settingsScroll);
    }
    
    private static void createLang()
    {
        JPanel lang = new JPanel();
        lang.setLayout(new BorderLayout());
        
        final JTextArea langtext = new JTextArea();
        
        try
        {
            Scanner in = new Scanner(MCLang.langFile);
            
            while (in.hasNextLine())
            {
                langtext.append(in.nextLine() + "\n");
            }
        }
        catch (FileNotFoundException e)
        {
            IOH.error("FileNotFoundException", e);
        }
        
        JScrollPane langscroll = new JScrollPane(langtext);
        langscroll.setPreferredSize(new Dimension(750, 500));
        lang.add(langscroll, BorderLayout.CENTER);
        
        JPanel langflow = new JPanel();
        langflow.setLayout(new FlowLayout());
        
        JButton savelang = new JButton("Save");
        
        savelang.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    PrintWriter out = new PrintWriter(MCLang.langFile);
                    out.print(langtext.getText());
                    out.close();
                }
                catch (FileNotFoundException e)
                {
                    IOH.error("FileNotFoundException", e);
                }
                
                MCLang.reload();
                
                JOptionPane.showMessageDialog(window, "Language File has been saved.", "MineConomy - Save Complete", JOptionPane.PLAIN_MESSAGE);
            }
            
        });
        
        langflow.add(savelang);
        
        lang.add(langflow, BorderLayout.SOUTH);
        
        tabs.addTab("Language", lang);
    }
    
    private static void createLog()
    {
        IOH.gui = true;
        JPanel log = new JPanel();
        log.setLayout(new BorderLayout());
        
        logtext = new JTextArea();
        logtext.setFont(new Font("monospace", Font.PLAIN, 12));
        logtext.setLineWrap(false);
        logtext.setEditable(false);
        IOH.updateGUILog();
        
        JScrollPane logscroll = new JScrollPane(logtext);
        logscroll.setPreferredSize(new Dimension(750, 500));
        
        log.add(logscroll, BorderLayout.CENTER);
        
        JPanel bflow1 = new JPanel();
        bflow1.setLayout(new FlowLayout());
        
        JButton clearlog = new JButton("Clear Log");
        clearlog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                IOH.clearLog();
            }
            
        });
        bflow1.add(clearlog);
        
        log.add(bflow1, BorderLayout.SOUTH);
        
        tabs.addTab("Log", log);
    }
    
    private static void createInfo()
    {
        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        
        JPanel vinfo = new JPanel();
        vinfo.setLayout(new GridLayout(7, 2));
        
        vinfo.add(new JLabel("MineConomy Version:"));
        vinfo.add(new JLabel(MineConomy.getVersion()));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel("Bukkit Version:"));
        vinfo.add(new JLabel(Bukkit.getBukkitVersion()));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel("Bukkit Build Version:"));
        vinfo.add(new JLabel(MineConomy.bukkitVersion));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel(""));
        vinfo.add(new JLabel("GUI Version:"));
        vinfo.add(new JLabel(consoleversion));
        
        JPanel infocontain = new JPanel();
        infocontain.setLayout(new BoxLayout(infocontain, BoxLayout.Y_AXIS));
        
        infocontain.add(vinfo);
        
        JPanel changeflow = new JPanel();
        changeflow.setLayout(new FlowLayout());
        
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(
                Settings.class.getClassLoader().getResourceAsStream(
                        "change_log.txt"))));
        StringBuffer sb = new StringBuffer();
        sb.append("<html><br><br><br>=== Change Log ===<br><br>");
        while (in.hasNextLine())
        {
            sb.append(in.nextLine() + "<br>");
        }
        sb.append("</html>");
        changeflow.add(new JLabel(sb.toString()));
        
        infocontain.add(changeflow);
        
        JScrollPane infoscroll = new JScrollPane(infocontain);
        infoscroll.setPreferredSize(new Dimension(750, 500));
        info.add(infoscroll, BorderLayout.CENTER);
        
        JPanel infobuttonflow = new JPanel();
        JButton ticket = new JButton("File a Support Ticket");
        ticket.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://dev.bukkit.org/server-mods/mineconomy/create-ticket/"));
                }
                catch (IOException e)
                {
                    IOH.error("IOException", e);
                }
                catch (URISyntaxException e)
                {
                    IOH.error("URISyntaxException", e);
                }
            }
            
        });
        infobuttonflow.add(ticket);
        info.add(infobuttonflow, BorderLayout.SOUTH);
        
        tabs.addTab("Info", info);
    }
    
    public static void error(String text)
    {
        final JFrame error = new JFrame("MineConomy - Error Report");
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel messageFlow = new JPanel();
        messageFlow.setLayout(new FlowLayout());
        JLabel errorlabel = new JLabel("<html><center>MineConomy has encountered this error.<br>"
                + "A special team of code monkeys has been dispatched.<br>"
                + "If you see them, show them this error trace.<br><br>"
                + "Or you can report it at <a href=\"http://dev.bukkit.org/server-mods/mineconomy\">http://dev.bukkit.org/server-mods/mineconomy</a>.</center></html>");
        errorlabel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://dev.bukkit.org/server-mods/mineconomy"));
                }
                catch (IOException e)
                {
                    IOH.error("IOException", e);
                }
                catch (URISyntaxException e)
                {
                    IOH.error("URISyntaxException", e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
        });
        messageFlow.add(errorlabel);
        panel.add(messageFlow);
        
        JTextArea pane = new JTextArea();
        
        pane.setText(text);
        pane.setLineWrap(false);
        pane.setEditable(false);
        
        JScrollPane paneScroll = new JScrollPane(pane);
        paneScroll.setPreferredSize(new Dimension(500, 300));
        
        panel.add(paneScroll);
        
        JPanel buttonFlow = new JPanel();
        buttonFlow.setLayout(new FlowLayout());
        
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                error.setVisible(false);
                error.dispose();
            }
            
        });
        buttonFlow.add(close);
        
        panel.add(buttonFlow);
        
        JButton report = new JButton("Report");
        report.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://dev.bukkit.org/server-mods/mineconomy/create-ticket/"));
                }
                catch (IOException e)
                {
                    IOH.error("IOException", e);
                }
                catch (URISyntaxException e)
                {
                    IOH.error("URISyntaxException", e);
                }
            }
            
        });
        buttonFlow.add(report);
        
        pane.select(0, 0);
        
        error.add(panel);
        error.pack();
        error.setLocationRelativeTo(window);
        error.setVisible(true);
    }
    
    public static void reloadAccounts(boolean update)
    {
        MCCom.getAccounting().save();
        
        int j = 0;
        
        try
        {
            j = accountList.getSelectedIndex();
            pane1.remove(accountList);
        }
        catch (NullPointerException e)
        {
            j = 0;
        }
        
        accountBalances = new Hashtable<String, Double>();
        
        accountNames = MCCom.getAccounts();
        for (int i = 0; accountNames.size() > i; i++)
        {
            accountBalances.put(accountNames.get(i),
                    MCCom.getBalance(accountNames.get(i)));
        }

        accountList = new JComboBox(accountNames.toArray());
        try
        {
            accountList.setSelectedIndex(j);
        }
        catch (IllegalArgumentException e)
        {
            try
            {
                accountList.setSelectedIndex(j - 1);
            }
            catch (IllegalArgumentException e1)
            {
                accountList.insertItemAt("Accounts ---", 0);
                accountList.setSelectedIndex(0);
            }
        }
        
        if (update)
        {
            JFrame oldWindow = window;
            new GUI();
            oldWindow.setVisible(false);
        }
        
        accountList.addActionListener(new AccountListListener());
        
        pane1.add(accountList);
        
        accountList.repaint();
    }
}

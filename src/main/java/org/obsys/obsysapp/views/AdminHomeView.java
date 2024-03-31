package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.AdminHomeModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminHomeView extends ViewBuilder implements IObsysBuilder{
    private final AdminHomeModel adminModel;
    private ArrayList<String> accounts = new ArrayList<>();
    private AnchorPane adminWindow;

    public AdminHomeView(AdminHomeModel adminModel) {
        this.adminModel = adminModel;
    }

    @Override
    public AnchorPane build() {
        adminWindow = super.build();

        adminWindow.getChildren().addAll(createPanels());
        adminWindow.getChildren().addAll(loadImages());
        adminWindow.getChildren().addAll(createLabels());
        adminWindow.getChildren().addAll(createButtons());
        adminWindow.getChildren().addAll(createTextFields());

        // TODO tie acct info into account selection handler
        // Should be able to move these into the event handler for account selection
        // I do not know if page will auto refresh
        adminWindow.getChildren().addAll(createAcctSummary());
        adminWindow.getChildren().add(buildHistoryPane());

        return adminWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>(){{
            add(obsysPanel(10, 75, 200, 465));
            add(obsysPanel(220, 75, 480, 465));
            add(obsysPanel(710, 75, 200, 465));
        }};
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>(List.of(obsysImage("dolphinLogoTan.png", 20, 380, 190, 160)));
    }

    @Override
    public ArrayList<Node> createLabels() {
        Label lblDateTime = obsysLabel("Month day, year - hh:mm AM", 30, 5);
        // TODO bind text to admin model

        Label lblWelcome = obsysLabel("Welcome <username>!", 30, 20, "sub-header");
        // TODO bind text to admin model

        Label lblSearchError = obsysLabel("", 20, 325, 190, "panel-warning");
        // TODO bind text to admin model

        return new ArrayList<>(List.of(lblDateTime, lblWelcome, lblSearchError)){{
            add(obsysLabel("Account Lookup", 25, 80, "header4"));
            add(obsysLabel("Action Panel", 740, 80, "header4"));
            add(obsysLabel("By Account", 25, 110, "hint"));
            add(obsysLabel("By Name", 25, 180, "hint"));
            add(obsysLabel("Account Actions", 740, 110, "hint"));
            add(obsysLabel("Transactions", 740, 230, "hint"));
            add(obsysLabel("Loan Payments", 740, 390, "hint"));
            add(obsysLabel("Customer Information", 240, 90));
        }};
    }

    private ArrayList<Node> createAcctSummary() {
        // TODO bind switch reference to model
        return switch ("LN") {
            case "LN" -> loanSummary();
            case "CH" -> checkingSummary();
            default -> savingsSummary();
        };
    }

    private ArrayList<Node> loanSummary() {
        // TODO bind values to model
        LocalDate opened = LocalDate.now();
        LocalDate maturity = LocalDate.now();
        double inrRate = 0;
        int payments = 0;
        double loanAmt = 100000.0;
        double principal = 0;
        double interest = 0;

        String loanDetails = String.format("%tD\n%tD\n%.2f\n%d\n%15s", opened, maturity, inrRate, payments, "");
        String loanBalances = String.format("$%,.2f\n$%,.2f\n$%,.2f\n%20s", loanAmt, principal, interest, "");

        String fields = "Opened:\nMaturity:\nAPR:\nRemain Pymts:";
        Label lblFields = obsysLabel(fields, 230, 300, "right-aligned");

        String balances = "Loan:\nPrincipal:\nInterest:";
        Label lblBalances = obsysLabel(balances, 475, 300, "right-aligned");

        return new ArrayList<>(List.of(lblFields, lblBalances)){{
            add(obsysLabel(loanDetails, 360, 300, "right-aligned"));
            add(obsysLabel(loanBalances, 570, 300, "right-aligned"));
        }};
    }

    private ArrayList<Node> checkingSummary() {
        String fields = "Posted:\nPend Debits:\nPend Credits:\nAvailable:";
        Label lblFields = obsysLabel(fields, 450, 300, 250, "right-aligned");

        // TODO bind amounts to model
        double posted = 1000000;
        double debits = 0;
        double credits = 0;
        double available = 0;
        String amounts = String.format("$%,.2f\n$%,.2f\n$%,.2f\n$%,.2f\n%30s", posted, debits, credits, available, "");

        Label lblAmounts = obsysLabel(amounts, 530, 300, 150, "right-aligned");

        return new ArrayList<>(List.of(lblFields, lblAmounts));
    }

    private ArrayList<Node> savingsSummary() {
        String fields = "Opened:\nInt Rate:\nInt Paid:";
        Label lblFields = obsysLabel(fields, 235, 300, "right-aligned");

        // TODO bind data to model
        LocalDate opened = LocalDate.now();
        double intRate = 0;
        double intPaid = 0;
        String summary = String.format("%tD\n%.2f\n$%,.2f\n%15s", opened, intRate, intPaid, "");

        Label lblSummary = obsysLabel(summary, 330, 300, "right-aligned");

        return new ArrayList<>(List.of(lblFields, lblSummary)){{
            addAll(checkingSummary());
        }};
    }

    private Node buildHistoryPane() {
        ScrollPane scrollPane = obsysScrollPane(230, 390, 460, 145);
        scrollPane.getStyleClass().add("history");
        scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(5), BorderWidths.DEFAULT)));

        VBox historyBox = new VBox();
        historyBox.setPrefWidth(440);
        scrollPane.setContent(historyBox);

        // TODO bind condition to account type
        historyBox.getChildren().addAll(false ? loanHistory() : accountHistory());

        return scrollPane;
    }

    private ArrayList<Node> loanHistory() {
        String headerScheduled = String.format("%-30s %-10s", "Date", "Amount");
        String headerPosted = String.format("%-10s %-12s %-12s %-12s", "Date", "Amount", "Principal", "Interest");

        // TODO get transactions from model
        ArrayList<String> testTransactions = new ArrayList<>() {{
            add("Transaction 1");
            add("Transaction 2");
            add("Transaction 3");
            add("Transaction 4");
        }};
        return new ArrayList<>() {{
            add(obsysLabel("Scheduled Payments", 0, 0));
            add(obsysLabel(headerScheduled, 0, 0, "table-small"));
            for (String s : testTransactions) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }

            add(obsysLabel("Posted Payments", 0, 0));
            add(obsysLabel(headerPosted, 0, 0, "table-small"));
            for (String s : testTransactions) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }
        }};
    }

    private ArrayList<Node> accountHistory() {
        String header = String.format("%-9s %-20s %9s %9s \n", "Date", "Description", "Credit", "Debit");

        // TODO get transactions from model
        ArrayList<String> testTransactions = new ArrayList<>() {{
            add("Transaction 1");
            add("Transaction 2");
            add("Transaction 3");
            add("Transaction 4");
        }};

        return new ArrayList<>() {{
            add(obsysLabel("Pending Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table-small"));
            for (String s : testTransactions) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }

            add(obsysLabel("\nPosted Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table-small"));
            for (String s : testTransactions) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        // TODO add logout event handler

        Hyperlink hypClear = obsysLink("Clear", 650, 54);
        // TODO add clear form handler

        Button btnSearch = obsysButton("Search", 105, 285, 100);
        // TODO add search handler

        Button btnOpenAcct = obsysButton("Open Account", 725, 125, 170);
        // TODO add open account handler

        Button btnCloseAcct = obsysButton("Close Account", 725, 165, 170);
        // TODO add close account handler

        Button btnDeposit = obsysButton("Deposit Funds", 725, 245, 170);
        // TODO add deposit handler

        Button btnWithdraw = obsysButton("Withdraw Funds", 725, 285, 170);
        // TODO add withdraw handler

        Button btnTransfer = obsysButton("Transfer Funds", 725, 325, 170);
        // TODO add transfer handler

        Button btnPayment = obsysButton("Make a Payment", 725, 405, 170);

        return new ArrayList<>(List.of(hypLogout, hypClear, btnSearch, btnOpenAcct, btnCloseAcct, btnDeposit,
                btnWithdraw, btnTransfer, btnPayment));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return new ArrayList<>(){{
            addAll(createLookupFields());
            addAll(createInfoFields());
        }};
    }

    private ArrayList<Node> createLookupFields() {
        TextField txtAcctNum = obsysTextField(15, 120, 190);
        Label lblAcctNum = obsysLabel("ACCOUNT NUMBER", 35, 122, "hint");
        // TODO bind to admin model

        TextField txtLastName = obsysTextField(15, 190, 190);
        Label lblLastName = obsysLabel("LAST NAME", 35, 192, "hint");
        // TODO bind to admin model

        TextField txtFirstName = obsysTextField(15, 235, 190);
        Label lblFirstName = obsysLabel("FIRST NAME", 35, 237, "hint");
        // TODO bind to admin model

        return new ArrayList<>(List.of(txtAcctNum, lblAcctNum, txtLastName, lblLastName, txtFirstName, lblFirstName));
    }

    private ArrayList<Node> createInfoFields() {
        TextField txtFirstName = obsysTextField(230, 115, 220);
        txtFirstName.setDisable(true);
        Label lblFirstName = obsysLabel("FIRST NAME", 250, 117, "hint");
        // TODO bind text to model

        TextField txtLastName = obsysTextField(230, 160, 220);
        txtLastName.setDisable(true);
        Label lblLastName = obsysLabel("LAST NAME", 250, 162, "hint");
        // TODO bind text to model

        TextField txtAddress = obsysTextField(465, 115, 220);
        txtAddress.setDisable(true);
        txtAddress.setPrefHeight(88);
        Label lblAddress = obsysLabel("ADDRESS", 485, 117, "hint");
        // TODO bind text to model

        TextField txtPhone = obsysTextField(465, 205, 220);
        txtPhone.setDisable(true);
        Label lblPhone = obsysLabel("PHONE", 485, 207, "hint");
        // TODO bind text to model

        TextField txtEmail = obsysTextField(465, 250, 220);
        txtEmail.setDisable(true);
        Label lblEmail = obsysLabel("EMAIL", 485, 252, "hint");
        // TODO bind text to model

        TextField txtStatus = obsysTextField(350, 250, 100);
        txtStatus.setDisable(true);
        Label lblStatus = obsysLabel("STATUS", 370, 252, "hint");
        // TODO bind text to model

        return new ArrayList<>(List.of(txtFirstName, lblFirstName, txtLastName, lblLastName, txtAddress, lblAddress,
                txtPhone, lblPhone, txtEmail, lblEmail, txtStatus, lblStatus)){{
            addAll(createAccountCombo());
        }};
    }

    private ArrayList<Node> createAccountCombo() {
        ComboBox<String> cmbAccount = obsysStringCombo(accounts, 232, 207, 218);
        Label lblAccount = obsysLabel("ACCOUNT", 252, 209, "hint");
        // TODO populate account list
        // TODO add account selection handler

        return new ArrayList<>(List.of(cmbAccount, lblAccount));
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }
}

package org.obsys.obsysapp.views;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.AdminHomeModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminHomeView extends ViewBuilder implements IObsysBuilder {
    private final AdminHomeModel adminModel;
    private AccountModel accountModel = new AccountModel();
    private final Runnable logoutHandler;
    private final Runnable clearFormHandler;
    private final Runnable accountSelectionHandler;
    private final Runnable searchNameHandler;
    private final Runnable openAccountHandler;
    private final Runnable closeAccountHandler;
    private final Runnable transactionHandler;
    private final Runnable searchAcctHandler;
    private AnchorPane adminWindow;

    public AdminHomeView(AdminHomeModel adminModel, Runnable logoutHandler,
                         Runnable clearFormHandler, Runnable accountSelectionHandler, Runnable searchNameHandler,
                         Runnable openAccountHandler, Runnable closeAccountHandler, Runnable transactionHandler,
                         Runnable searchAcctHandler) {
        this.adminModel = adminModel;
        this.logoutHandler = logoutHandler;
        this.clearFormHandler = clearFormHandler;
        this.accountSelectionHandler = accountSelectionHandler;
        this.searchNameHandler = searchNameHandler;
        this.searchAcctHandler = searchAcctHandler;
        this.openAccountHandler = openAccountHandler;
        this.closeAccountHandler = closeAccountHandler;
        this.transactionHandler = transactionHandler;
    }

    @Override
    public AnchorPane build() {
        adminWindow = super.build();

        adminWindow.getChildren().addAll(createPanels());
        adminWindow.getChildren().addAll(loadImages());
        adminWindow.getChildren().addAll(createLabels());
        adminWindow.getChildren().addAll(createButtons());
        adminWindow.getChildren().addAll(createTextFields());

        return adminWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>() {{
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
        Label lblDateTime = obsysLabel("Month day, year - H:m AM", 30, 5);
        lblDateTime.textProperty().bind(adminModel.dateTimeProperty());

        Label lblWelcome = obsysLabel("Welcome <username>!", 30, 20, "sub-header");
        lblWelcome.textProperty().bind(adminModel.usernameProperty());

        Label lblSearchError = obsysLabel("", 20, 365, 190, "panel-warning");
        lblSearchError.textProperty().bind(adminModel.searchErrorProperty());

        return new ArrayList<>(List.of(lblDateTime, lblWelcome, lblSearchError)) {{
            add(obsysLabel("Account Lookup", 25, 80, "header4"));
            add(obsysLabel("Action Panel", 740, 80, "header4"));
            add(obsysLabel("By Account", 25, 110, "hint"));
            add(obsysLabel("By Name", 25, 220, "hint"));
            add(obsysLabel("Account Actions", 740, 110, "hint"));
            add(obsysLabel("Transactions", 740, 230, "hint"));
            add(obsysLabel("Loan Payments", 740, 390, "hint"));
            add(obsysLabel("Customer Information", 240, 90));
        }};
    }

    private ArrayList<Node> createAcctSummary() {
        return switch (accountModel.getType()) {
            case "LN" -> loanSummary();
            case "CH" -> checkingSummary();
            case "IC", "SV" -> savingsSummary();
            default -> checkingSummary();
        };
    }

    private ArrayList<Node> loanSummary() {
        Node coverPanel = obsysPanel(230, 300, 460, 90);

        LocalDate opened = accountModel.getDateOpened();
        LocalDate maturity = accountModel.getMaturityDate();
        double intRate = accountModel.getInterestRate();
        int payments = accountModel.getTerm();
        double loanAmt = accountModel.getLoanAmt();
        double principal = accountModel.getPrincipalPaid();
        double interest = accountModel.getInterestPaid();

        String loanDetails = String.format("%tD\n%tD\n%.2f\n%d\n%15s", opened, maturity, intRate, payments, "");
        String loanBalances = String.format("$%,.2f\n$%,.2f\n$%,.2f\n%20s", loanAmt, principal, interest, "");

        String fields = "Opened:\nMaturity:\nAPR:\nRemain Pmts:";
        Label lblFields = obsysLabel(fields, 230, 300, "right-aligned");

        String balances = "Loan:\nPrincipal:\nInterest:";
        Label lblBalances = obsysLabel(balances, 475, 300, "right-aligned");

        return new ArrayList<>(List.of(coverPanel, lblFields, lblBalances)) {{
            add(obsysLabel(loanDetails, 360, 300, "right-aligned"));
            add(obsysLabel(loanBalances, 570, 300, "right-aligned"));
        }};
    }

    private ArrayList<Node> checkingSummary() {
        Node coverPanel = obsysPanel(230, 300, 460, 90);

        String fields = "Posted:\nPend Debits:\nPend Credits:\nAvailable:";
        Label lblFields = obsysLabel(fields, 450, 300, 250, "right-aligned");

        String posted = accountModel.getPostedBalance();
        String debits = accountModel.getPendingDebits();
        String credits = accountModel.getPendingCredits();
        double available = accountModel.getBalance();
        String amounts = String.format("%s\n%s\n%s\n$%,.2f\n%30s", posted, debits, credits, available, "");

        Label lblAmounts = obsysLabel(amounts, 530, 300, 150, "right-aligned");

        return new ArrayList<>(List.of(coverPanel, lblFields, lblAmounts));
    }

    private ArrayList<Node> savingsSummary() {
        String fields = "Opened:\nInt Rate:\nInt Paid:";
        Label lblFields = obsysLabel(fields, 235, 300, "right-aligned");

        LocalDate opened = accountModel.getDateOpened();
        double intRate = accountModel.getInterestRate();
        double intReceived = accountModel.getInterestReceived();
        String summary = String.format("%tD\n%.2f\n$%,.2f\n%15s", opened, intRate, intReceived, "");

        Label lblSummary = obsysLabel(summary, 330, 300, "right-aligned");

        return new ArrayList<>() {{
            addAll(checkingSummary());
            add(lblFields);
            add(lblSummary);
        }};
    }

    private Node buildHistoryPane() {
        ScrollPane scrollPane = obsysScrollPane(230, 390, 460, 145);
        scrollPane.getStyleClass().add("history");
        if (accountModel.getType().isEmpty()) {
            return scrollPane;
        }

        scrollPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                BorderWidths.DEFAULT)));

        VBox historyBox = new VBox();
        historyBox.setPrefWidth(440);
        scrollPane.setContent(historyBox);

        historyBox.getChildren().addAll(accountModel.getType().equals("LN") ?
                loanHistory() : accountHistory());

        return scrollPane;
    }

    private ArrayList<Node> loanHistory() {
        String headerScheduled = String.format("%-30s %-10s", "Date", "Amount");
        String headerPosted = String.format("%-10s %-12s %-12s %-12s",
                "Date", "Amount", "Principal", "Interest");

        return new ArrayList<>() {{
            add(obsysLabel("Scheduled Payments", 0, 0));
            add(obsysLabel(headerScheduled, 0, 0, "table-small"));
            for (String s : accountModel.getScheduledLoanPayments()) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }

            add(obsysLabel("Posted Payments", 0, 0));
            add(obsysLabel(headerPosted, 0, 0, "table-small"));
            for (String s : accountModel.getPostedLoanPayments()) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }
        }};
    }

    private ArrayList<Node> accountHistory() {
        String header = String.format("%-9s %-20s %9s %9s \n",
                "Date", "Description", "Credit", "Debit");

        return new ArrayList<>() {{
            add(obsysLabel("Pending Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table-small"));
            for (String s : accountModel.getPendingTransactions()) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }

            add(obsysLabel("\nPosted Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table-small"));
            for (String s : accountModel.getPostedTransactions()) {
                add(obsysLabel(s, 0, 0, "table-small"));
            }
        }};
    }

    @Override
    public ArrayList<Node> createButtons() {
        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Hyperlink hypClear = obsysLink("Clear", 650, 54);
        hypClear.setOnAction(evt -> {
            clearFormHandler.run();
            accountModel = new AccountModel();

            adminWindow.getChildren().addAll(createAcctSummary());
            adminWindow.getChildren().add(buildHistoryPane());
        });

        Button btnSearchName = obsysButton("Search", 105, 320, 100);
        btnSearchName.setOnAction(evt -> {
            searchNameHandler.run();
            createAccountCombo();
        });

        Button btnSearchByAcct = obsysButton("Search", 105, 165, 100);
        btnSearchByAcct.setOnAction(evt -> {
            searchAcctHandler.run();
            createAccountCombo();
        });

        Button btnOpenAcct = obsysButton("Open Account", 725, 125, 170);
        btnOpenAcct.setOnAction(evt -> openAccountHandler.run());

        Button btnCloseAcct = obsysButton("Close Account", 725, 165, 170);
        btnCloseAcct.disableProperty().bind(
                adminModel.actionPanelDisabledProperty());
        btnCloseAcct.setOnAction(evt -> closeAccountHandler.run());

        Button btnDeposit = obsysButton("Deposit Funds", 725, 245, 170);
        btnDeposit.disableProperty().bind(
                adminModel.actionPanelDisabledProperty());
        btnDeposit.setOnAction(evt -> {
            accountModel.setTransactionType("DP");
            transactionHandler.run();
        });

        Button btnWithdraw = obsysButton("Withdraw Funds", 725, 285, 170);
        btnWithdraw.disableProperty().bind(
                adminModel.actionPanelDisabledProperty());
        btnWithdraw.setOnAction(evt -> {
            accountModel.setTransactionType("WD");
            transactionHandler.run();
        });

        Button btnTransfer = obsysButton("Transfer Funds", 725, 325, 170);
        btnTransfer.disableProperty().bind(
                adminModel.actionPanelDisabledProperty());
        btnTransfer.setOnAction(evt -> {
            accountModel.setTransactionType("TF");
            transactionHandler.run();
        });

        Button btnPayment = obsysButton("Make a Payment", 725, 405, 170);
        btnPayment.disableProperty().bind(
                adminModel.actionPanelDisabledProperty());
        btnPayment.setOnAction(evt -> {
            accountModel.setTransactionType("PY");
            transactionHandler.run();
        });

        return new ArrayList<>(List.of(
                hypLogout,
                hypClear,
                btnSearchName,
                btnOpenAcct,
                btnCloseAcct,
                btnDeposit,
                btnWithdraw,
                btnTransfer,
                btnPayment,
                btnSearchByAcct));
    }

    @Override
    public ArrayList<Node> createTextFields() {
        return new ArrayList<>() {{
            addAll(createLookupFields());
            addAll(createInfoFields());
        }};
    }

    private ArrayList<Node> createLookupFields() {
        TextField txtAcctNum = obsysTextField(15, 120, 190);
        Label lblAcctNum = obsysLabel("ACCOUNT NUMBER", 35, 122, "hint");
        txtAcctNum.textProperty().bindBidirectional(
                adminModel.accountNumberProperty());

        TextField txtLastName = obsysTextField(15, 230, 190);
        Label lblLastName = obsysLabel("LAST NAME", 35, 232, "hint");
        txtLastName.textProperty().bindBidirectional(
                adminModel.searchLastNameProperty());

        TextField txtFirstName = obsysTextField(15, 275, 190);
        Label lblFirstName = obsysLabel("FIRST NAME", 35, 277, "hint");
        txtFirstName.textProperty().bindBidirectional(
                adminModel.searchFirstNameProperty());

        return new ArrayList<>(List.of(
                txtAcctNum,
                lblAcctNum,
                txtLastName,
                lblLastName,
                txtFirstName,
                lblFirstName));
    }

    private ArrayList<Node> createInfoFields() {
        TextField txtFirstName = obsysTextField(230, 115, 220);
        txtFirstName.setDisable(true);
        Label lblFirstName = obsysLabel("FIRST NAME", 250, 117, "hint");
        txtFirstName.textProperty().bind(adminModel.foundFirstNameProperty());

        TextField txtLastName = obsysTextField(230, 160, 220);
        txtLastName.setDisable(true);
        Label lblLastName = obsysLabel("LAST NAME", 250, 162, "hint");
        txtLastName.textProperty().bind(adminModel.foundLastNameProperty());

        TextArea txtAddress = obsysTextArea(465, 115, 220);
        txtAddress.setDisable(true);
        txtAddress.setPrefHeight(88);
        Label lblAddress = obsysLabel("ADDRESS", 485, 117, "hint");
        txtAddress.textProperty().bind(adminModel.addressProperty());

        TextField txtPhone = obsysTextField(465, 205, 220);
        txtPhone.setDisable(true);
        Label lblPhone = obsysLabel("PHONE", 485, 207, "hint");
        txtPhone.textProperty().bind(adminModel.phoneProperty());

        TextField txtEmail = obsysTextField(230, 250, 455);
        txtEmail.setDisable(true);
        Label lblEmail = obsysLabel("EMAIL", 250, 252, "hint");
        txtEmail.textProperty().bind(adminModel.emailProperty());

        return new ArrayList<>(List.of(
                txtFirstName,
                lblFirstName,
                txtLastName,
                lblLastName,
                txtAddress,
                lblAddress,
                txtPhone,
                lblPhone,
                txtEmail,
                lblEmail)) {{
            addAll(createAccountCombo());
        }};
    }

    private ArrayList<Node> createAccountCombo() {
        ComboBox<String> cmbAccount = obsysStringCombo(
                new ArrayList<>(), 230, 206, 218);
        cmbAccount.setItems(adminModel.getAcctDescriptions());
        Label lblAccount = obsysLabel("ACCOUNT", 250, 208, "hint");
        cmbAccount.setOnAction(evt -> {
            adminModel.setSelectedAccountDescription(cmbAccount.getValue());
            accountSelectionHandler.run();
            accountModel = adminModel.getAccountModel();

            adminWindow.getChildren().addAll(createAcctSummary());
            adminWindow.getChildren().add(buildHistoryPane());
        });

        return new ArrayList<>(List.of(cmbAccount, lblAccount));
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }
}

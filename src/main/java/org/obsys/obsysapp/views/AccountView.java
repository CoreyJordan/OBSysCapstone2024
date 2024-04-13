package org.obsys.obsysapp.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.obsys.obsysapp.models.AccountModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountView extends ViewBuilder implements IObsysBuilder {
    private final AccountModel acctModel;
    private final Runnable returnHandler;
    private final Runnable logoutHandler;
    private final Runnable transactionHandler;
    private final Runnable statementHandler;

    public AccountView(AccountModel acctModel,
                       Runnable returnHandler,
                       Runnable logoutHandler,
                       Runnable transactionHandler,
                       Runnable statementHandler) {
        this.acctModel = acctModel;
        this.returnHandler = returnHandler;
        this.logoutHandler = logoutHandler;
        this.transactionHandler = transactionHandler;
        this.statementHandler = statementHandler;
    }

    @Override
    public AnchorPane build() {
        AnchorPane acctWindow = super.build();
        acctWindow.getChildren().addAll(createPanels());
        acctWindow.getChildren().addAll(loadImages());
        acctWindow.getChildren().addAll(createLabels());
        acctWindow.getChildren().addAll(createButtons());

        return acctWindow;
    }

    @Override
    public ArrayList<Rectangle> createPanels() {
        return new ArrayList<>() {{
            add(obsysPanel(20, 210, 580, 105));
            add(obsysPanel(20, 325, 580, 215));
            add(obsysPanel(610, 210, 290, 330));
        }};
    }

    @Override
    public ArrayList<Node> loadImages() {
        return new ArrayList<>() {{
            add(obsysImage("dolphinLogoBlue.png", 660, 5, 250, 205));
        }};
    }

    @Override
    public ArrayList<Node> createLabels() {
        return new ArrayList<>() {{
            addAll(createAccountOverviewLabels());
            addAll(createSummaryLabels());
            add(createHistoryLabels());
            add(obsysLabel("Action Panel", 660, 215, "header3"));
        }};
    }

    private Node createHistoryLabels() {
        ScrollPane scrollPane = obsysScrollPane(25, 330, 570, 205);
        scrollPane.getStyleClass().add("history");
        VBox pane = addPane();
        scrollPane.setContent(pane);

        if (acctModel.getType().equals("LN")) {
            pane.getChildren().addAll(loanHistoryLabels());
        } else {
            pane.getChildren().addAll(historyLabels());
        }
        return scrollPane;
    }

    private VBox addPane() {
        VBox pane = new VBox();
        pane.setPrefWidth(555);
        return pane;
    }

    private ArrayList<Node> loanHistoryLabels() {
        String headerScheduled = String.format("%-30s %-10s",
                "Date", "Amount");
        String headerPosted = String.format("%-10s %-12s %-12s %-12s",
                "Date", "Amount", "Principal", "Interest");
        return new ArrayList<>() {{
            add(obsysLabel("Scheduled Payments", 0, 0));
            add(obsysLabel(headerScheduled, 0, 0, "table"));
            for (String s : acctModel.getScheduledLoanPayments()) {
                add(obsysLabel(s, 0, 0, "table"));
            }

            add(obsysLabel("Posted Payments", 0, 0));
            add(obsysLabel(headerPosted, 0, 0, "table"));
            for (String s : acctModel.getPostedLoanPayments()) {
                add(obsysLabel(s, 0, 0, "table"));
            }
        }};
    }

    private ArrayList<Node> historyLabels() {
        String header = String.format("%-9s %-20s %9s %9s \n",
                "Date", "Description", "Credit", "Debit");

        return new ArrayList<>() {{
            add(obsysLabel("Pending Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table"));
            for (String s : acctModel.getPendingTransactions()) {
                add(obsysLabel(s, 0, 0, "table"));
            }

            add(obsysLabel("\nPosted Transactions", 0, 0));
            add(obsysLabel(header, 0, 0, "table"));
            for (String s : acctModel.getPostedTransactions()) {
                add(obsysLabel(s, 0, 0, "table"));
            }
        }};
    }

    private ArrayList<Node> createSummaryLabels() {
        return switch (acctModel.getType()) {
            case "CH" -> balanceSummaryLabels();
            case "SV", "IC" -> savingsSummaryLabels();
            case "LN" -> loanSummaryLabels();
            default -> null;
        };
    }

    private ArrayList<Node> loanSummaryLabels() {
        return new ArrayList<>() {{
            addAll(loanBalanceSummary());
            addAll(loanDetailsSummary());
            addAll(paymentDetailsSummary());
        }};
    }

    private ArrayList<Node> paymentDetailsSummary() {
        Label lblFields = obsysLabel("Amount Due:\nDue Date:", 645, 380,
                "right-aligned");

        Label lblAmtDue = obsysLabel("$###.##", 780, 380);
        lblAmtDue.textProperty().bind(acctModel.installmentProperty());

        Label lblDueDate = obsysLabel("MM/dd/yy", 780, 402.5);
        lblDueDate.textProperty().bind(acctModel.dueDateProperty());

        return new ArrayList<>(List.of(lblFields, lblAmtDue, lblDueDate));
    }

    private ArrayList<Node> loanDetailsSummary() {
        String fields = "Open Date:\nMaturity Date:\nAPR:\nRemaining Payments:";

        Label lblOpened = obsysLabel("MM/dd/yy", 220, 220);
        lblOpened.textProperty().bind(acctModel.dateOpenedProperty());

        Label lblMature = obsysLabel("MM/dd/yy", 220, 242.5);
        lblMature.textProperty().bind(acctModel.maturityProperty());

        Label lblRate = obsysLabel("##.#%", 220, 265);
        lblRate.textProperty().bind(acctModel.interestRateProperty());

        Label lblPaymentsLeft = obsysLabel("###", 220, 287.5);
        lblPaymentsLeft.textProperty().bind(
                acctModel.remainingPaymentsCountProperty());
        return new ArrayList<>(List.of(
                lblOpened,
                lblMature,
                lblRate,
                lblPaymentsLeft)) {{
            add(obsysLabel(fields, 25, 220, "right-aligned"));
        }};
    }

    private ArrayList<Node> loanBalanceSummary() {
        Label lblAmount = obsysLabel("$###,###.##", 480, 230);
        lblAmount.textProperty().bind(acctModel.loanAmountProperty());

        Label lblPrincipal = obsysLabel("$###,###.##", 480, 252.5);
        lblPrincipal.textProperty().bind(acctModel.paidPrincipalProperty());

        Label lblInterest = obsysLabel("$###,###.##", 480, 275);
        lblInterest.textProperty().bind(acctModel.paidInterestProperty());
        return new ArrayList<>(List.of(lblAmount, lblPrincipal, lblInterest)) {{
            add(obsysLabel("Loan Amount:\nPrincipal Paid:\nInterest Paid:",
                    350, 230, "right-aligned"));
        }};
    }

    private ArrayList<Node> savingsSummaryLabels() {
        String fields = "Opened:\nInterest Rate:\nInterest to Date:";
        Label lblFields = obsysLabel(fields, 25, 220, "right-aligned");

        Label lblOpened = obsysLabel("mm/dd/yy", 190, 220);
        lblOpened.textProperty().bind(acctModel.dateOpenedProperty());

        Label lblRate = obsysLabel("00%", 190, 242.5);
        lblRate.textProperty().bind(acctModel.interestRateProperty());

        Label lblPaid = obsysLabel("$###.##", 190, 265);
        lblPaid.textProperty().bind(acctModel.interestPaymentsProperty());

        return new ArrayList<>(balanceSummaryLabels()) {{
            addAll(List.of(lblFields, lblOpened, lblRate, lblPaid));
        }};
    }

    private ArrayList<Node> balanceSummaryLabels() {
        String fields = """
                Posted Balance:
                Pending Debits:
                Pending Credits:
                Available Balance:""";
        Label lblFields = obsysLabel(fields, 300, 220, 250, "right-aligned");

        Label lblPosted = obsysLabel("", 500, 220, 150, "right-aligned");
        lblPosted.textProperty().bind(acctModel.postedBalanceProperty());

        Label lblDebits = obsysLabel("", 500, 242.5, 150, "right-aligned");
        lblDebits.textProperty().bind(acctModel.pendingDebitsProperty());

        Label lblCredits = obsysLabel("", 500, 265, 150, "right-aligned");
        lblCredits.textProperty().bind(acctModel.pendingCreditsProperty());

        Label lblAvailable = obsysLabel("", 500, 287.5, 150, "right-aligned");
        lblAvailable.textProperty().bind(acctModel.balanceProperty());

        return new ArrayList<>(List.of(
                lblFields,
                lblPosted,
                lblDebits,
                lblCredits,
                lblAvailable));
    }

    private ArrayList<Node> createAccountOverviewLabels() {
        Label lblAcctName = obsysLabel("AcctType", 40, 80, "sub-header");
        lblAcctName.textProperty().bind(acctModel.typeProperty());

        Label lblAcctNum = obsysLabel("....0000", 155, 135);
        lblAcctNum.textProperty().bind(acctModel.acctNumProperty());

        Label lblBalance = obsysLabel("Amount of Balance", 360, 120, 175);
        lblBalance.textProperty().bind(acctModel.balanceProperty());
        lblBalance.setAlignment(Pos.CENTER_RIGHT);

        Label lblBalanceType = obsysLabel("TYPE", 360, 140, 175, "hint");
        lblBalanceType.textProperty().bind(acctModel.balanceTypeProperty());
        lblBalanceType.setAlignment(Pos.CENTER_RIGHT);

        Label lblStatus = obsysLabel("", 295, 100, "warning");
        lblStatus.textProperty().bind(acctModel.statusProperty());

        return new ArrayList<>(List.of(
                lblAcctName,
                lblAcctNum,
                lblBalance,
                lblBalanceType,
                lblStatus));
    }

    @Override
    public ArrayList<Node> createButtons() {
        Image imgBack = new Image("back.png");
        Button btnBack = obsysButton("Home", 10, 10, 100, imgBack);
        btnBack.setOnAction(evt -> returnHandler.run());

        Hyperlink hypLogout = obsysLink("Logout", 830, 5);
        hypLogout.setOnAction(evt -> logoutHandler.run());

        Button btnPay = obsysButton("Make a Payment", 655, 300, 200);
        btnPay.setOnAction(evt -> {
            acctModel.setTransactionType("PY");
            transactionHandler.run();
        });

        Button btnDeposit = obsysButton("Deposit Funds", 655, 300, 200);
        btnDeposit.setOnAction(evt -> {
            acctModel.setTransactionType("DP");
            transactionHandler.run();
        });

        Button btnWithdraw = obsysButton("Withdraw Funds", 655, 350, 200);
        btnWithdraw.setOnAction(evt -> {
            acctModel.setTransactionType("WD");
            transactionHandler.run();
        });

        Button btnTransfer = obsysButton("Transfer Funds", 655, 400, 200);
        btnTransfer.setOnAction(evt -> {
            acctModel.setTransactionType("TF");
            transactionHandler.run();
        });

        return new ArrayList<>(List.of(btnBack, hypLogout)) {{
            addAll(acctModel.getType().equals("LN") ? List.of(btnPay) : List.of(
                    btnDeposit,
                    btnWithdraw,
                    btnTransfer));
            addAll(createComboBox());
        }};
    }

    private ArrayList<Node> createComboBox() {
        // Account page sufficiently displays payment history, rather than
        // design a loan statement, we just escape.
        if (acctModel.getType().equals("LN")) {
            return new ArrayList<>();
        }

        ComboBox<LocalDate> cmbMonths = this.obsysDateCombo(
                acctModel.getMonths(), 655, 475, 200);
        cmbMonths.setOnAction(evt -> {
            acctModel.setSelectedMonth(cmbMonths.getValue());
            statementHandler.run();
        });

        Label lblStatement = obsysLabel("STATEMENTS", 675, 477, "hint");

        return new ArrayList<>(List.of(cmbMonths, lblStatement));
    }


    // No text fields / passwords used on the accounts pages.
    @Override
    public ArrayList<Node> createTextFields() {
        return null;
    }

    @Override
    public ArrayList<Node> buildPasswordField() {
        return null;
    }
}

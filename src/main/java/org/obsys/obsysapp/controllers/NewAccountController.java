package org.obsys.obsysapp.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.AccountDAO;
import org.obsys.obsysapp.data.ObsysDbConnection;
import org.obsys.obsysapp.data.PersonDAO;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.NewAccountModel;
import org.obsys.obsysapp.views.NewAccountView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class NewAccountController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private final Scene adminHome;
    private NewAccountModel newAccountModel;

    public NewAccountController(Stage stage, NewAccountModel newAccountModel,
                                Scene adminHome) {
        this.stage = stage;
        this.newAccountModel = newAccountModel;
        this.viewBuilder = new NewAccountView(
                newAccountModel,
                this::logout,
                this::clearForm,
                this::goHome,
                this::toggleNewCustomerPanel,
                this::registerCustomer,
                this::openAcct,
                this::selectAccountType);
        this.adminHome = adminHome;
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        newAccountModel = null;

        LoginController loginController = new LoginController(
                stage,
                "dolphinExit.png",
                "Thank you!"
        );
        stage.setScene(new Scene(loginController.getView()));
    }

    private void clearForm() {
        for (StringProperty prop : newAccountModel.getTextFields()) {
            prop.set("");
        }
        for (ObjectProperty<String> prop : newAccountModel.getComboBoxes()) {
            prop.set("");
        }
        newAccountModel.setErrorMessage("");
    }

    private void goHome() {
        stage.setScene(adminHome);
    }

    private void toggleNewCustomerPanel() {
        newAccountModel.setNewCustomerFieldsDisabled(
                !newAccountModel.isNewCustomerFieldsDisabled());
    }

    private void registerCustomer() {
        if (!validateNewCustomer()) {
            return;
        }

        Person newCustomer = new Person(
                0,
                newAccountModel.getNewLastName(),
                newAccountModel.getNewFirstName(),
                newAccountModel.getNewAddress(),
                newAccountModel.getNewCity(),
                newAccountModel.getNewState(),
                newAccountModel.getNewPostal(),
                newAccountModel.getNewPhone(),
                newAccountModel.getNewEmail());

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            // Current search criteria is first and last name. This could
            // overlook duplicate entries.
            if (new PersonDAO().readPersonByFullName(
                    conn,
                    newCustomer.getFirstName(),
                    newCustomer.getLastName()) != null) {
                newAccountModel.setErrorMessage(
                        "This person is already in the database");
                return;
            }

            int personId = new PersonDAO().insertNewPerson(conn, newCustomer);
            newAccountModel.setPersonId(personId);

            newAccountModel.setFirstName(newCustomer.getFirstName());
            newAccountModel.setLastName(newAccountModel.getNewLastName());
            newAccountModel.setAddress(
                    newCustomer.getStreetAddress(),
                    newCustomer.getCity(),
                    newAccountModel.getNewState(),
                    newCustomer.getPostalCode());
            newAccountModel.setPhone(newCustomer.getPhone());
            newAccountModel.setEmail(newCustomer.getEmail());

            newAccountModel.setNewCustomerFieldsDisabled(true);

        } catch (SQLException e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }

    private void selectAccountType() {
        newAccountModel.setIntRateFieldVisible(false);
        newAccountModel.setLoanFieldsVisible(false);
        newAccountModel.setBalanceType("STARTING BALANCE");

        if (newAccountModel.getAcctType().equals("Checking +") ||
                newAccountModel.getAcctType().equals("Savings")) {
            newAccountModel.setIntRateFieldVisible(true);
        } else if (newAccountModel.getAcctType().equals("Loan")) {
            newAccountModel.setLoanFieldsVisible(true);
            newAccountModel.setIntRateFieldVisible(true);
            newAccountModel.setBalanceType("LOAN AMOUNT");
        }
    }

    private void openAcct() {
        newAccountModel.setErrorMessage("");

        if (!validateNewAccount()) {
            return;
        }

        try (Connection conn = ObsysDbConnection.openDBConn()) {
            AccountDAO acctDao = new AccountDAO();

            Account newAcct = new Account(
                    setType(newAccountModel.getAcctType()),
                    0,
                    "OP",
                    Double.parseDouble(newAccountModel.getBalance()),
                    LocalDate.now(),
                    0,
                    0
            );
            newAcct.setIntRate(Double.parseDouble(newAccountModel.getIntRate()));
            newAcct.setPersonId(newAccountModel.getPersonId());

            int accountNum = acctDao.createAccount(conn, newAcct);
            newAccountModel.setAcctNum(String.valueOf(accountNum));

            if (newAccountModel.getAcctType().equals("Loan")) {
                newAcct.setTerm(Integer.parseInt(newAccountModel.getTerm()));
                newAcct.setPaymentAmt(calculateLoanPayment());
                newAcct.setAcctNum(accountNum);

                if (acctDao.createLoanAccount(conn, newAcct) == 1) {
                    newAccountModel.setPaymentAmt(
                            String.format("$%,.2f", calculateLoanPayment()));
                }

            }
        } catch (SQLException e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }

    private String setType(String selectedType) {
        return switch (selectedType) {
            case "Savings" -> "SV";
            case "Checking +" -> "IC";
            case "Loan" -> "LN";
            default -> "CH";
        };
    }

    private double calculateLoanPayment() {
        double principal = Double.parseDouble(newAccountModel.getBalance());
        double rate = Double.parseDouble(newAccountModel.getIntRate()) / 100;
        int term = Integer.parseInt(newAccountModel.getTerm());
        double formulaRate = Math.pow((1 + (rate / 12.0)), term);
        return principal / ((formulaRate - 1) / ((rate / 12) * formulaRate));
    }

    private boolean validateNewCustomer() {
        newAccountModel.setErrorMessage("");

        if (newAccountModel.getNewFirstName().isEmpty() ||
                newAccountModel.getNewLastName().isEmpty() ||
                newAccountModel.getNewAddress().isEmpty() ||
                newAccountModel.getNewCity().isEmpty() ||
                newAccountModel.getNewState().isEmpty() ||
                newAccountModel.getNewPostal().isEmpty() ||
                newAccountModel.getNewPhone().isEmpty() ||
                newAccountModel.getNewEmail().isEmpty()) {
            newAccountModel.setErrorMessage(
                    "All new customer fields are required");
            return false;
        }

        return true;
    }

    private boolean validateNewAccount() {
        if (newAccountModel.getAcctType().isEmpty()) {
            newAccountModel.setErrorMessage("Select an account type");
            return false;
        }

        if (newAccountModel.getBalance().isEmpty()) {
            newAccountModel.setErrorMessage("Enter an amount");
            return false;
        }

        if ((newAccountModel.getAcctType().equals("Checking +") ||
                newAccountModel.getAcctType().equals("Loan") ||
                newAccountModel.getAcctType().equals("Savings")) &&
                newAccountModel.getIntRate().isEmpty()) {
            newAccountModel.setErrorMessage("Enter an interest rate");
            return false;

        }

        if (newAccountModel.getAcctType().equals("Loan") &&
                newAccountModel.getTerm().isEmpty()) {
            newAccountModel.setErrorMessage("Select a term value");
            return false;
        }

        return true;
    }
}

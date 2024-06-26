package org.obsys.obsysapp.controllers;

import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.obsys.obsysapp.data.*;
import org.obsys.obsysapp.domain.Account;
import org.obsys.obsysapp.domain.Login;
import org.obsys.obsysapp.domain.Payee;
import org.obsys.obsysapp.domain.Person;
import org.obsys.obsysapp.models.AccountModel;
import org.obsys.obsysapp.models.AdminHomeModel;
import org.obsys.obsysapp.models.NewAccountModel;
import org.obsys.obsysapp.models.TransactionModel;
import org.obsys.obsysapp.utils.AccountValidator;
import org.obsys.obsysapp.views.AdminHomeView;
import org.obsys.obsysapp.views.ViewBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminHomeController {
    private final Stage stage;
    private final ViewBuilder viewBuilder;
    private AdminHomeModel adminModel;
    private AccountModel accountModel;
    private final Login login;
    private Person customer;

    public AdminHomeController(Stage stage,
                               AdminHomeModel adminModel,
                               Login login) {
        this.stage = stage;
        this.adminModel = adminModel;
        this.login = login;
        viewBuilder = new AdminHomeView(adminModel,
                this::logout,
                this::clearForm,
                this::selectAccount,
                this::searchByName,
                this::openAccount,
                this::closeAccount,
                this::goToTransactions,
                this::searchByAcct);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void logout() {
        adminModel = null;

        LoginController loginCtrl = new LoginController(
                stage,
                "dolphinExit.png",
                "Thank you!"
        );
        stage.setScene(new Scene(loginCtrl.getView()));
    }

    private void clearForm() {
        for (StringProperty prop : adminModel.getTextFields()) {
            prop.set("");
        }
        adminModel.setSearchError("");
        adminModel.clearAcctDescriptions();
        adminModel.setSelectedAccount(0);
        adminModel.setActionPanelDisabled(true);
    }

    private void selectAccount() {
        try (Connection conn = ObsysDbConnection.openDBConn()){
            accountModel = new AccountDAO().readFullAccountDetails(
                    conn, adminModel.getSelectedAccount());
            adminModel.setAccountModel(accountModel);

            accountModel.setHistory(new TransactionDAO().readTransactionHistory(
                    conn, adminModel.getSelectedAccount()));
            if (accountModel.getAcctNum() != 0) {
                adminModel.setActionPanelDisabled(false);
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

    private void searchByName() {
        adminModel.setAccountNumber("");
        String firstName = adminModel.getSearchFirstName();
        String lastName = adminModel.getSearchLastName();
        AccountValidator nameValidator = new AccountValidator(
                "1111111111", firstName, lastName);

        if (!nameValidator.okToSearch()) {
            adminModel.setSearchError("Invalid name entry");
            return;
        }

        adminModel.setSearchError("");
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            customer = new PersonDAO().readPersonByFullName(
                    conn, firstName, lastName);

            if (customer.getPhone().isEmpty()) {
                adminModel.setSearchError("Person not found");
                return;
            }

            adminModel.setPerson(customer);
            adminModel.setAccounts(new PayeeDAO().readAccountsByPersonId(
                    conn, customer.getPersonId()));

        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }

    private void searchByAcct() {
        adminModel.setSearchFirstName("");
        adminModel.setSearchLastName("");
        String searchNum = adminModel.getAccountNumber();
        AccountValidator nameValidator = new AccountValidator(
                searchNum,
                "firstName",
                "lastName");

        if (!nameValidator.okToSearch()) {
            adminModel.setSearchError("Invalid account number");
            return;
        }

        int accountNum = Integer.parseInt(searchNum);
        adminModel.setSearchError("");
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            int personId = new AccountDAO().readPersonIdByAccountNum(
                    conn, accountNum);
            if (personId == 0) {
                adminModel.setSearchError("Person not found");
                return;
            }

            customer = new PersonDAO().readPersonByPersonId(conn, personId);
            if (customer.getPhone().isEmpty()) {
                adminModel.setSearchError("Person not found");
                return;
            }

            adminModel.setPerson(customer);
            adminModel.setAccounts(new PayeeDAO().readAccountsByPersonId(
                    conn, personId));
            adminModel.getAccountDescriptions();

        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }

    private void openAccount() {
        Scene returnScene = new Scene(this.getView());
        NewAccountController newAcctCtrl;
        if (adminModel.getFoundPersonId() == 0) {
            newAcctCtrl = new NewAccountController(
                    stage,
                    new NewAccountModel(),
                    returnScene
            );
        } else {
            newAcctCtrl = new NewAccountController(
                    stage,
                    new NewAccountModel(customer),
                    returnScene
            );
        }
        stage.setScene((new Scene(newAcctCtrl.getView())));

    }

    private void closeAccount() {
        adminModel.setPasswordError("");

        if (adminModel.getSelectedAccount() == 0) {
            return;
        }

        if (adminModel.getPassword().isEmpty()) {
            adminModel.setPasswordError("Enter password");
            return;
        }
        // TODO code close account handler
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            Login checkedLogin = new LoginDAO().readPasswordByUsername(
                    conn, adminModel.getUsername());

            if (!adminModel.getPassword().equals(checkedLogin.getPassword())) {
                adminModel.setPasswordError("Password does not match");
                return;
            }

            if (new AccountDAO().updateAccountStatus(
                    conn,
                    adminModel.getSelectedAccount(),
                    "CL") == 1) {
                adminModel.setPasswordError("Account CLOSED");
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

    private void goToTransactions() {
        try (Connection conn = ObsysDbConnection.openDBConn()) {
            PayeeDAO payeeDao = new PayeeDAO();

            ArrayList<Payee> payees = switch (
                    accountModel.getTransactionType()) {
                case "TF", "PY" ->
                        payeeDao.readAccountsByPersonId(
                                conn,
                                adminModel.getFoundPersonId(),
                                accountModel.getAcctNum());
                default -> payeeDao.readPayeesByAccount(
                        conn, accountModel.getAcctNum());
            };

            Account account = new Account(
                    accountModel.getType(),
                    accountModel.getAcctNum(),
                    accountModel.getStatus(),
                    accountModel.getBalance(),
                    accountModel.getDateOpened(),
                    accountModel.getInstallment(),
                    accountModel.getInterestDue());

            TransactionModel transactionModel = new TransactionModel(
                    accountModel.getTransactionType(), account, payees
            );

            TransactionController transactCtrl = new TransactionController(
                    stage,
                    this.getView(),
                    transactionModel,
                    login
            );
            stage.setScene(new Scene(transactCtrl.getView()));
        } catch (Exception e) {
            ErrorController eCtrl = new ErrorController(
                    stage,
                    e.getMessage(),
                    this.getView()
            );
            stage.setScene(new Scene(eCtrl.getView()));
        }
    }
}

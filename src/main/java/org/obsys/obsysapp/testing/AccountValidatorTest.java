package org.obsys.obsysapp.testing;

import org.junit.Before;
import org.junit.Test;
import org.obsys.obsysapp.utils.AccountValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountValidatorTest {
    @Test
    public void validDataPasses() {
        AccountValidator valid = new AccountValidator("1234567890", "John", "Smith");
        assertTrue(valid.okToSearch());
    }

    @Test
    public void accountNumberTooShortFails() {
        AccountValidator shortAcctNum = new AccountValidator("123456789", "John", "Smith");
        assertFalse(shortAcctNum.okToSearch());
    }

    @Test
    public void accountNumberTooLongFails() {
        AccountValidator longAcctNum = new AccountValidator("12345678901", "John", "Smith");
        assertFalse(longAcctNum.okToSearch());
    }

    @Test
    public void nonIntegerAccountNumFails() {
        AccountValidator decimal = new AccountValidator("123.567890", "John", "Smith");
        assertFalse(decimal.okToSearch(), "Decimal number accepted.");
    }

    @Test
    public void whiteSpaceNameFails() {
        AccountValidator whitespace = new AccountValidator("1234567890", "Jo hn", "Smith");
        assertFalse(whitespace.okToSearch());
    }

    @Test
    public void symbolInNameFails() {
        AccountValidator symbol = new AccountValidator("1234567890", "Joh&n", "Smith");
        AccountValidator period = new AccountValidator("1234567890", "John", "Smi.th");
        assertFalse(symbol.okToSearch());
        assertFalse(period.okToSearch());
    }

    @Test
    public void numberInNameFails() {
        AccountValidator number = new AccountValidator("1234567890", "John", "Sm5ith");
        assertFalse(number.okToSearch());
    }

    @Test
    public void emptyNameFails() {
        AccountValidator empty = new AccountValidator("1234567890", "", "Smith");
        assertFalse(empty.okToSearch());
    }

}

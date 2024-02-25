package org.obsys.obsysapp.testing;

import org.junit.Test;
import org.obsys.obsysapp.utils.LoginValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginValidatorTest {
    @Test
    public void validPasswordPasses() {
        LoginValidator validator = new LoginValidator("", "Adm!n1");
        assertTrue(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsIfEmpty() {
        LoginValidator validator = new LoginValidator("", "");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsWithoutSymbol() {
        LoginValidator validator = new LoginValidator("", "Admin1");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsWithoutUpper() {
        LoginValidator validator = new LoginValidator("", "adm!n1");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsWithoutLower() {
        LoginValidator validator = new LoginValidator("", "ADM!N1");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsWithoutNumber() {
        LoginValidator validator = new LoginValidator("", "Adm!nn");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void passwordFailsWithWhitespace() {
        LoginValidator validator = new LoginValidator("", " Adm!n1");
        assertFalse(validator.passwordIsValid());

        validator = new LoginValidator("", "Adm !n1");
        assertFalse(validator.passwordIsValid());

        validator = new LoginValidator("", "Adm!n1 ");
        assertFalse(validator.passwordIsValid());

    }

    @Test
    public void passwordFailsAt5Characters() {
        LoginValidator validator = new LoginValidator("", "Am!n1");
        assertFalse(validator.passwordIsValid());
    }

    @Test
    public void validUsernamePasses() {
        LoginValidator validator = new LoginValidator("admin1", "");
        assertTrue(validator.usernameIsValid());
    }

    @Test
    public void usernameFailsWithSymbol() {
        LoginValidator validator = new LoginValidator("admin1!", "");
        assertFalse(validator.usernameIsValid());
    }

    @Test
    public void usernameFailsWithWhitespace() {
        LoginValidator validator = new LoginValidator(" admin1", "");
        assertFalse(validator.usernameIsValid());

        validator = new LoginValidator("adm in1", "");
        assertFalse(validator.usernameIsValid());

        validator = new LoginValidator("admin1 ", "");
        assertFalse(validator.usernameIsValid());
    }
}

package com.project.splitwise.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNumberValidator {

    public boolean isValidPhoneNumber(String phoneNumber) {

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (phoneNumber == null) {
            return false;
        }

        Matcher m = p.matcher(phoneNumber);

        return m.matches() && phoneNumber.length() == 10;
    }

}

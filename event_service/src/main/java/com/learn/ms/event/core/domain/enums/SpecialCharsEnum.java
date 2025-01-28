package com.learn.ms.event.core.domain.enums;

public enum SpecialCharsEnum {

    COLON(":"),
    SPACE(" "),
    HYPHEN("-"),
    UNDERSCORE("_"),
    HASH("#"),
    AT_SIGN("@"),
    ASTERISK("*"),
    ;

    private final String sign;

    SpecialCharsEnum(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

}

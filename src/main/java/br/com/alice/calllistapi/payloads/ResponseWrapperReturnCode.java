package br.com.alice.calllistapi.payloads;

public enum ResponseWrapperReturnCode {
    SUCCESS(0, "SUCCESS"),
    FAIL(1, "FAIL"),
    ERROR(-1, "ERROR");

    private int code;
    private String message;

    private ResponseWrapperReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

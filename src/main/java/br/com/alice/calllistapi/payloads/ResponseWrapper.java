package br.com.alice.calllistapi.payloads;

public class ResponseWrapper<TData> {
    private int code;
    private String message;
    private TData data;

    public ResponseWrapper() {
        super();
    }

    public ResponseWrapper(TData data) {
        this(ResponseWrapperReturnCode.SUCCESS, data);
    }

    public ResponseWrapper(int code, TData data) {
        this(code, null, data);
    }

    public ResponseWrapper(ResponseWrapperReturnCode returnCode, TData data) {
        this(returnCode.getCode(), returnCode.getMessage(), data);
    }

    public ResponseWrapper(int code, String message, TData data) {
        this();

        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TData getData() {
        return data;
    }

    public void setData(TData data) {
        this.data = data;
    }
}

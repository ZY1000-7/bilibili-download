public class RequestError extends Exception {
    public String diyMessage = null;

    public RequestError(String diyMessage) {
        this.diyMessage = diyMessage;
    }

    @Override
    public String getMessage() {
        return diyMessage;
    }

    @Override
    public String toString() {
        return "RequestError{" +
                "message='" + diyMessage + '\'' +
                '}';
    }
}

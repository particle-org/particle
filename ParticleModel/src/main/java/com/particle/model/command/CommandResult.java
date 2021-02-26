package com.particle.model.command;

public class CommandResult {

    public static final int TYPE_ERROR_INPUT = 0;

    public static final int TYPE_ERROR_CORE = 1;

    private boolean success;

    private Object result;

    private String errorMsg;

    private int errorType;

    public static Builder builder() {
        return new Builder();
    }

    private CommandResult(boolean success, Object result, int errorType, String errorMsg) {
        this.success = success;
        this.result = result;
        this.errorType = errorType;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return !this.success;
    }

    public Object getResult() {
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getErrorType() {
        return errorType;
    }

    public static class Builder {

        private boolean success;

        private Object result;

        private String errorMsg;

        private int errorType;

        Builder() {
        }

        public Builder succeed() {
            this.success = true;
            return this;
        }

        public Builder error(int errorType) {
            this.success = false;
            this.errorType = errorType;
            return this;
        }

        public Builder errorMsg(String msg) {
            this.errorMsg = msg;
            return this;
        }

        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        public CommandResult build() {
            return new CommandResult(success, result, errorType, errorMsg);
        }
    }
}

package com.ey.dgs.api_response;

import com.ey.dgs.model.Question;

import java.util.Arrays;

public class GetQuestionsResponse {

    private Question[] result;

    private boolean success;

    private String message;

    public Question[] getResult() {
        return result;
    }

    public void setResult(Question[] result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GetQuestionsResponse{" +
                "result=" + Arrays.toString(result) +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}

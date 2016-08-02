package com.alibaba.webx.tutorial1;




public class HttpResult {

    private int             code;
    private int             resultCode;
    private String          resultInfo;
    private String          content;

    public final static int FAILURE = 0;
    public final static int OK      = 1;
    public final static int ERROR   = -1;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }


    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isOK() {
        if (this.resultCode == HttpResult.OK) {
            return true;
        } else {
            return false;
        }
    }

    public static HttpResult succResult() {
        HttpResult r = new HttpResult();
        r.setResultCode(HttpResult.OK);
        return r;
    }

    public static HttpResult errorResult(String message) {
        HttpResult r = new HttpResult();
        r.setResultCode(HttpResult.ERROR);
        r.setResultInfo(message);
        return r;
    }

    public static HttpResult failResult(String message) {
        HttpResult r = new HttpResult();
        r.setResultCode(HttpResult.FAILURE);
        r.setResultInfo(message);
        return r;
    }

    @Override
    public String toString() {
        return "HttpResult [code=" + code + ", resultCode=" + resultCode + ", resultInfo=" + resultInfo
               + ", contentLength=" + content.length() + "]";
    }

}

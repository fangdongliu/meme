package cn.fdongl.meme.tool;

import java.util.Map;

public enum ErrorDefiner {
    ERROR(0,"未知的错误"),
    N_PAIR(1, "未配对"),
    PAIR_ALREADY_EXIST(2,"已配对"),
    N_LOGIN(3,"未登录");

    private final int value;
    private final String reasonPhrase;

    private ErrorDefiner(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }


    public static void error(Map<String,Object> map, ErrorDefiner error) {
        if (map != null) {
            map.put("error",error.reasonPhrase);
            map.put("errro_code",error.value);
        }
    }

}
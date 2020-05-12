/*package com.example.myapplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버 URL 설정 (php 파일 연동)
    final  static  private  String URL = "http://raw20.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPW, String userNAME, int userAGE, String userUSERNAME, String userSCHOOL, String userMAJOR, String userAREA, String userEMAIL, Response.Listener<String> listener){
        super(Method.POST , URL , listener , null);
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPW", userPW);
        map.put("userNAME", userNAME);
        map.put("userAGE", userAGE + "");
        map.put("userUSERNAME", userUSERNAME);
        map.put("userSCHOOL", userSCHOOL);
        map.put("userMAJOR", userMAJOR);
        map.put("userAREA", userAREA);
        map.put("userEMAIL", userEMAIL);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
*/
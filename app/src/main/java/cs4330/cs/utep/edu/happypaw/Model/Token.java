package cs4330.cs.utep.edu.happypaw.Model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("Authorization")
    public String authorization;
    public String password;
    public String email;

    public Token(String password, String email){
        this.password = password;
        this.email = email;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

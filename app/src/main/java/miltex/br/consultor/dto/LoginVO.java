package miltex.br.consultor.dto;

public class LoginVO {

    private String login;
    private String pass;

    public LoginVO(String l, String p){
        login=l;
        pass=p;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}

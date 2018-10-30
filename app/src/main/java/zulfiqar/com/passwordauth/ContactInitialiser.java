package zulfiqar.com.passwordauth;

/**
 * Created by Imran on 06-02-2018.
 */

public class ContactInitialiser {

    private String name;
    private String profession;
    private String mail;
    private String phone;

    public ContactInitialiser()
    {

    }

    public ContactInitialiser(String name ,String profession ,String mail , String phone) {
        this.name = name;
        this.profession = profession;
        this.mail = mail;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }


    public String getMail() {
        return mail;
    }
}

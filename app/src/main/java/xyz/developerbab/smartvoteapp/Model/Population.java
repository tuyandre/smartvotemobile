package xyz.developerbab.smartvoteapp.Model;

public class Population {
    String name;
    String nid;
    String phone;
    String sex;
    String dob;
    String province;
    String district;
    String biometric;
    String email;
    String password;
    String id;
    String profile;

    public Population() {
    }

    public Population(String name, String nid, String phone, String sex, String dob, String province, String district, String biometric, String email, String password, String id, String profile) {
        this.name = name;
        this.nid = nid;
        this.phone = phone;
        this.sex = sex;
        this.dob = dob;
        this.province = province;
        this.district = district;
        this.biometric = biometric;
        this.email = email;
        this.password = password;
        this.id = id;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBiometric() {
        return biometric;
    }

    public void setBiometric(String biometric) {
        this.biometric = biometric;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

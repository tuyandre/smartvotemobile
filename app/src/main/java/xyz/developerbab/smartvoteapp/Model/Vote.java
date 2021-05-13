package xyz.developerbab.smartvoteapp.Model;

public class Vote
{
    String user,province,district,season,candidate;
    public Vote() {
    }

    public Vote(String user, String province, String district, String season, String candidate) {
        this.user = user;
        this.province = province;
        this.district = district;
        this.season = season;
        this.candidate = candidate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }
}

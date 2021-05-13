package xyz.developerbab.smartvoteapp.Model;

public class Candidate {

    String candidate_id,candidate_name,season_id,dob,party,profile,logo,strength,province_name,district_name,province_id,district_id;
    public Candidate() {
    }

    public Candidate(String candidate_id, String candidate_name, String season_id, String dob, String party, String profile, String logo, String strength, String province_name, String district_name, String province_id, String district_id) {
        this.candidate_id = candidate_id;
        this.candidate_name = candidate_name;
        this.season_id = season_id;
        this.dob = dob;
        this.party = party;
        this.profile = profile;
        this.logo = logo;
        this.strength = strength;
        this.province_name = province_name;
        this.district_name = district_name;
        this.province_id = province_id;
        this.district_id = district_id;
    }

    public String getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
    }

    public String getCandidate_name() {
        return candidate_name;
    }

    public void setCandidate_name(String candidate_name) {
        this.candidate_name = candidate_name;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }
}

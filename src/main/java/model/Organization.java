package model;

import java.util.List;

public class Organization {
    private String name;
    private String OKPO;
    private String OKDP;
    private List<String> units;
    public static final String FILE = "/jsons/organizations.txt";

    public Organization() {
    }

    public Organization(String name, String OKPO, String OKDP, List<String> units) {
        this.name = name;
        this.OKPO = OKPO;
        this.OKDP = OKDP;
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOKPO() {
        return OKPO;
    }

    public void setOKPO(String OKPO) {
        this.OKPO = OKPO;
    }

    public String getOKDP() {
        return OKDP;
    }

    public void setOKDP(String OKDP) {
        this.OKDP = OKDP;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return name;
    }
}

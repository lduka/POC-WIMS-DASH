package co.za.pos.wims.enterprise.pocwimsdash.beans;

public class PackageType {

    private Long id;
    private int unitsPerPackage;
    private int totalNumberPerPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnitsPerPackage() {
        return unitsPerPackage;
    }

    public void setUnitsPerPackage(int unitsPerPackage) {
        this.unitsPerPackage = unitsPerPackage;
    }

    public int getTotalNumberPerPackage() {
        return totalNumberPerPackage;
    }

    public void setTotalNumberPerPackage(int totalNumberPerPackage) {
        this.totalNumberPerPackage = totalNumberPerPackage;
    }
}

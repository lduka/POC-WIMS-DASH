package co.za.pos.wims.enterprise.pocwimsdash.beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@ViewScoped
@Named
public class DashboardBean implements Serializable {
    private String content = "/ui/pages/componants/supplier/searchSupplier.xhtml"; // default fragment

    public String getContent() {
        return content;
    }

    public void setContent(String page) {
        this.content = "/ui/pages/componants/" + page;
    }
}

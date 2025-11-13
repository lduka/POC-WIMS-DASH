package co.za.pos.wims.enterprise.pocwimsdash.beans;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceCommandDelegate;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceOperator;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PF;

import java.io.Serial;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.UUID;

@Named
@SessionScoped

public class LoyaltyViewBean implements Serializable
{

    @Serial
    private static final long serialVersionUID = -7679945083708684177L;
    private Long progress = 0L;
    private String phoneNumber;
    private String accountAlias;
    private RewardsAccount rewardsAccount;
    private int value = 65;        // 0..100
    private String color = "#4caf50"; // hex color

    public void noop() { /* used so commandButton can AJAX-update */ }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }



    @PostConstruct
    public void init()
    {
        phoneNumber="";
        accountAlias= "";
    }
    public String getAccountAlias()
    {
        return accountAlias;
    }

    public void setAccountAlias(String accountAlias)
    {
        this.accountAlias = accountAlias;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public RewardsAccount getRewardsAccount()
    {
        return rewardsAccount;
    }

    public void setRewardsAccount(RewardsAccount rewardsAccount)
    {
        this.rewardsAccount = rewardsAccount;
    }

    public void query()
    {
        WebServiceOperator<RewardsAccount> webServiceOperator= new WebServiceOperator<>(RewardsAccount.class, ApiEndpoint.GET_REWARDS_ACCOUNT_BY_CELL_NUMBER)
                                                                    .setParameter("cellNumber",getPhoneNumber())
                                                                    .asQueryParam()
                                                                    .expectOne();
        rewardsAccount= WebServiceCommandDelegate.execute(webServiceOperator);
        setProgress(rewardsAccount.getCurrentPoints());
      /*  PF.current().executeScript("setHalfRingProgress('myProgressBarIndicator', #{loyaltyViewBean.progress});");*/
  PF.current().executeScript("setHalfRingProgress("+getProgress()+20+",'#2196f3', 10);");

    }


    public void create()
    {
        RewardsAccount rewardsAccount= new RewardsAccount();
        rewardsAccount.setPhoneNumber(getPhoneNumber());
        rewardsAccount.setTierLevel(1);
        rewardsAccount.setCurrentPoints(0L);
        rewardsAccount.setRewardsId(UUID.randomUUID().toString());

        WebServiceOperator<RewardsAccount> webServiceOperator= new WebServiceOperator<>(RewardsAccount.class, ApiEndpoint.CREATE_REWARDS_ACCOUNT).withBody(rewardsAccount);
        HttpResponse<String> resp = WebServiceCommandDelegate.execute(webServiceOperator);

        int code = resp != null ? resp.statusCode() : 0;
        if (code >= 200 && code < 300) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "RewardsAccount saved", "Stock item created successfully."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save RewardsAccount  failed", "v  returned status "));
        }
    }
    public Long getProgress() { return progress+200; }
    public void setProgress(Long progress) { this.progress = progress; }

}

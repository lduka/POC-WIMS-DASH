package co.za.pos.wims.enterprise.pocwimsdash.beans;


import java.util.Date;

public class RewardsAccount
{

    private Long id;
    private String rewardsId;
    private String phoneNumber;
    private int tierLevel ;
    private long currentPoints ;

    private Date updatedAt;
    private Date createdAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getRewardsId()
    {
        return rewardsId;
    }

    public void setRewardsId(String rewardsId)
    {
        this.rewardsId = rewardsId;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public int getTierLevel()
    {
        return tierLevel;
    }

    public void setTierLevel(int tierLevel)
    {
        this.tierLevel = tierLevel;
    }

    public long getCurrentPoints()
    {
        return currentPoints;
    }

    public void setCurrentPoints(long currentPoints)
    {
        this.currentPoints = currentPoints;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

}

package co.za.pos.wims.enterprise.pocwimsdash.beans;


import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceCommandDelegate;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceOperator;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.options.LineOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.Title;
import software.xdev.chartjs.model.options.elements.Fill;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UserReportBean implements Serializable
{


    private String searchId;
    private CashierBean foundCashier;
    private List<SaleCreateRequest> saleCreateRequestList;
    private String lineModel;

    // Date range (inclusive). Defaults to today when building the model if null
    private LocalDate startDate;
    private LocalDate endDate;

    public String getLineModel()
    {
        return lineModel;
    }

    public void setLineModel(String lineModel)
    {
        this.lineModel = lineModel;
    }

    public String getSearchId()
    {
        return searchId;
    }

    public void setSearchId(String searchId)
    {
        this.searchId = searchId;
    }

    public CashierBean getFoundCashier()
    {
        return foundCashier;
    }

    public void setFoundCashier(CashierBean foundCashier)
    {
        this.foundCashier = foundCashier;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public void findById()
    {
        WebServiceOperator<CashierBean> webServiceOperator = new WebServiceOperator<>(CashierBean.class, ApiEndpoint.FIND_CASHIER_BY_EMPLOYEE_ID);
        webServiceOperator.setParameter("employeeId", searchId.trim()).asQueryParam().expectOne();
        foundCashier = WebServiceCommandDelegate.execute(webServiceOperator);

        if (foundCashier != null)
        {
            foundCashier.getName();
        }

    }

    public void createLineModel()
    {
        // Prepare labels for 24 hours
        String[] hourLabels = new String[24];
        for (int h = 0; h < 24; h++) {
            hourLabels[h] = String.format("%02d", h);
        }

        // If there is no data yet, render an empty chart with hour labels and two empty datasets
        if (saleCreateRequestList == null || saleCreateRequestList.isEmpty())
        {
            double[] zeros = new double[24];
            lineModel = new LineChart()
                .setData(new LineData()
                    .addDataset(new LineDataset()
                        .setLabel("Cash Sales")
                        .setBorderColor(new RGBAColor(54, 162, 235)) // blue
                        .setLineTension(0.1f)
                        .setFill(new Fill<Boolean>(false))
                        .setData(Arrays.stream(zeros).boxed().toArray(Double[]::new)))
                    .addDataset(new LineDataset()
                        .setLabel("Card Sales")
                        .setBorderColor(new RGBAColor(75, 192, 192)) // green
                        .setLineTension(0.1f)
                        .setFill(new Fill<Boolean>(false))
                        .setData(Arrays.stream(zeros).boxed().toArray(Double[]::new)))
                    .setLabels(hourLabels))
                .setOptions(new LineOptions()
                    .setResponsive(true)
                    .setMaintainAspectRatio(false)
                    .setPlugins(new Plugins()
                        .setTitle(new Title().setDisplay(true).setText("Sales by Hour"))))
                .toJson();
            return;
        }

        // Define date range defaults
        LocalDate today = LocalDate.now();
        LocalDate from = (startDate == null) ? today : startDate;
        LocalDate to = (endDate == null) ? today : endDate;
        if (to.isBefore(from)) {
            // swap if specified in wrong order
            LocalDate tmp = from; from = to; to = tmp;
        }

        double[] cashTotals = new double[24];
        double[] cardTotals = new double[24];

        DateTimeFormatter[] formats = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        };

        for (SaleCreateRequest s : saleCreateRequestList)
        {
            if (s == null) { continue; }
            String created = s.getCreatedAt();
            if (created == null || created.isBlank()) { continue; }
            LocalDateTime ldt = null;
            for (DateTimeFormatter f : formats) {
                try { ldt = LocalDateTime.parse(created, f); break; } catch (Exception ignored) {}
            }
            if (ldt == null) { continue; }
            LocalDate d = ldt.toLocalDate();
            if (d.isBefore(from) || d.isAfter(to)) { continue; }
            int hour = ldt.getHour();
            Integer type = s.getSaleType();
            if (type != null && type == 2) { // 2 = card
                cardTotals[hour] += s.getTotal();
            } else if (type != null && type == 1) { // 1 = cash
                cashTotals[hour] += s.getTotal();
            } else {
                // Unknown types can be ignored or added to cash; choose to include in cash
                cashTotals[hour] += s.getTotal();
            }
        }

        String chartTitle = "Sales by Hour";
        if (foundCashier != null && foundCashier.getName() != null)
        {
            chartTitle = "Sales by Hour - " + foundCashier.getName();
        }

        lineModel = new LineChart()
            .setData(new LineData()
                .addDataset(new LineDataset()
                    .setData(Arrays.stream(cashTotals).boxed().toArray(Double[]::new))
                    .setLabel("Cash Sales")
                    .setBorderColor(new RGBAColor(54, 162, 235)) // blue
                    .setLineTension(0.1f)
                    .setFill(new Fill<Boolean>(false)))
                .addDataset(new LineDataset()
                    .setData(Arrays.stream(cardTotals).boxed().toArray(Double[]::new))
                    .setLabel("Card Sales")
                    .setBorderColor(new RGBAColor(75, 192, 192)) // green
                    .setLineTension(0.1f)
                    .setFill(new Fill<Boolean>(false)))
                .setLabels(hourLabels))
            .setOptions(new LineOptions()
                .setResponsive(true)
                .setMaintainAspectRatio(false)
                .setPlugins(new Plugins().setTitle(new Title().setDisplay(true).setText(chartTitle))))
            .toJson();
    }

    public void saleOverView()
    {
        saleCreateRequestList= new ArrayList<>();
        WebServiceOperator<SaleCreateRequest> saleCreateRequestWebServiceOperator = new WebServiceOperator<>(SaleCreateRequest.class, ApiEndpoint.FIND_SALE_ITEM_BY_ID);
        saleCreateRequestWebServiceOperator.setParameter("id",foundCashier.getId()+"").asQueryParam();
        saleCreateRequestList = WebServiceCommandDelegate.execute(saleCreateRequestWebServiceOperator);
        createLineModel();

    }

    public List<SaleCreateRequest> getSaleCreateRequestList()
    {
        return saleCreateRequestList;
    }

    public void setSaleCreateRequestList(List<SaleCreateRequest> saleCreateRequestList)
    {
        this.saleCreateRequestList = saleCreateRequestList;
    }
}

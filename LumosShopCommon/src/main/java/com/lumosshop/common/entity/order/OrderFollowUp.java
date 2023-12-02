package com.lumosshop.common.entity.order;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "order_followUp")
public class OrderFollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Date Timestamp;
    @Column(name = "remarks", length = 256)
    private String Remarks;

    @Enumerated(EnumType.STRING)
    private Order_Phase orderPhase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public String getFormattedTimeStamp() {
        DateFormat dateFormatter = new SimpleDateFormat("dd MMM yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.Timestamp);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = dateFormatter.format(this.Timestamp).split(" ")[1];
        String year = String.format("%02d", calendar.get(Calendar.YEAR) % 100);

        return String.format("%02d %s %s", day, month, year);
    }
    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Order_Phase getOrderPhase() {
        return orderPhase;
    }

    public void setOrderPhase(Order_Phase orderPhase) {
        this.orderPhase = orderPhase;
    }

    @Transient
    public String retrieveTimeStamp() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormatter.format(this.Timestamp);
    }
    public void modifyTimeStamp(String dateInString) throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try {
            this.Timestamp = dateFormatter.parse(dateInString);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }
}

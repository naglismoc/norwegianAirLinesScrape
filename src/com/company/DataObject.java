package com.company;




public class DataObject {
        private String price;
        private String taxes;
        private String from;
        private String to;
        private String departueTime;
        private String arivalTime;



    public String getPrice() {


        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepartueTime() {
        return departueTime;
    }

    public void setDepartueTime(String departueTime) {
        this.departueTime = departueTime;
    }

    public String getArivalTime() {
        return arivalTime;
    }

    public void setArivalTime(String arivalTime) {
        this.arivalTime = arivalTime;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "price='" + price + '\'' +
                ", taxes='" + taxes + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", departueTime='" + departueTime + '\'' +
                ", arivalTime='" + arivalTime + '\'' +
                '}';
    }
}

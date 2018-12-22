package com.example.gaurav.helfy.Model;

public class Order {

    private String type, name, timesatmp, status, address, reqID, _id;

    public Order() {

    }

    public Order(String type, String name, String timesatmp, String status, String address, String reqID, String _id) {
        this.type = type;
        this.name = name;
        this.timesatmp = timesatmp;
        this.status = status;
        this.address = address;
        this.reqID = reqID;
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimesatmp() {
        return timesatmp;
    }

    public void setTimesatmp(String timesatmp) {
        this.timesatmp = timesatmp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

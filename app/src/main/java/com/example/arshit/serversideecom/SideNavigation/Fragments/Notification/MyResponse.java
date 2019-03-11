package com.example.arshit.serversideecom.SideNavigation.Fragments.Notification;

import java.util.List;

public class MyResponse {

    public long multicast_id;
    public int  success;
    public int canoical_ids;
    public List<Result> results;


    public MyResponse() {
    }

    public MyResponse(long multicast_id, int success, int canoical_ids, List<Result> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.canoical_ids = canoical_ids;
        this.results = results;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getCanoical_ids() {
        return canoical_ids;
    }

    public void setCanoical_ids(int canoical_ids) {
        this.canoical_ids = canoical_ids;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

package src.Model;

import src.MyExceptions.CanNotCancelOrderException;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class Sit {
    private final int sid; //primary key(class room field)
    private final Classroom classroom; //
    private boolean avl;
    private Student onSit;
    private final LinkedList<Order> servingOrders;
    private final LinkedList<Order> historyOrders;

    //constructor
    Sit(int sid, Classroom classroom) {
        this.sid = sid;
        this.classroom = classroom;
        avl = true;
        onSit = null;
        historyOrders = new LinkedList<>();
        servingOrders =new LinkedList<>();
    }

    //getter and setter
    public int getSid() {
        return sid;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public boolean isAvl() {
        return avl;
    }

    public void enable() {
        this.avl = true;
    }

    public void disable() {
        this.avl = false;
        if(servingOrders.isEmpty())return;
        ArrayList<Order> orders = new ArrayList<>(servingOrders);
        for(Order i : orders) {
            i.updateState();
        }
        orders = new ArrayList<>(servingOrders);
        for(Order i : orders) {
            try{
                i.cancel(true);
            } catch (CanNotCancelOrderException e) {
                return;
            }
        }
    }

    public Student getOnSit() {
        return onSit;
    }

    public void setOnSit(Student onSit) {
        this.onSit = onSit;
    }

    public LinkedList<Order> getHistoryOrders() {
        return historyOrders;
    }

    public LinkedList<Order> getServingOrders() {
        return servingOrders;
    }

    public Boolean checkSitInTimeRange(GregorianCalendar startTime, GregorianCalendar endTime) {
        if(this.getServingOrders().isEmpty()) {
            return true;
        }
        ArrayList<Order> orders = new ArrayList<>(this.getServingOrders());
        for(Order i : orders) {
            i.updateState();
            if(i.getStartTime().compareTo(endTime)*i.getEndTime().compareTo(startTime)<0.) {
                return false;
            }
        }
        return true;
    }
}

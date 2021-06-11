import MyExceptions.CanNotCancelOrderException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
/**
 * <p>此类为座位类
 */
public class Sit {
    private final int sid; //primary key(class room field)座位号
    private final Classroom classroom; //座位所在教室
    private boolean avl;//椅子是否可用
    private Student onSit;//当前坐在椅子上的同学
    private final LinkedList<Order> servingOrders;//当前预定订单
    private final LinkedList<Order> historyOrders;//历史订单

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
    /**
     *将对应的座位禁用。这将取消座位上的所有订单，并补偿相应学生10点信用，并关闭学生对该座位的预定权限。
     */
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

    public void sortHistory() {
        historyOrders.sort(Comparator.comparing(Order::getStartTime));
    }

    public void sortServing() {
        servingOrders.sort(Comparator.comparing(Order::getStartTime));
    }
    /**
     * 检查座位是否还在时间范围内
     * @param startTime
     * @param endTime
     * @return
     */
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

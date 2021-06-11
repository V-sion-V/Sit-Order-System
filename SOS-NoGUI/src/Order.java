import MyExceptions.CanNotCancelOrderException;
import MyExceptions.ClassroomNotExistException;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 本类为订单类，记录了订单的拥有者、目标和状态。
 */
public class Order {
    private final int oid;
    private Sit target;//预定的座位号
    private Student owner;//预订座位的学生
    private final GregorianCalendar startTime;
    private final GregorianCalendar endTime;
    private GregorianCalendar arriveTime;
    private GregorianCalendar leaveTime;
    private boolean isArrived;
    private boolean isCanceled;
    private boolean isEnded;
    //isArrived -> A, isCanceled->C, isEnded->E
    //创建时：~A~C~E
    //  如果按时到达：A~C~E
    //      到达并离开：A~CE
    //      到达未按时离开：ACE
    //      到达并中断：AC~E
    //  如果未按时到达：~A~CE
    //  如果取消：~AC~E

    public Order(int oid, Sit target, Student owner, GregorianCalendar startTime, GregorianCalendar endTime) {
        this.oid = oid;
        this.target = target;
        this.owner = owner;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isArrived = false;
        this.isCanceled = false;
        this.isEnded = false;
    }

    /**
     * 从文件读取订单的构造方法
     * @param fileIn
     * @param owner 提供该订单的拥有者用以创建订单
     */
    public Order(String fileIn,Student owner){
        String[] inArgs = fileIn.split("\\\\%\\\\");
        oid = Integer.parseInt(inArgs[0]);
        try {
            target = ClassroomList.getInstance().getClassroom(Integer.parseInt(inArgs[1].split(":")[0])).
                    getSits().get(Integer.parseInt(inArgs[1].split(":")[1]));
        }catch (ClassroomNotExistException ignored){}
        this.owner = owner;
        isArrived = inArgs[2].equals("1");
        isCanceled = inArgs[3].equals("1");
        isEnded = inArgs[4].equals("1");
        target.getHistoryOrders().push(this);
        String[] time = inArgs[5].split(":");
        startTime = new GregorianCalendar(Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]),
                Integer.parseInt(time[3]), 0);
        time = inArgs[6].split(":");
        endTime = new GregorianCalendar(Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]),
                Integer.parseInt(time[3]), 0);
        if(inArgs.length>7){
            time = inArgs[7].split(":");
            arriveTime = new GregorianCalendar(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]),
                    Integer.parseInt(time[3]), 0);
        }
        if(inArgs.length>8){
            time = inArgs[8].split(":");
            leaveTime = new GregorianCalendar(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]),
                    Integer.parseInt(time[3]), 0);
        }
    }

    //getter and setter
    public int getOid() {
        return oid;
    }

    public Sit getTarget() {
        return target;
    }

    public void setTarget(Sit target) {
        this.target = target;
    }

    public Student getOwner() {
        return owner;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public GregorianCalendar getArriveTime() {
        return arriveTime;
    }

    public GregorianCalendar getLeaveTime() {
        return leaveTime;
    }
    /**
     * isArrived,isCanceled,isEnded三个状态，分别对应不同情况
     */
    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean arrived) {
        isArrived = arrived;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    /**
     * 将订单移动至学生历史记录
     */
    public void moveToSitHistory() {
        target.getServingOrders().remove(this);
        target.getHistoryOrders().add(this);
    }
    /**
     * 随时间更新状态
     */
    public void updateState() {
        GregorianCalendar now = new GregorianCalendar();
        if(!isArrived&&!isCanceled&&!isEnded) {
            if(now.getTimeInMillis()-startTime.getTimeInMillis()>900000) {
                isEnded=true;
                moveToSitHistory();
            }
        } else if(isArrived&&!isCanceled&&!isEnded) {
            if(now.getTimeInMillis()-endTime.getTimeInMillis()>0) {
                isEnded=true;
                target.setOnSit(null);
                moveToSitHistory();
            }
        }
    }
    /**
     * 取消订单，如果时间晚于开始时间前的15分钟，将无法取消
     * @param force 是否强制
     * @throws CanNotCancelOrderException 不强制时超过取消时间
     */
    public void cancel(boolean force)
            throws CanNotCancelOrderException{
        if(!isCanceled&&!isEnded) {
            if(force) {
                isCanceled = true;
                owner.gainCredit(10);
                moveToSitHistory();
            } else {
                GregorianCalendar now = new GregorianCalendar();
                if(startTime.getTimeInMillis()-now.getTimeInMillis()>900000) {
                    isCanceled = true;
                    moveToSitHistory();
                } else {
                    throw new CanNotCancelOrderException(this.owner.getId(),oid);
                }
            }
        } else {
            throw new CanNotCancelOrderException(this.owner.getId(),oid);
        }
    }

    /**
     * 签到
     */
    public void arrive() {
        this.arriveTime = new GregorianCalendar();
        this.isArrived = true;
        this.target.setOnSit(owner);
        this.owner.gainCredit(2);
    }

    /**
     * 签退
     */
    public void leave() {
        this.leaveTime = new GregorianCalendar();
        this.isEnded = true;
        this.target.setOnSit(null);
        this.owner.gainCredit(1);
        moveToSitHistory();
    }

    /**
     * 将此订单的数据转化为字符串用于储存
     */
    public String toFileString() {
        return oid + "\\%\\" + target.getClassroom().getCid()
                + ":" + target.getSid()
                + "\\%\\" + (isArrived?1:0)
                + "\\%\\" + (isCanceled?1:0)
                + "\\%\\" + (isEnded?1:0)
                + "\\%\\" + startTime.get(Calendar.YEAR)
                + ":" + startTime.get(Calendar.MONTH)
                + ":" + startTime.get(Calendar.DATE)
                + ":" + startTime.get(Calendar.HOUR_OF_DAY)
                + "\\%\\" + endTime.get(Calendar.YEAR)
                + ":" + endTime.get(Calendar.MONTH)
                + ":" + endTime.get(Calendar.DATE)
                + ":" + endTime.get(Calendar.HOUR_OF_DAY)
                + ((arriveTime!=null)?"\\%\\" + arriveTime.get(Calendar.YEAR)
                + ":" + arriveTime.get(Calendar.MONTH)
                + ":" + arriveTime.get(Calendar.DATE)
                + ":" + arriveTime.get(Calendar.HOUR_OF_DAY):"")
                + ((leaveTime!=null)?"\\%\\" + leaveTime.get(Calendar.YEAR)
                + ":" + leaveTime.get(Calendar.MONTH)
                + ":" + leaveTime.get(Calendar.DATE)
                + ":" + leaveTime.get(Calendar.HOUR_OF_DAY):"");
    }
}

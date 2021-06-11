import MyExceptions.*;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
/**
 * <p>学生类，记录学生的信息，提供学生操作的接口
 */
public class Student {
    private final int id;
    private String pwd;
    private final String name;
    private int credit;
    private Order servingOrder;
    private final LinkedList<Order> historyOrders;
    private boolean isLogin;

    Student(int id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.credit = 100;
        this.isLogin = false;
        this.servingOrder = null;
        this.historyOrders = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void gainCredit(int i) {
        credit+=i;
        if(credit>100)credit = 100;
    }

    public void loseCredit(int i) {
        credit -= i;
        if(credit<0) credit = 0;
    }

    public Order getServingOrder() {
        return servingOrder;
    }

    public void setServingOrder(Order order) {
        servingOrder = order;
    }

    public LinkedList<Order> getHistoryOrders() {
        return historyOrders;
    }

    public void login(String pwd)
            throws PasswordNotMatchException {
        if(isLogin||pwd.equals(this.pwd)) {
            isLogin = true;
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public void logout() throws DoesNotLoginException {
        if(isLogin){
            isLogin = false;
        } else {
            throw new DoesNotLoginException("logout");
        }
    }
    /**
     * <p>学生对于密码的要求没那么严格：可以只有数字或字母。
     * @param oldPwd
     * @param newPwd
     * @throws PasswordNotMatchException
     * @throws PasswordFormatException
     * @throws DoesNotLoginException
     */
    public void changePwd(String oldPwd, String newPwd)
            throws PasswordNotMatchException,
            PasswordFormatException, DoesNotLoginException {
        if(isLogin) {
            if (oldPwd.equals(pwd)) {
                if (newPwd.matches("[0-9a-zA-Z]{8,}")) {
                    pwd = newPwd;
                } else {
                    throw new PasswordFormatException(newPwd);
                }
            } else {
                throw new PasswordNotMatchException();
            }
        } else {
            throw new DoesNotLoginException("change password");
        }
    }
    /**
     * <p>创建一个新的订单，分为三个阶段：首先选择时间段，学生可以选择当天当时到晚10点，第二天早8点到晚10点的任意时间段，以小时为单位，
     * 例如输入(0 8 10)代表选择今天早8点至10点，输入(1 16 19)代表选择明天下午4点至晚7点。
     * 其中，只有信用大于80的学生才可以预定第二天的座位，只有信用大于60的学生才可以预定座位；
     * @param date 日期
     * @param time 时间
     * @return
     * @throws TimeIllegalException
     * @throws NumberFormatException
     */
    public GregorianCalendar inputTime(String date, String time)
            throws TimeIllegalException,NumberFormatException {
        GregorianCalendar GreTime = new GregorianCalendar();
        if ((Integer.parseInt(date)==0&&credit>=60)||(Integer.parseInt(date)==1&&credit>=80)) {
            GreTime.add(Calendar.DATE,Integer.parseInt(date));
            int intTime = Integer.parseInt(time);
            if(intTime>=8&&intTime<=22) {
                GreTime.set(Calendar.MILLISECOND,0);
                GreTime.set(Calendar.SECOND,0);
                GreTime.set(Calendar.MINUTE,0);
                GreTime.set(Calendar.HOUR_OF_DAY,intTime);
                if(GreTime.compareTo(new GregorianCalendar())>0) {
                    return GreTime;
                } else {
                    throw new TimeIllegalException("Too early");
                }
            } else {
                throw new TimeIllegalException("out of range");
            }
        } else {
            throw new TimeIllegalException("permission denied for this day");
        }
    }

    /**
     * 创建一个新的订单，拥有者为this
     * @param target 目标座位
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @throws TimeIllegalException
     * @throws HoldingOrderException
     */
    public void generateOrder(Sit target,GregorianCalendar startTime, GregorianCalendar endTime)
            throws TimeIllegalException, HoldingOrderException {
        if(target.getClassroom().isAvl()) {
            if (target.isAvl()) {
                if (target.checkSitInTimeRange(startTime, endTime)) {
                    if (startTime.compareTo(endTime) < 0) {
                        if (startTime.get(Calendar.DATE) == endTime.get(Calendar.DATE)) {
                            if (servingOrder == null) {
                                servingOrder = new Order(historyOrders.size(), target, this, startTime, endTime);
                                target.getServingOrders().push(servingOrder);
                            } else {
                                throw new HoldingOrderException("Order Exist");
                            }
                        } else {
                            throw new TimeIllegalException("not in same day");
                        }
                    } else {
                        throw new TimeIllegalException("end<=start");
                    }
                } else {
                    throw new TimeIllegalException("conflicted with other order");
                }
            }
        }
    }

    /**
     * 签到
     * @throws HoldingOrderException check的异常
     * @throws TimeIllegalException
     * @throws OrderLateException
     * @throws DoesNotLeaveException
     * @throws OrderCanceledException
     */
    public void arrive()
            throws HoldingOrderException, TimeIllegalException,
            OrderLateException, DoesNotLeaveException, OrderCanceledException {
        if(servingOrder!=null) {
            servingOrder.updateState();
            if(!servingOrder.isCanceled()&&!servingOrder.isEnded()) {
                if(!servingOrder.isArrived()) {
                    if(new GregorianCalendar().compareTo(servingOrder.getStartTime())>=0) {
                        servingOrder.arrive();
                    } else {
                        throw new TimeIllegalException("please arrive later");
                    }
                } else {
                    throw new HoldingOrderException("Already arrived");
                }
            } else {
                checkOrder();
            }
        } else {
            throw new HoldingOrderException("No serving order");
        }
    }

    public void moveToHistory() throws HoldingOrderException {
        if(servingOrder!=null) {
            historyOrders.push(servingOrder);
            servingOrder = null;
        } else {
            throw new HoldingOrderException("No serving order");
        }
    }

    /**
     * 签退
     * @throws HoldingOrderException check的异常
     * @throws TimeIllegalException
     * @throws OrderLateException
     * @throws OrderCanceledException
     * @throws DoesNotLeaveException
     */
    public void leave()
            throws HoldingOrderException, TimeIllegalException,
            OrderLateException, OrderCanceledException, DoesNotLeaveException {
        if(servingOrder!=null) {
            servingOrder.updateState();
            if(!servingOrder.isCanceled()&&!servingOrder.isEnded()) {
                if(servingOrder.isArrived()) {
                    if(new GregorianCalendar().getTimeInMillis()-servingOrder.getEndTime().getTimeInMillis()>-900000) {
                        servingOrder.leave();
                        moveToHistory();
                    } else {
                        throw new TimeIllegalException("please arrive later");
                    }
                } else {
                    throw new HoldingOrderException("Please arrive first");
                }
            } else {
                checkOrder();
            }
        } else {
            throw new HoldingOrderException("No serving order");
        }
    }
    /**
     * <p>更新当前订单的状态
     * @throws OrderLateException 迟到
     * @throws DoesNotLeaveException 未签退
     * @throws HoldingOrderException 无订单
     * @throws OrderCanceledException 已取消
     */
    public void checkOrder()
            throws OrderLateException, DoesNotLeaveException,
            HoldingOrderException, OrderCanceledException {
        if(servingOrder!=null) {
            servingOrder.updateState();
            int oid = servingOrder.getOid();
            if(!servingOrder.isArrived()&&!servingOrder.isCanceled()&&servingOrder.isEnded()) {
                moveToHistory();
                loseCredit(10);
                throw new OrderLateException(oid);
            } else if(servingOrder.isCanceled()&&!servingOrder.isEnded()) {
                moveToHistory();
                throw new OrderCanceledException(oid);
            } else if(servingOrder.isArrived()&&servingOrder.isCanceled()&&servingOrder.isEnded()) {
                moveToHistory();
                loseCredit(10);
                throw new DoesNotLeaveException(oid);
            }
        } else {
            throw new HoldingOrderException("No serving order");
        }
    }
/**
 * 取消当前订单
 * @throws CanNotCancelOrderException
 * @throws HoldingOrderException
 */
    public void cancelOrder()
            throws CanNotCancelOrderException, HoldingOrderException {
        if(servingOrder!=null) {
            servingOrder.updateState();
            servingOrder.cancel(false);
            moveToHistory();
        } else {
            throw new HoldingOrderException("No order");
        }
    }
    /**
     * 显示学生的历史订单,订单信息除了当前订单的信息，还包含了订单结束的状态
     */
    public void sortHistory() {
        historyOrders.sort(Comparator.comparing(Order::getStartTime));
    }
}

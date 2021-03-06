package src.Model;

import src.MyExceptions.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

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

    public LinkedList<Order> getHistoryOrders() {
        return historyOrders;
    }

    public void login(String pwd)
            throws PasswordNotMatchException {
        if(!isLogin && pwd.equals(this.pwd)) {
            isLogin = true;
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public void logout() {
        isLogin = false;
    }

    public void changePwd(String oldPwd, String newPwd)
            throws PasswordNotMatchException,
            PasswordFormatException {
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
        }
    }

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
                    throw new TimeIllegalException("????????????");
                }
            } else {
                throw new TimeIllegalException("??????????????????");
            }
        } else {
            throw new TimeIllegalException("??????????????????");
        }
    }

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
                                throw new HoldingOrderException("?????????????????????");
                            }
                        } else {
                            throw new TimeIllegalException("not in same day");
                        }
                    } else {
                        throw new TimeIllegalException("end<=start");
                    }
                } else {
                    throw new TimeIllegalException("?????????????????????");
                }
            }
        }
    }

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
                        throw new TimeIllegalException("?????????????????????");
                    }
                } else {
                    throw new HoldingOrderException("????????????");
                }
            } else {
                checkOrder();
            }
        } else {
            throw new HoldingOrderException("????????????");
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
                        throw new TimeIllegalException("??????????????????");
                    }
                } else {
                    throw new HoldingOrderException("????????????");
                }
            } else {
                checkOrder();
            }
        } else {
            throw new HoldingOrderException("????????????");
        }
    }

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
            throw new HoldingOrderException("????????????");
        }
    }

    public void cancelOrder()
            throws CanNotCancelOrderException, HoldingOrderException {
        if(servingOrder!=null) {
            servingOrder.updateState();
            servingOrder.cancel(false);
            moveToHistory();
        } else {
            throw new HoldingOrderException("????????????");
        }
    }

}

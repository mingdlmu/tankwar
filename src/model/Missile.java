package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import model.Tank.Direction;
import client.TankClient;
public class Missile {

    //定义两个常量，表示运动的速度
    private static final int XSPEED = 10;
    private static final int YSPEED = 10;

    //子弹所在的位置
    private int x;
    private int y;

    //坦克的高度和宽度
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    //子弹的运行方向
    private Direction dir;

    //子弹对主坦克的威力
    private static final int POWER = 20;

    private boolean live = true;

    private boolean good =true;

    //拥有一个TankClient对象的引用
    private TankClient tc;
    public Missile(int x,int y,Direction dir,boolean good,TankClient tc){
        this(x,y,dir);
        this.good = good;
        this.tc = tc;

    }
    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
    public void draw(Graphics g){
        //如果该子弹不是存活的，则不进行绘图
        if(!live){
            return ;
        }
        Color c = g.getColor();
        //根据子弹的好坏来设置不同的颜色
        if(this.good){
            g.setColor(Color.RED);
        }
        else{
            g.setColor(Color.BLUE);
        }

        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);
        move();
    }

    private void move() {
        if(dir==Direction.L){//L,LU,U,RU,R,RD,D,LD,STOP
            x -= XSPEED;
        }
        else if(dir==Direction.LU){
            x -= XSPEED;
            y -= YSPEED;
        }
        else if(dir==Direction.U){
            y -= YSPEED;
        }
        else if(dir==Direction.RU){
            x += XSPEED;
            y -= YSPEED;
        }
        else if(dir==Direction.R){
            x += XSPEED;
        }
        else if(dir==Direction.RD){
            x += XSPEED;
            y += YSPEED;
        }
        else if(dir==Direction.D){
            y += YSPEED;
        }
        else if(dir==Direction.LD){
            x -= XSPEED;
            y += YSPEED;
        }

        //根据子弹所在的位置x,y来判断子弹是否还存活在
        if(x<0||x>TankClient.GAME_WIDTH||y<0||y>TankClient.GAME_HEIGHT){
            live = false;
        }
    }
    public boolean isLive() {
        return live;
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTank(Tank t){
        //先判断该坦克是否还是存活，如果已经死了，子弹就不打他了
        if(!t.isLive()){
            return false;
        }
        if(this.live&&this.good!=t.isGood()&&this.getRect().intersects(t.getRect())){//判断是否有碰撞
            //碰撞之后，子弹和该坦克就应该都死了
            this.live = false;//子弹死了
            /*
             * 如果是子弹击中了正方坦克，则生命值减 20，如果是敌方坦克，直接挂掉
             * */
            if(t.isGood()){
                int life = t.getLife() -POWER;
                if(life<=0){
                    t.setLive(false);
                }
                t.setLife(life);

            }
            else{
                t.setLive(false);//敌方坦克死了
            }

            Explode e = new Explode(x,y,tc);
            tc.getExplodes().add(e);
            return true;
        }
        else{
            return false;
        }
    }
    /*
     * 一颗子弹打多辆坦克
     * */
    public boolean hitTanks(List<Tank> tanks){
        for(int i=0;i<tanks.size();i++){
            if(hitTank(tanks.get(i))){
                return true;
            }
        }
        return false;
    }
    /*
     * 函数的功能：判断子弹是否击中了墙
     * */
    public boolean hitWall(Wall w){
        if(!this.live){
            return false;
        }

        if(this.live&&this.getRect().intersects(w.getRect())){
            this.live = false;
            return true;
        }
        else{
            return false;
        }

    }
    /*
     * 函数的功能：判断子弹是否击中了一系列的墙
     * */
    public boolean hitWalls(List<Wall> walls){
        for(int i=0;i<walls.size();i++){
            if(this.hitWall(walls.get(i))){
                return true;
            }
        }
        return false;
    }
}

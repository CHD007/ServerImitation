package server;

/**
 * Created by Danil on 27.02.2016.
 */
public class ServerParameters {
    public static final double player_decay = 0.4;
    public static final double ball_decay = 0.94;

    public static final double minpower = -100;
    public static final double maxpower = 100;

    public static final double minmoment = -180;
    public static final double maxmoment = 180;

    public static final double player_speed_max = 1.0;
    public static final double player_accel_max = 1.0;

    public static final double ball_speed_max = 2.7;
    public static final double ball_accel_max =2.7;

    public static final double dash_power_rate = 0.006;

    public static final double stamina_max = 4000.0; //4000
    public static final double stamina_inc_max = 45.0;

    public static final double recover_dec_thr = 0.3;
    public static final double recover_dec = 0.002;
    public static final double recover_min = 0.5;
    public static final double recover_max = 1.0;

    public static final double effort_min = 0.6;
    public static final double effort_max = 1.0; //1.0
    public static final double effort_dec_thr = 0.3;
    public static final double effort_dec = 0.005;
    public static final double effort_inc_thr = 0.6;
    public static final double effort_inc = 0.01;

    public static final double player_rand = 0.1;
    public static final double ball_rand = 0.05;

    public static final double inertia_moment = 5.0;

    public static final double player_size = 0.5;
    public static final double ball_size = 0.085;
    public static final double kickable_margin = 0.7;

    public static final double kick_rand = 0.006; // для обычных игроков = 0.00, страница 29 Бойра и Кока
    public static final double kick_power_rate = 0.027;

    public static final int FIELD_WIDTH = 105;
    public static final int FIELD_HEIGHT = 68;
}

package server;

import objects.FieldObject;
import objects.FieldPart;
import objects.Velocity;

/**
 * Класс для вспомогательных вычислений
 * Created by Danil on 26.03.2016.
 */
public class MyMath {
    /**
     * Расстояние от точки f1 до f2
     * @param f1
     * @param f2
     * @return
     */
    public static double distance(FieldObject f1, FieldObject f2) {
        return Math.sqrt((f1.getPosX() - f2.getPosX())*(f1.getPosX() - f2.getPosX())+(f1.getPosY()-f2.getPosY())*(f1.getPosY()-f2.getPosY()));
    }

    /**
     * Вычисление модуля вектора скорости
     * @param velocity вектор скорости
     * @return модуль вектора
     */
    public static double velocityModule(Velocity velocity) {
        return Math.sqrt(velocity.getX()*velocity.getX()+velocity.getY()*velocity.getY());
    }


    /**
     * Проверка принадлежности силы рывка допустимому промежутку
     * @param power сил рывка
     * @return скорректированная сила рывка
     */
    public static double normalizePower(double power) {
        if (power >= ServerParameters.maxpower) {
            return ServerParameters.maxpower;
        }
        else {
            if (power < ServerParameters.minpower) {
                return ServerParameters.minpower;
            }
            else {
                return power;
            }
        }
    }

    /**
     * Нормирование вектора
     * @param acceleration
     */
    public static void normalizeVector(Velocity acceleration) {
        double velocityLength = Math.sqrt(acceleration.getX()*acceleration.getX() + acceleration.getY()*acceleration.getY());
        acceleration.setX(acceleration.getX()/velocityLength);
        acceleration.setY(acceleration.getY()/velocityLength);
    }

    /**
     * Нормирование веткора к определенной длине
     * @param acceleration ускорение, которое норминуется
     * @param length максимальная длина веткора, к которой он нормируется
     */
    public static void normalizeVectorToSomeLength(Velocity acceleration, double length) {
        normalizeVector(acceleration);
        acceleration.setX(acceleration.getX()*length);
        acceleration.setY(acceleration.getY()*length);
    }


    /**
     * Проверка угла повората на принадлежность допустимому промежутку
     * @param angle угол для проверки
     * @return скорректированный угол поворота
     */
    public static double normalizeAngle(double angle) {
        if (angle > ServerParameters.maxmoment) {
            return ServerParameters.maxmoment;
        }
        else {
            if (angle < ServerParameters.minmoment) {
                return ServerParameters.minmoment;
            }
            else {
                return angle;
            }
        }
    }

    /**
     * Преобразование координаты x в отностильеную (сдвиг и перенос начала координат)
     * @param a начало коориднаты x новой системы в старых координатах
     * @param b начало коориднаты y новой системы в старых координатах
     * @param x кооридната x точки, для которой нужно получить относительную координату
     * @param y кооридната y точки, для которой нужно получить относительную координату
     * @param angle угол поворота координаты
     * @return относительная коориданата x (координата по x точки в новой системе отсчета)
     */
    public static double relativeX(double a, double b, double x, double y, double angle) {
        return (x-a)*Math.cos(Math.toRadians(angle))+(y-b)*Math.sin(Math.toRadians(angle));
    }

    /**
     * Преобразование координаты y в отностильеную (сдвиг и перенос начала координат)
     * @param a начало коориднаты x новой системы в старых координатах
     * @param b начало коориднаты y новой системы в старых координатах
     * @param x кооридната x точки, для которой нужно получить относительную координату
     * @param y кооридната y точки, для которой нужно получить относительную координату
     * @param angle угол поворота координаты
     * @return относительная коориданата y (координата по y точки в новой системе отсчета)
     */
    public static double relativeY(double a, double b, double x, double y, double angle) {
        return -(x-a)*Math.sin(Math.toRadians(angle))+(y-b)*Math.cos(Math.toRadians(angle));
    }

    /**
     * Обратоное преобразование относительных кооридинат в глобальные координаты
     * @param a абсцисса начала координат игрока в глобальной системе координат
     * @param b ординта начала кооридинат игрока в глобальной системе координат
     * @param x координата x точки в системе координат игрока
     * @param y кооридната y точки в системе координта игрока
     * @param angle угол поворота системы координат игрока (глобальный угол поворота тела + угол поворота головы относительно тела)
     * @return координата x точки в глобальной системе координат
     */
    public static double unRelativeX(double a, double b, double x, double y, double angle) {
        return x*Math.cos(Math.toRadians(angle)) - y*Math.sin(Math.toRadians(angle)) + a;
    }

    /**
     * Обратоное преобразование относительных кооридинат в глобальные координаты
     * @param a абсцисса начала координат игрока в глобальной системе координат
     * @param b ординта начала кооридинат игрока в глобальной системе координат
     * @param x координата x точки в системе координат игрока
     * @param y кооридната y точки в системе координта игрока
     * @param angle угол поворота системы координат игрока (глобальный угол поворота тела + угол поворота головы относительно тела)
     * @return координата y точки в глобальной системе координат
     */
    public static double unRelativeY(double a, double b, double x, double y, double angle) {
        return x*Math.sin(Math.toRadians(angle)) + y*Math.cos(Math.toRadians(angle)) + b;
    }

    /**
     * Угол точки в полярных координатах
     * @param x координата x точки
     * @param y координата y точки
     * @return угол
     */
    public static double polarAngle(double x, double y) {
        if (x ==0 & y ==0) {
            return 0;
        }
        double angle = Math.toDegrees(Math.atan(y/x));
        if (x<0 & y<=0) {
            angle = -180 + angle;
        } else {
            if (x<0 & y>0) {
                angle = 180 + angle;
            }
        }
        return angle;
    }

    /**
     * Полярная коориданата r
     * @param x координата x точки
     * @param y координата y точки
     * @return rs
     */
    public static double polarModule(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Перевод полярных координат в декартовы
     *
     * @param module модуль вектора
     * @param angle угол в радианах
     * @return декартовы координаты точки
     */
    public static FieldObject polarToDecart(double module, double angle) {
        FieldObject decart = new FieldObject();
        decart.setPosX(module * Math.cos(angle));
        decart.setPosY(module * Math.sin(angle));
        return decart;
    }

    /**
     * Вычисляет угол в радианах по теореме косинусов по трем сторонам треугольника.
     *
     * @param firstAdjoiningSide первая прилигающая сторона к углу, который хотим найти
     * @param secondAdjoingSide вторая прилигающая сторона к углу, который хотим найти
     * @param opposingSide противолежащая сторона угла, который хотим найти
     * @return угол в радианах
     */
    public static double getAngleInRadiansByCosTheorem(double firstAdjoiningSide, double secondAdjoingSide, double opposingSide) {
        return Math.acos((Math.pow(firstAdjoiningSide, 2) + Math.pow(secondAdjoingSide, 2) - Math.pow(opposingSide, 2))
                / (2 * firstAdjoiningSide * secondAdjoingSide));
    }
}

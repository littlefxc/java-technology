package com.littlefxc.examples.base.typeinfo;

/**
 * @author fengxuechao
 * @date 2019-05-21
 */
public class Shapes {

    abstract class Shape{
        void draw() {
            System.out.println(this + ".draw()");
        }

        @Override
        abstract public String toString();
    }

    /**
     * 圆形
     */
    class Circle extends Shape {

        @Override
        public String toString() {
            return "Circle";
        }
    }

    /**
     * 方形
     */
    class Square extends Shape {

        @Override
        public String toString() {
            return "Square";
        }
    }

    /**
     * 三角形
     */
    class Triangle extends Shape {

        @Override
        public String toString() {
            return "Triangle";
        }
    }

}

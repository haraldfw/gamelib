package com.wilhelmsen.gamelib.utils;

public class MathUtils {

    public static int BoolToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static float vectorLength(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float vectorLengthSquared(float x, float y) {
        return x * x + y * y;
    }

    public static float lowest(float[] list) {
        float low = list[0];
        for (float check : list) {
            if (low < check) {
                low = check;
            }
        }
        return low;
    }

    public static float bound(float f, float min, float max) {
        if (f < min) {
            f = min;
        } else if (f > max) {
            f = max;
        }
        return f;
    }

    /**
     * Converts a given HSL(Hue Saturation Lightness)-color to RGBA
     *
     * @param h The hue of the color(0-360)
     * @param s The saturation of the color(0-1)
     * @param l The lightness(or brightness) of the color(0-1)
     * @return A float-array containing values from 0 to 1 in order RGBA
     */
    public static float[] HSLtoRGB(float h, float s, float l) {
        float[] c = new float[]{0, 0, 0, 1};
        h = ((h / 360) % 1) * 360;
        int i;
        float f, p, q, t;
        if (s == 0) {
            c[0] = c[1] = c[2] = l;
            return c;
        }
        h /= 60;
        i = (int) h;
        f = h - i;
        p = l * (1 - s);
        q = l * (1 - s * f);
        t = l * (1 - s * (1 - f));
        switch (i) {
            case 0:
                c[0] = l;
                c[1] = t;
                c[2] = p;
                break;
            case 1:
                c[0] = q;
                c[1] = l;
                c[2] = p;
                break;
            case 2:
                c[0] = p;
                c[1] = l;
                c[2] = t;
                break;
            case 3:
                c[0] = p;
                c[1] = q;
                c[2] = l;
                break;
            case 4:
                c[0] = t;
                c[1] = p;
                c[2] = l;
                break;
            default:                // case 5:
                c[0] = l;
                c[1] = p;
                c[2] = q;
                break;
        }
        return c;
    }

    public static boolean isPrime(double n) {
        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Linear interpolation between the two given numbers.
     *
     * @param a Start
     * @param b End
     * @param f Fraction, should be between 0 and 1
     * @return The lerped float
     */
    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
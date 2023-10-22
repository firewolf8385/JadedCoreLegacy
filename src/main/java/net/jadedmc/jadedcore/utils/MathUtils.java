/*
 * This file is part of JadedCoreLegacy, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedcore.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A group of math-related utilities.
 */
public class MathUtils {

    /**
     * Calculate the distance between two Locations.
     * @param a First location.
     * @param b Second location.
     * @return Distance (in blocks) between them.
     */
    public static double distance(final Location a, final Location b) {
        return Math.sqrt(square(a.getX() - b.getX()) + square(a.getY() - b.getY()) + square(a.getZ() - b.getZ()));
    }

    /**
     * Calculate the percentage obtained compared to a total.
     * @param currentValue Obtained amount.
     * @param maxValue Total amount.
     * @return Percentage of total.
     */
    public static int percent(final double currentValue, final double maxValue) {
        double percent = (currentValue/maxValue) * 100;
        return (int) percent;
    }

    /**
     * Rotates a vector a given angle.
     * @param vector Vector to rotate.
     * @param whatAngle Angle (in radians).
     * @return Rotated vector.
     */
    public static Vector rotateVector(final Vector vector, final double whatAngle) {
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }

    /**
     * Rounds a double to a set number of decimal places.
     * @param value The double to be rounded.
     * @param places Number of decimal places.
     * @return Rounded double.
     */
    public static double round(final double value, final int places) {
        if(places > 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * Square a given double.
     * @param x Double to square.
     * @return Squared double.
     */
    public static double square(final double x) {
        return x * x;
    }
}
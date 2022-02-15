package lethbridge.core;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
@LocalBean
public class BlackScholesFormula implements Serializable {

    private static double NormDist(double x) {
        double d1 = 0.0498673470;
        double d2 = 0.0211410061;
        double d3 = 0.0032776263;
        double d4 = 0.0000380036;
        double d5 = 0.0000488906;
        double d6 = 0.0000053830;
        double a = Math.abs(x);
        double t = 1.0 + a * (d1 + a * (d2 + a * (d3 + a * (d4 + a * (d5 + a * d6)))));
        t *= t;
        t *= t;
        t *= t;
        t *= t;
        t = 1.0 / (t + t);
        if (x >= 0) t = 1 - t;
        return t;
    }

    /*
        EXERCISE PRICE -> k = Strike price
        UNDERLYING PRICE -> s = Spot price of underlying stock/asset
        TIME TO EXPIRATION -> t = Time in years until option expiration (maturity)
        RISK FREE RATE -> r = Risk free annual interest rate continuously compounded
        VOLATILITY -> v = Implied volatility of returns of underlying stock/asset
    */

    public static double[] calculate(double s, double k, double r, double t, double v) {
        t /= 365.0d;
        r /= 100.0d;
        v /= 100.0d;

        double[] p = new double[12];

        p[10] = ((Math.log(s / k) + (r + Math.pow(v, 2) / 2) * t) / (v * Math.sqrt(t)));
        p[11] = (p[10] - (v * Math.sqrt(t)));

        /* price_c */
        p[0] = (s * NormDist(p[10]) - k * Math.exp(-(r * t)) * NormDist(p[11]));
        /* price_p */
        p[1] = (k * Math.exp(-(r * t)) * NormDist(-p[11]) - s * NormDist(-p[10]));

        /* delta_c */
        p[4] = (NormDist(p[10]));
        /* delta_p */
        p[5] = (NormDist(p[10]) - 1);

        /* gamma */
        p[2] = ((Math.exp(-(Math.pow(p[10], 2)) / 2) / Math.sqrt(2 * Math.PI)) / (s * v * Math.sqrt(t)));
        /* vega */
        p[3] = (s * Math.sqrt(t) * (Math.exp(-(Math.pow(p[10], 2)) / 2) / Math.sqrt(2 * Math.PI)) / 100);

        /* theta_c */
        p[6] = ((-(s * (Math.exp(-(Math.pow(p[10], 2)) / 2) / Math.sqrt(2 * Math.PI)) * v) / (2 * Math.sqrt(t)) - r * k * Math.exp(-(r * t)) * NormDist(p[11])) / 365);
        /* theta_p */
        p[7] = ((-(s * (Math.exp(-(Math.pow(p[10], 2)) / 2) / Math.sqrt(2 * Math.PI)) * v) / (2 * Math.sqrt(t)) + r * k * Math.exp(-(r * t)) * NormDist(-p[11])) / 365);

        /* rho_c */
        p[8] = (k * t * Math.exp(-(r * t)) * NormDist(p[11]) / 100);
        /* rho_p */
        p[9] = (-k * t * Math.exp(-(r * t)) * NormDist(-p[11]) / 100);

        return p;
    }

}

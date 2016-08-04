package webonise.mapboxdemo;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolygonBuffer {

    public double[] computeDistanceAndBearing(double lat1, double lon1,
                                              double lat2, double lon2) {
        // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
        // using the "Inverse Formula" (section 4)
        double results[] = new double[3];
        int MAXITERS = 20;
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda
                    / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2
                    / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B
                    * sinSigma
                    * // (6)
                    (cos2SM + (B / 4.0)
                            * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0) * cos2SM
                            * (-3.0 + 4.0 * sinSigma * sinSigma)
                            * (-3.0 + 4.0 * cos2SMSq)));

            lambda = L
                    + (1.0 - C)
                    * f
                    * sinAlpha
                    * (sigma + C * sinSigma
                    * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }

        double distance = (b * A * (sigma - deltaSigma));
        results[0] = distance;
        if (results.length > 1) {
            double initialBearing = Math.atan2(cosU2 * sinLambda, cosU1 * sinU2
                    - sinU1 * cosU2 * cosLambda);
            initialBearing *= 180.0 / Math.PI;
            results[1] = initialBearing;
            if (results.length > 2) {
                double finalBearing = Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2
                        + cosU1 * sinU2 * cosLambda);
                finalBearing *= 180.0 / Math.PI;
                results[2] = finalBearing;
            }
        }

        return results;
    }

    public double[] computeDestinationAndBearing(double lat1, double lon1,
                                                 double brng, double dist) {
        double results[] = new double[3];
        double a = 6378137, b = 6356752.3142, f = 1 / 298.257223563; // WGS-84
        // ellipsiod
        double s = dist;
        double alpha1 = toRad(brng);
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(toRad(lat1));
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384
                * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double sinSigma = 0, cosSigma = 0, deltaSigma = 0, cos2SigmaM = 0;
        double sigma = s / (b * A), sigmaP = 2 * Math.PI;

        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            deltaSigma = B
                    * sinSigma
                    * (cos2SigmaM + B
                    / 4
                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6
                    * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma)
                    * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = s / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1
                * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda
                - (1 - C)
                * f
                * sinAlpha
                * (sigma + C * sinSigma
                * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        double lon2 = (toRad(lon1) + L + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // normalise
        // to
        // -180...+180

        double revAz = Math.atan2(sinAlpha, -tmp); // final bearing, if required

        results[0] = toDegrees(lat2);
        results[1] = toDegrees(lon2);
        results[2] = toDegrees(revAz);
        return results;
    }

    private double toRad(double angle) {
        return angle * Math.PI / 180;
    }

    private double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    public LatLng computeIntersectionPoint(LatLng p1, double brng1, LatLng p2, double brng2) {
        double lat1 = toRad(p1.getLatitude()), lng1 = toRad(p1.getLongitude());
        double lat2 = toRad(p2.getLatitude()), lng2 = toRad(p2.getLongitude());
        double brng13 = toRad(brng1), brng23 = toRad(brng2);
        double dlat = lat2 - lat1, dlng = lng2 - lng1;
        double delta12 = 2 * Math.asin(Math.sqrt(Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlng / 2) * Math.sin(dlng / 2)));

        if (delta12 == 0) return null;


        double initBrng1 = Math.acos((Math.sin(lat2) - Math.sin(lat1) * Math.cos(delta12)) / (Math.sin(delta12) * Math.cos(lat1)));

        double initBrng2 = Math.acos((Math.sin(lat1) - Math.sin(lat2) * Math.cos(delta12)) / (Math.sin(delta12) * Math.cos(lat2)));

        double brng12 = Math.sin(lng2 - lng1) > 0 ? initBrng1 : 2 * Math.PI - initBrng1;
        double brng21 = Math.sin(lng2 - lng1) > 0 ? 2 * Math.PI - initBrng2 : initBrng2;


        double alpha1 = (brng13 - brng12 + Math.PI) % (2 * Math.PI) - Math.PI;
        double alpha2 = (brng21 - brng23 + Math.PI) % (2 * Math.PI) - Math.PI;

        double alpha3 = Math.acos(-Math.cos(alpha1) * Math.cos(alpha2) + Math.sin(alpha1) * Math.sin(alpha2) * Math.cos(delta12));
        double delta13 = Math.atan2(Math.sin(delta12) * Math.sin(alpha1) * Math.sin(alpha2), Math.cos(alpha2) + Math.cos(alpha1) * Math.cos(alpha3));
        double lat3 = Math.asin(Math.sin(lat1) * Math.cos(delta13) + Math.cos(lat1) * Math.sin(delta13) * Math.cos(brng13));
        double dlng13 = Math.atan2(Math.sin(brng13) * Math.sin(delta13) * Math.cos(lat1), Math.cos(delta13) - Math.sin(lat1) * Math.sin(lat3));
        double lng3 = lng1 + dlng13;

        return new LatLng(toDegrees(lat3), (toDegrees(lng3) + 540) % 360 - 180);
    }

    public List<LatLng> buffer(List<LatLng> lngs) {
        List<LatLng> bufferedLatLngList = new ArrayList<>();
        List<LatLng> finalPointList = new ArrayList<>();
        List<Double> finalBearingList = new ArrayList<>();
        for (int i = 0; i < lngs.size(); i++) {
            int nextPosition = i == lngs.size() - 1 ? 0 : i + 1;
            LatLng first = lngs.get(i);
            LatLng second = lngs.get(nextPosition);

            double[] dnb = computeDistanceAndBearing(first.getLatitude(),
                    first.getLongitude(), second.getLatitude(), second.getLongitude());

            double[] deNB = computeDestinationAndBearing(first.getLatitude(),
                    first.getLongitude(), dnb[2], -5);

            LatLng finalP = new LatLng(deNB[0], deNB[1]);
            finalPointList.add(finalP);
            finalBearingList.add(deNB[2]);
        }
        for (int i = 0; i < finalPointList.size(); i++) {
            int nextPosition = i == lngs.size() - 1 ? 0 : i + 1;
            LatLng firstPoint = finalPointList.get(i);
            LatLng nextPoint = finalPointList.get(nextPosition);
            double firstBearing = finalBearingList.get(i);
            double nextBearing = finalBearingList.get(nextPosition);
            LatLng finalBufferPoint = computeIntersectionPoint(firstPoint, nextBearing,
                    nextPoint, firstBearing);
            LatLng finalPoint = new LatLng(finalBufferPoint.getLatitude() * -1, finalBufferPoint
                    .getLongitude() * -1);
            bufferedLatLngList.add(finalPoint);
        }
        bufferedLatLngList.add(bufferedLatLngList.get(0));
        return bufferedLatLngList;
    }
}

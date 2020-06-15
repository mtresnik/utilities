package com.resnik.util.rest;

import com.resnik.util.geo.GeoUtils;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.histogram.Histogram;
import com.resnik.util.math.plot.histogram.HistogramData;
import com.resnik.util.math.stats.StatisticsUtils;
import com.resnik.util.objects.structures.tree.TreeNode;
import com.resnik.util.serial.geo.kml.KML;
import com.resnik.util.serial.geo.kml.feature.Document;
import com.resnik.util.serial.geo.kml.feature.Placemark;
import com.resnik.util.serial.geo.kml.geometry.Point;
import com.resnik.util.serial.json.JSONElement;
import com.resnik.util.serial.json.JSONNode;
import com.resnik.util.serial.json.JSONTree;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestRest {

    @Test
    public void testRest() throws IOException {
        List<double[]> foodLocations = getFoodLion();
        long waitTime = 5 * 1000;
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() < start + waitTime){
            // Wait
        }
        List<double[]> abcLocations = getABC();

        List<Double> minDistances = new ArrayList<>();
        double minMin = Double.MAX_VALUE;
        double maxMin = - Double.MAX_VALUE;
        for(double[] loc : foodLocations){
            double lat1 = loc[0];
            double lon1 = loc[1];
            double minDist = Double.MAX_VALUE;
            for(double[] abcLoc : abcLocations){
                double lat2 = abcLoc[0];
                double lon2 = abcLoc[1];
                double dist = GeoUtils.haversine(lat1, lon1, lat2, lon2);
                if(dist < minDist){
                    minDist = dist;
                }
            }
            minDistances.add(minDist);
            minMin = Math.min(minMin, minDist);
            maxMin = Math.max(maxMin, minDist);
        }

        double mean = StatisticsUtils.mean(minDistances);
        double std = StatisticsUtils.std(minDistances);

        double[] newVals = new double[minDistances.size()];
        for(int i = 0; i < minDistances.size(); i++){
            newVals[i] = minDistances.get(i);
        }
        HistogramData histogramData = HistogramData.fromArray(newVals, 0.0, maxMin, (maxMin) / 100);
        Histogram histogram = new Histogram(1280, 720);
        histogram.add("coor", histogramData);
        byte[][][] bars = histogram.getBarsImage();
        ImageUtils.saveImageBytes(bars, "src/res/coor.bmp");

        Log.d("REST", mean);
        Log.d("REST", std);
        Log.d("REST", "min:" + minMin);
        Log.d("REST", "max:" + maxMin);

    }

    public List<double[]> getFoodLion() throws IOException {
        double minLat = 33.842316;
        double minLon = -84.321869;
        double maxLat = 36.588117;
        double maxLon = -75.460621;

        String q = "<osm-script output=\"json\" output-config=\"\" timeout=\"25\">\n" +
                "  <union into=\"_\">\n" +
                "    <query into=\"_\" type=\"node\">\n" +
                "      <has-kv k=\"brand:wikidata\" modv=\"\" v=\"Q1435950\"/>\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"supermarket\"/>\n" +
                "      <bbox-query s=\""+ minLat+ "\" w=\""+ minLon + "\" n=\""+ maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"way\">\n" +
                "      <has-kv k=\"brand:wikidata\" modv=\"\" v=\"Q1435950\"/>\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"supermarket\"/>\n" +
                "      <bbox-query s=\""+ minLat+ "\" w=\""+ minLon + "\" n=\""+ maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"relation\">\n" +
                "      <has-kv k=\"brand:wikidata\" modv=\"\" v=\"Q1435950\"/>\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"supermarket\"/>\n" +
                "      <bbox-query s=\""+ minLat+ "\" w=\""+ minLon + "\" n=\""+ maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "  </union>\n" +
                "  <print e=\"\" from=\"_\" geometry=\"skeleton\" ids=\"yes\" limit=\"\" mode=\"body\" n=\"\" order=\"id\" s=\"\" w=\"\"/>\n" +
                "  <recurse from=\"_\" into=\"_\" type=\"down\"/>\n" +
                "  <print e=\"\" from=\"_\" geometry=\"skeleton\" ids=\"yes\" limit=\"\" mode=\"skeleton\" n=\"\" order=\"quadtile\" s=\"\" w=\"\"/>\n" +
                "</osm-script>";
        HTTPResponse response = HTTPRequest
                .post("http://overpass-api.de/api/interpreter")
                .putArgument("data", q)
                .putArgument("format", "json")
                .putArgument("accept-language", "en")
                .send();
        return writeKML(response, "Food Lion", "foodlion");
    }

    public List<double[]> writeKML(HTTPResponse response, String filter, String kmlName) throws IOException{
        try {
            JSONTree tree = response.toJSON();
            JSONNode<JSONElement> root = (JSONNode<JSONElement>)tree.getRoot();
            JSONNode<JSONElement> elements = root.findChildByName("elements");
            List<double[]> locations = new ArrayList<>();
            for(TreeNode<JSONElement> child : elements.getChildren()){
                if(child instanceof JSONNode){
                    JSONNode<JSONElement> tags = ((JSONNode<JSONElement>) child).findChildByName("tags");
                    if(tags != null){
                        JSONNode<JSONElement> nameTag = tags.findChildByName("\"name\"");
                        if(nameTag != null){
                            Object nameObj = nameTag.value;
                            String nameString = Objects.toString(nameObj);
                            if(!nameString.contains(filter)){
                                continue;
                            }
                        }
                    }
                    // Just put lat lon in list
                    try{
                        Object LAT_O = ((JSONNode<JSONElement>) child).findChildByName("\"lat\"").value;
                        Object LON_O = ((JSONNode<JSONElement>) child).findChildByName("\"lon\"").value;
                        double LAT = (double) LAT_O;
                        double LON = (double) LON_O;
                        double[] loc = new double[]{LAT, LON};
                        locations.add(loc);
                    }catch (Exception e){
                        continue;
                    }
                }
            }
            KML kml = new KML();
            Document doc = new Document();
            for(double[] loc : locations){
                Placemark placemark = new Placemark();
                Point point = new Point();
                point.setCoordinates((float) loc[1], (float) loc[0]);
                placemark.setGeometry(point);
                placemark.setName(filter);
                doc.addChild(placemark);
            }
            kml.setDocument(doc);
            kml.save("src/res/" + kmlName + ".kml");
            Log.d("REST", elements);
            return locations;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<double[]> getABC() throws IOException{
        //double[] bbox = Polygon.boundingBox(lat, lon, meters);
        // -84.321869	33.842316	-75.460621	36.588117
        double minLat = 33.842316;
        double minLon = -84.321869;
        double maxLat = 36.588117;
        double maxLon = -75.460621;
        String q = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<osm-script output=\"json\" output-config=\"\" timeout=\"25\">\n" +
                "  <union into=\"_\">\n" +
                "    <query into=\"_\" type=\"node\">\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"alcohol\"/>\n" +
                "      <bbox-query s=\"" + minLat + "\" w=\"" + minLon + "\" n=\"" + maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"way\">\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"alcohol\"/>\n" +
                "      <bbox-query s=\"" + minLat + "\" w=\"" + minLon + "\" n=\"" + maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"relation\">\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"alcohol\"/>\n" +
                "      <bbox-query s=\"" + minLat + "\" w=\"" + minLon + "\" n=\"" + maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "  </union>\n" +
                "  <print e=\"\" from=\"_\" geometry=\"skeleton\" ids=\"yes\" limit=\"\" mode=\"body\" n=\"\" order=\"id\" s=\"\" w=\"\"/>\n" +
                "  <recurse from=\"_\" into=\"_\" type=\"down\"/>\n" +
                "  <print e=\"\" from=\"_\" geometry=\"skeleton\" ids=\"yes\" limit=\"\" mode=\"skeleton\" n=\"\" order=\"quadtile\" s=\"\" w=\"\"/>\n" +
                "</osm-script>";
        HTTPResponse response = HTTPRequest
                .post("http://overpass-api.de/api/interpreter")
                .putArgument("data", q)
                .putArgument("format", "json")
                .putArgument("accept-language", "en")
                .send();
        return writeKML(response, "ABC", "abc");
    }

}

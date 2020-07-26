package com.resnik.util.serial.geo.kml;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.shapes.ConcaveHull;
import com.resnik.util.math.shapes.PolygonPoint;
import com.resnik.util.math.stats.StatisticsUtils;
import com.resnik.util.objects.structures.tree.TreeNode;
import com.resnik.util.rest.HTTPRequest;
import com.resnik.util.rest.HTTPResponse;
import com.resnik.util.serial.csv.CSV;
import com.resnik.util.serial.json.JSONElement;
import com.resnik.util.serial.json.JSONNode;
import com.resnik.util.serial.json.JSONTree;
import com.resnik.util.serial.xml.XMLElement;
import com.resnik.util.serial.xml.XMLNode;
import com.resnik.util.serial.xml.XMLTree;
import com.resnik.util.serial.xml.XMLTreeParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TestCounties {


    public void testCounties() throws IOException {
        XMLTree tree = null;
        try {
            tree = XMLTreeParser.fromFileLocation("res/counties.kml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        List<XMLNode> placemarks = tree.findAllByName("Placemark");
        Map<String, List<PolygonPoint>> countyMap = new LinkedHashMap<>();
        for(XMLNode placemark : placemarks){
            List<XMLNode> simpleDataList = placemark.findAllByName("SimpleData");
            String countyName = null;
            for(XMLNode simpleData : simpleDataList){
                XMLElement element = (XMLElement) simpleData.getValue();
                String name = Objects.toString(element.getOrDefault("name", null));
                if(name.equals("CO_NAME")){
                    countyName = element.getNodeInner().toLowerCase();
                }
            }
            List<XMLNode> coordinates = placemark.findAllByName("coordinates");
            XMLNode coords = coordinates.get(0);
            Object inner = coords.value;
            if(inner instanceof XMLElement){
                List<PolygonPoint> points = new ArrayList<>();
                String coordRep =((XMLElement) inner).getNodeInner();
                String[] splitString = coordRep.split(" ");
                for(String encodedCoordinate : splitString){
                    String[] lonLat = encodedCoordinate.split(",");
                    if(lonLat.length < 2){
                        continue;
                    }
                    double lon = Double.parseDouble(lonLat[0]);
                    double lat = Double.parseDouble(lonLat[1]);
                    points.add(new PolygonPoint(lon, lat));
                }
                countyMap.put(countyName, points);
                Log.d("TestCounties", countyMap.size());
            }
        }
        double ratio = 1600.0/900;
        double num = 2000;


        File popFile = new File("res/popnc.csv");
        File incomeFile = new File("res/incomenc.csv");
        try {
            Scanner scanner = new Scanner(popFile);
            String header = scanner.nextLine();
            String[] headerArr =header.split(",");
            CSV csv = new CSV(headerArr);
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                csv.addLine(line);
            }
            scanner.close();
            Map<String, Double> populationMap = csv.getNumberMap(0, 12);

            scanner = new Scanner(incomeFile);
            header = scanner.nextLine();
            headerArr =header.split(",");
            csv = new CSV(headerArr);
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                csv.addLine(line);
            }
            scanner.close();
            Map<String, Double> incomeMap = csv.getNumberMap(1, 3);
            double maxIncome = -1;
            for(Map.Entry<String, Double> entry : incomeMap.entrySet()){
                maxIncome = Math.max(maxIncome, entry.getValue());
            }
            double[] populations = new double[populationMap.size()];
            double[] incomes = new double[incomeMap.size()];
            int count = 0;
            for(Map.Entry<String, Double> entry : populationMap.entrySet()){
                populations[count] = entry.getValue();
                incomes[count] = incomeMap.get(entry.getKey());
                count++;
            }
            Log.e("TestCounties", StatisticsUtils.cor(populations, incomes));


            Map<ConcaveHull, byte[]> colorMap = new LinkedHashMap<>();
            List<double[]> supermarkets = new ArrayList<>();
            for(Map.Entry<String, Double> entry : incomeMap.entrySet()){
                double income = entry.getValue();
                double t = income / maxIncome;
//                double t = 0.0;
//                if(pop < 10_000){
//                    t = 1.0 / 8;
//                }else if(pop < 30_000){
//                    t = 2.0 / 8;
//                }else if (pop < 40_000){
//                    t = 3.0 / 8;
//                }else if (pop < 70_000){
//                    t = 5.0 / 8;
//                }else if (pop < 100_000){
//                    t = 6.0 / 8;
//                }else if (pop < 500_000){
//                    t = 7.0 / 8;
//                }else{
//                    t = 1.0;
//                }

                if(!countyMap.containsKey(entry.getKey())){
                    Log.e("TestCounties", "Doesn't contain:" + entry.getKey());
                    continue;
                }
                List<PolygonPoint> points = countyMap.get(entry.getKey());
                int base = 64;
                int scale = (int)(128 * (1 - t)) + 32;
                byte[] color = new byte[]{(byte) scale,(byte) scale,(byte) (scale + base),(byte) 255};
                ConcaveHull hull = new ConcaveHull(points);
                hull.name = entry.getKey();
                colorMap.put(hull, color);
                double minLon = hull.minX;
                double maxLon = hull.maxX;
                double minLat = hull.minY;
                double maxLat = hull.maxY;
                try{
                    supermarkets.addAll(getSupermarkets(minLat, minLon, maxLat, maxLon));
                }catch (Exception ex){
                    long start = System.currentTimeMillis();
                    while(System.currentTimeMillis() - start < 20000){}
                    supermarkets.addAll(getSupermarkets(minLat, minLon, maxLat, maxLon));
                }
                long start = System.currentTimeMillis();
                while(System.currentTimeMillis() - start < 3000){}
                Log.d("TestCounties", "Done waiting");
            }
            byte[][][] image = ConcaveHull.commit(1600, 1280, colorMap, new ArrayList<>(), supermarkets, populationMap, incomeMap);
            try {
                ImageUtils.saveImageBytes(image, "res/counties.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public List<double[]> extractPoints(HTTPResponse response, String filter){
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
                            if(filter != null){
                                if(!nameString.toLowerCase().contains(filter.toLowerCase())){
                                    continue;
                                }
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
        return extractPoints(response, null);
    }

    public List<double[]> getSupermarkets(double minLat, double minLon, double maxLat, double maxLon) throws IOException{
        String q = "<osm-script output=\"json\" output-config=\"\" timeout=\"25\">\n" +
                "  <union into=\"_\">\n" +
                "    <query into=\"_\" type=\"node\">\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"supermarket\"/>\n" +
                "      <bbox-query s=\""+ minLat+ "\" w=\""+ minLon + "\" n=\""+ maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"way\">\n" +
                "      <has-kv k=\"shop\" modv=\"\" v=\"supermarket\"/>\n" +
                "      <bbox-query s=\""+ minLat+ "\" w=\""+ minLon + "\" n=\""+ maxLat + "\" e=\"" + maxLon + "\"/>\n" +
                "    </query>\n" +
                "    <query into=\"_\" type=\"relation\">\n" +
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
        return extractPoints(response, null);
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
        return extractPoints(response, null);
    }

}

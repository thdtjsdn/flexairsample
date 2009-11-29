package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import sun.misc.Regexp;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

import flex2.tools.oem.Application;
import flex2.tools.oem.Configuration;
import flex2.tools.oem.VirtualLocalFile;
import flex2.tools.oem.VirtualLocalFileSystem;

//import flex2.tools.oem.Application;

public class Geo {
	
	private static double SIMPLIFY_DISTANCE_TOLERANCE = 0.01; // 0.001 0.005
	private static String GEN_VAR_REGEXP = "\\$\\{GENERATED_VARIABLES\\}";
	private static String CLASS_NAME_REGEXP = "\\$\\{CLASS_NAME\\}";
	private static Coordinate SCREEN_TARGET = new Coordinate(1024, 768);
	private static double Y_MULTIPLIER = 1.0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			geo();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void geo() throws FileNotFoundException, MalformedURLException, IOException {
		FeatureIterator<SimpleFeature> iter;
		SimpleFeature feat;
		
		File file = new File("c:/appdata/archi-tech-gis/co27_d00_shp/co27_d00.shp");
		ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
		
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];
		
		System.out.println("Reading content " + typeName);
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
		
		SimpleFeatureType simpleFeatureType = featureSource.getSchema();
		System.out.println("Header: " + DataUtilities.spec( simpleFeatureType ));
		
		DefaultQuery query = new DefaultQuery();
		query.setTypeName(typeName);
		
		CoordinateReferenceSystem prj = simpleFeatureType.getCoordinateReferenceSystem();
		if (prj == null) {
			System.out.println("No projection fround for " + file + "!");
		}
		
		FeatureCollection<SimpleFeatureType, SimpleFeature> newcoll = new MemoryFeatureCollection(simpleFeatureType);
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
		System.out.println("collection has " + collection.size() + " features");
		iter = collection.features();
		
		SimpleFeature hennepin = null;
		SimpleFeature ramsey = null;
		try {
			while (iter.hasNext()) {
				feat = iter.next();
				if (feat.getAttribute("NAME").equals("Hennepin")) {
					hennepin = feat;
				} else if (feat.getAttribute("NAME").equals("Ramsey")) {
					ramsey = feat;
				} else {
					SimpleFeature clone = new SimpleFeatureImpl( feat.getAttributes(), simpleFeatureType, feat.getIdentifier() );
					clone.setDefaultGeometry(DouglasPeuckerSimplifier.simplify((Geometry)feat.getDefaultGeometry(), SIMPLIFY_DISTANCE_TOLERANCE));
					newcoll.add(clone);
				}
				//System.out.println(String.format("Feature: %s (%s)", feat.getAttribute("NAME"), feat.getID()));
			}
		} finally {
			if (iter != null) iter.close();
		}
		
		// combine hennepin and ramsey counties
		if (hennepin != null && ramsey != null) {
			System.out.println("Combining Hennepin and Ramsey counties...");
			Geometry geoHenn = (Geometry)hennepin.getDefaultGeometry();
			Geometry geoRams = (Geometry)ramsey.getDefaultGeometry();
			
			System.out.println(String.format(
				"[geoHenn:geoRams] = [contains:%s] [covers:%s] [crosses:%s] [touches:%s] [geometries:%s:%s] [points:%s:%s]",
				geoHenn.contains(geoRams), geoHenn.covers(geoRams), geoHenn.crosses(geoRams), geoHenn.touches(geoRams), geoHenn.getNumGeometries(), geoRams.getNumGeometries(), geoHenn.getNumPoints(), geoRams.getNumPoints()
			));
			Geometry geoHennirams = geoHenn.union(geoRams);
			System.out.println(String.format(
				"[orig/hennirams] = [geometries:%s] [points:%s]",
				geoHennirams.getNumGeometries(), geoHennirams.getNumPoints()
			));
			geoHennirams = DouglasPeuckerSimplifier.simplify(geoHennirams, SIMPLIFY_DISTANCE_TOLERANCE);
			System.out.println(String.format(
				"[simp/hennirams] = [geometries:%s] [points:%s]",
				geoHennirams.getNumGeometries(), geoHennirams.getNumPoints()
			));
			
			// add merged shape to new collection
			SimpleFeature hennirams = new SimpleFeatureImpl( hennepin.getAttributes(), simpleFeatureType, hennepin.getIdentifier() );
			hennirams.setDefaultGeometry(geoHennirams);
			hennirams.setAttribute("NAME", "Hennirams");
			newcoll.add(hennirams);
			
			// write out
//			File newFile = new File("c:/appdata/archi-tech-gis/co_hennirams/hennirams.shp");
//			DataStoreFactorySpi factory = new ShapefileDataStoreFactory();
//			Map<String, Serializable> create = new HashMap<String,Serializable>();
//			create.put("url", newFile.toURI().toURL());
//			DataStore newstore = factory.createNewDataStore(create);
//			
//			newstore.createSchema(simpleFeatureType);
//			FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) newstore.getFeatureSource(typeName);
//			featureStore.addFeatures(newcoll);
			
			// generate swf module
			File mapTmpl = getWorkDirFile("Map.as.tmpl");
			if (mapTmpl.exists()) {
				// generate actionscript shape definitions
				StringBuffer as3 = new StringBuffer();
				int index = 0;
				String points; // = "100,350, 0,500, 200,800, -100,800, -100,300";
				iter = newcoll.features();
				ReferencedEnvelope env = collection.getBounds();
				double offsetY = env.centre().y;
				double offsetX = env.centre().x;
				double outputMultiplier = Math.min(SCREEN_TARGET.x/env.getWidth(), SCREEN_TARGET.y/env.getHeight());
				while (iter.hasNext()) {
					feat = iter.next();
					Geometry geom = (Geometry)feat.getDefaultGeometry();
					int numgeo = geom.getNumGeometries();
					for (int j=0; j<numgeo; j++) {
						points = "";
						boolean some = false;
						Coordinate coords[] = geom.getGeometryN(j).getCoordinates();
						for (int k=coords.length-1; k>=0; k--) {
							Coordinate coord = coords[k];
							if (some) points += ",";
							points += String.format("%1$.2f,%2$.2f", (coord.x-offsetX)*outputMultiplier, (coord.y-offsetY)*Y_MULTIPLIER*outputMultiplier);
							some = true;
						}
						as3.append( String.format("private const _names_%1$d:String = \"%2$s\";\n", index, feat.getAttribute("NAME") ) );
						as3.append( String.format("private const _points_%1$d:Array = [%2$s];\n", index, points) );
						index++;
					}
				}
				as3.append( String.format("private const _num:uint = %1$d;\n", index) );
				as3.append( String.format("private const _offsetX:Number = %1$.2f;\n", offsetX*outputMultiplier));
				as3.append( String.format("private const _offsetY:Number = %1$.2f;\n", offsetY*outputMultiplier));
				as3.append( String.format("private const _multiplier:Number = %1$.4f;\n", outputMultiplier));
				
				// read swf template
				FileReader fr = new FileReader(mapTmpl);
				BufferedReader br = new BufferedReader(fr);
				StringBuffer sb = new StringBuffer();
				String s;
				Pattern re = Pattern.compile(GEN_VAR_REGEXP);
				Pattern clname = Pattern.compile(CLASS_NAME_REGEXP);
				Matcher reMatch;
				Matcher clnameMatch;
				String as3ClassName = "GenShape"; // TODO: make function param - must be unique classnames
				String as3Package = "com.architech.maps";
				while ((s = br.readLine()) != null) {
					reMatch = re.matcher(s);
					clnameMatch = clname.matcher(s);
					if (clnameMatch.find()) {
						sb.append( clnameMatch.replaceAll(as3ClassName) + "\n" );
					} else if (reMatch.find()) {
						sb.append( reMatch.replaceAll(as3.toString() + "\n") );
					} else {
						sb.append( s + "\n" );
					}
				}
				System.out.println(sb.toString());
				
				
				
//				System.exit(0);
//				if (true) return;
				
				
				
				// generate map as3 file
				File parent = new File(".").getCanonicalFile();
				VirtualLocalFileSystem vlfs = new VirtualLocalFileSystem();
				File as3File = new File(parent, as3Package.replaceAll("\\.", "/") + "/" + as3ClassName + ".as");
				VirtualLocalFile vlf = vlfs.create(as3File.getCanonicalPath(), sb.toString(), parent, System.currentTimeMillis());
				// create app
				Application app = new Application(vlf);
				Configuration conf = app.getDefaultConfiguration();
				//conf.setConfiguration( new File("C:/Program Files/Adobe/Flex Builder 3/sdks/3.3.0/frameworks/flex-config.xml") );
				
				// add libs
				File swflib = getWorkDirFile("swflib");
				File libs[] = new File[] { new File(swflib, "ArchiTechLib.swc") };
				conf.addExternalLibraryPath( libs );
//				conf.optimize(true);
//				conf.useActionScript3(true);
//				conf.useHeadlessServer(true);
				// set config
				app.setConfiguration(conf);
				// build
				app.setOutput( new File("C:/Documents and Settings/nate/My Documents/Projects/MapTest/bin-debug/generated-map.swf") );
				if (app.build(true) > 0) {
					System.out.println("COMPILE OK");
				} else {
					System.out.println("COMPILE FAILED");
				}
			} else {
				throw new FileNotFoundException(mapTmpl.getName());
			}
		}
	}
	
	private static File getWorkDirFile(String filePath) {
		File file = null;
		File workDir = new File(System.getProperty("user.dir"));
		String testPaths[] = {"","build/classes","src/"};
		for (String test : testPaths) {
			file = new File(new File(workDir, test), filePath);
			if (!file.exists()) {
				file = null;
			} else {
				break;
			}
		}
		return file;
	}

}

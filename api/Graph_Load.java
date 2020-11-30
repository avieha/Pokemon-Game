package api;

import com.google.gson.*;

import javax.swing.*;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.Map;

public class Graph_Load implements JsonDeserializer<directed_weighted_graph> {
    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();
        JsonObject graphJsonObj = jsonObject.get("_nodesList").getAsJsonObject();
        JsonObject outJsonObj = jsonObject.get("_out").getAsJsonObject();
        JsonObject inJsonObj = jsonObject.get("_in").getAsJsonObject();
        for (Map.Entry<String, JsonElement> set : graphJsonObj.entrySet())
        {

            String hashKey = set.getKey(); //the key of the hashmap
            JsonElement jsonValueElement = set.getValue(); //the value of the hashmap as json element
            double nodeWeight = jsonValueElement.getAsJsonObject().get("_weight").getAsDouble();
            String nodeInfo = jsonValueElement.getAsJsonObject().get("_info").getAsString();
            int nodeTag = jsonValueElement.getAsJsonObject().get("_tag").getAsInt();
            int nodeKey = jsonValueElement.getAsJsonObject().get("_key").getAsInt();
            JsonObject locationJsonObj = jsonValueElement.getAsJsonObject().get("_location").getAsJsonObject();
            double x=locationJsonObj.get("_x").getAsDouble();
            double y=locationJsonObj.get("_y").getAsDouble();
            double z=locationJsonObj.get("_z").getAsDouble();
            node_data node = new NodeData(nodeKey);
            geo_location geo=new NodeData.GeoLocation(x,y,z);
            node.setLocation(geo);
            node.setTag(nodeTag);
            node.setInfo(nodeInfo);
            node.setWeight(nodeWeight);
            graph.addNode(node);
        }
        for (Map.Entry<String, JsonElement> setout : outJsonObj.entrySet()){
            JsonElement jsonValueElement = setout.getValue();
            JsonObject edge=jsonValueElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> edgeOut : edge.entrySet()){
                JsonElement outjsonValueElement = edgeOut.getValue();
                int src=outjsonValueElement.getAsJsonObject().get("_src").getAsInt();
                int dest=outjsonValueElement.getAsJsonObject().get("_dest").getAsInt();
                double weight=outjsonValueElement.getAsJsonObject().get("_weight").getAsDouble();
                String info=outjsonValueElement.getAsJsonObject().get("_info").getAsString();
                int tag=outjsonValueElement.getAsJsonObject().get("_tag").getAsInt();
                graph.connect(src,dest,weight);
                graph.getEdge(src,dest).setInfo(info);
                graph.getEdge(src,dest).setTag(tag);
            }


        }

        return graph;
    }
}

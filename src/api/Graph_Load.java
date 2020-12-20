package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Graph_Load implements JsonDeserializer<directed_weighted_graph> {

    /**
     * this method takes json element and
     * turns it into a directed weighted graph(DWGraph_DS Object)
     */
    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        directed_weighted_graph graph = new DWGraph_DS();
        JsonArray nodesJsonObj = jsonObject.getAsJsonArray("Nodes");
        JsonArray edgesJsonObj = jsonObject.getAsJsonArray("Edges");
        for (int i = 0; i < nodesJsonObj.size(); i++) {
            JsonObject node = nodesJsonObj.get(i).getAsJsonObject();
            int nodeTag = node.get("id").getAsInt();
            String nodeInfo = node.get("pos").getAsString();
            double x = Double.parseDouble(nodeInfo.substring(0, nodeInfo.indexOf(',')));
            double y = Double.parseDouble(nodeInfo.substring(nodeInfo.indexOf(',') + 1, nodeInfo.lastIndexOf(',')));
            double z = Double.parseDouble(nodeInfo.substring(nodeInfo.lastIndexOf(',') + 1, nodeInfo.length()));
            geo_location p = new NodeData.GeoLocation(x, y, z);
            node_data n = new NodeData(nodeTag);
            graph.addNode(n);
            graph.getNode(nodeTag).setLocation(p);
        }
        for (int i = 0; i < edgesJsonObj.size(); i++) {
            JsonObject edge = edgesJsonObj.get(i).getAsJsonObject();
            int src = edge.get("src").getAsInt();
            double w = edge.get("w").getAsDouble();
            int dest = edge.get("dest").getAsInt();
            graph.connect(src, dest, w);
        }
        return graph;
    }
}
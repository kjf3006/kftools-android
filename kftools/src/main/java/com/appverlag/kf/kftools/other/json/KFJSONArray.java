//package com.appverlag.kf.kftools.other.json;
//
//import androidx.annotation.NonNull;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.Iterator;
//
//public class KFJSONArray extends JSONArray {
//
//    @Override
//    public JSONArray optJSONArray(int index) {
////        for(JSONObject o : JSONObjects()) {
////
////        }
//
//        JSONArray array = super.optJSONArray(index);
//        return array != null ? array : new JSONArray();
//    }
//
//    @NonNull
//    @Override
//    public JSONObject optJSONObject(int index) {
//        JSONObject object = super.optJSONObject(index);
//        return object != null ? object : new JSONObject();
//    }
//
//    @NonNull
//    public Iterator<JSONObject> JSONObjects() {
//        return new ObjectIterator();
//    }
//
//    private class ObjectIterator implements Iterator<JSONObject> {
//
//        private int index = 0;
//        @Override
//        public boolean hasNext() {
//            return index < length();
//        }
//
//        @Override
//        public JSONObject next() {
//            return optJSONObject(index++);
//        }
//    }
//}

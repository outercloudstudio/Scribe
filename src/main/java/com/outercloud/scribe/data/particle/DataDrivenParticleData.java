package com.outercloud.scribe.data.particle;

import com.google.gson.JsonElement;
import com.outercloud.scribe.data.DataDrivenData;

public class DataDrivenParticleData extends DataDrivenData {
    public enum MovementType {
        NONE,
        WANDER,
        LINEAR,
        DIFFUSE
    }

    public DataDrivenParticleData(JsonElement data) {
        super(data);
    }

    public Number GetScale(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("scale")) return 1;

        return ReadFloat(data.getAsJsonObject().get("scale"));
    }

    public Number GetLifetime(){
        if(!data.isJsonObject()) return 1;

        if(data.getAsJsonObject().has("lifetime")){
            return ReadFloat(data.getAsJsonObject().get("lifetime")) * 20;
        }else if(data.getAsJsonObject().has("lifetime_tick")){
            return ReadFloat(data.getAsJsonObject().get("lifetime_tick"));
        }

        return 1;
    }

    public MovementType GetMovementType(){
        if(!data.isJsonObject()) return MovementType.NONE;

        MovementType currentType = MovementType.NONE;

        if(data.getAsJsonObject().has("wander")) currentType = MovementType.WANDER;

        if(data.getAsJsonObject().has("linear")) currentType = MovementType.LINEAR;

        if(data.getAsJsonObject().has("diffuse")) currentType = MovementType.DIFFUSE;

        return currentType;
    }

    public float GetWanderMagnitude(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("wander")) return 1;

        if(!data.getAsJsonObject().get("wander").isJsonObject()) return 1;

        if(!data.getAsJsonObject().get("wander").getAsJsonObject().has("magnitude")) return 1;

        return ReadFloat(data.getAsJsonObject().get("wander").getAsJsonObject().get("magnitude"));
    }

    public float GetWanderSmoothness(){
        if(!data.isJsonObject()) return 300;

        if(!data.getAsJsonObject().has("wander")) return 300;

        if(!data.getAsJsonObject().get("wander").isJsonObject()) return 300;

        if(!data.getAsJsonObject().get("wander").getAsJsonObject().has("smoothness")) return 300;

        return ReadFloat(data.getAsJsonObject().get("wander").getAsJsonObject().get("smoothness"));
    }

    public float GetLinearCoord(String coord){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("linear")) return 1;

        if(!data.getAsJsonObject().get("linear").isJsonObject()) return 1;

        if(!data.getAsJsonObject().get("linear").getAsJsonObject().has(coord)) return 1;

        return ReadFloat(data.getAsJsonObject().get("linear").getAsJsonObject().get(coord));
    }

    public float ScaleOverLifetime(float scale){
        if(!data.isJsonObject()) return scale;

        if(!data.getAsJsonObject().has("scale_over_lifetime")) return scale;

        if(!data.getAsJsonObject().get("scale_over_lifetime").isJsonObject()) return scale;

        float offset = 0;
        float factor = 1;

        if (data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().has("offset")) offset = ReadFloat(data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().get("offset"));
        if (data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().has("factor")) factor = ReadFloat(data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().get("factor"));

        return scale * factor + offset;
    }

    public float AlphaOverLifetime(float alpha){
        if(!data.isJsonObject()) return alpha;

        if(!data.getAsJsonObject().has("alpha_over_lifetime")) return alpha;

        if(!data.getAsJsonObject().get("alpha_over_lifetime").isJsonObject()) return alpha;

        float offset = 0;
        float factor = 1;

        if (data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().has("offset")) offset = ReadFloat(data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().get("offset"));
        if (data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().has("factor")) factor = ReadFloat(data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().get("factor"));

        return alpha * factor + offset;
    }

    public boolean GetDoesCollision(){
        if(!data.isJsonObject()) return false;

        if(!data.getAsJsonObject().has("physics")) return false;

        if(!data.getAsJsonObject().get("physics").isJsonObject()) return false;

        if(!data.getAsJsonObject().get("physics").getAsJsonObject().has("collides")) return false;

        return ReadBool(data.getAsJsonObject().get("physics").getAsJsonObject().get("collides"));
    }

    public float GetGravity(){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("physics")) return 0;

        if(!data.getAsJsonObject().get("physics").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("physics").getAsJsonObject().has("gravity")) return 0;

        return ReadFloat(data.getAsJsonObject().get("physics").getAsJsonObject().get("gravity"));
    }

    public float AccelerationDrag(float vel){
        if(!data.isJsonObject()) return vel;

        if(!data.getAsJsonObject().has("acceleration_drag")) return vel;

        if(!data.getAsJsonObject().get("acceleration_drag").isJsonObject()) return vel;

        float offset = 0;
        float factor = 1;

        if (data.getAsJsonObject().get("acceleration_drag").getAsJsonObject().has("offset")) offset = ReadFloat(data.getAsJsonObject().get("acceleration_drag").getAsJsonObject().get("offset"));
        if (data.getAsJsonObject().get("acceleration_drag").getAsJsonObject().has("factor")) factor = ReadFloat(data.getAsJsonObject().get("acceleration_drag").getAsJsonObject().get("factor"));

        return vel * factor + offset;
    }

    public boolean DoSpawnOverTime(){
        if(!data.isJsonObject()) return false;

        if(!data.getAsJsonObject().has("spawn_over_time")) return false;

        if(!data.getAsJsonObject().get("spawn_over_time").isJsonObject()) return false;

        return true;
    }

    public float GetSpawnOverTimeRate(){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("spawn_over_time")) return 0;

        if(!data.getAsJsonObject().get("spawn_over_time").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().has("rate")) return 0;

        return ReadFloat(data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().get("rate"));
    }

    public int GetSpawnOverTimeAmount(){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("spawn_over_time")) return 0;

        if(!data.getAsJsonObject().get("spawn_over_time").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().has("amount")) return 0;

        return (int)ReadFloat(data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().get("amount"));
    }

    public String GetSpawnOverTimeIdentifier(){
        if(!data.isJsonObject()) return "";

        if(!data.getAsJsonObject().has("spawn_over_time")) return "";

        if(!data.getAsJsonObject().get("spawn_over_time").isJsonObject()) return "";

        if(!data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().has("identifier")) return "";

        return ReadString(data.getAsJsonObject().get("spawn_over_time").getAsJsonObject().get("identifier"));
    }

    public boolean DoSpawnOverDistance(){
        if(!data.isJsonObject()) return false;

        if(!data.getAsJsonObject().has("spawn_over_distance")) return false;

        if(!data.getAsJsonObject().get("spawn_over_distance").isJsonObject()) return false;

        return true;
    }

    public float GetSpawnOverDistanceDistance(){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("spawn_over_distance")) return 0;

        if(!data.getAsJsonObject().get("spawn_over_distance").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().has("distance")) return 0;

        return ReadFloat(data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().get("distance"));
    }

    public int GetSpawnOverDistanceAmount(){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("spawn_over_distance")) return 0;

        if(!data.getAsJsonObject().get("spawn_over_distance").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().has("amount")) return 0;

        return (int)ReadFloat(data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().get("amount"));
    }

    public String GetSpawnOverDistanceIdentifier(){
        if(!data.isJsonObject()) return "";

        if(!data.getAsJsonObject().has("spawn_over_distance")) return "";

        if(!data.getAsJsonObject().get("spawn_over_distance").isJsonObject()) return "";

        if(!data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().has("identifier")) return "";

        return ReadString(data.getAsJsonObject().get("spawn_over_distance").getAsJsonObject().get("identifier"));
    }

    public Boolean DoTick(){
        if(!data.isJsonObject()) return false;

        if(!data.getAsJsonObject().has("tick")) return false;

        return true;
    }

    public String GetTick(){
        if(!data.isJsonObject()) return "";

        if(!data.getAsJsonObject().has("tick")) return "";

        return ReadString(data.getAsJsonObject().get("tick"));
    }
}
